package br.unicarioca.redesepistemicas.modelo;
/**
 * Listener de ciclo de vida de um agente <br>
 * que recebe eventos de quando o agente &eacute; criado ou morto
 */
public interface CicloVidaAgenteListener {
	
	/**
	 * Chamado quando o agente &eacute; criado
	 * @param agente AgenteEpistemico
	 */
	public void criado(AgenteEpistemico agente);

	/**
	 * Chamado quando o agente &eacute; morto
	 * @param agente AgenteEpistemico
	 */
	public void morto(AgenteEpistemico agente);
}
