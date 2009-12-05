package br.unicarioca.redesepistemicas.modelo;

import java.util.Random;

public class NumeroAleatorio {
	private static Random random = new Random(10L);
	public static double gerarNumero(){
		return random.nextDouble();
	}
	public static void restart(){
		random = new Random(10L);
	}
}
