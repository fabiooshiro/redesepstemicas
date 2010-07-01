package br.unicarioca.redesepistemicas.dao;

import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaDao {
	public static String getXml(ParEpistemico parEpistemico){
		StringBuilder a = new StringBuilder();
		for(Double d:parEpistemico.getDoubleAntecedentes()){
			a.append(d).append(",");
		}
		StringBuilder c = new StringBuilder();
		for(Double d:parEpistemico.getDoubleConsequentes()){
			c.append(d).append(",");
		}
		return "<crenca a=\"{a}\" c=\"{c}\" cid=\"{cid}\" nome=\"{nome}\" />"
			.replace("{a}", a.toString().substring(0, a.length()-2))
			.replace("{c}", c.toString().substring(0, a.length()-2))
			.replace("{cid}", parEpistemico.getId()+"")
			.replace("{nome}", parEpistemico.toString());
	}
}
