package com.araki.sales.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> listErrors = new ArrayList<>();

	public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getListErrors() {
		return listErrors;
	}

	public void addError(String fieldName, String message) {
		listErrors.add(new FieldMessage(fieldName, message));
	}
	
}
