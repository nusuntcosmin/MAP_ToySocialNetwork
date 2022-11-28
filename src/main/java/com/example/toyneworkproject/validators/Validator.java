package com.example.toyneworkproject.validators;

import com.example.toyneworkproject.exceptions.ValidationException;

public interface Validator<T>{
    void validate(T entity) throws ValidationException;
}
