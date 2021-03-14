package com.tmate.web;

import com.tmate.domain.*;
import com.tmate.service.MemberService;
import com.tmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/tuser/*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MemberService memberService;

    @GetMapping("/main")
    public String userMain(String m_id, Model model) {
        log.info("넘어오는 회원번호 : " + m_id);
        model.addAttribute("memberp", userService.getMainPage(m_id));
        model.addAttribute("like", userService.totalCountLike(m_id));
        model.addAttribute("dislike", userService.totalCountDislike(m_id));
        model.addAttribute("ban", userService.totalCountBan(m_id));
        model.addAttribute("history", userService.getWeeklyHistoryList(m_id));
        model.addAttribute("point", userService.getWeeklyPointList(m_id));
        return "/user/main";
    }

    @GetMapping("/pointd")
    public String userPointD(String m_id, Criteria cri, Model model) {
        List<JoinPointVO> pointList = userService.getMyPointList(cri, m_id);
        int total = memberService.getTotalPointCount(m_id);

        model.addAttribute("point", pointList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/pointDetail";
    }

    @GetMapping("/paymentd")
    public String userPaymentD(String m_id, Criteria cri, Model model) {
        List<JoinReceiptVO> receiptList = userService.getMyReceiptList(cri, m_id);
        int total = userService.totalReceiptCount(m_id);

        model.addAttribute("receipt", receiptList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/paymentDetail";
    }

    @GetMapping("/historyd")
    public String userHistoryD(Criteria cri, String m_id, Model model) {
        List<JoinHistoryVO> historyList = userService.getMyHistoryList(cri, m_id);
        int total = memberService.getTotalHistoryCount(m_id);

        model.addAttribute("history", historyList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));

        return "/user/historyDetail";
    }


}
