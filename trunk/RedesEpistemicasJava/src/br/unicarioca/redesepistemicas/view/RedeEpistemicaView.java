package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemicoFactory;
import br.unicarioca.redesepistemicas.modelo.ComunicacaoListener;
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
	private boolean verLinhasVermelhas = true;
	private boolean verLinhasAzuis = true;
	private int algoritimoRepulsao = 2;
	public RedeEpistemicaView(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
		this.redeEpistemica.setComunicacaoListener(this);
		
		t = new Thread() {
			@Override
			public void run() {
				while (run) {
					try {
						if (!pause) {
							paused = false;
							initDesenho();
							desenharAgentes();
							Thread.sleep(delay);
							atualizar();
						} else {
							paused = true;
							Thread.sleep(delay);
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

			@Override
			public void componentHidden(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentResized(ComponentEvent e) {
				reiniciarBi = true;
			}

			@Override
			public void componentShown(ComponentEvent e) {

			}

		});
	}

	private void atualizar() {
		// pede para fazer uma etapa
		redeEpistemica.fazUmaEtapa();
	}

	private void initDesenho() {
		if (reiniciarBi) {
			bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			graphics = bi.getGraphics();
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
		for (AgenteEpistemico agente : redeEpistemica.getListAgenteEpistemico()) {
			desenharAgente(agente);
		}
		this.setIcon(new ImageIcon(bi));
	}

	/**
	 * Desenha um agente com a cor corrente
	 * 
	 * @param agente
	 */
	private AgenteEpistemico desenharAgente(AgenteEpistemico agente) {
		float x = (agente.getX() - agente.getRaio()) * escala;
		float y = (agente.getY() - agente.getRaio()) * escala;
		int dim = Math.round(agente.getRaio() * 2 * escala);
		graphics.drawOval(arrastarX + Math.round(x), arrastarY + Math.round(y), dim, dim);
		return agente;
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

	public void depoisDeComunicar(AgenteEpistemico emissor, AgenteEpistemico receptor, Double peso, Double diff) {

		int ex = emissor.getX();
		int ey = emissor.getY();
		int rx = receptor.getX();
		int ry = receptor.getY();
		// graphics.setColor(Color.BLUE);

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
			receptor.setX(rx + (int) mx);
			receptor.setY(ry + (int) my);
			graphics.setColor(Color.BLUE);
			if(verLinhasAzuis){
				graphics.drawLine(arrastarX + Math.round(ex * escala), arrastarY + Math.round(ey * escala), arrastarX + Math.round(rx * escala), arrastarY + Math.round(ry * escala));
			}
		} else {
			// repulsao
			if(algoritimoRepulsao==1){
				if (hip < distanciaMaxRepulsao) {// max
					if (mx + my < 1.0) {
						if (Math.random() < .5) {
							if (Math.random() < .5)
								mx = 5.0;
							else
								mx = -5.0;
						} else {
							if (Math.random() < .5)
								my = 5.0;
							else
								my = -5.0;
						}
					}
					mx *= .5f;
					my *= .5f;
					receptor.setX(rx - (int) mx);
					receptor.setY(ry - (int) my);
				}
			}else{
				if (hip < distanciaMaxRepulsao) {
					mx *= hip/distanciaMaxRepulsao;
					my *= hip/distanciaMaxRepulsao;
					receptor.setX(rx - (int) mx);
					receptor.setY(ry - (int) my);
				}
			}
			graphics.setColor(Color.RED);
			if (verLinhasVermelhas) {
				graphics.drawLine(arrastarX + Math.round(ex * escala), arrastarY + Math.round(ey * escala), arrastarX + Math.round(rx * escala), arrastarY + Math.round(ry * escala));
			}
		}
		desenharAgente(receptor);
		this.setIcon(new ImageIcon(bi));
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void comunicadorEscolhido(AgenteEpistemico emissor) {
		graphics.setColor(Color.GREEN);
		desenharAgente(emissor);
		this.setIcon(new ImageIcon(bi));
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		pause = true;
	}

	public void continuar() {
		pause = false;
	}

	public void setVelocidade(int value) {
		delay = value;
	}

	public void reiniciar() {
		pause = true;
		while (!paused)
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		;
		this.redeEpistemica.getListAgenteEpistemico().clear();
		pause = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		String message;
		int notches = e.getWheelRotation();
		if (notches < 0) {
			message = "Mouse wheel moved UP " + -notches + " notch(es)";
			escala *= 1.2f;
		} else {
			message = "Mouse wheel moved DOWN " + notches + " notch(es)";

			escala *= 0.8f;
		}
		logger.debug(message);
		if(pause){
			initDesenho();
			desenharAgentes();
		}
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

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			startDragX = e.getX();
			startDragY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			arrastarX += e.getX() - startDragX;
			arrastarY += e.getY() - startDragY;
			initDesenho();
			desenharAgentes();
		}
	}

	public void setAgenteEpistemicoFactory(AgenteEpistemicoFactory agenteEpistemicoFactory) {
		this.agenteEpistemicoFactory = agenteEpistemicoFactory;
	}

	public void setDistanciaMaximaRepulsao(int distanciaMaxRepulsao) {
		this.distanciaMaxRepulsao = distanciaMaxRepulsao;
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
}
