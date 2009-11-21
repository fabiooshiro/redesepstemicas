package br.unicarioca.redesepistemicas.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton pause;
	private JButton play;
	private JButton reiniciar;
	private RedeEpistemicaView redeEpistemicaView;
	private JSlider slider;
	public ControlePanel() {
		this.setLayout(new FlowLayout());
		reiniciar = new JButton("Reiniciar");
		pause = new JButton("Pause");
		play = new JButton("Play");
		
		slider = new JSlider();
		
		this.add(reiniciar);
		this.add(pause);
		this.add(play);
		this.add(slider);
		reiniciar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.reiniciar();
			}
		});
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.pause();
			}
		});
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.continuar();
			}
		});
		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				redeEpistemicaView.setVelocidade(slider.getMaximum() - slider.getValue());				
			}
			
		});
	}
	public void setControlado(RedeEpistemicaView redeEpistemicaView) {
		this.redeEpistemicaView = redeEpistemicaView;
	}
}
