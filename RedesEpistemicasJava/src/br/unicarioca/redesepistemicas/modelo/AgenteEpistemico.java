package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
	private List<Aresta> arestas = new ArrayList<Aresta>();  
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
	private Double reputacao = null;
	private Color color;
	private double vontadeDePublicar=1.0;
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
	 * Sorteia um novo espaço X,Y e Z e gera um par
	 * @return novo par
	 */
	private ParEpistemico criarNovoPar(){
		ParEpistemico parEpistemico;
		parEpistemico = new ParEpistemicoDiffHip();
		Antecedente antecedente = new Antecedente();
		antecedente.setX(NumeroAleatorio.gerarNumero());
		antecedente.setY(NumeroAleatorio.gerarNumero());
		antecedente.setZ(NumeroAleatorio.gerarNumero());
		parEpistemico.setAntecedente(antecedente);
		Consequente consequente = new Consequente();
		consequente.setX(NumeroAleatorio.gerarNumero());
		consequente.setY(NumeroAleatorio.gerarNumero());
		parEpistemico.setConsequente(consequente);
		Aresta aresta = new Aresta();
		aresta.setAgenteEpistemico(null);
		aresta.setPeso(50.0);
		//receberComunicado(parEpistemico, aresta);
		//atualizar o consequente
		consequente = new Consequente(neuralNetwork.getOutputs());
		parEpistemico.setConsequente(consequente);
		return parEpistemico;
	}
	
	public double getReputacao(){
		if(reputacao==null){
			double res = 0;
			synchronized (arestas) {
				for(Aresta aresta:arestas){
					res+=aresta.getPeso();
				}
			}
			reputacao=res;
		}
		return reputacao;
	}
	
	public void addReputacao(double x){
		reputacao = getReputacao()+x;
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
	public Double receberComunicado(ParEpistemico parEpistemicoInformado,Aresta aresta, AgenteEpistemico agenteEpistemico){
		double peso = aresta.getPeso();
		double diff;
		//guarda a informacao?
		ParEpistemico parEpistemicoExistente = procurar(parEpistemicoInformado.getAntecedente());
		if(parEpistemicoExistente==null){
			logger.debug("Aprendendo " + parEpistemicoInformado);
			parEpistemicoExistente = interpretar(parEpistemicoInformado);
			diff = parEpistemicoExistente.calcularDiferencaConsequente(parEpistemicoInformado);
			if(maxDiff>diff){
				//crenca.add(parEpistemicoExistente);
				int qtd = (int)(numberEvaluations * peso);
				treinar(parEpistemicoInformado,qtd);
			}//else recusa a aprender
			
		}else{
			//ja existe, ver diferenca
			neuralNetwork.train();
			ParEpistemico parEpistemicoPessoalDepoisTreino = interpretar(parEpistemicoInformado);
			logger.debug("evolucao depois treino = "+
			parEpistemicoExistente.calcularDiferencaConsequente(parEpistemicoPessoalDepoisTreino)
			);
			parEpistemicoExistente.setConsequente(parEpistemicoPessoalDepoisTreino.getConsequente());
			
			diff = parEpistemicoPessoalDepoisTreino.calcularDiferencaConsequente(parEpistemicoInformado);
		}
		if(agenteEpistemico!=null){
			double delta = 0.2 * (1.0/(diff+1.0)) * peso; /*regra de Hebb*/
			aresta.setPeso(peso + delta);
			agenteEpistemico.addReputacao(delta);
		}
		return diff;
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
	
	public List<Aresta> getArestas() {
		return arestas;
	}
	
	public void setArestas(List<Aresta> arestas) {
		this.arestas = arestas;
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
		aresta.setAgenteEpistemico(agenteNovo);
		aresta.setPeso(peso);
		synchronized (arestas) {
			arestas.add(aresta);
		}
		reputacao = null;
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
			return nome + " ["+qtdParComunicado+"] " + Math.round(getReputacao()*10)/10.0 + "*";	
		}else{
			return nome + " ["+qtdParComunicado+"] " + Math.round(getReputacao()*10)/10.0;
		}
		
	}

	public void morrer() {
		for(Aresta aresta:arestas){
			aresta.getAgenteEpistemico().removerAgente(this);
		}
	}

	private void removerAgente(AgenteEpistemico agenteEpistemico) {
		Aresta aresta = new Aresta();
		aresta.setAgenteEpistemico(agenteEpistemico);
		arestas.remove(aresta);
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
