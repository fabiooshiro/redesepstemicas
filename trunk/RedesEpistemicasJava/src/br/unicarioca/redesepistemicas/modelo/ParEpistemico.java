package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.ArrayList;

public interface ParEpistemico extends Cloneable{
	public long getId();
	public void setId(long id);
	public void addAntecedente(Double d);
	public void addConsequente(Double d);
	public Double calcularDiferencaConsequente(ParEpistemico par);
	public int getSizeAntecedente();
	public int getSizeConsequente();
	public Object clone() throws CloneNotSupportedException;
	public boolean antecedenteEquals(ParEpistemico antecedente);
	public ArrayList<Double> getDoubleAntecedentes();
	public ArrayList<Double> getDoubleConsequentes();
	public void setNome(String nomeInRow);
	public void setCor(Color colorInRow);
	public Color getCor();
}