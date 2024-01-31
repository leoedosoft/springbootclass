package com.teoresi.blogdataapi.exceptions;

import java.util.Date;


public class ErrorResponse 
{
	private Date data = new Date();
	private int codice;
	private String messaggio;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}
