package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemicoFactory;
import br.unicarioca.redesepistemicas.modelo.CicloVidaAgenteListener;
import br.unicarioca.redesepistemicas.modelo.InfoListener;
import br.unicarioca.redesepistemicas.modelo.NumeroAleatorio;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class MainFrame extends JFrame implements InfoListener,WindowListener, CicloVidaAgenteListener,AgenteEpistemicoFactory{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	private static MainFrame instance;
	private RedeEpistemicaView redeEpistemicaView;
	private RedeEpistemica redeEpistemica;
	private AgenteListPanel agenteListPanel;
	private ControlePanel controlePanel;
	private MenuPrincipal menuPrincipal;
	private ConfiguracoesPanel configuracoesPanel = null;
	private JLabel sysInfo = new JLabel("Redes Epistêmicas");
	public MainFrame() {
		this.setTitle("IndraNet 1.0 - Simulador de Redes Epistêmicas");
		//instancias
		redeEpistemica = new RedeEpistemica();
		controlePanel = new ControlePanel();
		redeEpistemicaView = new RedeEpistemicaView(redeEpistemica);
		agenteListPanel = new AgenteListPanel();
		menuPrincipal = new MenuPrincipal();
		configuracoesPanel = new ConfiguracoesPanel();
		new AgenteListMouseMenu(agenteListPanel.getJList());
		//configuracoes
		CrencaTreinarView crencaTreinarView = CrencaTreinarView.getInstance();
		crencaTreinarView.setAgenteListPanel(agenteListPanel);
		controlePanel.addControlado(redeEpistemicaView);
		redeEpistemica.setCicloVidaAgenteListener(this);
		redeEpistemica.setAgenteEpistemicoFactory(this);
		redeEpistemicaView.setAgenteEpistemicoFactory(this);
		redeEpistemicaView.setAgenteListPanel(agenteListPanel);
		agenteListPanel.setRedeEpistemica(redeEpistemica);
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
		menuPrincipal.getVerLinhasVermelhas().setSelected(redeEpistemicaView.isVerLinhasVermelhas());
		menuPrincipal.getVerLinhasVermelhas().addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				redeEpistemicaView.setVerLinhasVermelhas(menuPrincipal.getVerLinhasVermelhas().isSelected());
			}
		});
		menuPrincipal.getVerLinhasAzuis().setSelected(redeEpistemicaView.isVerLinhasAzuis());
		menuPrincipal.getVerLinhasAzuis().addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				redeEpistemicaView.setVerLinhasAzuis(menuPrincipal.getVerLinhasAzuis().isSelected());
			}
		});
		menuPrincipal.getZoomMais().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.zoomMais();
			}
		});
		menuPrincipal.getZoomMenos().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.zoomMenos();
			}
		});
		menuPrincipal.getTreinamentoPrevio().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				treinar();
			}
		});
		menuPrincipal.getVerPesosDoSelecionado().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				redeEpistemicaView.setVerPesosDoSelecionado(menuPrincipal.getVerPesosDoSelecionado().isSelected());
			}
		});
		configuracoesPanel.getBtnOk().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				novo();
			}
		});
		configuracoesPanel.getSpnPassoMax().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				int passoMax = Integer.valueOf(configuracoesPanel.getSpnPassoMax().getValue().toString());
				redeEpistemicaView.setPassoMax(passoMax);
			}
		});
		agenteListPanel.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				logger.debug("keyPressed");
				if(e.getKeyCode()==KeyEvent.VK_DELETE){
					boolean paused = redeEpistemicaView.isPaused();
					if(!paused){
						redeEpistemicaView.pause();
						//espera pausar
						while(!redeEpistemicaView.isPaused()){
							try{Thread.sleep(100);}catch(Exception e2){}
						}
					}
					
					List<AgenteEpistemico> agentes = agenteListPanel.getSelecionados();
					logger.debug("Matando "+agentes.size());
					for(AgenteEpistemico agente:agentes){
						redeEpistemica.matarAgente(agente);
					}
					if(!paused) redeEpistemicaView.continuar();
				}
			}
			public void keyTyped(KeyEvent e) {}
		});
		agenteListPanel.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				redeEpistemicaView.selecionarAgentes(agenteListPanel.getSelecionados());
			}
		});
		//layout
		this.setJMenuBar(menuPrincipal);
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(configuracoesPanel,BorderLayout.NORTH);
		
		this.setLayout(new BorderLayout());
		rightPanel.add(redeEpistemicaView,BorderLayout.CENTER);
		rightPanel.add(controlePanel,BorderLayout.NORTH);
		
		final JScrollPane scrollPane = new JScrollPane(agenteListPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		this.addWindowListener(this);
		this.add(leftPanel,BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
		sysInfo.setBorder(BorderFactory.createEmptyBorder(3, 3,3, 3));
		sysInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(sysInfo,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
		instance = this;
	}
	
	public void treinar(){
		JFrame jFrame = new JFrame("Treinamento Prévio");
		jFrame.setLayout(new BorderLayout());
		jFrame.add(CrencaTreinarView.getInstance());
		jFrame.pack();
		jFrame.setVisible(true);
	}
	public void novo(){
		NumeroAleatorio.restart();
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
		System.exit(0);
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
		boolean pesoAleatorio = configuracoesPanel.getChkPesoAleatorio().isSelected();
		int distanciaMaxRepulsao = Integer.valueOf(configuracoesPanel.getTxtDistanciaMaxRepulsao().getText());
		int criarNovoEm = Integer.valueOf(configuracoesPanel.getTxtCriarNovoEm().getText());
		int w = redeEpistemicaView.getWidth();
		int h = redeEpistemicaView.getHeight();
		logger.debug("chkSomenteUltimaTeoria = " + chkSomenteUltimaTeoria);
		int x = (int)(w*NumeroAleatorio.gerarNumero());
		int y = (int)(h*NumeroAleatorio.gerarNumero());
		double freq = Double.valueOf(configuracoesPanel.getTxtFrequencia().getText());
		AgenteEpistemico agente = new AgenteEpistemico();
		agente.setVontadeDePublicar(freq);
		redeEpistemica.inserirAgente(agente,pesoAleatorio);
		redeEpistemicaView.addAgente(agente, x, y);
		agente.setMaxDiff(maxDiff);
		agente.setMorrerEmXpublicacoes(morrerEm);
		agente.setSomenteUltimaTeoria(chkSomenteUltimaTeoria);
		agente.setCriarNovoEm(criarNovoEm);
		redeEpistemicaView.setDistanciaMaximaRepulsao(distanciaMaxRepulsao);
		return agente;
	}

	@Override
	public void info(String info) {
		sysInfo.setText(info);		
	}

	public static MainFrame getLastInstance() {
		return instance;
	}
}
