package br.unicarioca.redesepistemicas.dao;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemicoOrkut;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;
import br.unicarioca.redesepistemicas.view.ConfiguracoesPanel;
import br.unicarioca.redesepistemicas.view.RedeEpistemicaView;

public class RedeDao {
	private static final Logger logger = Logger.getLogger(RedeDao.class);
	public static void loadFromXml(ConfiguracoesPanel configuracoesPanel, RedeEpistemica redeEpistemica, RedeEpistemicaView redeEpistemicaView, File xmlFile) {
		try {
			logger.info("Abrindo xml " + xmlFile.getAbsolutePath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFile);
			Element redeTag = doc.getDocumentElement();
			String nnsStr[] = redeTag.getAttribute("neuralNetworkStructure").split(",");
			boolean distrAleatoria = redeTag.getAttribute("distrAleatoria").equals("s");
			int nns[] = new int[nnsStr.length];
			for(int i=0;i<nns.length;i++){
				nns[i] = Integer.parseInt(nnsStr[i].trim());
			}
			configuracoesPanel.getTxtEstruturaRede().setText(redeTag.getAttribute("neuralNetworkStructure"));
			configuracoesPanel.getSpnPassoMax().setValue(Integer.valueOf(redeTag.getAttribute("passoAgente")));
			configuracoesPanel.getChkDistribuicaoAleatoria().setSelected(distrAleatoria);
			redeEpistemicaView.setDistanciaMaximaRepulsao(Integer.valueOf(redeTag.getAttribute("distMaxRepulsao")));
			redeEpistemicaView.setPassoMax(Integer.valueOf(redeTag.getAttribute("passoAgente")));
			NodeList crencas = redeTag.getElementsByTagName("crenca");
			String parType = redeTag.getAttribute("parEpistemicoType");
			Map<Long,ParEpistemico> mapCrencas = new HashMap<Long,ParEpistemico>();
			for(int i=0;i<crencas.getLength();i++){
				Node crencaTag = crencas.item(i);
				ParEpistemico par = (ParEpistemico)Class.forName(parType).newInstance();
				String aStr[] = crencaTag.getAttributes().getNamedItem("a").getNodeValue().split(";");
				par.setSizeAntecedente(aStr.length);
				for(int j=0;j<aStr.length;j++){
					par.addAntecedente(Double.valueOf(aStr[j]));
				}
				String cStr[] = crencaTag.getAttributes().getNamedItem("c").getNodeValue().split(";");
				par.setSizeConsequente(cStr.length);
				for(int j=0;j<cStr.length;j++){
					par.addConsequente(Double.valueOf(cStr[j]));
				}
				par.setNome(crencaTag.getAttributes().getNamedItem("nome").getNodeValue());
				par.setId(Long.valueOf(crencaTag.getAttributes().getNamedItem("cid").getNodeValue()));
				Color cor = new Color(Integer.parseInt(crencaTag.getAttributes().getNamedItem("color").getNodeValue(), 16));
				par.setCor(cor);
				mapCrencas.put(par.getId(), par);
			}
			Map<Long,AgenteEpistemico> mapAgentes = new HashMap<Long,AgenteEpistemico>();
			NodeList agentes = redeTag.getElementsByTagName("agente");
			ArrayList<InsertAresta> inserts = new ArrayList<InsertAresta>();
			ArrayList<AgenteEpistemico> agenteList = new ArrayList<AgenteEpistemico>();
			for(int i=0;i<agentes.getLength();i++){
				Node agenteTag = agentes.item(i);
				AgenteEpistemico agente = new AgenteEpistemico(nns);
				NamedNodeMap att = agenteTag.getAttributes();
				agente.setId(Long.valueOf(att.getNamedItem("aid").getNodeValue()));
				agente.setMaxDiff(Double.valueOf(att.getNamedItem("maxDif").getNodeValue()));
				agente.setMorrerEmXpublicacoes(Integer.valueOf(att.getNamedItem("morrerEm").getNodeValue()));
				agente.setSomenteUltimaTeoria(att.getNamedItem("somenteUltimo").getNodeValue().equals("1"));
				agente.setCriarNovoEm(Integer.valueOf(att.getNamedItem("criarNovoEm").getNodeValue()));
				agente.setVontadeDePublicar(Double.valueOf(att.getNamedItem("freq").getNodeValue()));
				agente.setX(Integer.valueOf(att.getNamedItem("x").getNodeValue()));
				agente.setY(Integer.valueOf(att.getNamedItem("y").getNodeValue()));
				agente.setRaio(Integer.valueOf(att.getNamedItem("raio").getNodeValue()));
				NodeList arestasCrencasref = agenteTag.getChildNodes();
				ArrayList<ParEpistemico> par2add = new ArrayList<ParEpistemico>();
				Integer qtd=1000;
				for(int j=0;j<arestasCrencasref.getLength();j++){
					Node arestaCrenca = arestasCrencasref.item(j);
					if(arestaCrenca.getNodeName().equals("crencaref")){
						Long parId = Long.parseLong(arestaCrenca.getAttributes().getNamedItem("ref").getNodeValue());
						qtd = Integer.parseInt(arestaCrenca.getAttributes().getNamedItem("qtd").getNodeValue());
						ParEpistemico par = mapCrencas.get(parId);
						par2add.add(par);
					}else if (arestaCrenca.getNodeName().equals("aresta")){
						inserts.add(
								new InsertAresta(agente, 
										Double.parseDouble(arestaCrenca.getAttributes().getNamedItem("peso").getNodeValue()),
										Long.parseLong(arestaCrenca.getAttributes().getNamedItem("aid").getNodeValue()),
										mapAgentes
								));
					}
				}
				if(qtd>0){
					logger.info("treinando agente " + agente.getId() + " qtd = " + qtd + " crencas " + par2add.size());
					agente.treinar(par2add,qtd);
					agente.addCrencas(par2add);
				}
				mapAgentes.put(agente.getId(), agente);
				agenteList.add(agente);
				agente.setRedeEpistemica(redeEpistemica);
			}
			for(InsertAresta insert:inserts){
				insert.execute();
			}
			//matar os antigos
			for(AgenteEpistemico agente: redeEpistemica.getListAgenteEpistemico()){
				redeEpistemica.matarAgente(agente);
			}
			for(AgenteEpistemico agente: agenteList){
				redeEpistemica.fireAgenteCriadoEvent(agente);
			}
			redeEpistemica.setListAgenteEpistemico(agenteList);
			
			redeEpistemicaView.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getXml(RedeEpistemica redeEpistemica, RedeEpistemicaView redeEpistemicaView, ConfiguracoesPanel configuracoesPanel){
		String parType = ParEpistemicoOrkut.class.getCanonicalName();
		StringBuilder crencaSb = new StringBuilder();
		for(AgenteEpistemico agente : redeEpistemica.getListAgenteEpistemico()){
			List<ParEpistemico> crencas = agente.getCrencas();
			for(ParEpistemico crenca : crencas){
				parType = crenca.getClass().getCanonicalName();
				crencaSb.append(CrencaDao.getXml(crenca));
			}
		}
		StringBuilder sb = new StringBuilder();
		int a[] = redeEpistemica.getListAgenteEpistemico().get(0).getNeuralNetworkStructure();
		String nns = a[0]+","+a[1]+","+a[2];
		String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<rede neuralNetworkStructure=\"{nns}\" " +
			"distMaxRepulsao=\"{distMaxRepulsao}\" " +
			"passoAgente=\"{passoAgente}\" " +
			"distrAleatoria=\"{distrAleatoria}\" " +
			"parEpistemicoType=\"{parType}\">";
		template = template
			.replace("{nns}", nns)
			.replace("{distMaxRepulsao}", redeEpistemicaView.getDistanciaMaxRepulsao()+"")
			.replace("{passoAgente}", redeEpistemicaView.getPassoMax()+"")
			.replace("{distrAleatoria}", configuracoesPanel.getChkDistribuicaoAleatoria().isSelected()? "s":"n")
			.replace("{parType}", parType);
		sb.append(template);
		sb.append(crencaSb.toString());
		for(AgenteEpistemico agente: redeEpistemica.getListAgenteEpistemico()){
			sb.append(AgenteDao.getXml(agente));
		}
		
		sb.append("</rede>");
		return sb.toString();
	}
}
class InsertAresta{
	AgenteEpistemico agente;
	double peso;
	long idAgente;
	Map<Long, AgenteEpistemico> mapAgentes;
	public InsertAresta(AgenteEpistemico agente, double peso, long idAgente, Map<Long, AgenteEpistemico> mapAgentes) {
		super();
		this.agente = agente;
		this.peso = peso;
		this.idAgente = idAgente;
		this.mapAgentes = mapAgentes;
	}
	void execute(){
		AgenteEpistemico receptor = mapAgentes.get(idAgente);
		if(receptor!=null){
			agente.conhecer(receptor, peso);
		}else{
			throw new RuntimeException("Agente id " + idAgente + " not found.");
		}
	}
}
