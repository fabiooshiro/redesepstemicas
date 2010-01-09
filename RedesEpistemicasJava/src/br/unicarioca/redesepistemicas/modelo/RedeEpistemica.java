package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RedeEpistemica {
	private static Logger logger = Logger.getLogger(RedeEpistemica.class);
	private List<AgenteEpistemico> listAgenteEpistemico = new ArrayList<AgenteEpistemico>();
	private ComunicacaoListener comunicacaoListener = null;
	private CicloVidaAgenteListener cicloVidaAgenteListener;
	private AgenteEpistemicoFactory agenteEpistemicoFactory;
	private boolean normalizarPesos = false;
	private boolean ligado = false;
	private Thread t;
	private int etapa;
	public void setCicloVidaAgenteListener(CicloVidaAgenteListener cicloVidaAgenteListener) {
		this.cicloVidaAgenteListener = cicloVidaAgenteListener;
	}
	public CicloVidaAgenteListener getCicloVidaAgenteListener() {
		return cicloVidaAgenteListener;
	}
	public void setComunicacaoListener(ComunicacaoListener comunicacaoListener) {
		this.comunicacaoListener = comunicacaoListener;
	}
	public List<AgenteEpistemico> getListAgenteEpistemico() {
		return listAgenteEpistemico;
	}

	public void setListAgenteEpistemico(List<AgenteEpistemico> listAgenteEpistemico) {
		this.listAgenteEpistemico = listAgenteEpistemico;
	}
	
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
						Double peso = aresta.getPeso();
						Double diff = receptor.receberComunicado(parEpistemico, aresta,agenteEmissor);
						logger.debug("diff = "  + diff + " peso " + aresta.getPeso());
						if(comunicacaoListener!=null) comunicacaoListener.depoisDeComunicar(agenteEmissor, receptor, peso,diff);
					}
					normalizarPesos();
					etapa++;
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
		if(cicloVidaAgenteListener!=null){
			cicloVidaAgenteListener.criado(agenteNovo);
		}
		return agenteNovo;
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
	public void setNormalizarPesos(boolean normalizarPesos) {
		this.normalizarPesos = normalizarPesos;
	}
	public boolean isNormalizarPesos() {
		return normalizarPesos;
	}
	public int getEtapa() {
		return etapa;
	}
}
