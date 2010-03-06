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
	
	public RedeEpistemica getRedeEpistemica() {
		return redeEpistemica;
	}
	public void setRedeEpistemica(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
	}
	public Foco getFoco() {
		return foco;
	}
	public void setFoco(Foco foco) {
		this.foco = foco;
	}
	public double getVontadeDePublicar() {
		return vontadeDePublicar;
	}
	public void setVontadeDePublicar(double vontadeDePublicar) {
		this.vontadeDePublicar = vontadeDePublicar;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * O maximo de diferenca para rejeitar o resultado
	 */
	private Double maxDiff = 0.2;
	
	public AgenteEpistemico() {
		int[] neuralNetworkStructure = new int[] { 3, 4, 2 };

		// create new neural network with the defined structure
		neuralNetwork = new NeuralNetwork(neuralNetworkStructure);
		neuralNetwork.initWeights();
		foco = FocoFactory.criarFoco();
		//gerarPar();
		//gera um nome padrao
		id = lastId++;
		setNome("A" + id);
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCriarNovoEm() {
		return criarNovoEm;
	}
	
	public void setCriarNovoEm(int criarNovoEm) {
		this.criarNovoEm = criarNovoEm;
	}
	
	public Double getMaxDiff() {
		return maxDiff;
	}
	
	public void setMaxDiff(Double maxDiff) {
		this.maxDiff = maxDiff;
	}
	
	public String getNome() {
		return nome;
	}
	
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
		parEpistemico = ParEpistemicoFactory.criar(foco);
		//atualizar o consequente
		Consequente consequente = new Consequente();
		consequente = new Consequente(neuralNetwork.getOutputs());
		parEpistemico.setConsequente(consequente);
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
		ParEpistemico retorno = new ParEpistemicoDiffHip();
		retorno.setAntecedente(parEpistemico.getAntecedente());
		neuralNetwork.setInputs(getInputs(parEpistemico));
		Consequente consequente = new Consequente(neuralNetwork.getOutputs());
		retorno.setConsequente(consequente);
		return retorno;
	}
	
	private ArrayList<Double> getInputs(ParEpistemico parEpistemico){
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
			ArrayList<Double> in = getInputs(parEpistemicoInformado);
			ArrayList<Double> out = new ArrayList<Double>();
			out.add(parEpistemicoInformado.getConsequente().getX());
			out.add(parEpistemicoInformado.getConsequente().getY());
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
		ArrayList<TrainingExample> listTraining = new ArrayList<TrainingExample>();
		
		TrainingExample te = new TrainingExample();
		ArrayList<Double> in = getInputs(parEpistemicoInformado);
		ArrayList<Double> out = new ArrayList<Double>();
		out.add(parEpistemicoInformado.getConsequente().getX());
		out.add(parEpistemicoInformado.getConsequente().getY());
		te.setInputs(in);
		te.setOutputs(out);
		listTraining.add(te);
		
		double errorTolerance = 0.2; 
		
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
	public Double receberComunicado(ParEpistemico parEpistemicoInformado,Aresta aresta, AgenteEpistemico emissor){
		double peso = aresta.getPeso();
		double deltaErro;
		//guarda a informacao?
		ParEpistemico parEpistemicoExistente = procurar(parEpistemicoInformado.getAntecedente());
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
			parEpistemicoExistente.setConsequente(parEpistemicoPessoalDepoisTreino.getConsequente());
			
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
	 * @param antecedente
	 * @return ParEpistemico
	 */
	private ParEpistemico procurar(Antecedente antecedente){
		for(ParEpistemico parEpistemico:crencas){
			if(parEpistemico.getAntecedente().equals(antecedente)){
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
	
	private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.000", new DecimalFormatSymbols (new Locale ("pt", "BR")));
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

	public void morrer() {
		for(Aresta aresta:arestasSaida){
			aresta.getReceptor().removerAgente(this);
		}
	}

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
	public int getQtdParComunicado() {
		return qtdParComunicado;
	}
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
	
	public void setSomenteUltimaTeoria(boolean somenteUltimaTeoria) {
		this.somenteUltimaTeoria = somenteUltimaTeoria;
	}
	public List<ParEpistemico> getCrencas() {
		return crencas;
	}
	/**
	 * Adiciona se nao existir
	 * @param pares
	 */
	public void addCrencas(List<ParEpistemico> pares) {
		for(ParEpistemico par:pares){
			ParEpistemico procurado = procurar(par.getAntecedente());
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
