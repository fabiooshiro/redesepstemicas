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
	 * Necessario para realizar a busca em largura
	 */
	private Integer ordem;
	
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
		this.invalido = invalido;
	}
}
