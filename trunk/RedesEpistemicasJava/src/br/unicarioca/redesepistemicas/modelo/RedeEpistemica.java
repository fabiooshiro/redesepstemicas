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
				AgenteEpistemico agenteEpistemico;
				int tentativa=0;
				while(true){
					int agente = (int)((double)listAgenteEpistemico.size()*NumeroAleatorio.gerarNumero());
					logger.debug("agente = "  + (agente+1) + " de " + listAgenteEpistemico.size());
					agenteEpistemico = listAgenteEpistemico.get(agente);
					//ver se ele quer publicar
					if(agenteEpistemico.querPublicar()){
						break;
					}
					if(tentativa>100){
						return;
					}
					tentativa++;
				}
				if(agenteEpistemico.getMorrerEmXpublicacoes()!=0 && agenteEpistemico.getQtdParComunicado()>agenteEpistemico.getMorrerEmXpublicacoes()){
					matarAgente(agenteEpistemico);
					if(agenteEpistemicoFactory!=null){
						agenteEpistemicoFactory.criarAgente();
					}
				}else{
					ParEpistemico parEpistemico = agenteEpistemico.gerarPar();
					if(comunicacaoListener!=null) comunicacaoListener.comunicadorEscolhido(agenteEpistemico);
					synchronized (agenteEpistemico.getArestas()) {
						for(Aresta aresta:agenteEpistemico.getArestas()){
							AgenteEpistemico receptor = aresta.getAgenteEpistemico();
							Double peso = aresta.getPeso();
							Double diff = receptor.receberComunicado(parEpistemico, aresta,agenteEpistemico);
							logger.debug("diff = "  + diff + " peso " + aresta.getPeso());
							
							if(comunicacaoListener!=null) comunicacaoListener.depoisDeComunicar(agenteEpistemico, receptor, peso,diff);
						}
					}
				}
			}
		}
	}
	/**
	 * Inicio da morte
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
	public AgenteEpistemico inserirAgente(AgenteEpistemico agenteNovo,boolean pesoAleatorio) {
		
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
}
