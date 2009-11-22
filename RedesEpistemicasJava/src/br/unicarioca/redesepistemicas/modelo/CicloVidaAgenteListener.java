package br.unicarioca.redesepistemicas.modelo;

public interface CicloVidaAgenteListener {
	public void criado(AgenteEpistemico agente);

	public void morto(AgenteEpistemico agente);
}
