package com.tmate.web;

import com.tmate.domain.Criteria;

import com.tmate.domain.MemberDTO;
import com.tmate.domain.MemberProfileVO;
import com.tmate.domain.PageDTO;
import com.tmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j2
public class MemberController {

    private final MemberService memberService;

    // 회원 목록
    @GetMapping("/user")
    public String memberList(Model model, Criteria cri) {
        List<MemberDTO> memberList = memberService.getListMembers(cri);
        int total = memberService.getTotalCount(cri);

        model.addAttribute("pageMaker", new PageDTO(cri, total));
        model.addAttribute("memberList", memberList); // 멤버 목록
        return "admin/userList";
    }

    // 회원 상세 조회
    @Transactional
    @GetMapping("/user/{m_id}")
    public String userDetail(@PathVariable String m_id,Model model) {
//        Map<String, Object> memberDetail = memberService.getMemberDetail(m_id);
//        model.addAttribute("List", memberDetail);  // 회원 포인트 리스트

        Criteria cri = new Criteria();
        // 멤버
        MemberProfileVO member = memberService.getMember(m_id);

        log.info("member = " + member);
        model.addAttribute("member", member);
        model.addAttribute("noshow", memberService.getNoShowByUser(m_id));

        return "admin/userDetail";
    }



}
