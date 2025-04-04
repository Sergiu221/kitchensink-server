package com.kitchensink.data;

import com.kitchensink.model.Member;
import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, Long> {

    @NonNull Optional<Member> findById(@NotNull Long id);

    boolean existsByEmail(@NotNull @NotEmpty @Email String email);

    List<Member> findAllByOrderByNameAsc();
}
