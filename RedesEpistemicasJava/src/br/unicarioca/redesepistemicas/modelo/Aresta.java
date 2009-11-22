package br.unicarioca.redesepistemicas.modelo;

public class Aresta {
	private Double peso;
	private AgenteEpistemico agenteEpistemico;
	/**
	 * @return the peso
	 */
	public Double getPeso() {
		return peso;
	}
	/**
	 * @param peso the peso to set
	 */
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	/**
	 * @return the agenteEpistemico
	 */
	public AgenteEpistemico getAgenteEpistemico() {
		return agenteEpistemico;
	}
	/**
	 * @param agenteEpistemico the agenteEpistemico to set
	 */
	public void setAgenteEpistemico(AgenteEpistemico agenteEpistemico) {
		this.agenteEpistemico = agenteEpistemico;
	}
	@Override
	public boolean equals(Object obj) {
		Aresta a = (Aresta)obj;
		if(this.getAgenteEpistemico().equals(a.getAgenteEpistemico())){
			return true;
		}else{
			return false;
		}
	}
}
