package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RedeEpistemica {
	private static Logger logger = Logger.getLogger(RedeEpistemica.class);
	private List<AgenteEpistemico> listAgenteEpistemico = new ArrayList<AgenteEpistemico>();
	private ComunicacaoListener comunicacaoListener = null;
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
		if(listAgenteEpistemico.size()>1){
			int agente = (int)((double)listAgenteEpistemico.size()*Math.random());
			logger.debug("agente = "  + (agente+1) + " de " + listAgenteEpistemico.size());
			AgenteEpistemico agenteEpistemico = listAgenteEpistemico.get(agente);
			ParEpistemico parEpistemico = agenteEpistemico.gerarPar();
			if(comunicacaoListener!=null) comunicacaoListener.comunicadorEscolhido(agenteEpistemico);
			
			for(Aresta aresta:agenteEpistemico.getArestas()){
				AgenteEpistemico receptor = aresta.getAgenteEpistemico();
				Double peso = aresta.getPeso();
				Double diff = receptor.receberComunicado(parEpistemico, aresta);
				logger.debug("diff = "  + diff + " peso " + aresta.getPeso());
				
				if(comunicacaoListener!=null) comunicacaoListener.depoisDeComunicar(agenteEpistemico, receptor, peso,diff);
			}
		}
	}
	public void criarAgente(int x, int y) {
		AgenteEpistemico agenteNovo = new AgenteEpistemico();
		agenteNovo.setX(x);
		agenteNovo.setY(y);
		agenteNovo.setRaio(50);
		//apresentar a todos
		for(AgenteEpistemico agenteEpistemico:listAgenteEpistemico){
			agenteEpistemico.conhecer(agenteNovo,1.0);
			agenteNovo.conhecer(agenteEpistemico, 1.0);
		}
		listAgenteEpistemico.add(agenteNovo);
		
	}
}
