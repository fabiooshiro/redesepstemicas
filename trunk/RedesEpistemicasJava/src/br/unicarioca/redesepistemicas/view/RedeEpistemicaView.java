package br.unicarioca.redesepistemicas.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.bo.SalvarSnapShoot;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemicoFactory;
import br.unicarioca.redesepistemicas.modelo.Aresta;
import br.unicarioca.redesepistemicas.modelo.ComunicacaoListener;
import br.unicarioca.redesepistemicas.modelo.NumeroAleatorio;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

/**
 * Extends JButton
 */
public class RedeEpistemicaView extends JButton implements ComunicacaoListener, ControladoListener, MouseWheelListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RedeEpistemicaView.class);
	private RedeEpistemica redeEpistemica;
	private Thread t;
	private boolean run = true;
	private BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
	private Graphics graphics = bi.getGraphics();
	private int delay = 0;
	private boolean pause = false;
	private boolean paused = false;
	private float escala = 0.25f;
	private int arrastarX = 0;
	private int arrastarY = 0;
	private boolean reiniciarBi = false;
	private AgenteEpistemicoFactory agenteEpistemicoFactory;
	private int startDragX = 0;
	private int startDragY = 0;
	private int distanciaMaxRepulsao;
	private int passoMax = 15;
	private boolean verLinhasVermelhas = false;
	private boolean verLinhasAzuis = false;
	private boolean verPesosDoSelecionado = false;
	private int algoritimoRepulsao = 2;
	private AgenteListPanel agenteListPanel;
	private int ordenarAgentesEmNEtapas = 10;
	private int snapshot = 1000;
	private BasicStroke comunicadorStroke = new BasicStroke(3.0f);
	public RedeEpistemicaView(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
		this.redeEpistemica.setComunicacaoListener(this);
		this.redeEpistemica.ligarLoop(false);
		t = new Thread() {
			@Override
			public void run() {
				while (run) {
					try {
						if (!pause) {
							paused = false;
							initDesenho();
							desenharAgentes();
							atualizar();
							logger.debug("Atualizando imagem delay " + delay*10);
							
							setIcon(new ImageIcon(bi));
							
							//30 is a magic number, just to not flick the image
							Thread.sleep((delay*10)+30);
						} else {
							paused = true;
							Thread.sleep(84);
						}
					} catch (Exception e) {
						if (run)
							e.printStackTrace();
					}
				}
			}
		};
		t.start();
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				reiniciarBi = true;
				refresh();
			}
			public void componentShown(ComponentEvent e) {}
		});
	}

	/**
	 * Redesenha os agentes
	 */
	public void refresh(){
		initDesenho();
		desenharAgentes();
		drawSelection();
		setIcon(new ImageIcon(bi));
	}
	
	/**
	 * redeEpistemica.fazUmaEtapa();
	 */
	private void atualizar() {
		if(redeEpistemica.getListAgenteEpistemico().size()!=0){
			int etapa = redeEpistemica.getEtapa();
			if(etapa%ordenarAgentesEmNEtapas==0 && agenteListPanel!=null){
				agenteListPanel.refresh();
			}
			// pede para fazer uma etapa
			redeEpistemica.fazUmaEtapa();
			
			if(etapa%snapshot==0){
				//salvar o estado do programa
				SalvarSnapShoot.getInstance().salvar(bi, etapa);
			}
		}
	}

	private void initDesenho() {
		if (reiniciarBi) {
			try{
				bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
				graphics = bi.getGraphics();
			}catch(Exception e){}
				reiniciarBi = false;
		}
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	/**
	 * Desenha todos os agentes em cor BLACK
	 */
	private void desenharAgentes() {
		graphics.setColor(Color.BLACK);
		synchronized (redeEpistemica.getListAgenteEpistemico()) {
			logger.debug("desenharAgentes()");
			for (AgenteEpistemico agente : redeEpistemica.getListAgenteEpistemico()) {
				desenharAgente(agente);
			}
		}
		drawSelection();
	}

	/**
	 * Desenha um agente com a cor corrente
	 * 
	 * @param agente
	 * @param color 
	 */
	private AgenteEpistemico desenharAgente(AgenteEpistemico agente, boolean fill) {
		float x = (agente.getX() - agente.getRaio()) * escala;
		float y = (agente.getY() - agente.getRaio()) * escala;
		int dim = Math.round(agente.getRaio() * 2 * escala);
		int raio = Math.round(agente.getRaio() * escala);
		//desenhar o que ele acredita
		if(agente.crencaMonitorada.size()>0){
			int parte = 360/(agente.crencaMonitorada.size()/2);
			int startAngle=0;
			Color old = graphics.getColor();
			int i=0;
			for(ParEpistemico par: agente.crencaMonitorada){
				graphics.setColor(par.getCor());
				if(i%2==0){
					graphics.fillArc(arrastarX + Math.round(x), arrastarY + Math.round(y), dim, dim, startAngle, parte);
				}else{
					graphics.fillArc(arrastarX + Math.round(x+(raio/2)), arrastarY + Math.round(y+(raio/2)), raio, raio, startAngle, parte);
					startAngle+=parte;
				}
				i++;
			}
			graphics.setColor(old);
		}
		if(fill){
			Graphics2D g2d = (Graphics2D) graphics;
			Stroke oldS = g2d.getStroke();
			g2d.setStroke(comunicadorStroke);
			g2d.drawOval(arrastarX + Math.round(x), arrastarY + Math.round(y), dim, dim);
			g2d.setStroke(oldS);
		}else if(agente.getColor()!=null){
			logger.info("Color = " + agente.getColor());
			Color old = graphics.getColor();
			graphics.setColor(agente.getColor());
			graphics.fillOval(arrastarX + Math.round(x), arrastarY + Math.round(y), dim, dim);
			graphics.setColor(old);
			desenharArestasComPesos(agente);
		}else{
			graphics.drawOval(arrastarX + Math.round(x), arrastarY + Math.round(y), dim, dim);
		}
		
		return agente;
	}
	
	/**
	 * Desenha um agente com a cor corrente
	 * 
	 * @param agente
	 */
	private AgenteEpistemico desenharAgente(AgenteEpistemico agente) {
		return desenharAgente(agente,false);
	}
	
	/**
	 * Coloca o agente na posicao X e Y
	 * 
	 * @param agente
	 * @param x
	 *            coordenada x
	 * @param y
	 *            coordenada y
	 * @return agenteNovo
	 */
	public AgenteEpistemico addAgente(AgenteEpistemico agente, int x, int y) {
		agente.setX(Math.round((x - arrastarX) / escala));
		agente.setY(Math.round((y - arrastarY) / escala));
		return desenharAgente(agente);
	}

	public void setRedeEpistemica(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
	}

	public RedeEpistemica getRedeEpistemica() {
		return redeEpistemica;
	}

	public void finalizar() {
		// flag do loop infinito
		run = false;
		// thread do loop infinito
		t.interrupt();
	}
	
	private void drawString(String string, int x, int y) {
		graphics.drawString(string,arrastarX + Math.round(x * escala), arrastarY + Math.round(y * escala));
	}
	
	private void drawLine(int x,int y, int x2, int y2){
		graphics.drawLine(arrastarX + Math.round(x * escala), arrastarY + Math.round(y * escala), arrastarX + Math.round(x2 * escala), arrastarY + Math.round(y2 * escala));
	}

	
	boolean checkColisao(AgenteEpistemico agente){
		int ex = agente.getX();
		int ey = agente.getY();
		for(AgenteEpistemico receptor:redeEpistemica.getListAgenteEpistemico()){
			if(agente==receptor) continue;
			int rx = receptor.getX();
			int ry = receptor.getY();
			double dx = ex - rx;
			double dy = ey - ry;
			double hip = Math.sqrt(dx * dx + dy * dy);
			double diam = agente.getRaio() + receptor.getRaio();
			if(hip < diam){//colidiu
				double relacao = diam/hip + 0.05;
				double mx = dx * relacao;
				double my = dy * relacao;
				agente.setX(rx + (int) mx);
				agente.setY(ry + (int) my);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Faz o agente dar um passo
	 * TODO calcular colisao para nao deixar sobrepor
	 */
	public void depoisDeComunicar(AgenteEpistemico emissor, AgenteEpistemico receptor, Double peso, Double diff) {

		int ex = emissor.getX();
		int ey = emissor.getY();
		int rx = receptor.getX();
		int ry = receptor.getY();
		// graphics.setColor(Color.BLUE);

		boolean colidiu = checkColisao(receptor);
		// andar
		double ref = 1.0;

		double passo = (ref - diff) * passoMax;
		double dx = ex - rx;
		double dy = ey - ry;
		double hip = Math.sqrt(dx * dx + dy * dy);
		double relacao = Math.min(passo / hip, 1.0);
		double mx = dx * relacao;
		double my = dy * relacao;
		if (receptor.getMaxDiff() - diff > 0) {
			if(!colidiu){
				receptor.setX(rx + (int) mx);
				receptor.setY(ry + (int) my);
			}else{
				//receptor.setX(rx - (int) mx);
				//receptor.setY(ry - (int) my);
			}
			graphics.setColor(Color.BLUE);
			if(verLinhasAzuis){
				drawLine(ex,ey,rx,ry);
			}
		} else {
			// repulsao
			if(algoritimoRepulsao==1){
				if (hip < distanciaMaxRepulsao) {// max
					if (mx + my < 1.0) {
						if (NumeroAleatorio.gerarNumero() < .5) {
							if (NumeroAleatorio.gerarNumero() < .5)
								mx = 5.0;
							else
								mx = -5.0;
						} else {
							if (NumeroAleatorio.gerarNumero() < .5)
								my = 5.0;
							else
								my = -5.0;
						}
					}
					mx *= .5f;
					my *= .5f;
					if(!colidiu){
						receptor.setX(rx - (int) mx);
						receptor.setY(ry - (int) my);
					}
				}
			}else{
				if (hip < distanciaMaxRepulsao) {
					mx *= hip/distanciaMaxRepulsao;
					my *= hip/distanciaMaxRepulsao;
					if(!colidiu){
						receptor.setX(rx - (int) mx);
						receptor.setY(ry - (int) my);
					}else{
						//receptor.setX(rx + (int) mx);
						//receptor.setY(ry + (int) my);
					}
				}
			}
			graphics.setColor(Color.RED);
			if (verLinhasVermelhas) {
				drawLine(ex,ey,rx,ry);
			}
		}
		desenharAgente(receptor);
		//this.setIcon(new ImageIcon(bi));
	}

	@Override
	public void comunicadorEscolhido(AgenteEpistemico emissor) {
		graphics.setColor(Color.GREEN);
		desenharAgente(emissor,true);
		//this.setIcon(new ImageIcon(bi));
	}

	/**
	 * Metodo agora sincrono,
	 * ao sair dele garante que pausou.
	 */
	public void pause() {
		pause = true;
		redeEpistemica.ligarLoop(false);
		//espera pausar
		while (!paused)
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		;
	}

	public void continuar() {
		pause = false;
		redeEpistemica.ligarLoop(true);
	}

	public void setVelocidade(int value) {
		delay = value;
	}

	public void reiniciar() {
		pause = true;
		
		this.redeEpistemica.getListAgenteEpistemico().clear();
		pause = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		String message;
		int notches = e.getWheelRotation();
		if (notches < 0) {
			message = "Mouse wheel moved UP " + -notches + " notch(es)";
			float escalaAnt = escala;
			zoomMais();
			int x = e.getX();
			x = Math.round((x - arrastarX) / escalaAnt);
			x = arrastarX + Math.round(x * escala);
			arrastarX += e.getX() - x;
			int y = e.getY();
			y = Math.round((y - arrastarY) / escalaAnt);
			y = arrastarY + Math.round(y * escala);
			arrastarY += e.getY() - y;
		} else {
			message = "Mouse wheel moved DOWN " + notches + " notch(es)";
			float escalaAnt = escala;
			zoomMenos();
			int x = e.getX();
			x = Math.round((x - arrastarX) / escalaAnt);
			x = arrastarX + Math.round(x * escala);
			arrastarX += e.getX() - x;
			int y = e.getY();
			y = Math.round((y - arrastarY) / escalaAnt);
			y = arrastarY + Math.round(y * escala);
			arrastarY += e.getY() - y;
		}
		logger.debug(message);
		if(pause){
			refresh();
		}
	}
	public void zoomMais(){
		escala *= 1.2f;
	}
	public void zoomMenos(){
		escala *= 0.8f;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		addAgente(agenteEpistemicoFactory.criarAgente(), e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private Point startSelection = null;
	private Point endSelection = null;
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			startDragX = e.getX();
			startDragY = e.getY();
		}else if(e.getButton()== MouseEvent.BUTTON1){
			startSelection = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			arrastarX += e.getX() - startDragX;
			arrastarY += e.getY() - startDragY;
			refresh();
		}else if(e.getButton()== MouseEvent.BUTTON1){
			endSelection = e.getPoint();
			initDesenho();
			desenharAgentes();
			int x=0,y=0,x2=0,y2=0;
			if(startSelection!=null && endSelection!=null){
				
				x2 = Math.max(startSelection.x, endSelection.x);
				y2 = Math.max(startSelection.y, endSelection.y);
				x = Math.min(startSelection.x, endSelection.x);
				y = Math.min(startSelection.y, endSelection.y);
				x = Math.round((x - arrastarX) / escala);
				y = Math.round((y - arrastarY) / escala);
				x2 = Math.round((x2 - arrastarX) / escala);
				y2 = Math.round((y2 - arrastarY) / escala);
			}
			synchronized (redeEpistemica.getListAgenteEpistemico()) {
				logger.debug("desenharAgentes()");
				List<Integer> listaSelecionados = new ArrayList<Integer>();
				for (AgenteEpistemico agente : redeEpistemica.getListAgenteEpistemico()) {
					desenharAgente(agente);
					boolean selecionado = false;
					logger.debug(x + "< (ax="+agente.getX() + ")<"+x2);
					if(x<agente.getX() && agente.getX()<x2){
						if(y<agente.getY() && agente.getY()<y2){
							agente.setColor(new Color(55,55,255));
							selecionado = true;
							logger.debug("Selecionando " + agente.getNome());
							int ic=agenteListPanel.indexOf(agente);
							if(ic!=-1){
								listaSelecionados.add(ic);
							}
						}
					}
					if(!selecionado){
						logger.debug("Nao selecionado " + agente.getNome());
					}
				}
				if(agenteListPanel!=null){
					int[] arr = new int[listaSelecionados.size()];
					for(int i=0;i<listaSelecionados.size();i++)arr[i]=listaSelecionados.get(i);
					agenteListPanel.getJList().setSelectedIndices(arr);
				}
			}
			drawSelection();
		}
	}

	private void drawSelection(){
		logger.debug("drawSelection()");
		if(startSelection == null || endSelection==null) return;
		int width = Math.abs(startSelection.x-endSelection.x);
		int height = Math.abs(startSelection.y-endSelection.y);
		if(width<1) return;
		int x = Math.min(startSelection.x, endSelection.x);
		int y = Math.min(startSelection.y, endSelection.y);
		graphics.setColor(Color.GRAY);
		graphics.drawRect(x, y, width, height);
	}
	
	public void setAgenteEpistemicoFactory(AgenteEpistemicoFactory agenteEpistemicoFactory) {
		this.agenteEpistemicoFactory = agenteEpistemicoFactory;
	}

	public int getDistanciaMaxRepulsao() {
		return distanciaMaxRepulsao;
	}
	
	public void setDistanciaMaximaRepulsao(int distanciaMaxRepulsao) {
		this.distanciaMaxRepulsao = distanciaMaxRepulsao;
	}

	public int getPassoMax() {
		return passoMax;
	}
	
	public void setPassoMax(int passoMax) {
		this.passoMax = passoMax;
	}

	public void setVerLinhasVermelhas(boolean mostrarLinhasVermelhas) {
		this.verLinhasVermelhas = mostrarLinhasVermelhas;
	}
	
	public boolean isVerLinhasVermelhas(){
		return verLinhasVermelhas;
	}
	
	public void setVerLinhasAzuis(boolean verLinhasAzuis) {
		this.verLinhasAzuis = verLinhasAzuis;
	}
	
	public boolean isVerLinhasAzuis() {
		return verLinhasAzuis;
	}

	/**
	 * Chamado quando existe um evento vindo do ListSelectionListener
	 * @param agentes
	 */
	public void selecionarAgentes(List<AgenteEpistemico> agentes) {
		List<AgenteEpistemico> listAgentes=redeEpistemica.getListAgenteEpistemico();
		for(AgenteEpistemico agente:listAgentes)
			agente.setColor(null);
		if(pause){
			initDesenho();
			desenharAgentes();
			graphics.setColor(new Color(55,55,255));
			for(AgenteEpistemico agente:agentes){
				agente.setColor(new Color(55,55,255));
				desenharAgente(agente,true);
				desenharArestasComPesos(agente);
			}
			this.setIcon(new ImageIcon(bi));
		}else{
			for(AgenteEpistemico agente:agentes)
				agente.setColor(new Color(55,55,255));
		}
	}

	private void desenharArestasComPesos(AgenteEpistemico agente){
		if(verPesosDoSelecionado){
			synchronized (agente.getArestas()) {
				for(Aresta aresta:agente.getArestas()){
					//achar o meio
					int x = (agente.getX() - aresta.getReceptor().getX())/2;
					int y = (agente.getY()-aresta.getReceptor().getY())/2;
					drawString(aresta.getPeso().toString(), aresta.getReceptor().getX()+x, aresta.getReceptor().getY()+y);
					drawLine(agente.getX(),agente.getY(),aresta.getReceptor().getX(),aresta.getReceptor().getY());
				}
			}
		}
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setVerPesosDoSelecionado(boolean verPesosDoSelecionado) {
		this.verPesosDoSelecionado = verPesosDoSelecionado;
	}
	
	public void setAgenteListPanel(AgenteListPanel agenteListPanel) {
		this.agenteListPanel = agenteListPanel;
	}
	public int getSnapshot() {
		return snapshot;
	}
	public void setSnapshot(int snapshot) {
		this.snapshot = snapshot;
	}
	
	public BufferedImage getCurrentScreen(){
		return bi;
	}

	/**
	 * Gera uma foto do momento
	 */
	public void criarFotografia() {
		if(bi!=null){
			boolean estavaPausado = pause;
			if(!estavaPausado) pause();
			SalvarSnapShoot.getInstance().salvar(bi);
			if(!estavaPausado) continuar();
		}
	}
}
