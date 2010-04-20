package br.unicarioca.rottweiler;

import java.io.Serializable;

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
}
