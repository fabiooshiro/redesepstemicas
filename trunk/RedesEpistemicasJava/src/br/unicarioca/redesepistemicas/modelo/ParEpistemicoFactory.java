package br.unicarioca.redesepistemicas.modelo;

/**
 * Classe responsavel por criar os pares epistemicos
 * @author Fabio Issamu Oshiro
 *
 */
public class ParEpistemicoFactory {
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			double x=NumeroAleatorio.gerarNumero(),y=NumeroAleatorio.gerarNumero(),z=NumeroAleatorio.gerarNumero(),r=NumeroAleatorio.gerarNumero();
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
	 * @param foco
	 * @return ParEpistemico
	 */
	public static ParEpistemico3x2 criar(Foco foco) {
		return criar(foco.getX(),foco.getY(),foco.getZ(),foco.getRaio());
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
	public static ParEpistemico3x2 criar(double x, double y, double z, double raio) {
		double[][] a = criarPontoEmEsfera(x, y, z, raio);
		Antecedente antecedente = new Antecedente();
		
		antecedente.setX(normaliza(a[0][0]));
		antecedente.setY(normaliza(a[0][1]));
		antecedente.setZ(normaliza(a[0][2]));
		ParEpistemico3x2 parEpistemico = new ParEpistemicoDiffHip();
		parEpistemico.setAntecedente(antecedente);
		Consequente consequente = new Consequente();
		parEpistemico.setConsequente(consequente);
		return parEpistemico;
	}
	
	/**
	 * Garante que o numero esta entre 0 e 1
	 * @param a numero qualquer
	 * @return valor entre 0 e 1
	 */
	private static double normaliza(double a){
		return Math.min(1, Math.max(0, a));
	}

	private static double[][] criarPontoEmEsfera(double x, double y, double z, double raio){
		double nX = 0, nY = 0, nZ = NumeroAleatorio.gerarNumero() * raio;
		double radX = Math.toRadians(360.0 * NumeroAleatorio.gerarNumero());
		double radY = Math.toRadians(360.0 * NumeroAleatorio.gerarNumero());
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

	public static ParEpistemico criar(int antecedente, int consequente) {
		ParEpistemicoOrkut par = new ParEpistemicoOrkut();
		par.setSizeAntecedente(antecedente);
		for(int i=0;i<antecedente;i++){
			par.addAntecedente(Math.random());
		}
		par.setSizeConsequente(consequente);
		return par;
	}
	
}
