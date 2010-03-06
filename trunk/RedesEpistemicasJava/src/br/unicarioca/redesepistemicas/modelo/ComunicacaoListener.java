package br.unicarioca.redesepistemicas.modelo;

/**
 * Listener para receber eventos de comunicacao 
 */
public interface ComunicacaoListener {
	/**
	 * Informa qual agente foi escolhido como emissor ou comunicador
	 * @param emissor AgenteEpistemico sorteado
	 */
	public void comunicadorEscolhido(AgenteEpistemico emissor);
	
	/**
	 * Chamado depois de uma comunicacao ocorrer
	 * @param emissor dono do par transmitido
	 * @param receptor quem recebe o par
	 * @param peso Peso da relacao
	 * @param diff diferenca dos pontos de vista do emissor para o receptor
	 */
	public void depoisDeComunicar(AgenteEpistemico emissor,AgenteEpistemico receptor, Double peso,Double diff);
}
