package br.unicarioca.redesepistemicas.modelo;
/**
 * Classe responsave por criar os pares epistemicos
 * @author fabio
 *
 */
public class ParEpistemicoFactory {
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			double x=Math.random(),y=Math.random(),z=Math.random(),r=Math.random();
			for(int k=0;k<100;k++){
				double[][] a = criarPontoEmEsfera(x,y,z,r);
				double dist = distancia(x, y, z, a[0][0], a[0][1], a[0][2]);
				if(dist>r){
					System.out.println("ERRO");
				}else{
					System.out.println("("+x+", "+y+", "+z+") r"+r + " d"+dist);
				}
			}
		}
	}
	/**
	 * Cria um par epistemico dentro de uma esfera
	 * 
	 * @param x
	 *            coordenada
	 * @param y
	 *            coordenada
	 * @param z
	 *            coordenada
	 * @param raio
	 *            medida
	 * @return ParEpistemico
	 */
	public static ParEpistemico criar(double x, double y, double z, double raio) {
		double[][] a = criarPontoEmEsfera(x, y, z, raio);
		Antecedente antecedente = new Antecedente();
		antecedente.setX(a[0][0]);
		antecedente.setY(a[0][1]);
		antecedente.setZ(a[0][2]);
		ParEpistemico parEpistemico = new ParEpistemicoDiffHip();
		parEpistemico.setAntecedente(antecedente);
		Consequente consequente = new Consequente();
		parEpistemico.setConsequente(consequente);
		return parEpistemico;
	}

	private static double[][] criarPontoEmEsfera(double x, double y, double z, double raio){
		double nX = 0, nY = 0, nZ = Math.random() * raio;
		double radX = Math.toRadians(360.0 * Math.random());
		double radY = Math.toRadians(360.0 * Math.random());
		double[][] a = {{ nX, nY, nZ, 1.0 }};
		double[][] mX =	
			{
				{1, 0, 0, 0},
				{0,Math.cos(radX),Math.sin(radX),0},
				{0,-Math.sin(radX),Math.cos(radX),0},
				{0, 0, 0, 1}
			};
		a = multiplicar(a,mX);
		double[][] mY =	
		{
			{Math.cos(radY), 0, -Math.sin(radY), 0},
			{0, 1, 0, 0},
			{Math.sin(radX),0,Math.cos(radX),0},
			{x, y, z, 1}
		};
		return multiplicar(a,mY);
	}
	
	/**
	 * Calcula a distancia de Ponto1 para Ponto2
	 * @param x do Ponto
	 * @param y do Ponto
	 * @param z do Ponto
	 * @param x2 do Ponto 2
	 * @param y2 do Ponto 2
	 * @param z2 do Ponto 2
	 * @return distancia
	 */
	public static double distancia(double x, double y, double z, double x2,double y2, double z2){
		x=x-x2;
		y=y-y2;
		z=z-z2;
		return Math.sqrt(x*x+y*y+z*z);
	}
	private static double[][] multiplicar(double[][] mat1, double[][] mat2) {
		int i;
		double[][] mat3 = new double[mat1.length][mat2[0].length];
		for (int linha = 0; linha < mat1.length; linha++){
			for (int coluna = 0; coluna < mat2[0].length; coluna++) {
				double acumula_somaprod = 0;
				for (i = 0; i < mat1[0].length; i++){
					acumula_somaprod = acumula_somaprod + mat1[linha][i] * mat2[i][coluna];
				}
				mat3[linha][coluna] = acumula_somaprod;
			}
		}
		return mat3;
	}
}
