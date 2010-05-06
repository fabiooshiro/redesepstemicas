package br.unicarioca.rottweiler;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * Classe de teste apenas por comodidade para subir o selenium
 * @author Fabio Issamu Oshiro, Leandro Freire
 *
 */
public class RottweilerTest extends SeleneseTestCase {
	private static Logger logger = Logger.getLogger(RottweilerTest.class);
	private EntityManager em;
	/**
	 * Maximo de profiles para visitar
	 */
	private final static int MAX_PROFILE=100;
	private int totalValido=0;
	private int totalInvalido=0;
	/**
	 * dataHora do scan
	 */
	private Date dataHora = new Date();
	
	public void setUp() throws Exception {
		// open/read the application context file
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("./META-INF/spring/applicationContext.xml");

	    // instantiate our spring dao object from the application context
	    EntityManagerFactory emf = (EntityManagerFactory)ctx.getBean("entityManagerFactory");
	    em = emf.createEntityManager();
	    setUp("http://www.orkut.com.br/", "*chrome");
	}
	
	@SuppressWarnings("unchecked")
	private void salvarOuAtualizar(Profile profile){
		List<Profile> profiles = em.createQuery("Select o From Profile o where o.uid=?").setParameter(1, profile.getUid()).getResultList();
		if(profiles.size()==0){//salva se nao existir
			em.getTransaction().begin();
			em.persist(profile);
			em.getTransaction().commit();
			logger.debug("Profile salvo "+profile.getUid());
		}else{
			logger.debug("Profile ja existe "+profile.getUid());
		}
	}
	
	public void testTeste() throws Exception {
		selenium.open("/Main#Home");
		selenium.windowMaximize();
		System.out.println("Abra o navegador e digite a sua senha");
		Thread.sleep(1000);
		
		//listando os amigos
		boolean ok=false;
		while(!ok){
			Thread.sleep(1000);
			try{
				selenium.selectFrame("orkutFrame");
			}catch(Exception e){
				System.out.print(".");
			}
			try{
				selenium.click("link=Amigos");
				selenium.waitForPageToLoad("30000");
				ok = true;
			}catch(Exception e){
				System.out.print(".");
			}
		}
		//pegar a primeira lista de amigos e colocar no banco
		List<LinkProfile> listLinkProfile=LinkProfile.findAll(selenium.getHtmlSource());
		Profile profile=null;
		for (LinkProfile linkProfile : listLinkProfile) {
			profile = new Profile();
			profile.setUid(linkProfile.getUid());
			salvarOuAtualizar(profile);
		}
		
		/*
		 * scanear os caras 
		 */
		profile = selecionarNaoScaneado();
		while(profile!=null){
			salvarAmigos(profile);
			Thread.sleep(3000+(int)(Math.random()*2000));//wait some time
			
			salvarRecados(profile);
			Thread.sleep(3000+(int)(Math.random()*2000));//wait some time
			
			salvarComunidades(profile);
			Thread.sleep(3000+(int)(Math.random()*2000));//wait some time
			
			profile = selecionarNaoScaneado();
		}
	}

	/**
	 * Seleciona um profile nao scaneado
	 * @return null caso nao exista
	 */
	private Profile selecionarNaoScaneado() {
		try{
			em.getTransaction().begin();
			Profile profile = (Profile)em.createQuery("Select o from Profile o where o.dataHora <> ? or o.dataHora is null").setParameter(1, dataHora).setMaxResults(1).getSingleResult();
			profile.setDataHora(dataHora);//atualizar a hora do scan
			em.flush();
			em.getTransaction().commit();
			return profile;
		}catch(NoResultException e){
			logger.debug("finalizando...");
			em.getTransaction().rollback();
			return null;
		}catch(Exception e){
			logger.error("Erro " + e.getMessage(),e);
			em.getTransaction().rollback();
			return null;
		}
	}

	/**
	 * Comunidades
	 * http://www.orkut.com.br/Main#ProfileC?uid=188435169541448637&rl=cpc
	 */
	private void salvarComunidades(Profile profile) {
		selenium.open("/Main#ProfileC?uid="+profile.getUid()+"&rl=cpc");
		selenium.waitForPageToLoad("30000");
		selenium.selectFrame("orkutFrame");
	}

	/**
	 * Pagina de recados, pegar as datas
	 * http://www.orkut.com.br/Main#Scrapbook?rl=lo&uid=12235031985893526985
	 */
	private void salvarRecados(Profile profile) {
		//se houver as mensagens abaixo ignorar usuario
		//Você só pode ver os recados que enviou para
		//nenhum recado seu
		selenium.open("/Main#Scrapbook?rl=lo&uid="+profile.getUid());
		selenium.waitForPageToLoad("30000");
		selenium.selectFrame("orkutFrame");
		if(selenium.isTextPresent("nenhum recado seu")){
			em.getTransaction().begin();
			em.refresh(profile);
			profile.setInvalido("nenhum recado seu");
			em.getTransaction().commit();
			totalInvalido++;
		}else if(selenium.isTextPresent("Você só pode ver os recados que enviou para")){
			em.getTransaction().begin();
			em.refresh(profile);
			profile.setInvalido("Você só pode ver os recados que enviou para");
			em.getTransaction().commit();
			totalInvalido++;
		}else{
			totalValido++;
		}
		logger.info("valido vs invalido " + totalValido + " x " + totalInvalido);
		
		
	}
	
	/**
	 * Lista de amigos primeira pagina
	 * /Main#FriendsList?uid=188435169541448637&rl=fpc
	 */
	private void salvarAmigos(Profile profile){
		selenium.open("/Main#FriendsList?uid="+profile.getUid()+"&rl=fpc");
		selenium.waitForPageToLoad("30000");
		{//Salvar os amigos
			selenium.selectFrame("orkutFrame");
			List<LinkProfile> linksAmigos = LinkProfile.findAll(selenium.getHtmlSource());
			logger.debug("salvando "+linksAmigos.size()+" amigos de "+profile.getUid());
			//String src = selenium.getHtmlSource();
			//System.out.println(src);
			for (LinkProfile lnProfile : linksAmigos) {
				Profile prof = new Profile();
				prof.setUid(lnProfile.getUid());
				salvarOuAtualizar(prof);
			}
		}
	}
}
