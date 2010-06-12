package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Modelo de experimento, com variaveis definidas 
 */
public class Experimento {
	private AgenteEpistemico pivo;
	private ParEpistemico crenca;
	private double maxDiff=0.3;
	private Map<ParEpistemico,Color> crencas;
	
	
	public Experimento(){
		crencas = new HashMap<ParEpistemico,Color>();
	}
	
	public AgenteEpistemico getPivo() {
		return pivo;
	}
	public void setPivo(AgenteEpistemico pivo) {
		this.pivo = pivo;
	}
		
	public void addCrenca(ParEpistemico crenca, Color cor){
		crencas.put(crenca, cor);
	}
	
	public Set<ParEpistemico> getSetCrencas(){
		return crencas.keySet();
	}
	
	public Color getColor(ParEpistemico crenca){
		return crencas.get(crenca);
	}
	public double getMaxDiff() {
		return maxDiff;
	}
	public void setMaxDiff(double maxDiff) {
		this.maxDiff = maxDiff;
	}
}
