package br.unicarioca.rottweiler;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MontarXmlGrafico {
	private static Logger logger = Logger.getLogger(MontarXmlGrafico.class);
	private EntityManagerFactory emf;
	private EntityManager em;
	/**
	 * dataHora do scan
	 */
	private Date dataHora = new Date();
	
	public MontarXmlGrafico() throws ParseException {
		// open/read the application context file
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("./META-INF/spring/applicationContext.xml");

		// instantiate our spring dao object from the application context
		emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
		em = emf.createEntityManager();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dataHora = sdf.parse("2010-05-25 19:25:16");
	}
	
	public void montarXml(){
		List<Profile> profiles = em.createQuery("Select o From Profile o where o.valido=1")
			.setMaxResults(100)
			.getResultList();
		String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">" +
			"<key id=\"d0\" for=\"node\" attr.name=\"color\" attr.type=\"string\">yellow</key>" +
			"<key id=\"d1\" for=\"edge\" attr.name=\"weight\" attr.type=\"double\"/>";
		String graph = head + "<graph id=\"G\" edgedefault=\"directed\">";
		StringBuilder sbGraph = new StringBuilder();
		sbGraph.append(graph);
		sbGraph.append("<node id=\"centro\" />");
		for(int i=0;i<profiles.size();i++){
			Profile profile = profiles.get(i);
			sbGraph.append("<node id=\"uid" + i + "\">");
			sbGraph.append("<data key=\"d0\">"+doHTMLEntityEncode(profile.getNome())+"</data>");
			//sbGraph.append("<data key=\"d0\">n"+i+"</data>");
			sbGraph.append("</node>");
		}
		
		StringBuilder edges = new StringBuilder();
		//todos para todos
		for(int i=0;i<profiles.size();i++){
			boolean temEdge = false;
			for(int j=0;j<profiles.size();j++){
				if(i!=j){
					Long total = (Long)em.createQuery("Select count(s) From Scrap s where s.from=? and s.to=?")
						.setParameter(1, profiles.get(i))
						.setParameter(2, profiles.get(j))
						.getSingleResult();
					
					if(total>0){
						sbGraph.append("<node id=\"v" + i + "x" + j + "\">")
							.append("<data key=\"d0\">"+total+"</data>")
							.append("</node>");
						edges.append("<edge id=\"a" + i + "x" + j + "\" source=\"uid" + i + "\" target=\"v" + i + "x" + j + "\"/>");
						edges.append("<edge id=\"b" + i + "x" + j + "\" source=\"v" + i + "x" + j + "\" target=\"uid" + j + "\"/>");
						System.out.print(".");
						temEdge = true;
					}
				}
			}
			if(!temEdge){
				edges.append("<edge id=\"e" + i + "x\" source=\"centro\" target=\"uid" + i + "\" />");
			}
			System.out.println();
		}
		sbGraph.append(edges);
		sbGraph.append("</graph></graphml>");
		try {
			FileUtils.writeStringToFile(new File("/grafoOrkut.xml"), sbGraph.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String doHTMLEntityEncode(String s) {
		StringBuffer buf = new StringBuffer();
		int len = (s == null ? -1 : s.length());

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') 
					|| c=='.' || c=='?' || c=='/' || c==':' || c=='=' || c=='_') {
				buf.append(c);
			} else {
				buf.append("&#" + (int) c + ";");
			}
		}
		return buf.toString();
	}
	public static void main(String[] args) {
		try{
			MontarXmlGrafico montador = new MontarXmlGrafico();
			montador.montarXml();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
