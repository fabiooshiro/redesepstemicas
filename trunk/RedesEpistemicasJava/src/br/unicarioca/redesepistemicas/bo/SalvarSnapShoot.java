package br.unicarioca.redesepistemicas.bo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import br.unicarioca.redesepistemicas.view.ConfiguracoesPanel;

/**
 * Responsavel por salvar o estado da aplicacao
 *
 */
public class SalvarSnapShoot {
	
	private boolean pastasVerificadas = false;
	private static SalvarSnapShoot instance;
	private static int snapshotN=0;
	private ConfiguracoesPanel configuracoesPanel;
	
	private SalvarSnapShoot(){
		pastasVerificadas = false;
	}
	
	public static SalvarSnapShoot getInstance() {
		if(instance==null){
			instance = new SalvarSnapShoot();
		}
		return instance;
	}
	
	public void salvar(BufferedImage bi,long n) {
		try {
			snapshotN++;
			if(!pastasVerificadas){
				File dir = new File("screenshots");
				if(!dir.exists())dir.mkdir();
				dir = new File("screenshots/imagens");
				if(!dir.exists())dir.mkdir();
			}
			//salva o png
			ImageIO.write(bi, "png", new File("screenshots/imagens/f"+snapshotN+".png"));
			//salvar o html
			StringBuilder html=new StringBuilder();
			String configuracao = criarHtmlConf();
			html.append("<html><head><title>RedeEpistemica</title></head><body>");
			html.append("<div id=\"nav\">");
			html.append("<a href=\"p").append(snapshotN-1).append(".html\">Anterior</a> - ");
			html.append("<a href=\"p").append(snapshotN+1).append(".html\">Pr&oacute;xima</a>");
			html.append("</div>");
			html.append("<img src=\"imagens/f").append(snapshotN).append(".png\">");
			html.append("<div id=\"conf\">");
			html.append("Passo: ").append(n).append("<br />");
			html.append(configuracao);
			html.append("</div>");
			html.append("</body></html>");
			FileUtils.writeStringToFile(new File("screenshots/p"+snapshotN+".html"), html.toString(), "iso-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String criarHtmlConf() throws Exception{
		StringBuilder retorno=new StringBuilder();
		Method metodos[] = configuracoesPanel.getClass().getMethods();
		for(Method metodo : metodos){
			if(metodo.getName().startsWith("getTxt")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getTxt","getLbl"));
				JTextField obj = (JTextField)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.getText());
				retorno.append("<br />");
			}else if(metodo.getName().startsWith("getChk")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getChk","getLbl"));
				JCheckBox obj = (JCheckBox)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.isSelected());
				retorno.append("<br />");
			}else if(metodo.getName().startsWith("getSpn")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getSpn","getLbl"));
				JSpinner obj = (JSpinner)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.getValue());
				retorno.append("<br />");
			}
		}
		return retorno.toString();
	}
	
	public ConfiguracoesPanel getConfiguracoesPanel() {
		return configuracoesPanel;
	}
	public void setConfiguracoesPanel(ConfiguracoesPanel configuracoesPanel) {
		this.configuracoesPanel = configuracoesPanel;
	}
}
