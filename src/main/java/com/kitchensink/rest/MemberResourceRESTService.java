package com.kitchensink.rest;

import com.kitchensink.data.MemberRepository;
import com.kitchensink.model.Member;
import com.kitchensink.service.MemberRegistration;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/members")
public class MemberResourceRESTService {

    private final static Logger log = Logger.getLogger(MemberResourceRESTService.class.getName());
    @Autowired
    private Validator validator;

    private final MemberRepository memberRepository;
    private final MemberRegistration registration;

    public MemberResourceRESTService(MemberRepository memberRepository, MemberRegistration registration) {

        this.memberRepository = memberRepository;
        this.registration = registration;
    }

    @GetMapping(produces = "application/json")
    public List<Member> listMembers() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    @GetMapping(value = "/{id:[0-9]+}", produces = "application/json")
    public Member lookupMemberById(@PathVariable("id") Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createMember(@RequestBody  Member member) {
        try {
            // Validates member using bean validation
            validateMember(member);

            registration.register(member);

        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObj);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private ResponseEntity<?> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObj);
    }

    public boolean emailAlreadyExists(String email) {
        Member member = null;
        //try {
            member = memberRepository.findByEmail(email);
        //} catch (NoResultException e) {
            // ignore
        //}
        return member != null;
    }
}
