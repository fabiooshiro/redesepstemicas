package br.unicarioca.rottweiler;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Profile implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private String uid;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
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
}
