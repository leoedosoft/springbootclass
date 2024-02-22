package com.teoresi.webapp.exception;

public class DuplicateException  extends Exception
{
	private static final long serialVersionUID = -5870522280695461980L;
	
	private String messaggio;
	
	public DuplicateException()
	{
		super();
	}
	
	public DuplicateException(String messaggio)
	{
		super(messaggio);
		this.messaggio = messaggio;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}
