package com.kitchensink.data;

import com.kitchensink.model.Member;
import com.kitchensink.service.MemberRegisteredEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MemberListProducer {

    @Autowired
    private MemberRepository memberRepository;

    private List<Member> members = new ArrayList<>();

    @EventListener
    public void handleMemberRegisteredEvent(MemberRegisteredEvent event) {
        retrieveAllMembersOrderedByName();
    }

    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
        members = memberRepository.findAllByOrderByNameAsc();
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
