package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ComunicacaoListener;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

/**
 * Extends JButton
 */
public class RedeEpistemicaView extends JButton implements ComunicacaoListener{
	private static final long serialVersionUID = 1L;
	private RedeEpistemica redeEpistemica;
	private Thread t;
	private boolean run=true;
	private BufferedImage bi = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
	private Graphics graphics = bi.getGraphics();
	private int delay = 100;
	private boolean pause = false;
	private boolean paused = false;
	
	public RedeEpistemicaView(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
		this.redeEpistemica.setComunicacaoListener(this);
		t = new Thread(){
			@Override
			public void run() {
				while(run){
					try{
						if(!pause){
							paused = false;
							initDesenho();
							desenharAgentes();
							Thread.sleep(delay);
							atualizar();
						}else{
							paused = true;
							Thread.sleep(delay);
						}
					}catch (Exception e) {
						if(run) e.printStackTrace();
					}
				}
			}			
		};
		t.start();
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				addAgente(e.getX(),e.getY());
				
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
			
		});
	}
	private void atualizar(){
		//pede para fazer uma etapa
		redeEpistemica.fazUmaEtapa();
	}
	private void initDesenho(){
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.getWidth(),this.getHeight());
	}
	private void desenharAgentes(){
		graphics.setColor(Color.BLACK);
		for(AgenteEpistemico agente :redeEpistemica.getListAgenteEpistemico()){
			desenharAgente(agente);
		}
		this.setIcon(new ImageIcon(bi));
	}
	private void desenharAgente(AgenteEpistemico agente){
		graphics.drawOval(agente.getX()-agente.getRaio(), agente.getY()-agente.getRaio(), agente.getRaio()*2,agente.getRaio()*2);
	}
	public void addAgente(int x, int y){
		desenharAgente(redeEpistemica.criarAgente(x,y));
	}
	public void setRedeEpistemica(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
	}
	public RedeEpistemica getRedeEpistemica() {
		return redeEpistemica;
	}
	
	public void finalizar() {
		//flag do loop infinito
		run = false;
		//thread do loop infinito
		t.interrupt();
	}
	public void depoisDeComunicar(AgenteEpistemico emissor, AgenteEpistemico receptor, Double peso,Double diff) {
		
		int ex = emissor.getX();
		int ey = emissor.getY();
		int rx = receptor.getX();
		int ry = receptor.getY();
		graphics.setColor(Color.BLUE);
		graphics.drawLine(ex, ey, rx, ry);
		//andar
		double ref = 0.5;
		int passoMax = 10;
		double passo = (ref-diff)*passoMax;
		double dx = ex - rx;
		double dy = ey - ry;
		double hip = Math.sqrt(dx*dx + dy*dy);
		double relacao = Math.min(passo/hip,1.0);
		double mx = dx*relacao;
		double my = dy*relacao;
		if(0.1-diff>0){
			receptor.setX(rx+(int)mx);
			receptor.setY(ry+(int)my);
			graphics.setColor(Color.BLUE);
		}else{
			//repulsao
			if(hip<200){//max
				if(mx+my<1.0){
					mx=5.0;
				}
				receptor.setX(rx-(int)mx);
				receptor.setY(ry-(int)my);
			}
			graphics.setColor(Color.RED);
		}
		
		desenharAgente(receptor);
		this.setIcon(new ImageIcon(bi));
		try{
			Thread.sleep(delay);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void comunicadorEscolhido(AgenteEpistemico emissor) {
		graphics.setColor(Color.GREEN);
		desenharAgente(emissor);
		this.setIcon(new ImageIcon(bi));
		try{
			Thread.sleep(delay);
		}catch (Exception e) {
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
		while(!paused)try{Thread.sleep(10);}catch(Exception e){};
		this.redeEpistemica.getListAgenteEpistemico().clear();
		pause = false;
	}
}
