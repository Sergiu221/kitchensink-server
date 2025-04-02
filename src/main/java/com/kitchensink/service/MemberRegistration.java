package com.kitchensink.service;

import com.kitchensink.data.MemberRepository;
import com.kitchensink.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberRegistration {
    private final static Logger log = LoggerFactory.getLogger(MemberRegistration.class);

    private final MemberRepository memberRepository;

    public MemberRegistration(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void register(Member member)  {
        log.info("Registering {}", member.getName());
        memberRepository.save(member);
    }
}
