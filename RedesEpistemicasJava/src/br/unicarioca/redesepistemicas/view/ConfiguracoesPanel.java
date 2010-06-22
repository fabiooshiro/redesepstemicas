package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * TODO criar a configuração interna da rede<br>
 * se quisermos 5 neuronios na entrada 3 na camada intermediária e 1 na saída<br>
 * colocaremos no campo o seguinte: 5,3,1
 * @author Fabio Issamu Oshiro, Leandro Freire
 *
 */
public class ConfiguracoesPanel extends JTabbedPane {
	private static final long serialVersionUID = 2L;
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
	
	private JLabel lblDistribuicaoAleatoria;
	private JCheckBox chkDistribuicaoAleatoria;

	private JLabel lblEstruturaRede;
	private JTextField txtEstruturaRede;
	
	private JLabel lblErrorTolerance;
	private JTextField txtErrorTolerance;
	
	private JButton btnOk;

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		spnPassoMax.setModel(new SpinnerNumberModel());
	}
	
	public ConfiguracoesPanel() {
		lblErrorTolerance = new JLabel("Error Tolerance:");
		txtErrorTolerance = new JTextField("0.0001");
		
		lblEstruturaRede = new JLabel("Estrutura da Rede");
		txtEstruturaRede = new JTextField("5,3,1");
		
		lblDistribuicaoAleatoria = new JLabel("Distr. Aleatória:");
		chkDistribuicaoAleatoria = new JCheckBox();
		chkDistribuicaoAleatoria.setSelected(true);
		
		lblSnapShot = new JLabel("PrintScreen:");
		lblSnapShot.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSnapShot.setToolTipText("Print a cada x publicações");
		
		txtSnapShot = new JTextField("10");

		lblQtdAgentes = new JLabel("Qtd. Agentes:");
		lblQtdAgentes.setHorizontalAlignment(SwingConstants.RIGHT);

		lblMaxDiff = new JLabel("Max Diff:");
		lblMaxDiff.setToolTipText("Tolerância");
		lblMaxDiff.setHorizontalAlignment(SwingConstants.RIGHT);

		txtQtdAgentes = new JTextField("500");
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
	
	public int[] getEstruturaRede(){
		//Configurar a rede neural
		String arr[] = getTxtEstruturaRede().getText().split(",");
		int estruturaRede[] = new int[arr.length]; 
		for(int i=0;i<arr.length;i++){
			estruturaRede[i] = Integer.parseInt(arr[i]);
		}
		return estruturaRede;
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
		tabela.add(lblEstruturaRede);
		tabela.add(txtEstruturaRede);
		tabela.add(lblErrorTolerance);
		tabela.add(txtErrorTolerance);
		
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
		tabela.add(lblDistribuicaoAleatoria);
		tabela.add(chkDistribuicaoAleatoria);
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
	public JTextField getTxtSnapShot() {
		return txtSnapShot;
	}

	/**
	 * @return the lblQtdAgentes
	 */
	public JLabel getLblQtdAgentes() {
		return lblQtdAgentes;
	}

	/**
	 * @param lblQtdAgentes the lblQtdAgentes to set
	 */
	public void setLblQtdAgentes(JLabel lblQtdAgentes) {
		this.lblQtdAgentes = lblQtdAgentes;
	}

	/**
	 * @return the lblMaxDiff
	 */
	public JLabel getLblMaxDiff() {
		return lblMaxDiff;
	}

	/**
	 * @param lblMaxDiff the lblMaxDiff to set
	 */
	public void setLblMaxDiff(JLabel lblMaxDiff) {
		this.lblMaxDiff = lblMaxDiff;
	}

	/**
	 * @return the lblMorrerEmXpublicacoes
	 */
	public JLabel getLblMorrerEmXpublicacoes() {
		return lblMorrerEmXpublicacoes;
	}

	/**
	 * @param lblMorrerEmXpublicacoes the lblMorrerEmXpublicacoes to set
	 */
	public void setLblMorrerEmXpublicacoes(JLabel lblMorrerEmXpublicacoes) {
		this.lblMorrerEmXpublicacoes = lblMorrerEmXpublicacoes;
	}

	/**
	 * @return the lblPassoMax
	 */
	public JLabel getLblPassoMax() {
		return lblPassoMax;
	}

	/**
	 * @param lblPassoMax the lblPassoMax to set
	 */
	public void setLblPassoMax(JLabel lblPassoMax) {
		this.lblPassoMax = lblPassoMax;
	}

	/**
	 * @return the lblDistanciaMaxRepulsao
	 */
	public JLabel getLblDistanciaMaxRepulsao() {
		return lblDistanciaMaxRepulsao;
	}

	/**
	 * @param lblDistanciaMaxRepulsao the lblDistanciaMaxRepulsao to set
	 */
	public void setLblDistanciaMaxRepulsao(JLabel lblDistanciaMaxRepulsao) {
		this.lblDistanciaMaxRepulsao = lblDistanciaMaxRepulsao;
	}

	/**
	 * @return the lblCriarNovoEm
	 */
	public JLabel getLblCriarNovoEm() {
		return lblCriarNovoEm;
	}

	/**
	 * @param lblCriarNovoEm the lblCriarNovoEm to set
	 */
	public void setLblCriarNovoEm(JLabel lblCriarNovoEm) {
		this.lblCriarNovoEm = lblCriarNovoEm;
	}

	/**
	 * @return the lblSomenteUltimaTeoria
	 */
	public JLabel getLblSomenteUltimaTeoria() {
		return lblSomenteUltimaTeoria;
	}

	/**
	 * @param lblSomenteUltimaTeoria the lblSomenteUltimaTeoria to set
	 */
	public void setLblSomenteUltimaTeoria(JLabel lblSomenteUltimaTeoria) {
		this.lblSomenteUltimaTeoria = lblSomenteUltimaTeoria;
	}

	/**
	 * @return the lblFrequencia
	 */
	public JLabel getLblFrequencia() {
		return lblFrequencia;
	}

	/**
	 * @param lblFrequencia the lblFrequencia to set
	 */
	public void setLblFrequencia(JLabel lblFrequencia) {
		this.lblFrequencia = lblFrequencia;
	}

	/**
	 * @return the lblSnapShot
	 */
	public JLabel getLblSnapShot() {
		return lblSnapShot;
	}

	/**
	 * @param lblSnapShot the lblSnapShot to set
	 */
	public void setLblSnapShot(JLabel lblSnapShot) {
		this.lblSnapShot = lblSnapShot;
	}

	/**
	 * @return the lblPesoAleatorio
	 */
	public JLabel getLblPesoAleatorio() {
		return lblPesoAleatorio;
	}

	/**
	 * @param lblPesoAleatorio the lblPesoAleatorio to set
	 */
	public void setLblPesoAleatorio(JLabel lblPesoAleatorio) {
		this.lblPesoAleatorio = lblPesoAleatorio;
	}

	/**
	 * @param txtQtdAgentes the txtQtdAgentes to set
	 */
	public void setTxtQtdAgentes(JTextField txtQtdAgentes) {
		this.txtQtdAgentes = txtQtdAgentes;
	}

	/**
	 * @param txtMaxDiff the txtMaxDiff to set
	 */
	public void setTxtMaxDiff(JTextField txtMaxDiff) {
		this.txtMaxDiff = txtMaxDiff;
	}

	/**
	 * @param txtMorrerEmXpublicacoes the txtMorrerEmXpublicacoes to set
	 */
	public void setTxtMorrerEmXpublicacoes(JTextField txtMorrerEmXpublicacoes) {
		this.txtMorrerEmXpublicacoes = txtMorrerEmXpublicacoes;
	}

	/**
	 * @param spnPassoMax the spnPassoMax to set
	 */
	public void setSpnPassoMax(JSpinner spnPassoMax) {
		this.spnPassoMax = spnPassoMax;
	}

	/**
	 * @param txtDistanciaMaxRepulsao the txtDistanciaMaxRepulsao to set
	 */
	public void setTxtDistanciaMaxRepulsao(JTextField txtDistanciaMaxRepulsao) {
		this.txtDistanciaMaxRepulsao = txtDistanciaMaxRepulsao;
	}

	/**
	 * @param txtCriarNovoEm the txtCriarNovoEm to set
	 */
	public void setTxtCriarNovoEm(JTextField txtCriarNovoEm) {
		this.txtCriarNovoEm = txtCriarNovoEm;
	}

	/**
	 * @param chkSomenteUltimaTeoria the chkSomenteUltimaTeoria to set
	 */
	public void setChkSomenteUltimaTeoria(JCheckBox chkSomenteUltimaTeoria) {
		this.chkSomenteUltimaTeoria = chkSomenteUltimaTeoria;
	}

	/**
	 * @param txtFrequencia the txtFrequencia to set
	 */
	public void setTxtFrequencia(JTextField txtFrequencia) {
		this.txtFrequencia = txtFrequencia;
	}

	/**
	 * @param txtSnapShot the txtSnapShot to set
	 */
	public void setTxtSnapShot(JTextField txtSnapShot) {
		this.txtSnapShot = txtSnapShot;
	}

	/**
	 * @param chkPesoAleatorio the chkPesoAleatorio to set
	 */
	public void setChkPesoAleatorio(JCheckBox chkPesoAleatorio) {
		this.chkPesoAleatorio = chkPesoAleatorio;
	}

	/**
	 * @param btnOk the btnOk to set
	 */
	public void setBtnOk(JButton btnOk) {
		this.btnOk = btnOk;
	}


	public JCheckBox getChkDistribuicaoAleatoria() {
		return chkDistribuicaoAleatoria;
	}

	public void setChkDistribuicaoAleatoria(JCheckBox chkDistribuicaoAleatoria) {
		this.chkDistribuicaoAleatoria = chkDistribuicaoAleatoria;
	}
	
	public JLabel getLblDistribuicaoAleatoria() {
		return lblDistribuicaoAleatoria;
	}

	public void setLblDistribuicaoAleatoria(JLabel lblDistribuicaoAleatoria) {
		this.lblDistribuicaoAleatoria = lblDistribuicaoAleatoria;
	}
	
	public JLabel getLblEstruturaRede() {
		return lblEstruturaRede;
	}

	public void setLblEstruturaRede(JLabel lblEstruturaRede) {
		this.lblEstruturaRede = lblEstruturaRede;
	}

	public JTextField getTxtEstruturaRede() {
		return txtEstruturaRede;
	}

	public void setTxtEstruturaRede(JTextField txtEstruturaRede) {
		this.txtEstruturaRede = txtEstruturaRede;
	}
	
	public JLabel getLblErrorTolerance() {
		return lblErrorTolerance;
	}
	
	public void setLblErrorTolerance(JLabel lblErrorTolerance) {
		this.lblErrorTolerance = lblErrorTolerance;
	}
	
	public JTextField getTxtErrorTolerance() {
		return txtErrorTolerance;
	}
	
	public void setTxtErrorTolerance(JTextField txtErrorTolerance) {
		this.txtErrorTolerance = txtErrorTolerance;
	}
	
}
