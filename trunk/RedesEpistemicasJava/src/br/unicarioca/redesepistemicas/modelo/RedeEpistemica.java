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
	private boolean ligado = false;
	private Thread t;
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
					if(agenteEmissor.querPublicar()){
						break;
					}
					if(tentativa>100){
						return;
					}
					tentativa++;
				}
				if(agenteEmissor.getMorrerEmXpublicacoes()!=0 && agenteEmissor.getQtdParComunicado()>agenteEmissor.getMorrerEmXpublicacoes()){
					matarAgente(agenteEmissor);
					if(agenteEpistemicoFactory!=null){
						agenteEpistemicoFactory.criarAgente();
					}
				}else{
					ParEpistemico parEpistemico = agenteEmissor.gerarPar();
					if(comunicacaoListener!=null) comunicacaoListener.comunicadorEscolhido(agenteEmissor);
					for(int i=0;i<agenteEmissor.getArestas().size();i++){
						Aresta aresta = agenteEmissor.getArestas().get(i);
						AgenteEpistemico receptor = aresta.getReceptor();
						Double peso = aresta.getPeso();
						Double diff = receptor.receberComunicado(parEpistemico, aresta,agenteEmissor);
						logger.debug("diff = "  + diff + " peso " + aresta.getPeso());
						if(comunicacaoListener!=null) comunicacaoListener.depoisDeComunicar(agenteEmissor, receptor, peso,diff);
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
}
