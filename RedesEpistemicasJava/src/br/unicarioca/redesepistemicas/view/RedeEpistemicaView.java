package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
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
	private int delay = 100;
	private boolean pause = false;
	private boolean paused = false;
	private float escala = 1.0f;
	private int arrastarX = 0;
	private int arrastarY = 0;
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
	}

	private void atualizar() {
		// pede para fazer uma etapa
		redeEpistemica.fazUmaEtapa();
	}

	private void initDesenho() {
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
	private void desenharAgente(AgenteEpistemico agente) {
		float x = (agente.getX() - agente.getRaio()) * escala;
		float y = (agente.getY() - agente.getRaio()) * escala;
		int dim = Math.round(agente.getRaio() * 2 * escala);
		graphics.drawOval(arrastarX+Math.round(x), arrastarY+Math.round(y), dim, dim);
	}

	public void addAgente(int x, int y) {
		desenharAgente(redeEpistemica.criarAgente(Math.round((x-arrastarX)/escala), Math.round((y-arrastarY)/escala)));
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
		graphics.setColor(Color.BLUE);
		graphics.drawLine(arrastarX+Math.round(ex*escala),arrastarY+ Math.round(ey*escala),arrastarX+ Math.round(rx*escala), arrastarY+Math.round(ry*escala));
		// andar
		double ref = 0.5;
		int passoMax = 10;
		double passo = (ref - diff) * passoMax;
		double dx = ex - rx;
		double dy = ey - ry;
		double hip = Math.sqrt(dx * dx + dy * dy);
		double relacao = Math.min(passo / hip, 1.0);
		double mx = dx * relacao;
		double my = dy * relacao;
		if (0.1 - diff > 0) {
			receptor.setX(rx + (int) mx);
			receptor.setY(ry + (int) my);
			graphics.setColor(Color.BLUE);
		} else {
			// repulsao
			if (hip < 200) {// max
				if (mx + my < 1.0) {
					mx = 5.0;
				}
				receptor.setX(rx - (int) mx);
				receptor.setY(ry - (int) my);
			}
			graphics.setColor(Color.RED);
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
			escala*=1.2f;
		} else {
			message = "Mouse wheel moved DOWN " + notches + " notch(es)";
			
			escala*=0.8f;
		}
		logger.debug(message);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		addAgente(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	int startX=0;
	int startY=0;
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3){
			startX = e.getX();
			startY = e.getY();
		}
	}

	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3){
			arrastarX += e.getX()-startX;
			arrastarY += e.getY()-startY;
			initDesenho();
			desenharAgentes();
		}
	}
}
