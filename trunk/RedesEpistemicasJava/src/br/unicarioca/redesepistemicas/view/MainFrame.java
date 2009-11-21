package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class MainFrame extends JFrame implements WindowListener{
	private static final long serialVersionUID = 1L;
	private RedeEpistemicaView redeEpistemicaView;
	private RedeEpistemica redeEpistemica;
	private AgenteListPanel agenteListPanel;
	private ControlePanel controlePanel;
	public MainFrame() {
		//instancias
		redeEpistemica = new RedeEpistemica();
		controlePanel = new ControlePanel();
		redeEpistemicaView = new RedeEpistemicaView(redeEpistemica);
		agenteListPanel = new AgenteListPanel();
		
		//configuracoes
		controlePanel.addControlado(redeEpistemicaView);
		redeEpistemica.setCicloVidaAgenteListener(agenteListPanel);
		controlePanel.getReiniciar().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				agenteListPanel.reiniciar();
			}
		});
		
		//layout
		JPanel rightPanel = new JPanel(new BorderLayout());
		this.setLayout(new BorderLayout());
		rightPanel.add(redeEpistemicaView,BorderLayout.CENTER);
		rightPanel.add(controlePanel,BorderLayout.SOUTH);
		JScrollPane scrollPane = new JScrollPane(agenteListPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPane,rightPanel);
		splitPane.setDividerLocation(100);
		splitPane.setAutoscrolls(true);
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
