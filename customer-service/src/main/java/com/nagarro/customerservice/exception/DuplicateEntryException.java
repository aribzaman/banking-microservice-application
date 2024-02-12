package com.nagarro.customerservice.exception;

import java.io.Serial;

public class DuplicateEntryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateEntryException(String entryName) {
        super("Duplicate Entry Found in Database for: " + entryName);
    }}