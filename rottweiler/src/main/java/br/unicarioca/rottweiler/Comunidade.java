package br.unicarioca.rottweiler;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Representa uma comunidade
 * @author Fabio Issamu Oshiro
 *
 */
@Entity
public class Comunidade implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String cid;
	private String nome;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
