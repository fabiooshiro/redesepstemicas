package br.unicarioca.redesepistemicas.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
	private List<ControladoListener> listControladoListener = new ArrayList<ControladoListener>();
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
				for(ControladoListener controladoListener:listControladoListener)
					controladoListener.reiniciar();
			}
		});
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(ControladoListener controladoListener:listControladoListener)
					controladoListener.pause();
			}
		});
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(ControladoListener controladoListener:listControladoListener)
					controladoListener.continuar();
			}
		});
		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				for(ControladoListener controladoListener:listControladoListener)
					controladoListener.setVelocidade(slider.getMaximum() - slider.getValue());				
			}
			
		});
	}
	public void addControlado(ControladoListener controladoListener) {
		listControladoListener.add(controladoListener);
	}
	public JButton getReiniciar() {
		return reiniciar;
	}
}
