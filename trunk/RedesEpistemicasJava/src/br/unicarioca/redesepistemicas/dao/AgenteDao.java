package br.unicarioca.redesepistemicas.dao;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.Aresta;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class AgenteDao {
	public static final String TEMPLATE = "<agente aid=\"{aid}\" maxDif=\"{maxDif}\" morrerEm=\"{morrerEm}\" somenteUltimo=\"{somenteUltimo}\" criarNovoEm=\"{criarNovoEm}\" freq=\"{freq}\" >"; 
	public static String getXml(AgenteEpistemico agente){
		String header = TEMPLATE
			.replace("{aid}", agente.getId()+"")
			.replace("{maxDif}", agente.getMaxDiff()+"")
			.replace("{morrerEm}", agente.getMorrerEmXpublicacoes()+"")
			.replace("{somenteUltimo}", agente.isSomenteUltimaTeoria()? "1" : "0")
			.replace("{criarNovoEm}", agente.getCriarNovoEm()+ "0")
			.replace("{freq}", agente.getVontadeDePublicar()+"");
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		for(ParEpistemico par:agente.getCrencas()){
			sb.append("<crenca ref=\"");
			sb.append(par.getId());
			sb.append("\" />");
		}
		for(Aresta aresta: agente.getArestas()){
			sb.append("<aresta aid=\"");
			sb.append(aresta.getReceptor().getId());
			sb.append("\" peso=\"");
			sb.append(aresta.getPeso());
			sb.append("\" />");
		}
		sb.append("</agente>");
		return sb.toString();
	}
}