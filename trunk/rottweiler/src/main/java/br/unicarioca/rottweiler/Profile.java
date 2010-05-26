package br.unicarioca.rottweiler;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Representa um profile
 * @author Fabio Issamu Oshiro
 */
@Entity
public class Profile implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private String uid;
	
	/**
	 * 1 ou 0
	 */
	private Integer valido;
	/**
	 * Necessario para realizar a busca em largura
	 */
	private Integer ordem;
	
	/**
	 * 1 ou 0
	 * @return 1 ou 0
	 */
	public Integer getValido() {
		return valido==null?0:valido;
	}
	
	/**
	 * Deve ser atribuido somente no final do scan, quando
	 * se sabe se existem scraps publicos
	 * @param valido 1 ou 0
	 */
	public void setValido(Integer valido) {
		this.valido = valido;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	private String nome;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	private String invalido;
	private Date dataHora;
	
	/**
	 * @return data do scan
	 */
	public Date getDataHora() {
		return dataHora;
	}
	
	/**
	 * @param dataHora data do scan
	 */
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	
	/**
	 * Motivo para estar marcado como invalido
	 * @return null caso seja valido ou o motivo
	 */
	public String getInvalido() {
		return invalido;
	}
	
	/**
	 * Identifique o motivo pelo qual ele esta invalido.<br>
	 * Pode ser por causa que apaga os scraps<br>
	 * Pode ser por causa que os scraps sao restritos
	 * 
	 * @param invalido motivo
	 */
	public void setInvalido(String invalido) {
		if(invalido!=null){
			setValido(0);
		}
		this.invalido = invalido;
	}
}
