package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um par epistemico composto por um antecedente e um consequente
 * @author Fabio Issamu Oshiro
 */
public class ParEpistemico3x2 implements ParEpistemico {
	protected Antecedente antecedente;
	protected Consequente consequente;
	public ParEpistemico3x2() {
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
	 * Calcula a diferenca,
	 * pode dar throws em class cast exception
	 * @param par Par a ser confrontado
	 * @return diferenca
	 */
	public Double calcularDiferencaConsequente(ParEpistemico par2) {
		ParEpistemico3x2 par = (ParEpistemico3x2)par2;
		Double diff = 0.0;
		Consequente con = par.getConsequente();
		
		diff+=Math.abs(consequente.getX()-con.getX());		
		diff+=Math.abs(consequente.getY()-con.getY());
		return diff;
	}
	@Override
	public void addAntecedente(Double d) {
		if(antecedente==null){
			antecedente = new Antecedente();
		}
		if(antecedente.getX()==null)
			antecedente.setX(d);
		if(antecedente.getY()==null)
			antecedente.setY(d);
		if(antecedente.getZ()==null)
			antecedente.setZ(d);
	}
	@Override
	public void addConsequente(Double d) {
		if(consequente==null){
			consequente = new Consequente();
		}
		if(consequente.getX()==null)
			consequente.setX(d);
		if(consequente.getY()==null)
			consequente.setY(d);
	}
	@Override
	public int getSizeAntecedente() {
		return 3;
	}
	@Override
	public int getSizeConsequente() {
		return 2;
	}
	
	@Override
	public boolean antecedenteEquals(ParEpistemico antecedente) {
		return false;
	}
	
	@Override
	public ArrayList<Double> getDoubleAntecedentes() {
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(antecedente.getX());
		list.add(antecedente.getY());
		list.add(antecedente.getZ());
		return list;
	}
	@Override
	public ArrayList<Double> getDoubleConsequentes() {
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(consequente.getX());
		list.add(consequente.getY());
		return list;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
