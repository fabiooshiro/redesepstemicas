package br.unicarioca.redesepistemicas.modelo;

/**
 * Calcula a diferenca por hipotenusa 
 */
public class ParEpistemicoDiffHip extends ParEpistemico{
	
	public Double calcularDiferencaConsequente(ParEpistemicoDiffHip par) {
		Double diffX,diffY;
		Consequente con = par.getConsequente();
		diffX=consequente.getX()-con.getX();		
		diffY=consequente.getY()-con.getY();
		return Math.sqrt(diffX*diffX+diffY*diffY);
	}
	
}
