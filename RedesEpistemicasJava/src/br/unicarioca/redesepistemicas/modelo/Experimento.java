package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

/**
 * Modelo de experimento, com variaveis definidas 
 */
public class Experimento {
	private AgenteEpistemico pivo;
	private ParEpistemico crenca;
	private double maxDiff=0.3;
	private Set<ParEpistemico> crencas;
	
	
	public Experimento(){
		crencas = new HashSet<ParEpistemico>();
	}
	
	public AgenteEpistemico getPivo() {
		return pivo;
	}
	public void setPivo(AgenteEpistemico pivo) {
		this.pivo = pivo;
	}
		
	public void addCrenca(ParEpistemico crenca){
		crencas.add(crenca);
		
	}
	
	public Set<ParEpistemico> getSetCrencas(){
		return crencas;
	}
		
	public double getMaxDiff() {
		return maxDiff;
	}
	public void setMaxDiff(double maxDiff) {
		this.maxDiff = maxDiff;
	}
}
