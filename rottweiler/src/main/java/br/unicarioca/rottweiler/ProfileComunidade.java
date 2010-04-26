package br.unicarioca.rottweiler;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProfileComunidade implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Integer aderiu;
	private Profile profile;
	private Comunidade comunidade;
	private Date dataHora;
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Comunidade getComunidade() {
		return comunidade;
	}
	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}
	
	/**
	 * 
	 * @return data do scan
	 */
	public Date getDataHora() {
		return dataHora;
	}
	
	/**
	 * 
	 * @param dataHora data do scan
	 */
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	
	/**
	 * 1 aderiu
	 * 0 fora
	 * @param aderiu
	 */
	public void setAderiu(Integer aderiu) {
		this.aderiu = aderiu;
	}
	
	/**
	 * 1 aderiu
	 * 0 fora
	 * @param aderiu
	 */
	public Integer getAderiu() {
		return aderiu;
	}
}
