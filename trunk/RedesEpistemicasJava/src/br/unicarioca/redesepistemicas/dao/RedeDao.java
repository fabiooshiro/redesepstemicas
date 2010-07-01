package br.unicarioca.redesepistemicas.dao;

import java.util.List;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;
import br.unicarioca.redesepistemicas.view.RedeEpistemicaView;

public class RedeDao {

	public static String getXml(RedeEpistemica redeEpistemica, RedeEpistemicaView redeEpistemicaView){
		
		StringBuilder sb = new StringBuilder();
		int a[] = redeEpistemica.getListAgenteEpistemico().get(0).getNeuralNetworkStructure();
		String nns = a[0]+","+a[1]+","+a[2];
		String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<rede neuralNetworkStructure=\"{nns}\" " +
			"distMaxRepulsao=\"{distMaxRepulsao}\" " +
			"passoAgente=\"{passoAgente}\" " +
			"distrAleatoria=\"{distrAleatoria}\">"
			.replace("{nns}", nns)
			.replace("{distMaxRepulsao}", redeEpistemicaView.getDistanciaMaxRepulsao()+"")
			.replace("{passoAgente}", redeEpistemicaView.getPassoMax()+"")
			.replace("{distrAleatoria}", nns);
		sb.append(template);
		for(AgenteEpistemico agente: redeEpistemica.getListAgenteEpistemico()){
			sb.append(AgenteDao.getXml(agente));
		}
		for(AgenteEpistemico agente : redeEpistemica.getListAgenteEpistemico()){
			List<ParEpistemico> crencas = agente.getCrencas();
			for(ParEpistemico crenca : crencas){
				sb.append(CrencaDao.getXml(crenca));
			}
		}
		return sb.toString();
	}
}
