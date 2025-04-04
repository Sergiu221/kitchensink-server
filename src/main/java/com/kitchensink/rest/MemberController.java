package com.kitchensink.rest;

import com.kitchensink.model.Member;
import com.kitchensink.service.MemberService;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest/members")
public class MemberController {

    private final static Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> listMembers() {
        return memberService.findAllByOrderByNameAsc();
    }

    @GetMapping(value = "/{id:[0-9]+}")
    public Member lookupMemberById(@PathVariable("id") Long id) {
        return memberService.findMember(id);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
            memberService.register(member);

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

    private ResponseEntity<?> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.error("Validation completed. violations found: {}", violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObj);
    }
}
