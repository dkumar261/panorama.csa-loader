package com.csc.csa.loader.exception;

/**
 * 
 * @author dkumar261
 *
 */
public class CsaLoaderException extends RuntimeException {

	private static final long serialVersionUID = -8034680027853590740L;

	private final String message;

	
	public CsaLoaderException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 *
	 * @param cause
	 * @param messageCode
	 * @param message
	 */
	public CsaLoaderException(Throwable cause, String message) {

		super(message, cause);
		this.message = message;
	}
	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {

		return message;
	}
}
