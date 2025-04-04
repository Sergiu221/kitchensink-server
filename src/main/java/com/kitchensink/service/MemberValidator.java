package com.kitchensink.service;

import com.kitchensink.model.Member;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class MemberValidator {

    private final Validator validator;

    @Autowired
    public MemberValidator(Validator validator) {

        this.validator = validator;
    }

    public void validateMember(Member member) throws ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }
}
