package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private RedeEpistemicaView redeEpistemicaView;
	private RedeEpistemica redeEpistemica;
	public MainFrame() {
		this.setLayout(new BorderLayout());
		redeEpistemica = new RedeEpistemica();
		redeEpistemicaView = new RedeEpistemicaView(redeEpistemica);
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				redeEpistemicaView.finalizar();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
		});
		this.add(redeEpistemicaView, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
	}
}
