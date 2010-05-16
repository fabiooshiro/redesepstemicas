package br.unicarioca.redesepistemicas.modelo;

/**
 * Modelo de experimento, com variaveis definidas 
 */
public class Experimento {
	private AgenteEpistemico pivo;
	private ParEpistemico crenca;
	public AgenteEpistemico getPivo() {
		return pivo;
	}
	public void setPivo(AgenteEpistemico pivo) {
		this.pivo = pivo;
	}
	public ParEpistemico getCrenca() {
		return crenca;
	}
	public void setCrenca(ParEpistemico crenca) {
		this.crenca = crenca;
	}
	
}
