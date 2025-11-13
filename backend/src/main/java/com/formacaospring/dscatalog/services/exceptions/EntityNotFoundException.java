package com.formacaospring.dscatalog.services.exceptions;

@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException{
	public EntityNotFoundException(String msg) {
		super(msg);
	}
}	
