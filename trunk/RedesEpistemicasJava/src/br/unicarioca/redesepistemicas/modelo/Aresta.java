package br.unicarioca.redesepistemicas.modelo;

public class Aresta {
	private Double peso;
	private AgenteEpistemico receptor;
	private AgenteEpistemico emissor;
	public AgenteEpistemico getEmissor() {
		return emissor;
	}
	public void setEmissor(AgenteEpistemico emissor) {
		this.emissor = emissor;
	}
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
	 * @return receptor
	 */
	public AgenteEpistemico getReceptor() {
		return receptor;
	}
	/**
	 * @param receptor the agenteEpistemico to set
	 */
	public void setReceptor(AgenteEpistemico receptor) {
		this.receptor = receptor;
	}
	@Override
	public boolean equals(Object obj) {
		Aresta a = (Aresta)obj;
		if(this.getReceptor().equals(a.getReceptor())){
			return true;
		}else{
			return false;
		}
	}
}
