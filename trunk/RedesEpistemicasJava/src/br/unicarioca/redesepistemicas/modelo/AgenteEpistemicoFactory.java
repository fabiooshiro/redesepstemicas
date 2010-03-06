package br.unicarioca.redesepistemicas.modelo;

/**
 * Interface para criar agentes 
 */
public interface AgenteEpistemicoFactory {
	
	/**
	 * Criar um agente epistemico com as configuracoes
	 * @return AgenteEpistemico
	 */
	public AgenteEpistemico criarAgente();
}
