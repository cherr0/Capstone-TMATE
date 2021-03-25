package com.tmate.web;

import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.service.BoardService;
import com.tmate.service.MemberService;
import com.tmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    private final BoardService boardService;

    // 메인화면
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

    // 사용자 포인트 디테일 확인
    @GetMapping("/pointd")
    public String userPointD(String m_id, Criteria cri, Model model) {
        List<JoinPointVO> pointList = userService.getMyPointList(cri, m_id);
        int total = memberService.getTotalPointCount(m_id);

        model.addAttribute("point", pointList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/pointDetail";
    }

    // 사용자 사용내역 확인
    @GetMapping("/paymentd")
    public String userPaymentD(String m_id, Criteria cri, Model model) {
        List<JoinReceiptVO> receiptList = userService.getMyReceiptList(cri, m_id);
        int total = userService.totalReceiptCount(m_id);

        model.addAttribute("receipt", receiptList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/paymentDetail";
    }

    // 사용자 이용기록 확인
    @GetMapping("/historyd")
    public String userHistoryD(Criteria cri, String m_id, Model model) {
        List<JoinHistoryVO> historyList = userService.getMyHistoryList(cri, m_id);
        int total = memberService.getTotalHistoryCount(m_id);

        model.addAttribute("history", historyList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));

        return "/user/historyDetail";
    }

    // 사용자 프로필 수정
    @GetMapping("/profiled")
    public String userProfiled(String m_id, Model model) {
        MemberDTO member = userService.getMainPage(m_id);
        model.addAttribute("member", member);
        return "/user/profileModify";
    }

    // 사용자 공지사항 확인
    @GetMapping("/notice")
    public String userNoticeList(Criteria cri, Model model) {
        log.info("사용자 페이지 공지사항");
        List<BoardDTO> boardList = boardService.getUserBoardList(cri);
        int total = boardService.getUserBoardCount();

        log.info("넘어가는 공지사항 : " + boardList);
        log.info("넘어가는 개수 : " + total);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/noticeList";
    }

    // 사용자 공지사항 세부 내용 확인
    @GetMapping("/notice/{bd_id}")
    public String userNotice(@PathVariable("bd_id") String bd_id, Model model) {
        log.info("넘어오는 글번호 : " + bd_id);
        BoardDTO boardDTO = boardService.getN(bd_id);

        model.addAttribute("notice", boardDTO);
        return "/user/noticeDetail";
    }

    // 사용자 이벤트 리스트 확인
    @Transactional
    @GetMapping("/event")
    public String userEventList(Criteria cri, Model model) {
        log.info("이벤트 리스트 - 사용자 컨트롤러");
        List<BoardDTO> eventList = boardService.getEventList(cri);
        int total = boardService.totalECount(cri);


        model.addAttribute("event", eventList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "/user/eventList";
    }

    // 사용자 이벤트 세부내용 확인
    @Transactional
    @GetMapping("/event/{bd_id}")
    public String userEvent(@PathVariable("bd_id") String bd_id, Model model) {
        log.info("넘어오는 글번호 : " + bd_id);
        BoardDTO boardDTO = boardService.getE(bd_id);
        List<BoardImageDTO> boardImageList = boardService.getBoardImageList(bd_id);
        log.info("조회되는 글 정보 : " + boardDTO);
        model.addAttribute("eimages", boardImageList);
        model.addAttribute("event", boardDTO);
        return "/user/eventDetail";
    }

    // 사용자 결제정보 확인
    @GetMapping("/payment")
    public String paymentByUser(String m_id, Model model) {
        List<PaymentDTO> paymentList = userService.getPaymentList(m_id);
        model.addAttribute("paymentList", paymentList);
        return "user/userCard";
    }

    // 사용자 지인 확인
    @GetMapping("/friend")
    public String friendByUser(String m_id, Model model) {

        List<NotificationDTO> myNotifiList = userService.getMyNotifiList(m_id);
        List<ApprovalDTO> myApproValList = userService.getMyApproValList(m_id);

        model.addAttribute("myNotifi", myNotifiList);
        model.addAttribute("myAppro", myApproValList);
        return "user/friend";
    }

    // 사용자 핫플레이스 검색
    @GetMapping("/place")
    public String placeByUser() {
        return "user/place";
    }
}
