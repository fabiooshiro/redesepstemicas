package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;

public class Consequente {
	private Double x;
	private Double y;
	
	public Consequente(){}
	
	public Consequente(ArrayList<Double> outputs) {
		x = outputs.get(0);
		y = outputs.get(1);
	}
	/**
	 * @return the x
	 */
	public Double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(Double x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public Double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(Double y) {
		this.y = y;
	}
	
}