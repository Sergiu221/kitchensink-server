package com.kitchensink.data;

import com.kitchensink.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findById(Long id);

    Member findByEmail(@NotNull @NotEmpty @Email String email);

    List<Member> findAllByOrderByNameAsc();
}
