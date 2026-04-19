package com.gestionganado.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " with id " + id + " not found");
    }
}
