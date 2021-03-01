package com.tmate.web;

<<<<<<< HEAD
import com.tmate.domain.JoinApprovalVO;
=======
import com.tmate.domain.Criteria;
import com.tmate.domain.JoinApprovalVO;
import com.tmate.domain.PageDTO;
>>>>>>> changhyeon
import com.tmate.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ApprovalController {

    private final ApprovalService approvalService;

    // 기사 승인 대기 목록
    @GetMapping("/approval")
<<<<<<< HEAD
    public String approval(Model model) {
        List<JoinApprovalVO> noneApprovalList = approvalService.getNoneApprovalList();

=======
    public String approval(Model model, Criteria cri) {
        List<JoinApprovalVO> noneApprovalList = approvalService.getNoneApprovalList(cri);
        int total = approvalService.getTotalApproCount(cri);

        model.addAttribute("pageMaker", new PageDTO(cri, total));
>>>>>>> changhyeon
        model.addAttribute("apList", noneApprovalList);
        return "approvalList";
    }

    // 관리자 승인
    @PostMapping("/api/approvalallow")
    public int allowApproval(String d_id) {
        System.out.println("PostMapping allowApproval() driver No : " + d_id);
        return approvalService.allowApproval(d_id);
    }

    // 관리자 거절
    @DeleteMapping("/api/approvalremove")
    public int removeDriver(String d_id) {
        System.out.println("PostMapping removeDriver() driver No : " + d_id);
        return approvalService.removeDriver(d_id);
    }
}
