package br.unicarioca.redesepistemicas.bo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SalvarSnapShoot {

	//TODO colocar os parametros do programa talvez passando o ConfiguracaoPanel
	public static void salvar(BufferedImage bi,long n) {
		try {
			ImageIO.write(bi, "png", new File("screenshots/"+n+".png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
