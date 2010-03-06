package br.unicarioca.redesepistemicas.modelo;

/**
 * Representa um par epistemico composto por um antecedente e um consequente
 */
public class ParEpistemico {
	protected Antecedente antecedente;
	protected Consequente consequente;
	public ParEpistemico() {
	}
	/**
	 * @return the antecedente
	 */
	public Antecedente getAntecedente() {
		return antecedente;
	}
	/**
	 * @param antecedente the antecedente to set
	 */
	public void setAntecedente(Antecedente antecedente) {
		this.antecedente = antecedente;
	}
	/**
	 * @return the consequente
	 */
	public Consequente getConsequente() {
		return consequente;
	}
	/**
	 * @param consequente the consequente to set
	 */
	public void setConsequente(Consequente consequente) {
		this.consequente = consequente;
	}
	
	/**
	 * Calcula a diferenca
	 * @param par Par a ser confrontado
	 * @return diferenca
	 */
	public Double calcularDiferencaConsequente(ParEpistemico par) {
		Double diff = 0.0;
		Consequente con = par.getConsequente();
		
		diff+=Math.abs(consequente.getX()-con.getX());		
		diff+=Math.abs(consequente.getY()-con.getY());
		return diff;
	}
	
}
