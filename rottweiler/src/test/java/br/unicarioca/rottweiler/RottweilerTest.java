package br.unicarioca.rottweiler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.swing.JOptionPane;

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
	private EntityManagerFactory emf;
	private EntityManager em;
	/**
	 * Maximo de profiles para visitar
	 */
	private final static int MAX_PROFILE=100;
	private int totalValido=0;
	private int totalInvalido=0;
	/**
	 * Total de dias dos scraps
	 */
	private int diasScraps=15;
	/**
	 * dataHora do scan
	 */
	private Date dataHora = new Date();
	
	public void setUp() throws Exception {
		// open/read the application context file
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("./META-INF/spring/applicationContext.xml");

	    // instantiate our spring dao object from the application context
	    emf = (EntityManagerFactory)ctx.getBean("entityManagerFactory");
	    em = emf.createEntityManager();
	    setUp("http://www.orkut.com.br/", "*chrome");
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    dataHora = sdf.parse("2010-05-25 19:25:16");
	}
	
	private Integer lastOrdemProfile=null;
	
	@SuppressWarnings("unchecked")
	private void salvarSeNaoExistir(Profile profile){
		
		
		List<Profile> profiles;
		//lastOrdemProfile 
		if(lastOrdemProfile==null){
			profiles = em.createQuery("Select o From Profile o order by o.ordem desc").setMaxResults(1).getResultList();
			if(profiles.size()==0){
				lastOrdemProfile = 1;
			}else{
				lastOrdemProfile = profiles.get(0).getOrdem();
				if(lastOrdemProfile==null){
					lastOrdemProfile = 1;
				}
			}
		}
		profiles = em.createQuery("Select o From Profile o where o.uid=?").setParameter(1, profile.getUid()).getResultList();
		if(profiles.size()==0){//salva se nao existir
			//verificar se possui o nome
			if(profile.getNome()==null){
				String nome = getNomeProfile(profile.getUid());
				logger.debug("resgatando o nome " + profile.getUid() + " = " + nome);
				profile.setNome(nome);
			}
			em.getTransaction().begin();
			profile.setOrdem(lastOrdemProfile);
			em.persist(profile);
			lastOrdemProfile++;
			em.getTransaction().commit();
			logger.debug("Profile salvo "+profile.getUid());
		}else{
			if(profile.getNome()==null){
				profile.setNome(profiles.get(0).getNome());
			}
			logger.debug("Profile ja existe "+profile.getUid() + " nome = '" + profile.getNome() + "'");
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
		System.out.println();
		delay(1000);
		//pegar a primeira lista de amigos e colocar no banco
		List<LinkProfile> listLinkProfile=LinkProfile.findAll(selenium.getHtmlSource());
		if(listLinkProfile.size()==0){
			throw new RuntimeException("Voce esta sem amigos???");
		}
		Profile profile=null;
		for (LinkProfile linkProfile : listLinkProfile) {
			profile = new Profile();
			profile.setUid(linkProfile.getUid());
			salvarSeNaoExistir(profile);
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
			if(profile.getInvalido()==null){// se for valido
				salvarComunidades(profile);
				Thread.sleep(3000+(int)(Math.random()*2000));//wait some time
				em.getTransaction().begin();
				em.createQuery("Update Profile o Set o.valido=1 where o.uid=?").setParameter(1, profile.getUid()).executeUpdate();
				em.flush();//nao consegue quando o Scrap.from nao esta salvo
				em.getTransaction().commit();
			}
			em.close();
			em = emf.createEntityManager();
			
			profile = selecionarNaoScaneado();
			//verificar antes se ja temos o total MAX_PROFILE
			//se houver o total pulamos o salvamento dos demais
			long total = (Long)em.createQuery("Select count(o) From Profile o where o.valido=1)").getSingleResult();
			logger.debug("Total de profiles validos no banco = " + total);
			if(total>=MAX_PROFILE){
				logger.info("Nao visitando o "+profile.getNome()+", pois ja existe o numero "+MAX_PROFILE+" de profiles validos no banco.");
				break;
			}
		}
		logger.info("FINALIZOU!!!!");
	}

	private Profile refresh(Profile profile){
		try{
			return (Profile)em.createQuery("Select o from Profile o where o.uid=?").setParameter(1, profile.getUid()).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * Seleciona um profile nao scaneado
	 * @return null caso nao exista
	 */
	private Profile selecionarNaoScaneado() {
		try{
			em.getTransaction().begin();
			Profile profile = (Profile)em.createQuery("Select o from Profile o where o.dataHora <> ? or o.dataHora is null order by o.ordem").setParameter(1, dataHora).setMaxResults(1).getSingleResult();
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
		int totalComunidades = 0;
		int pagina = 1;
		selenium.open("/Main#ProfileC?uid="+profile.getUid()+"&rl=cpc");
		selenium.waitForPageToLoad("30000");
		delay(1000);
		try{
			selenium.selectFrame("orkutFrame");
		}catch(Exception e){
			//e.printStackTrace(); no problem
		}
		while(true){
			try{
				logger.debug("Comunidade Pagina = " + pagina);
				delay(2000);
				String codHtml = selenium.getHtmlSource();
				List<Comunidade> comunidades = ComunidadeHtml.findAll(codHtml);
				logger.debug(comunidades.size() + " comunidades encontradas de " + profile.getNome());
				for(Comunidade comunidade:comunidades){
					//verificar se nao existe
					List<Comunidade> found = em.createQuery("Select o From Comunidade o where o.cid=?").setParameter(1, comunidade.getCid()).getResultList();
					if(found.size()==0){
						em.getTransaction().begin();
						em.persist(comunidade);
						em.getTransaction().commit();
					}else if(found.size()==1){
						comunidade = found.get(0);
					}else{
						logger.error("Comunidade duplicada " + comunidade.getCid());
						throw new RuntimeException("Comunidade id " + comunidade.getCid() + " duplicada, " + found.size()+" encontrada com o mesmo id");
					}
					ProfileComunidade profileComunidade = new ProfileComunidade();
					profileComunidade.setComunidade(comunidade);
					profileComunidade.setProfile(profile);
					profileComunidade.setDataHora(dataHora);
					profileComunidade.setAderiu(1);
					em.getTransaction().begin();
					em.persist(profileComunidade);
					em.getTransaction().commit();
					totalComunidades++;
				}
				selenium.click("link=>");
				selenium.waitForPageToLoad("30000");
				pagina++;
			}catch(Exception e){
				logger.error("link?");
				break;
			}
		}
		logger.info("comunidades = " + totalComunidades);
		///JOptionPane.showMessageDialog(null,"TotalComunidades = " + totalComunidades);
	}

	//TODO para resgatar o nome quando estiver null
	private String getNomeProfile(String uid){
		selenium.open("http://www.orkut.com.br/Main#Profile?uid="+uid);
		selenium.waitForPageToLoad("30000");
		delay(1000);
		try{
			selenium.selectFrame("orkutFrame");
		}catch(Exception e){
			//e.printStackTrace(); no problem
		}
		delay(2000);
		Pattern pat = Pattern.compile("<h1 id=\"title\">([^<]*)<",Pattern.DOTALL);
		Matcher mat = pat.matcher(selenium.getHtmlSource());
		if(mat.find()){
			return mat.group(1);
		}else{
			logger.warn("Nao foi possivel recuperar o nome de " + uid);
			return null;
		}
	}
	
	private void delay(long tempo){
		try{
			Thread.sleep(tempo);
		}catch(Exception e){
			
		}
	}
	/**
	 * Pagina de recados, pegar as datas
	 * http://www.orkut.com.br/Main#Scrapbook?rl=lo&uid=12235031985893526985
	 */
	private void salvarRecados(Profile profile) {
		//se houver as mensagens abaixo ignorar usuario
		//Voc� s� pode ver os recados que enviou para
		//nenhum recado seu
		selenium.open("/Main#Scrapbook?rl=lo&uid="+profile.getUid());
		selenium.waitForPageToLoad("30000");
		delay(1000);
		try{
			selenium.selectFrame("orkutFrame");
		}catch(Exception e){
			//e.printStackTrace(); no problem
		}
		delay(2000);
		if(selenium.isTextPresent("nenhum recado seu")){
			em.getTransaction().begin();
			em.refresh(profile);
			profile.setInvalido("nenhum recado seu");
			em.getTransaction().commit();
			totalInvalido++;
		}else if(selenium.isTextPresent("Voc� s� pode ver os recados que enviou para")){
			em.getTransaction().begin();
			em.refresh(profile);
			profile.setInvalido("Voc� s� pode ver os recados que enviou para");
			em.getTransaction().commit();
			totalInvalido++;
		}else{
			totalValido++;
			int pagina=0;
			int qtdScrap=0;
			//vamos entao pegar os scraps
			//calcular a data limit para o scrap
			long timeOut = new Date().getTime()-(diasScraps*24*60*60*1000);
			boolean run=true;
			while(run){
				try{
					pagina++;
					logger.info("Scrap pagina " + pagina);
					String codHtml = selenium.getHtmlSource();
					List<Scrap> scraps = ScrapHtml.findAll(codHtml);
					logger.debug(scraps.size() + " scraps encontrados de " + profile.getNome());
					for(Scrap scrap:scraps){
						long scrapTime = scrap.getDataHora().getTime();
						if(scrapTime<timeOut){
							//nao pegamos mais os scraps antigos
							logger.info("Scrap : recados antigos foram ignorados de " + profile.getNome());
							run = false;
						}else{
							qtdScrap++;
							Profile from = refresh(scrap.getFrom());
							if(from==null){
								from = scrap.getFrom();
								salvarSeNaoExistir(from);
							}
							scrap.setFrom(from);
							scrap.setTo(profile);
							logger.debug("scrap numero " + qtdScrap);
							em.getTransaction().begin();
							em.persist(scrap);
							em.getTransaction().commit();
						}
					}
					//navegar nas outras paginas
					if(run){
						logger.debug("Indo para a proxima pagina de recados de " + profile.getNome());
						selenium.click("link=pr�xima >");
						selenium.waitForPageToLoad("30000");
						delay(2000);
					}else{
						logger.debug("parando de pegar scraps...");
						break;
					}
				}catch(Exception e){
					break;
				}
			}//fim do while(true)
			logger.info("Total de Scraps " + qtdScrap);
			//JOptionPane.showMessageDialog(null,"Total de Scraps " + qtdScrap);
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
			List<LinkProfile> linksAmigos;
			do{
				try{
					selenium.selectFrame("orkutFrame");
					delay(1000);
				}catch(Exception e){}
				linksAmigos = LinkProfile.findAll(selenium.getHtmlSource());
				logger.debug("salvando "+linksAmigos.size()+" amigos de "+profile.getNome()+" "+profile.getUid());
				//if(linksAmigos.size()==0){
					//int res = JOptionPane.showConfirmDialog(null, "Zero amigos. Continuar?");
					//if(res==JOptionPane.CANCEL_OPTION){
					//	return;
					//}
				//}
				//selenium.isTextPresent("");
			}while(linksAmigos.size()==0);
			//String src = selenium.getHtmlSource();
			//System.out.println(src);
			for (LinkProfile lnProfile : linksAmigos) {
				Profile prof = new Profile();
				prof.setUid(lnProfile.getUid());
				prof.setNome(lnProfile.getNome());
				salvarSeNaoExistir(prof);
			}
		}
	}
}
