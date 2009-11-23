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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConfiguracoesPanel extends JPanel {
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
	private JButton btnOk;

	public ConfiguracoesPanel() {

		lblQtdAgentes = new JLabel("Qtd Agentes:");
		lblQtdAgentes.setHorizontalAlignment(SwingConstants.RIGHT);

		lblMaxDiff = new JLabel("Max Diff:");
		lblMaxDiff.setHorizontalAlignment(SwingConstants.RIGHT);

		txtQtdAgentes = new JTextField("1000");
		txtMaxDiff = new JTextField("0.05");

		lblMorrerEmXpublicacoes = new JLabel("Morrer em:");
		lblMorrerEmXpublicacoes.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMorrerEmXpublicacoes = new JTextField("10");

		lblSomenteUltimaTeoria = new JLabel("Somente último:");
		lblSomenteUltimaTeoria.setToolTipText("Publica somente o último par gerado");
		lblSomenteUltimaTeoria.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblCriarNovoEm = new JLabel("Criar novo em:");
		lblCriarNovoEm.setToolTipText("Cria um novo par ao comunicar a cada X vezes");
		lblCriarNovoEm.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCriarNovoEm = new JTextField("3");

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
		
		//layout
		this.setLayout(new BorderLayout());
		JPanel tabela = new JPanel(new GridLayout(0, 2));
		tabela.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabela.add(lblQtdAgentes);
		tabela.add(txtQtdAgentes);
		tabela.add(lblMaxDiff);
		tabela.add(txtMaxDiff);
		tabela.add(lblMorrerEmXpublicacoes);
		tabela.add(txtMorrerEmXpublicacoes);
		tabela.add(lblSomenteUltimaTeoria);
		tabela.add(chkSomenteUltimaTeoria);
		tabela.add(lblDistanciaMaxRepulsao);
		tabela.add(txtDistanciaMaxRepulsao);
		tabela.add(lblCriarNovoEm);
		tabela.add(txtCriarNovoEm);
		tabela.add(lblPassoMax);
		tabela.add(spnPassoMax);
		
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(btnOk);
		this.add(tabela, BorderLayout.CENTER);
		this.add(sul, BorderLayout.SOUTH);
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
}
