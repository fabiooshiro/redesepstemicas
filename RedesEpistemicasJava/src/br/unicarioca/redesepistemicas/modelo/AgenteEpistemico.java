package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.bo.BackPropagation;

import com.rmit.neuralnetwork.NeuralNetwork;
import com.rmit.neuralnetwork.training.Training;
import com.rmit.neuralnetwork.trainingdata.TrainingExample;
import com.rmit.neuralnetwork.trainingdata.TrainingSet;

/**
 * Um agente epistêmico é um construto capaz de gerar, comunicar e representar
 * pares epistêmicos.
 */
public class AgenteEpistemico{
	private static Logger logger = Logger.getLogger(AgenteEpistemico.class);
	private List<ParEpistemico> crencas = new ArrayList<ParEpistemico>();
	
	/**
	 * O sistema ficou muito lento ao formatar no toString()
	 * fica aqui para documentar o problema
	 */
	private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.000", new DecimalFormatSymbols (new Locale ("pt", "BR")));
	
	/**
	 * Arestas que saem deste agente para outros<br>
	 * arestas usadas para falar
	 */
	private List<Aresta> arestasSaida = new ArrayList<Aresta>();
	private List<Aresta> arestasEntrada = new ArrayList<Aresta>();
	private NeuralNetwork neuralNetwork;
	private int x,y;
	private int raio;
	private int numberEvaluations = 100;
	private String nome;
	private static long lastId=0;
	private long id;
	private int qtdParComunicado=0;
	private int criarNovoEm = 10;
	private boolean somenteUltimaTeoria = true;
	private int morrerEmXpublicacoes = 100; 
	private Double pesoReputacao = null;
	private Color color;
	private double vontadeDePublicar=1.0;
	private Foco foco;
	private double maxAtencao = 100;
	private RedeEpistemica redeEpistemica;
	
	/**
	 * Rede
	 * @return rede a qual este agente pertence
	 */
	public RedeEpistemica getRedeEpistemica() {
		return redeEpistemica;
	}
	
	/**
	 * Atribui a rede
	 * @param redeEpistemica rede a qual este agente pertence
	 */
	public void setRedeEpistemica(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
	}
	
	/**
	 * Foco
	 * @return foco do agente
	 */
	public Foco getFoco() {
		return foco;
	}
	
	/**
	 * Foco
	 * @param foco do agente
	 */
	public void setFoco(Foco foco) {
		this.foco = foco;
	}
	
	/**
	 * Representa a vontade de publicar alguma coisa	
	 * @return Double normalmente de 0 a 1
	 */
	public double getVontadeDePublicar() {
		return vontadeDePublicar;
	}
	
	/**
	 * Representa a vontade de publicar alguma coisa	
	 * @param vontadeDePublicar normalmente de 0 a 1
	 */
	public void setVontadeDePublicar(double vontadeDePublicar) {
		this.vontadeDePublicar = vontadeDePublicar;
	}
	
	/**
	 * Cor para desenhar o agente
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Cor para desenhar o agente
	 * @param color cor para o desenho na interface do usu&aacute;rio
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * O maximo de diferenca para rejeitar o resultado
	 */
	private Double maxDiff = 0.2;
	
	public AgenteEpistemico() {
		//int[] neuralNetworkStructure = new int[] { 3, 4, 2 };
		int[] neuralNetworkStructure = new int[] { 5, 4, 1 };
		// create new neural network with the defined structure
		neuralNetwork = new NeuralNetwork(neuralNetworkStructure);
		neuralNetwork.initWeights();
		foco = FocoFactory.criarFoco();
		//gerarPar();
		//gera um nome padrao
		id = lastId++;
		setNome("A" + id);
	}
	
	/**
	 * Identificador do agente
	 * @return Long
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Identificador do agente
	 * @param id Long
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * De quanto em quanto tempo publica um novo par para publicar<br>
	 * coloque 1 para publicar um novo par epistemico sempre.
	 * @return int
	 */
	public int getCriarNovoEm() {
		return criarNovoEm;
	}
	
	/**
	 * De quanto em quanto tempo publica um novo par para publicar<br>
	 * coloque 1 para publicar um novo par epistemico sempre.
	 * @param criarNovoEm
	 */
	public void setCriarNovoEm(int criarNovoEm) {
		this.criarNovoEm = criarNovoEm;
	}
	
	/**
	 * M&aacute;ximo de diferen&ccedil;a de um consequente para o outro
	 * para que este agente aceite o resultado durante o receberComunicado e fa&ccedil;a um treinamento.
	 * @return Double
	 */
	public Double getMaxDiff() {
		return maxDiff;
	}
	
	/**
	 * M&aacute;ximo de diferen&ccedil;a de um consequente para o outro
	 * para que este agente aceite o resultado durante o receberComunicado e fa&ccedil;a um treinamento.
	 * @param maxDiff Double
	 */
	public void setMaxDiff(Double maxDiff) {
		this.maxDiff = maxDiff;
	}
	
	/**
	 * Nome deste agente
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * 
	 * @param nome nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Sorteia um novo espaço X,Y e Z e gera um par,
	 * delegado para ParEpistemicoFactory
	 * @return novo par
	 */
	private ParEpistemico criarNovoPar(){
		ParEpistemico parEpistemico;
		if(false){
			ParEpistemico3x2 parEpistemico2 = ParEpistemicoFactory.criar(foco);
			//atualizar o consequente
			Consequente consequente = new Consequente();
			consequente = new Consequente(neuralNetwork.getOutputs());
			parEpistemico2.setConsequente(consequente);
			return parEpistemico2;
		}else{
			parEpistemico = ParEpistemicoFactory.criar(5,1);
			parEpistemico = interpretar(parEpistemico);
		}
		return parEpistemico;
	}
	
	/**
	 * Se for nulo, calcula o valor de todas as arestas de saida.<br>
	 * Se nao, retorna o valor já calculado
	 * 
	 * @return valor somado de todas as arestas
	 */
	public double getPesoReputacao(){
		if(pesoReputacao==null){
			double res = 0;
			synchronized (arestasSaida) {
				for(Aresta aresta:arestasSaida){
					res+=aresta.getPeso();
				}
			}
			pesoReputacao=res;
		}
		return pesoReputacao;
	}
	
	/**
	 * Usado para:<br>
	 * 1 - zerar, ou seja, colocar null
	 * quando null o peso é todo recalculado
	 * <br>
	 * 2 - Normalizar o peso em {@link RedeEpistemica#normalizarPesos()}
	 * @param pesoReputacao
	 */
	public void setPesoReputacao(Double pesoReputacao){
		this.pesoReputacao = pesoReputacao;
	}
	
	/**
	 * Usado para comodidade<br>
	 * pesoReputacao = getPesoTotal()+x;
	 * @param x valor a ser adicionado
	 */
	public void addPesoReputacao(double x){
		pesoReputacao = getPesoReputacao()+x;
	}
	
	/**
	 * Gera um par aleatorio,
	 * ou pega um par aleatorio do conhecimento
	 */
	public ParEpistemico gerarPar(){
		qtdParComunicado++;
		
		ParEpistemico parEpistemico;
		if(crencas.size()==0){
			//iniciar
			parEpistemico = criarNovoPar();
			treinar(parEpistemico,500);
			parEpistemico = interpretar(parEpistemico);
			crencas.add(parEpistemico);
		}else if(qtdParComunicado%criarNovoEm==0){
			//Nova teoria
			parEpistemico = criarNovoPar();
			treinar(parEpistemico,500);
			parEpistemico = interpretar(parEpistemico);
			crencas.add(parEpistemico);
		}else if(somenteUltimaTeoria){
			parEpistemico = crencas.get(crencas.size()-1);
		}else{
			int indice = (int)((crencas.size())*NumeroAleatorio.gerarNumero());
			logger.debug("crenca " + indice);
			parEpistemico = crencas.get(indice);
		}
		
		
		return parEpistemico;
	}
	
	/**
	 * Somente interpreta e retorna um novo par de
	 * acordo com a sua visao
	 * @param parEpistemico
	 * @return ParEpistemico
	 */
	public ParEpistemico interpretar(ParEpistemico parEpistemico){
		ParEpistemico retorno = null;
		try {
			retorno = (ParEpistemico)parEpistemico.clone();
			for(int i=0;i<parEpistemico.getSizeAntecedente();i++){
				retorno.addAntecedente(parEpistemico.getDoubleAntecedentes().get(i));
			}
			neuralNetwork.setInputs(parEpistemico.getDoubleAntecedentes());
			for(int i=0;i<parEpistemico.getSizeConsequente();i++){
				retorno.addConsequente(neuralNetwork.getOutputs().get(i));
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	private ArrayList<Double> getInputs(ParEpistemico3x2 parEpistemico){
		ArrayList<Double> in = new ArrayList<Double>();
		in.add(parEpistemico.getAntecedente().getX());
		in.add(parEpistemico.getAntecedente().getY());
		in.add(parEpistemico.getAntecedente().getZ());
		return in;
	}
	
	/**
	 * Treina mas nao adiciona nas crencas
	 * @param pares
	 * @param qtd
	 */
	public void treinar(List<ParEpistemico> pares, int qtd) {
		ArrayList<TrainingExample> listTraining = new ArrayList<TrainingExample>();
		for(ParEpistemico parEpistemicoInformado:pares){
			TrainingExample te = new TrainingExample();
			ArrayList<Double> in = parEpistemicoInformado.getDoubleAntecedentes();
			ArrayList<Double> out = parEpistemicoInformado.getDoubleConsequentes();
			if(out.size()!=parEpistemicoInformado.getSizeConsequente()){
				throw new RuntimeException("Out errado!");
			}
			if(in.size()!=parEpistemicoInformado.getSizeAntecedente()){
				throw new RuntimeException("In errado!");
			}
			te.setInputs(in);
			te.setOutputs(out);
			listTraining.add(te);
		}
		double errorTolerance = 0.0; 
		
		// pso specific settings
		int numberParticles = 20; 
		double learningFactor = 1.49618; 
		double inertialWeight = 0.7298; 
		
		logger.debug("qtd = " + qtd);
		// create a instance of a training method
		//Training training = new ParticleSwarmOptimization(numberEvaluations, errorTolerance, learningFactor, inertialWeight, numberParticles);
		Training training = new BackPropagation(qtd,errorTolerance);
		
		// set the training method and set for the neural network to use
		neuralNetwork.setTraining(training);
		TrainingSet trainingSet = new TrainingSet("MyT",listTraining);
		//neuralNetwork.setTrainingSet(trainingData.getTrainingSet());
		neuralNetwork.setTrainingSet(trainingSet);
		
		neuralNetwork.train();
		
	}	
	
	/**
	 * Treina mas nao adiciona nas crencas
	 * @param parEpistemicoInformado
	 * @param qtd
	 */
	public void treinar(ParEpistemico parEpistemicoInformado,int qtd){
		List<ParEpistemico> pares = new ArrayList<ParEpistemico>();
		pares.add(parEpistemicoInformado);
		treinar(pares, qtd);
	}
	
	/**
	 * Recebe um comunicado, uma publica&ccedil;&atilde;o
	 * @param parEpistemicoInformado par informado
	 * @param aresta relacao com o emissor
	 * @param emissor Agente emissor, publicador
	 * @return Double deltaErro
	 */
	public Double receberComunicado(ParEpistemico parEpistemicoInformado,Aresta aresta, AgenteEpistemico emissor){
		double peso = aresta.getPeso();
		double deltaErro;
		//guarda a informacao?
		ParEpistemico parEpistemicoExistente = procurar(parEpistemicoInformado);
		if(parEpistemicoExistente==null){
			logger.debug("Aprendendo " + parEpistemicoInformado);
			parEpistemicoExistente = interpretar(parEpistemicoInformado);
			deltaErro = parEpistemicoExistente.calcularDiferencaConsequente(parEpistemicoInformado);
			if(maxDiff>deltaErro){
				treinar(parEpistemicoInformado,(int)(numberEvaluations * peso * deltaErro));
			}//else recusa a aprender
			
		}else{
			//ja existe, ver diferenca
			neuralNetwork.train();
			ParEpistemico parEpistemicoPessoalDepoisTreino = interpretar(parEpistemicoInformado);
			logger.debug("evolucao depois treino = "+
			parEpistemicoExistente.calcularDiferencaConsequente(parEpistemicoPessoalDepoisTreino)
			);
			for(int i=0;i<parEpistemicoPessoalDepoisTreino.getSizeConsequente();i++){
				parEpistemicoExistente.addConsequente(parEpistemicoPessoalDepoisTreino.getDoubleConsequentes().get(i));
			}
			
			deltaErro = parEpistemicoPessoalDepoisTreino.calcularDiferencaConsequente(parEpistemicoInformado);
		}
		if(emissor!=null){
			double delta = 0.2 * (1.0/(deltaErro+1.0)) * peso; /*regra de Hebb*/
			aresta.setPeso(peso + delta);
			emissor.addPesoReputacao(delta);
			//retirar o delta dos outros agentes
			//retirarDelta(delta,emissor,peso);
			distribuirAtencao();
		}
		return deltaErro;
	}
	private void distribuirAtencao(){
		//recuperar o maximo
		double max = arestasEntrada.get(0).getPeso();
		double total = 0;
		for(Aresta aresta:arestasEntrada){
			max = Math.max(aresta.getPeso(), max);
			total +=aresta.getPeso();
		}
		for(Aresta aresta:arestasEntrada){
			double peso = aresta.getPeso()/total;
			peso = peso*maxAtencao;
			aresta.setPeso(peso);
		}
	}
	private void retirarDelta(double delta,AgenteEpistemico emissor,double pesoEmissor) {
		if(redeEpistemica.isNormalizarPesos()) return;
		double relacao = delta/(maxAtencao-pesoEmissor);
		double retirado = 0;
		for(Aresta aresta:arestasEntrada){
			if(!aresta.getEmissor().equals(emissor)){
				double peso = aresta.getPeso(); 
				double retirar = peso*relacao;
				retirado+=retirar;
				aresta.setPeso(peso-retirar);
			}
		}
		if(retirado!=delta){
			//throw new RuntimeException("Erro de precisao?"+(delta-retirado));
		}
	}
	/**
	 * Procura dentro da crenca
	 * @param parAntecedente
	 * @return ParEpistemico
	 */
	private ParEpistemico procurar(ParEpistemico parAntecedente){
		for(ParEpistemico parEpistemico:crencas){
			if(parEpistemico.antecedenteEquals(parAntecedente)){
				return parEpistemico;
			}
		}
		return null;
	}
	
	/**
	 * Arestas para comunicar
	 * @return Arestas de Saida de fala
	 */
	public List<Aresta> getArestas() {
		return arestasSaida;
	}
	
	/**
	 * Arestas para comunicar
	 * @param arestas Arestas
	 */
	public void setArestas(List<Aresta> arestas) {
		this.arestasSaida = arestas;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Coloca na lista de arestas
	 * @param agenteNovo novo agente
	 * @param peso novo peso
	 */
	public void conhecer(AgenteEpistemico agenteNovo, double peso) {
		Aresta aresta = new Aresta();
		aresta.setEmissor(this);
		aresta.setReceptor(agenteNovo);
		aresta.setPeso(peso);
		synchronized (arestasSaida) {
			arestasSaida.add(aresta);
			agenteNovo.arestasEntrada.add(aresta);
		}
		pesoReputacao = null;
	}

	public int getRaio() {
		return raio;
	}
	public void setRaio(int raio) {
		this.raio = raio;
	}
	
	
	/**
	 * retorna o this.nome
	 */
	@Override
	public String toString() {
		if(color!=null){
			return nome + " ["+qtdParComunicado+"] " + getPesoReputacao()+ "*";	
		}else{
			return nome + " ["+qtdParComunicado+"] " + getPesoReputacao();
		}
	}

	/**
	 * Usado para matar o agente.
	 * Pede para todos que possui aresta que
	 * remova ele
	 */
	public void morrer() {
		for(Aresta aresta:arestasSaida){
			aresta.getReceptor().removerAgente(this);
		}
	}

	/**
	 * Remove o agente informado das arestas de saida
	 * @param agenteEpistemico Agente
	 */
	private void removerAgente(AgenteEpistemico agenteEpistemico) {
		Aresta aresta = new Aresta();
		aresta.setReceptor(agenteEpistemico);
		arestasSaida.remove(aresta);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		AgenteEpistemico a = (AgenteEpistemico)obj;
		logger.debug("a.id==this.id " + (a.id==this.id));
		return a.id==this.id;
	}
	
	/**
	 * Quantidade de pares publicados
	 * @return int
	 */
	public int getQtdParComunicado() {
		return qtdParComunicado;
	}
	
	/**
	 * Configura um n&uacute;mero para que depois de publicar esta quantidade,
	 * o agente morra
	 * @return int
	 */
	public int getMorrerEmXpublicacoes() {
		return morrerEmXpublicacoes;
	}
	
	/**
	 * zero para imortal
	 * @param morrerEmXpublicacoes
	 */
	public void setMorrerEmXpublicacoes(int morrerEmXpublicacoes) {
		this.morrerEmXpublicacoes = morrerEmXpublicacoes;
	}
	
	/**
	 * true publicar&aacute; somente a ultima teoria criada
	 * @param somenteUltimaTeoria boolean
	 */
	public void setSomenteUltimaTeoria(boolean somenteUltimaTeoria) {
		this.somenteUltimaTeoria = somenteUltimaTeoria;
	}
	
	/**
	 * Cren&ccedil;as deste agente
	 * @return lista de pares epistemicos
	 */
	public List<ParEpistemico> getCrencas() {
		return crencas;
	}
	
	/**
	 * Adiciona se nao existir
	 * @param pares
	 */
	public void addCrencas(List<ParEpistemico> pares) {
		for(ParEpistemico par:pares){
			ParEpistemico procurado = procurar(par);
			if(procurado==null){
				crencas.add(par);
			}
		}
	}
	
	/**
	 * Verifica se o agente quer publicar
	 * @return true caso queira publicar
	 */
	public boolean querPublicar() {
		return NumeroAleatorio.gerarNumero()<vontadeDePublicar;
	}
	
}
