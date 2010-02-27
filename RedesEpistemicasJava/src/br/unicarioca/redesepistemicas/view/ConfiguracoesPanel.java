package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConfiguracoesPanel extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	private JLabel lblQtdAgentes;
	private JTextField txtQtdAgentes;
	private JLabel lblMaxDiff;
	private JTextField txtMaxDiff;
	private JLabel lblMorrerEmXpublicacoes;
	private JTextField txtMorrerEmXpublicacoes;
	private JSpinner spnPassoMax;
	private JLabel lblPassoMax;
	private JLabel lblDistanciaMaxRepulsao;
	private JTextField txtDistanciaMaxRepulsao;
	private JLabel lblCriarNovoEm;
	private JTextField txtCriarNovoEm;
	private JLabel lblSomenteUltimaTeoria;
	private JCheckBox chkSomenteUltimaTeoria;
	private JLabel lblFrequencia;
	private JTextField txtFrequencia;
	private JLabel lblSnapShot;
	private JTextField txtSnapShot;
	
	private JLabel lblPesoAleatorio;
	private JCheckBox chkPesoAleatorio;
	
	private JButton btnOk;

	public ConfiguracoesPanel() {
		
		lblSnapShot = new JLabel("PrintScreen:");
		lblSnapShot.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSnapShot.setToolTipText("Print a cada x publicações");
		
		txtSnapShot = new JTextField("10");

		lblQtdAgentes = new JLabel("Qtd. Agentes:");
		lblQtdAgentes.setHorizontalAlignment(SwingConstants.RIGHT);

		lblMaxDiff = new JLabel("Max Diff:");
		lblMaxDiff.setToolTipText("Tolerância");
		lblMaxDiff.setHorizontalAlignment(SwingConstants.RIGHT);

		txtQtdAgentes = new JTextField("1000");
		txtMaxDiff = new JTextField("2.0");

		lblMorrerEmXpublicacoes = new JLabel("Morrer em:");
		lblMorrerEmXpublicacoes.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMorrerEmXpublicacoes.setToolTipText("Morre ao comunicar X vezes");
		txtMorrerEmXpublicacoes = new JTextField("10");

		lblSomenteUltimaTeoria = new JLabel("Somente último:");
		lblSomenteUltimaTeoria.setToolTipText("Publica somente o último par gerado");
		lblSomenteUltimaTeoria.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblCriarNovoEm = new JLabel("Criar novo em:");
		lblCriarNovoEm.setToolTipText("Cria um novo par ao comunicar a cada X vezes");
		lblCriarNovoEm.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCriarNovoEm = new JTextField("1");

		chkSomenteUltimaTeoria = new JCheckBox();
		chkSomenteUltimaTeoria.setSelected(true);

		btnOk = new JButton("Ok");

		lblDistanciaMaxRepulsao = new JLabel("Dist. Max. Repulsão:");
		lblDistanciaMaxRepulsao.setToolTipText("Limite de distância para a repulsão");
		txtDistanciaMaxRepulsao = new JTextField("1000");

		spnPassoMax = new JSpinner();
		spnPassoMax.setValue(15);
		lblPassoMax = new JLabel("Passo Agente:");
		lblPassoMax.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblPesoAleatorio = new JLabel("Peso aleatório:");
		lblPesoAleatorio.setHorizontalAlignment(SwingConstants.RIGHT);
		chkPesoAleatorio = new JCheckBox();
		chkPesoAleatorio.setSelected(false);
		
		lblFrequencia = new JLabel("Freq.:");
		lblFrequencia.setHorizontalAlignment(SwingConstants.RIGHT);
		txtFrequencia = new JTextField("1.0");
		
		//layout
		this.addTab("Rede E.",criarPainelConfRede());
		this.addTab("Agente E.",criarPainelConfAgente());
		this.addTab("View",criarPainelConfView());
	}
	
	private JPanel criarPainelConfRede(){
		JPanel retorno = new JPanel();
		retorno.setLayout(new BorderLayout());
		JPanel tabela = new JPanel(new GridLayout(0, 2));
		tabela.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabela.add(lblQtdAgentes);
		tabela.add(txtQtdAgentes);
		tabela.add(lblSnapShot);
		tabela.add(txtSnapShot);
		
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(btnOk);
		retorno.add(tabela, BorderLayout.CENTER);
		retorno.add(sul, BorderLayout.SOUTH);
		
		JPanel retornoLayout = new JPanel();
		retornoLayout.add(retorno, BorderLayout.NORTH);
		retornoLayout.add(new JPanel(),BorderLayout.CENTER);
		return retornoLayout;
	}
	private JPanel criarPainelConfView(){
		JPanel retorno = new JPanel();
		retorno.setLayout(new BorderLayout());
		JPanel tabela = new JPanel(new GridLayout(0, 2));
		tabela.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabela.add(lblDistanciaMaxRepulsao);
		tabela.add(txtDistanciaMaxRepulsao);
		tabela.add(lblPassoMax);
		tabela.add(spnPassoMax);
		retorno.add(tabela, BorderLayout.NORTH);
		retorno.add(new JPanel(),BorderLayout.CENTER);
		return retorno;
	}
	private JPanel criarPainelConfAgente(){
		JPanel retorno = new JPanel();
		retorno.setLayout(new BorderLayout());
		JPanel tabela = new JPanel(new GridLayout(0, 2));
		tabela.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabela.add(lblMaxDiff);
		tabela.add(txtMaxDiff);
		tabela.add(lblMorrerEmXpublicacoes);
		tabela.add(txtMorrerEmXpublicacoes);
		tabela.add(lblSomenteUltimaTeoria);
		tabela.add(chkSomenteUltimaTeoria);
		tabela.add(lblCriarNovoEm);
		tabela.add(txtCriarNovoEm);
		tabela.add(lblPesoAleatorio);
		tabela.add(chkPesoAleatorio);
		tabela.add(lblFrequencia);
		tabela.add(txtFrequencia);
		retorno.add(tabela, BorderLayout.NORTH);
		retorno.add(new JPanel(),BorderLayout.CENTER);
		return retorno;
	}
	
	public JButton getBtnOk() {
		return btnOk;
	}

	public JTextField getTxtQtdAgentes() {
		return txtQtdAgentes;
	}

	public JTextField getTxtMaxDiff() {
		return txtMaxDiff;
	}

	public JTextField getTxtMorrerEmXpublicacoes() {
		return txtMorrerEmXpublicacoes;
	}

	public JCheckBox getChkSomenteUltimaTeoria() {
		return chkSomenteUltimaTeoria;
	}

	public JTextField getTxtDistanciaMaxRepulsao() {
		return txtDistanciaMaxRepulsao;
	}

	public JTextField getTxtCriarNovoEm() {
		return txtCriarNovoEm;
	}
	public JSpinner getSpnPassoMax() {
		return spnPassoMax;
	}
	public JCheckBox getChkPesoAleatorio() {
		return chkPesoAleatorio;
	}
	public JTextField getTxtFrequencia() {
		return txtFrequencia;
	}
}
