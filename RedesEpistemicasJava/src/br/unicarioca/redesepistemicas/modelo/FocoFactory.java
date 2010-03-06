package br.unicarioca.redesepistemicas.modelo;

/**
 * Responsavel por gerar um foco no espa&ccedil;o para o agente epistemico 
 */
public class FocoFactory {
	/**
	 * Cria um foco pseudo aleatoriamente
	 * @return Foco no espa&ccedil;o
	 */
	public static Foco criarFoco(){
		Foco retorno = new Foco();
		retorno.setX(NumeroAleatorio.gerarNumero());
		retorno.setY(NumeroAleatorio.gerarNumero());
		retorno.setZ(NumeroAleatorio.gerarNumero());
		retorno.setRaio(NumeroAleatorio.gerarNumero());
		return retorno;
	}
}
