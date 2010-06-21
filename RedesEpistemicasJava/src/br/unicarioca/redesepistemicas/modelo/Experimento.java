package br.unicarioca.redesepistemicas.modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Modelo de experimento, com variaveis definidas 
 */
public class Experimento {
	private AgenteEpistemico pivo;
	private double maxDiff=0.002;
	private Set<ParEpistemico> crencas;
	private double minDiff=0.007;
	
	public Experimento(){
		crencas = new HashSet<ParEpistemico>();
	}
	
	public AgenteEpistemico getPivo() {
		return pivo;
	}
	public void setPivo(AgenteEpistemico pivo) {
		this.pivo = pivo;
	}
		
	public void addCrenca(ParEpistemico par){
		crencas.add(par);
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

	public void setMinDiff(double minDiff) {
		this.minDiff = minDiff;
	}
	
	public double getMinDiff() {
		return minDiff;
	}
}
