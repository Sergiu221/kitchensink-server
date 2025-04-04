package com.kitchensink.service;

import com.kitchensink.data.MemberRepository;
import com.kitchensink.exception.EmailExistException;
import com.kitchensink.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberService {
    private final static Logger log = LoggerFactory.getLogger(MemberService.class);

    private final SequenceGeneratorService sequenceGenerator;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public MemberService(SequenceGeneratorService sequenceGenerator, MemberRepository memberRepository, MemberValidator memberValidator) {
        this.sequenceGenerator = sequenceGenerator;
        this.memberRepository = memberRepository;
        this.memberValidator = memberValidator;
    }

    @Transactional
    public void register(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new EmailExistException("Unique Email Violation");
        }
        memberValidator.validateMember(member);
        log.info("Registering {}", member.getName());
        member.setId(sequenceGenerator.generateSequence("member_sequence"));
        memberRepository.save(member);
    }

    public List<Member> findAllByOrderByNameAsc() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
