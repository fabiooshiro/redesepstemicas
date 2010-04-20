package br.unicarioca.rottweiler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thoughtworks.selenium.SeleneseTestCase;

public class RottweilerTest extends SeleneseTestCase {

	EntityManager em;
	public void setUp() throws Exception {
		
		// open/read the application context file
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("./META-INF/spring/applicationContext.xml");

	    // instantiate our spring dao object from the application context
	    EntityManagerFactory emf = (EntityManagerFactory)ctx.getBean("entityManagerFactory");
	    em = emf.createEntityManager();
	    setUp("http://www.orkut.com.br/", "*chrome");
	}

	public void testTeste() throws Exception {
		selenium.open("/Main#Home");
		selenium.windowMaximize();
		System.out.println("Abra o navegador e digite a sua senha");
		Thread.sleep(1000);
		
		//listando os amigos
		boolean ok=false;
		while(!ok){
			try{
				selenium.selectFrame("orkutFrame");
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			try{
				selenium.click("link=Amigos");
				selenium.waitForPageToLoad("30000");
				ok = true;
				Thread.sleep(1000);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		List<LinkProfile> listLinkProfile=LinkProfile.findAll(selenium.getHtmlSource());
		for (LinkProfile linkProfile : listLinkProfile) {
			System.out.println("nome -> " + linkProfile.getNome());
			System.out.println("uid -> " + linkProfile.getUid());
			System.out.println("url -> " + linkProfile.getUrl());
			System.out.println();
			Profile profile = new Profile();
			profile.setUid(linkProfile.getUid());
			em.persist(profile);
			
			selenium.open(linkProfile.getUrl());
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Lista de amigos primeira pagina
			 * /Main#FriendsList?uid=188435169541448637&rl=fpc
			 */
			selenium.open("/Main#FriendsList?uid="+linkProfile.getUid()+"&rl=fpc");
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Pagina de recados
			 * http://www.orkut.com.br/Main#Scrapbook?rl=lo&uid=12235031985893526985
			 */
			selenium.open("/Main#Scrapbook?rl=lo&uid="+linkProfile.getUid());
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Comunidades
			 * http://www.orkut.com.br/Main#ProfileC?uid=188435169541448637&rl=cpc
			 */
			selenium.open("/Main#ProfileC?uid="+linkProfile.getUid()+"&rl=cpc");
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
		}
	}
}
