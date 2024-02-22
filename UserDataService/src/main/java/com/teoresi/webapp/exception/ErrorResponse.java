package com.teoresi.webapp.exception;


import java.time.LocalDate;


public class ErrorResponse 
{
	private LocalDate date;
	private int code;
	private String message;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public static class InfoMsg
    {
        public LocalDate data;

        public String message;

        public InfoMsg(LocalDate data, String message) {
            this.data = data;
            this.message = message;
        }

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
