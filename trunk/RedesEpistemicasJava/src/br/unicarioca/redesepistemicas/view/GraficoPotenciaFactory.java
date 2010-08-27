package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class GraficoPotenciaFactory{
	public static BufferedImage create(RedeEpistemica redeEpistemica, int totFaixas, int w, int h){
		List<AgenteEpistemico> listAgenteEpistemicos = redeEpistemica.getListAgenteEpistemico();
		if(listAgenteEpistemicos.size()==0) return null;
		double max, min;
		max = min=listAgenteEpistemicos.get(0).getPesoReputacao();
		for(AgenteEpistemico agente: listAgenteEpistemicos){
			max = Math.max(max, agente.getPesoReputacao()+0.005);
			min = Math.min(min, agente.getPesoReputacao());
		}
		double intervalo = (max-min) / (double)totFaixas;
		double last = min;
		List<Faixa> faixas = new ArrayList<Faixa>();
		System.out.println("intervalo " + intervalo);
		for(int i=0;i<totFaixas;i++){
			Faixa faixa = new Faixa();
			faixa.min = last;
			faixa.max = last = min + intervalo + intervalo*i;
			faixas.add(faixa);
			//System.out.println(" min " + faixa.min + " max " + faixa.max);
		}
		int maxTotal = 0;
		for(AgenteEpistemico agente: listAgenteEpistemicos){// O(a*f)
			boolean enfaixado = false;
			for(Faixa faixa: faixas){
				if(faixa.min<=agente.getPesoReputacao() && agente.getPesoReputacao() < faixa.max){
					faixa.total++;
					maxTotal = Math.max(maxTotal, faixa.total);
					enfaixado = true;
					break;
				}
			}
			if(!enfaixado){
				System.out.println("not " + agente.getPesoReputacao() + " min " + min + " max " + max);
			}
		}
		
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		intervalo = w/(float)totFaixas;
		float uy = (h-20)/(float)maxTotal;
		Graphics gra = bi.getGraphics();
		gra.setColor(new Color(200,200,200));
		gra.fillRect(0, 0, w, h);
		int x1 = 0;
		int x2 = (int)intervalo/2;
		int y1 = (int)(faixas.get(0).total * uy);
		System.out.println("uy = " + uy);
		for(Faixa faixa:faixas){
			System.out.println( faixa.total + "\t"  + maxTotal + " faixa [" + faixa.min + " " + faixa.max + "]");
			int y2 = (int)(faixa.total * uy);
			gra.setColor(Color.BLACK);
			if(x1!=0)
			gra.drawLine(x1, h - y1 - 10, x2, h - y2 - 10);
			y1 = y2;
			x1 = x2;
			x2+=(int)intervalo;
			gra.setColor(Color.LIGHT_GRAY);
			gra.drawLine(x1, 0, x1, h);
		}
		return bi;
	}
}
class Faixa{
	int total = 0;
	double max, min;
}
