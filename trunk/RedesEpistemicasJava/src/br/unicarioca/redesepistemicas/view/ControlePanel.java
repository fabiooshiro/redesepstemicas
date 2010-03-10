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

import br.unicarioca.redesepistemicas.bo.SalvarSnapShoot;

public class ControlePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton pause;
	private JButton play;
	private JButton zerar;
	private JButton fotografar;
	private List<ControladoListener> listControladoListener = new ArrayList<ControladoListener>();
	private JSlider slider;
	private boolean iniciou = false;
	public ControlePanel(final RedeEpistemicaView rev) {
		this.setLayout(new FlowLayout());
		fotografar = new JButton("Fotografar");
		zerar = new JButton("Zerar");
		pause = new JButton("Pause");
		play = new JButton("Play");
		
		slider = new JSlider();
		slider.setValue(100);
		slider.setName("Vel.");
		this.add(fotografar);
		this.add(zerar);
		this.add(pause);
		this.add(play);
		this.add(slider);
		
		fotografar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(iniciou){
					for(ControladoListener controladoListener:listControladoListener)
						controladoListener.pause();
					SalvarSnapShoot.getInstance().salvar(rev.getCurrentScreen());
					for(ControladoListener controladoListener:listControladoListener)
						controladoListener.continuar();
				}
			}
		});
		
		zerar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(ControladoListener controladoListener:listControladoListener)
					controladoListener.reiniciar();
				iniciou = false;
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
				iniciou = true;
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
	public JButton getZerar() {
		return zerar;
	}
}
