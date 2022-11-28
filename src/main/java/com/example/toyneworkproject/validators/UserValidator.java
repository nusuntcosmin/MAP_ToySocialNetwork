package com.example.toyneworkproject.validators;

import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.ValidationException;

public class UserValidator implements Validator<User> {
    public UserValidator() {

    }

    @Override
    public void validate(User entity) throws ValidationException {
        if(!(entity.getFirstName()+" "+entity.getLastName()).matches("^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)")){
            throw new ValidationException("Invalid name");
        }
        if(!(entity.getEmail()).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new ValidationException("Invalid email");
    }
}
