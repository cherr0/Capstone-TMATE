package com.tmate.web;

import com.tmate.domain.Criteria;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.JoinPointVO;
import com.tmate.domain.MemberDTO;
import com.tmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    // 회원 목록
    @GetMapping("/user")
    public String memberList(Model model, Criteria cri) {
        List<MemberDTO> memberList = memberService.getListMembers(cri);

        model.addAttribute("memberList", memberList); // 멤버 목록
        return "userList";
    }

    // 회원 상세 조회
    @GetMapping("/user/{m_id}")
    public String userDetail(@PathVariable String m_id, Model model) {
        Map<String, Object> memberDetail = memberService.getMemberDetail(m_id);

        model.addAttribute("pointList", memberDetail);  // 회원 포인트 리스트
        return "userDetail";
    }

}
