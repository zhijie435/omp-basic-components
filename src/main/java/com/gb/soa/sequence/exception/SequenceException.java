package com.gb.soa.sequence.exception;



public class SequenceException extends RuntimeException {
	private static final long serialVersionUID = 58326433760774844L;
	
	private Long code;
	public SequenceException(String message) {
		super(message);	
	}

	/**
	 * 获得code
	 * @return long
	 */
	public long getCode() {
		return code;
	}

	/**
	 * 设置code
	 * @param code
	 */
	public void setCode(long code) {
		this.code = code;
	}
	
	
}

