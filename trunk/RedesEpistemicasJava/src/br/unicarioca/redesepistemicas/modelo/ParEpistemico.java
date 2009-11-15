package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;

public class ParEpistemico {
	private Antecedente antecedente;
	private Consequente consequente;
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
	public Double calcularDiferencaConsequente(ParEpistemico par) {
		Double diff = 0.0;
		Consequente con = par.getConsequente();
		diff+=Math.abs(consequente.getX()-con.getX());		
		diff+=Math.abs(consequente.getY()-con.getY());
		return diff;
	}
	
}
