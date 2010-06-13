package br.unicarioca.redesepistemicas.modelo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ParEpistemicoOrkut implements ParEpistemico{
	private ArrayList<Double> antecedente = new ArrayList<Double>();
	private ArrayList<Double> consequente = new ArrayList<Double>();
	private int sizeAntecedente;
	private int sizeConsequente;
	private Color cor;
	private String nome;
	
	public ArrayList<Double> getAntecedente() {
		return antecedente;
	}
	public void setAntecedente(ArrayList<Double> antecedente) {
		this.antecedente = antecedente;
	}
	public List<Double> getConsequente() {
		return consequente;
	}
	public void setConsequente(ArrayList<Double> consequente) {
		this.consequente = consequente;
	}
	
	
	@Override
	public Double calcularDiferencaConsequente(ParEpistemico par2) {
		ParEpistemicoOrkut par = (ParEpistemicoOrkut) par2;
		par.getConsequente();
		Double retorno=0.0;
		for(int i=0;i<par.getConsequente().size();i++){
			double a = par.getConsequente().get(i);
			double b = getConsequente().get(i);
			retorno+=Math.abs(a-b);		
		}
		return retorno;
	}
	
	@Override
	public synchronized void addAntecedente(Double d) {
		if(antecedente.size()==sizeAntecedente){
			antecedente.clear();
		}
		antecedente.add(d);		
	}
	
	@Override
	public synchronized void addConsequente(Double d) {
		if(consequente.size()==sizeConsequente){
			consequente.clear();
		}
		consequente.add(d);
	}
	public void setSizeAntecedente(int i) {
		sizeAntecedente = i;
	}
	public void setSizeConsequente(int i){
		sizeConsequente = i;
	}
	@Override
	public int getSizeAntecedente() {
		return sizeAntecedente;
	}
	@Override
	public int getSizeConsequente() {
		return sizeConsequente;
	}
	@Override
	public boolean antecedenteEquals(ParEpistemico parAnt) {
		for(int i=0;i<parAnt.getDoubleAntecedentes().size();i++){
			if(!parAnt.getDoubleAntecedentes().get(i).equals(antecedente.get(i))){
				return false;
			}
		}
		return true;
	}
	@Override
	public ArrayList<Double> getDoubleAntecedentes() {
		return antecedente;
	}
	@Override
	public ArrayList<Double> getDoubleConsequentes() {
		return consequente;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ParEpistemicoOrkut o = (ParEpistemicoOrkut)super.clone();
		o.antecedente=new ArrayList<Double>();
		for(double d:this.antecedente){
			o.antecedente.add(d);//manual copy, may addAll
		}
		o.consequente=new ArrayList<Double>();
		for(double d:this.consequente){
			o.consequente.add(d);//manual copy, may addAll
		}
		return o;
	}
	
	public Color getCor() {
		return cor;
	}
	public void setCor(Color cor) {
		this.cor = cor;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
