package com.crossover.techtrial.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.util.TestUtil;

/**
 * MemberControllerTest Test cases for MemberController.
 * 
 * @author crossover
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberControllerTest {

    @Autowired
    private TestRestTemplate template;

    /**
     * Test case for Member related APIs.
     */
    @Test
    public void aMemberShouldRegisterAndGet() throws Exception {

        // Arrange
        Map<String, String> params = new HashMap<>();
        HttpEntity<Object> member1 = TestUtil.getHttpEntity(
            "{\"name\": \"test 1\", \"email\": \"test1000000000000test1@gmail.com\"," +
            " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");
        HttpEntity<Object> member2 = TestUtil.getHttpEntity("{\"name\": \"1est 1\", \"email\": \"test1000000000000test1\"," +
            " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");

        // Act
        // POST
        ResponseEntity<Member> resultMemberShouldRegisterReturnOkStatus = template.postForEntity("/api/member", member1, Member.class);
        ResponseEntity<Member> restultMemberShouldNotRegisterReturnConflictStatus = template.postForEntity("/api/member", member1, Member.class);
        ResponseEntity<Member> resultMemberShouldNotRegisterReturnBadRequestStatus = template.postForEntity("/api/member", member2, Member.class);
        Long memberId = resultMemberShouldRegisterReturnOkStatus.getBody().getId();
        params.put("member-id", String.valueOf(memberId));

        // GET
        ResponseEntity<Member[]> resultAllMembersShouldBeGetReturnOkStatus = template.getForEntity("/api/member", Member[].class);
        ResponseEntity<Member> resultMemberShouldBeGetByMemberIdReturnOkStatus = template.getForEntity("/api/member/{member-id}", Member.class, params);
        ResponseEntity<Member> resultMemberShouldNotBeGetByMemberIdReturnNotFoundStatus = template.getForEntity("/api/member/000000000000", Member.class);

        // Assert
        Assert.assertEquals(resultMemberShouldRegisterReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(restultMemberShouldNotRegisterReturnConflictStatus.getStatusCode(), HttpStatus.CONFLICT);
        Assert.assertEquals(resultMemberShouldNotRegisterReturnBadRequestStatus.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(resultAllMembersShouldBeGetReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultMemberShouldBeGetByMemberIdReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultMemberShouldNotBeGetByMemberIdReturnNotFoundStatus.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * Test case for Member entity.
     */
    @Test
    public void bMemberGetterSetterAndToString() {

        // Arrange
        Member member = new Member();

        // Act
        member.setId(1L);
        String member1Str = member.toString();

        // Assert
        Assert.assertEquals(member.toString(), member1Str);
    }
}
