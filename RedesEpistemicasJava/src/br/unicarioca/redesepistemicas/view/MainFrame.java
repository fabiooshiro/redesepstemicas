package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class MainFrame extends JFrame implements WindowListener{
	private static final long serialVersionUID = 1L;
	private RedeEpistemicaView redeEpistemicaView;
	private RedeEpistemica redeEpistemica;
	private AgenteListPanel agenteListPanel;
	private ControlePanel controlePanel;
	public MainFrame() {
		
		redeEpistemica = new RedeEpistemica();
		controlePanel = new ControlePanel();
		redeEpistemicaView = new RedeEpistemicaView(redeEpistemica);
		controlePanel.setControlado(redeEpistemicaView);
		agenteListPanel = new AgenteListPanel();
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		this.setLayout(new BorderLayout());
		rightPanel.add(redeEpistemicaView,BorderLayout.CENTER);
		rightPanel.add(controlePanel,BorderLayout.SOUTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,agenteListPanel,rightPanel);
		this.addWindowListener(this);
		this.add(splitPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
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
}
