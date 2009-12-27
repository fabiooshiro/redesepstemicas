package br.unicarioca.redesepistemicas.modelo;

public class FocoFactory {
	public static Foco criarFoco(){
		Foco retorno = new Foco();
		retorno.setX(NumeroAleatorio.gerarNumero());
		retorno.setY(NumeroAleatorio.gerarNumero());
		retorno.setZ(NumeroAleatorio.gerarNumero());
		retorno.setRaio(NumeroAleatorio.gerarNumero());
		return retorno;
	}
}
