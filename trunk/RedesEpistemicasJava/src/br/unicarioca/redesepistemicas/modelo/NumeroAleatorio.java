package br.unicarioca.redesepistemicas.modelo;

import java.util.Random;

/**
 * Classe utilitaria para gerar um numero pseudo aleatorio.<br>
 * Desta forma os numeros sorteados podem ser os mesmos.
 */
public class NumeroAleatorio {
	private static Random random = new Random(10L);
	
	/**
	 * Numero
	 * @return numero pseudo aleatorio
	 */
	public static double gerarNumero(){
		return random.nextDouble();
	}
	
	/**
	 * Reinicia o sorteio
	 */
	public static void restart(){
		random = new Random(10L);
	}
}
