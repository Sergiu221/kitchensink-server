package com.kitchensink.data;

import com.kitchensink.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, Long> {

    Optional<Member> findById(@NotNull Long id);

    Member findByEmail(@NotNull @NotEmpty @Email String email);

    List<Member> findAllByOrderByNameAsc();
}
