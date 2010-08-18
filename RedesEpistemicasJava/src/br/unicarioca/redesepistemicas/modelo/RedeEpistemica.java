package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Representa a rede epistemica, possui a lista dos N agentes da rede.<br>
 * Guarda configuracoes da rede, o numero de etapas que o algoritmo executou e o proprio algoritmo.<br>
 */
public class RedeEpistemica {
	private static Logger logger = Logger.getLogger(RedeEpistemica.class);
	private List<AgenteEpistemico> listAgenteEpistemico = new ArrayList<AgenteEpistemico>();
	private ComunicacaoListener comunicacaoListener = null;
	private CicloVidaAgenteListener cicloVidaAgenteListener;
	private AgenteEpistemicoFactory agenteEpistemicoFactory;
	private boolean normalizarPesos = false;
	private boolean ligado = false;
	private Experimento experimento;
	private Thread t;
	private static int etapa;
	
	public Experimento getExperimento() {
		return experimento;
	}
	
	public void setExperimento(Experimento experimento) {
		this.experimento = experimento;
	}
	
	/**
	 * Listener de ciclo de vida de um agente<br>
	 * que recebe eventos de quando o agente &eacute; criado ou morto
	 * @param cicloVidaAgenteListener CicloVidaAgenteListener
	 */
	public void setCicloVidaAgenteListener(CicloVidaAgenteListener cicloVidaAgenteListener) {
		this.cicloVidaAgenteListener = cicloVidaAgenteListener;
	}
	
	/**
	 * Listener de ciclo de vida de um agente
	 * @return CicloVidaAgenteListener
	 */
	public CicloVidaAgenteListener getCicloVidaAgenteListener() {
		return cicloVidaAgenteListener;
	}
	
	/**
	 * Listener de comunicacao
	 * @param comunicacaoListener ComunicacaoListener
	 */
	public void setComunicacaoListener(ComunicacaoListener comunicacaoListener) {
		this.comunicacaoListener = comunicacaoListener;
	}
	
	/**
	 * Lista de agentes
	 * @return List AgenteEpistemico
	 */
	public List<AgenteEpistemico> getListAgenteEpistemico() {
		return listAgenteEpistemico;
	}

	public void setListAgenteEpistemico(List<AgenteEpistemico> listAgenteEpistemico) {
		this.listAgenteEpistemico = listAgenteEpistemico;
	}
	
	/**
	 * Faz um passo do algoritmo que consiste em sortear um agente. <br>
	 * Verificar se ele deseja publicar.<br>
	 * pedir ao agente que gere o par a ser publicado.<br>
	 * transmitir o par para todos os outros agentes.<br>
	 */
	public void fazUmaEtapa(){
		synchronized (listAgenteEpistemico) {
			if(listAgenteEpistemico.size()>1){
				AgenteEpistemico agenteEmissor;
				int tentativa=0;
				while(true){
					int agente = (int)((double)listAgenteEpistemico.size()*NumeroAleatorio.gerarNumero());
					logger.debug("agente = "  + (agente+1) + " de " + listAgenteEpistemico.size());
					agenteEmissor = listAgenteEpistemico.get(agente);
					//ver se ele quer publicar
					if(agenteEmissor.querPublicar()) break;
					//ver se passou do maximo de tentativas
					if(tentativa>100) return;
					tentativa++;
				}
				if(agenteEmissor.getMorrerEmXpublicacoes()!=0 && agenteEmissor.getQtdParComunicado()>agenteEmissor.getMorrerEmXpublicacoes()){
					matarAgente(agenteEmissor);
					if(agenteEpistemicoFactory!=null){
						agenteEpistemicoFactory.criarAgente();
					}
				}else{
					//comunicar o par para todos os agentes
					ParEpistemico parEpistemico = agenteEmissor.gerarPar();
					logger.info("Agente " + agenteEmissor.getId() + " comunicando...");
					if(comunicacaoListener!=null) comunicacaoListener.comunicadorEscolhido(agenteEmissor);
					for(int i=0;i<agenteEmissor.getArestas().size();i++){
						Aresta aresta = agenteEmissor.getArestas().get(i);
						AgenteEpistemico receptor = aresta.getReceptor();
						if(receptor == agenteEmissor) continue;
						Double peso = aresta.getPeso();
						Double diff = receptor.receberComunicado(parEpistemico, aresta,agenteEmissor);
						logger.debug("diff = "  + diff + " peso " + aresta.getPeso());
						if(comunicacaoListener!=null) comunicacaoListener.depoisDeComunicar(agenteEmissor, receptor, peso,diff);
					}
					normalizarPesos();
					colorirAgentesDoExperimento(experimento);
					etapa++;
				}
			}//fim do if(listAgenteEpistemico.size()>1){
		}
	}
	
	public void colorirAgentesDoExperimento(Experimento experimento){
		//lance do experimento de cores
		if(experimento!=null){
			Set<ParEpistemico> p = experimento.getSetCrencas();
			for(AgenteEpistemico agente: listAgenteEpistemico){
				agente.crencaMonitorada.clear();
				for(ParEpistemico crenca:p){
					ParEpistemico opiniao = agente.interpretar(crenca);
					double diff = opiniao.calcularDiferencaConsequente(crenca);
					if(diff <= experimento.getMaxDiff()){//acredita
						agente.crencaMonitorada.add(crenca);
						agente.crencaMonitorada.add(crenca);
					}else if(diff <= experimento.getMinDiff()){//quase acredita
						try {
							ParEpistemico par2 = (ParEpistemico)crenca.clone();
							par2.setCor(new Color(0xEEEEEE));
							agente.crencaMonitorada.add(par2);
							agente.crencaMonitorada.add(crenca);
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}else{//Nao acredita
						try {
							ParEpistemico par2 = (ParEpistemico)crenca.clone();
							par2.setCor(new Color(0xEEEEEE));
							agente.crencaMonitorada.add(par2);
							agente.crencaMonitorada.add(par2);
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Normaliza os pesos deixando todos em um valor de 0 ate 1
	 */
	protected void normalizarPesos(){
		{
			//validacoes de dados
			if(!normalizarPesos) return;
			if(listAgenteEpistemico.size()==0) return;
		}
		logger.info("Normalizando pesos");
		//recuperar o maximo
		double max = listAgenteEpistemico.get(0).getPesoReputacao();
		for(AgenteEpistemico agente:listAgenteEpistemico)
			max = Math.max(agente.getPesoReputacao(), max);
		logger.debug("max="+max);
		//normalizar
		for(AgenteEpistemico agente:listAgenteEpistemico){
			double newValue = agente.getPesoReputacao()/max;
			agente.setPesoReputacao(newValue);
		}
		
	}
	
	/**
	 * Inicio da morte,
	 * o processo de morte inicia por este metodo
	 * @param agente
	 */
	public void matarAgente(AgenteEpistemico agente){
		//retirar ele da lista
		if(cicloVidaAgenteListener!=null){
			cicloVidaAgenteListener.morto(agente);
		}
		listAgenteEpistemico.remove(agente);
		agente.morrer();
		
	}
	
	/**
	 * Insere um agente na rede e apresenta ele a todos os outros agentes
	 * criando as arestas necessarias
	 * @param agenteNovo agente novo
	 * @param pesoAleatorio true para peso aleatorio
	 * @return AgenteEpistemico
	 */
	public AgenteEpistemico inserirAgente(AgenteEpistemico agenteNovo,boolean pesoAleatorio) {
		agenteNovo.setRedeEpistemica(this);
		agenteNovo.setRaio(50);
		//apresentar a todos
		if(pesoAleatorio){
			for(AgenteEpistemico agenteEpistemico:listAgenteEpistemico){
				agenteEpistemico.conhecer(agenteNovo, NumeroAleatorio.gerarNumero());
				agenteNovo.conhecer(agenteEpistemico, NumeroAleatorio.gerarNumero());
			}
		}else{
			for(AgenteEpistemico agenteEpistemico:listAgenteEpistemico){
				agenteEpistemico.conhecer(agenteNovo, 1.0);
				agenteNovo.conhecer(agenteEpistemico, 1.0);
			}
		}
		synchronized (listAgenteEpistemico) {
			listAgenteEpistemico.add(agenteNovo);
		}
		fireAgenteCriadoEvent(agenteNovo);
		return agenteNovo;
	}
	
	public void fireAgenteCriadoEvent(AgenteEpistemico agenteNovo){
		if(cicloVidaAgenteListener!=null){
			cicloVidaAgenteListener.criado(agenteNovo);
		}
	}
	/**
	 * Quem providencia um agente criado inteiramente
	 * chama o metodo criarAgente
	 * @param agenteEpistemicoFactory
	 */
	public void setAgenteEpistemicoFactory(AgenteEpistemicoFactory agenteEpistemicoFactory) {
		this.agenteEpistemicoFactory = agenteEpistemicoFactory;
	}
	
	/**
	 * Loop independente da view
	 * @param b
	 */
	public void ligarLoop(boolean b) {
		if(true)return;
		ligado = b;
		if(t==null){
			t = new Thread(){
				@Override
				public void run() {
					while(true){
						if(ligado){
							try{
								fazUmaEtapa();
							}catch(Exception e){
								
							}
						}
					}
				}
			};
			t.start();
		}
	}
	
	/**
	 * Flag para saber se normalizamos ou nao os pesos
	 * @param normalizarPesos
	 */
	public void setNormalizarPesos(boolean normalizarPesos) {
		this.normalizarPesos = normalizarPesos;
	}
	
	/**
	 * Flag para saber se normalizamos ou nao os pesos
	 * @return boolean
	 */
	public boolean isNormalizarPesos() {
		return normalizarPesos;
	}
	
	/**
	 * Retorna quantas vezes o algoritmo foi executado
	 * @return int
	 */
	public int getEtapa() {
		return etapa;
	}
	
	public static void setEtapa(int etapa) {
		RedeEpistemica.etapa = etapa;
	}
}
