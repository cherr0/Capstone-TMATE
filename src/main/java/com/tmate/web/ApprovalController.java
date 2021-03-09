package com.tmate.web;


import com.tmate.domain.Criteria;
import com.tmate.domain.JoinApprovalVO;
import com.tmate.domain.PageDTO;

import com.tmate.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j2
public class ApprovalController {

    private final ApprovalService approvalService;

    // 기사 승인 대기 목록
    @GetMapping("/approval")
    public String approval(Model model, Criteria cri) {
        List<JoinApprovalVO> noneApprovalList = approvalService.getNoneApprovalList(cri);
        log.info(noneApprovalList);
        int total = approvalService.getTotalApproCount(cri);

        model.addAttribute("pageMaker", new PageDTO(cri, total));
        model.addAttribute("apList", noneApprovalList);
        return "/admin/approvalList";
    }


}
