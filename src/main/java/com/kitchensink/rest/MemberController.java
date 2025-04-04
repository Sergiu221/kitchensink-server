package com.kitchensink.rest;

import com.kitchensink.model.Member;
import com.kitchensink.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest/members")
public class MemberController {
    private final MemberService memberService;

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
        memberService.register(member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
