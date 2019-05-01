package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.service.MemberService;

/**
 * MemberController for Member related APIs.
 * 
 * @author crossover
 */
@RestController
public class MemberController {
    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * This method is used to register a member, member is Library community people
     * to whom Librarian can issue books.
     * 
     * @param p It is a Member type, Required member details to register it.
     * @return Member Details if pass all validation & registered successfully.
     */
    @PostMapping(path = "/api/member")
    public ResponseEntity<Member> register(@RequestBody Member p) {

        if (memberService.findByEmail(p.getEmail()) == null) {
            LOG.info("Provided Member details: {} register successfully", p);
            return ResponseEntity.ok(memberService.save(p));
        }

        LOG.info("Provided member-email: {} already available", p.getEmail());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * This method is used to get all registered member details.
     * 
     * @return List<Member> List of registered member details, Also can be empty if
     *         no member registered yet.
     */
    @GetMapping(path = "/api/member")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /**
     * This method is used to get a registered member details by Id.
     * 
     * @param memberId A Member Id, by which member registered.
     * @return Member Registered member details, Also can be empty if no registered
     *         member details found for provided memberId.
     */
    @GetMapping(path = "/api/member/{member-id}")
    public ResponseEntity<Member> getMemberById(@PathVariable(name = "member-id", required = true) Long memberId) {

    	Member member = memberService.findById(memberId);
        if (member != null) {
            return ResponseEntity.ok(member);
        }

        LOG.info("Provided member-id: {} not found", memberId);
        return ResponseEntity.notFound().build();
    }

    /**
     * This API returns the top 5 members who issued the most books within the
     * search duration. Only books that have dateOfIssue and dateOfReturn within the
     * mentioned duration should be counted. Any issued book where dateOfIssue or
     * dateOfReturn is outside the search, should not be considered.
     * 
     * @param startTime Book's dateOfIssue as per Transaction entity.
     * @param endTime   Book's dateOfReturn as per Transaction entity.
     * @return topMembers top 5 or less[as per availability] members details whose
     *         transactions is in between start & end time, Also can be empty if no
     *         member found in that time period.
     */
    @GetMapping(path = "/api/member/top-member")
    public ResponseEntity<List<TopMemberDTO>> getTopMembers(
        @RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
        @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        List <TopMemberDTO> topMembers = new ArrayList<>();

        if (ChronoUnit.SECONDS.between(startTime, endTime) >= 0) {

            List <Object[]> topMembersObjList = transactionRepository.getTopMembers(startTime, endTime);
            if (!topMembersObjList.isEmpty()) {

                for (Object[] topMembersObj: topMembersObjList) {
                    TopMemberDTO topMemberDTO = new TopMemberDTO();
                    topMemberDTO.setMemberId(Long.parseLong(topMembersObj[0].toString()));
                    topMemberDTO.setName(topMembersObj[1].toString());
                    topMemberDTO.setEmail(topMembersObj[2].toString());
                    topMemberDTO.setBookCount(Integer.parseInt(topMembersObj[3].toString()));
                    topMembers.add(topMemberDTO);
                }
                return ResponseEntity.ok(topMembers);
            } else {
                LOG.info("No Members found in between {} - {}", startTime, endTime);
                return ResponseEntity.notFound().build();
            }
        }
        LOG.info("endTime: {} should not be less than or equal to startTime: {}", endTime, startTime);
        return ResponseEntity.badRequest().build();
    }
}
