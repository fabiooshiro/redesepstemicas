package br.unicarioca.redesepistemicas.modelo;

/**
 * Calcula a diferenca por hipotenusa 
 */
public class ParEpistemicoDiffHip extends ParEpistemico3x2{
	
	/**
	 * Calcula a diferen&ccedil;a achando o valor da hipotenusa
	 * @param par2 Par para verificar a distancia
	 * @return Double diferenca
	 */
	public Double calcularDiferencaConsequente(ParEpistemico par2) {
		ParEpistemicoDiffHip par = (ParEpistemicoDiffHip) par2;
		Double diffX,diffY;
		Consequente con = par.getConsequente();
		diffX=consequente.getX()-con.getX();		
		diffY=consequente.getY()-con.getY();
		return Math.sqrt(diffX*diffX+diffY*diffY);
	}
}
