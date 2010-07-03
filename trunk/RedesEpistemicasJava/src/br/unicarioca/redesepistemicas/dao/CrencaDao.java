package br.unicarioca.redesepistemicas.dao;

import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaDao {
	public static String getXml(ParEpistemico parEpistemico){
		StringBuilder a = new StringBuilder();
		for(Double d:parEpistemico.getDoubleAntecedentes()){
			a.append(";").append(d);
		}
		StringBuilder c = new StringBuilder();
		for(Double d:parEpistemico.getDoubleConsequentes()){
			c.append(";").append(d);
		}
		if(c.length()==0) c.append(";");
		if(a.length()==0) a.append(";");
		System.out.println("a " + a.toString());
		System.out.println("c " + c.toString());
		return "<crenca a=\"{a}\" c=\"{c}\" cid=\"{cid}\" nome=\"{nome}\" />"
			.replace("{a}", a.toString().substring(1))
			.replace("{c}", c.toString().substring(1))
			.replace("{cid}", parEpistemico.getId()+"")
			.replace("{nome}", parEpistemico.toString());
	}
}
