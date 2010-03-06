package br.unicarioca.redesepistemicas.modelo;

/**
 * Calcula a diferenca por hipotenusa 
 */
public class ParEpistemicoDiffHip extends ParEpistemico{
	
	/**
	 * Calcula a diferenca achando o valor da hipotenusa
	 * @param par Par para verificar a distancia
	 * @return Double diferenca
	 */
	public Double calcularDiferencaConsequente(ParEpistemicoDiffHip par) {
		Double diffX,diffY;
		Consequente con = par.getConsequente();
		diffX=consequente.getX()-con.getX();		
		diffY=consequente.getY()-con.getY();
		return Math.sqrt(diffX*diffX+diffY*diffY);
	}
}
