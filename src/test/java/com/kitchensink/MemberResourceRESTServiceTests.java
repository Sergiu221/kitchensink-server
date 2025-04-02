package com.kitchensink;

import com.kitchensink.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MemberResourceRESTServiceTests {
    private final String baseUrl = "http://localhost";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnOkAfterCreateMember() {

        final String url = baseUrl + ":" + port + "/rest/members";
        Member member = new Member();
        member.setName("Test Name");
        member.setEmail("test@test.com");
        member.setPhoneNumber("1234567890");
        HttpEntity<Member> entity = new HttpEntity<>(member, new HttpHeaders());

        ResponseEntity<?> response = testRestTemplate.postForEntity(url, entity, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, Objects.requireNonNull(response.getStatusCode()));
    }
    @Test
    void shouldReturnOkAfterFindAllMembers() {
        final String url = baseUrl + ":" + port + "/rest/members";
        Member member = new Member();
        member.setName("Test First");
        member.setEmail("test1@test.com");
        member.setPhoneNumber("2234567890");
        testRestTemplate.postForEntity(url, new HttpEntity<>(member, new HttpHeaders()), Object.class);

        Member member2 = new Member();
        member2.setName("Test Second");
        member2.setEmail("test2@test.com");
        member2.setPhoneNumber("2234567890");
        testRestTemplate.postForEntity(url, new HttpEntity<>(member2, new HttpHeaders()), Object.class);

        ResponseEntity<?> response = testRestTemplate.getForEntity(url,Object.class);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getStatusCode()));
        Assertions.assertEquals(2, ((List<?>) Objects.requireNonNull(response.getBody())).size());
    }

    @Test
    void shouldReturnNotOkEmailAlreadyExists() {
        final String url = baseUrl + ":" + port + "/rest/members";
        Member member = new Member();
        member.setName("Test First");
        member.setEmail("test1@test.com");
        member.setPhoneNumber("2234567890");
        testRestTemplate.postForEntity(url, new HttpEntity<>(member, new HttpHeaders()), Object.class);

        Member member2 = new Member();
        member2.setName("Test Second");
        member2.setEmail("test1@test.com");
        member2.setPhoneNumber("2234567890");
        ResponseEntity<?> response =testRestTemplate.postForEntity(url, new HttpEntity<>(member2, new HttpHeaders()), Object.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, Objects.requireNonNull(response.getStatusCode()));
        Assertions.assertEquals("{email=Email taken}", Objects.requireNonNull(response.getBody()).toString());
    }

    @Test
    void shouldReturnNotOkPhoneNumbertooLong() {
        final String url = baseUrl + ":" + port + "/rest/members";
        Member member = new Member();
        member.setName("Test First");
        member.setEmail("test1@test.com");
        member.setPhoneNumber("223456789023232");

        ResponseEntity<?> response =testRestTemplate.postForEntity(url, new HttpEntity<>(member, new HttpHeaders()), Object.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, Objects.requireNonNull(response.getStatusCode()));
        Assertions.assertEquals("{phoneNumber=numeric value out of bounds (<12 digits>.<0 digits> expected)}", Objects.requireNonNull(response.getBody()).toString());
    }
}
