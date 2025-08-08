package com.trading.exception;

// Custom exception when trying to sell more stocks than owned
public class InsufficientHoldingException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientHoldingException(String message) {
        super(message);
    }
}