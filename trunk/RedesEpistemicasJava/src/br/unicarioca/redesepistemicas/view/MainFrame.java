package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemicoFactory;
import br.unicarioca.redesepistemicas.modelo.CicloVidaAgenteListener;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class MainFrame extends JFrame implements WindowListener, CicloVidaAgenteListener,AgenteEpistemicoFactory{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	private RedeEpistemicaView redeEpistemicaView;
	private RedeEpistemica redeEpistemica;
	private AgenteListPanel agenteListPanel;
	private ControlePanel controlePanel;
	private MenuPrincipal menuPrincipal;
	private ConfiguracoesPanel configuracoesPanel = null;
	public MainFrame() {
		//instancias
		redeEpistemica = new RedeEpistemica();
		controlePanel = new ControlePanel();
		redeEpistemicaView = new RedeEpistemicaView(redeEpistemica);
		agenteListPanel = new AgenteListPanel();
		menuPrincipal = new MenuPrincipal();
		configuracoesPanel = new ConfiguracoesPanel();
		//configuracoes
		controlePanel.addControlado(redeEpistemicaView);
		redeEpistemica.setCicloVidaAgenteListener(this);
		redeEpistemica.setAgenteEpistemicoFactory(this);
		redeEpistemicaView.setAgenteEpistemicoFactory(this);
		controlePanel.getZerar().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				agenteListPanel.reiniciar();
			}
		});
		menuPrincipal.getNovo().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				novo();
			}
		});
		configuracoesPanel.getBtnOk().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				novo();
			}
		});
		
		//layout
		this.setJMenuBar(menuPrincipal);
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(configuracoesPanel,BorderLayout.NORTH);
		
		this.setLayout(new BorderLayout());
		rightPanel.add(redeEpistemicaView,BorderLayout.CENTER);
		rightPanel.add(controlePanel,BorderLayout.SOUTH);
		
		final JScrollPane scrollPane = new JScrollPane(agenteListPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		this.addWindowListener(this);
		this.add(leftPanel,BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
		Thread t = new Thread(){
			public void run() {
				while(isVisible()){
					try{
						Thread.sleep(1000);
					}catch(Exception e){}
					agenteListPanel.refresh();
				}
			}
		};
		t.start();
	}
	
	public void novo(){
		agenteListPanel.reiniciar();
		redeEpistemicaView.pause();
		int qtd = Integer.valueOf(configuracoesPanel.getTxtQtdAgentes().getText());
		for(int i=0;i<qtd;i++){
			criarAgente();
		}
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

	@Override
	public void criado(AgenteEpistemico agente) {
		agenteListPanel.criado(agente);
	}

	@Override
	public void morto(AgenteEpistemico agente) {
		agenteListPanel.morto(agente);
	}

	@Override
	public AgenteEpistemico criarAgente() {
		int morrerEm = Integer.valueOf(configuracoesPanel.getTxtMorrerEmXpublicacoes().getText());
		double maxDiff = Double.valueOf(configuracoesPanel.getTxtMaxDiff().getText());
		boolean chkSomenteUltimaTeoria = configuracoesPanel.getChkSomenteUltimaTeoria().isSelected();
		int distanciaMaxRepulsao = Integer.valueOf(configuracoesPanel.getTxtDistanciaMaxRepulsao().getText());
		int criarNovoEm = Integer.valueOf(configuracoesPanel.getTxtCriarNovoEm().getText());
		int w = redeEpistemicaView.getWidth();
		int h = redeEpistemicaView.getHeight();
		logger.debug("chkSomenteUltimaTeoria = " + chkSomenteUltimaTeoria);
		int x = (int)(w*Math.random());
		int y = (int)(h*Math.random());
		
		AgenteEpistemico agente = new AgenteEpistemico();
		redeEpistemica.inserirAgente(agente);
		redeEpistemicaView.addAgente(agente, x, y);
		
		agente.setMaxDiff(maxDiff);
		agente.setMorrerEmXpublicacoes(morrerEm);
		agente.setSomenteUltimaTeoria(chkSomenteUltimaTeoria);
		agente.setCriarNovoEm(criarNovoEm);
		redeEpistemicaView.setDistanciaMaximaRepulsao(distanciaMaxRepulsao);
		return agente;
	}
}
