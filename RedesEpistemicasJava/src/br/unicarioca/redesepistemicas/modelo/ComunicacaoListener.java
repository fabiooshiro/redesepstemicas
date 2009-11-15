package br.unicarioca.redesepistemicas.modelo;

public interface ComunicacaoListener {
	public void comunicadorEscolhido(AgenteEpistemico emissor);
	public void depoisDeComunicar(AgenteEpistemico emissor,AgenteEpistemico receptor, Double peso,Double diff);
}
