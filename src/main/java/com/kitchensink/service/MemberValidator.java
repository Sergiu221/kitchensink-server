package com.kitchensink.service;

import com.kitchensink.model.Member;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class MemberValidator {

    private final Validator validator;

    @Autowired
    public MemberValidator(Validator validator) {

        this.validator = validator;
    }

    public void validateMember(Member member) throws ValidationException {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).findFirst().get());
        }
    }
}
