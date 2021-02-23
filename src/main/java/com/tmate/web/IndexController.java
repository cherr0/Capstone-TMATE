package com.tmate.web;

import com.tmate.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {


//    private final
    // 로그인 전 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 메인화면
    @GetMapping("/main")
    public String main() {
        List<MonthlyUsersVO> monthlyUser = new ArrayList<>();


        return "main";
    }

    // 공지 목록
    @GetMapping("/notice")
    public String noticeList(Model model) {
        List<BoardDTO> boardList = new ArrayList<>();


        model.addAttribute("noticeList", boardList);    // 공지 항목 보여주기
        return "noticeList";
    }

    // 공지 글 조회
    @GetMapping("/notice/{bd_id}")
    public String notceDetail(@PathVariable String bd_id, Model model) {
        BoardDTO notice = new BoardDTO();


        model.addAttribute("notice", notice);   // 공지 내용
        return "noticeDetail";
    }

    // 이벤트 목록
    @GetMapping("/event")
    public String eventList(Model model) {
        List<BoardDTO> eventList = new ArrayList<>();


        model.addAttribute("eventList", eventList);
        return "eventList";
    }

    // 이벤트 글 조회
    @GetMapping("/event/{bd_id}")
    public String eventDetail(@PathVariable String bd_id, Model model) {
        BoardDTO event = new BoardDTO();

        model.addAttribute("event", event);   // 이벤 내용
        return "eventDetail";
    }

    // 회원 목록
    @GetMapping("/user")
    public String memberList(Model model) {
        List<MemberDTO> memberList = new ArrayList<>();



        model.addAttribute("memberList", memberList); // 멤버 목록
        return "userList";
    }

    // 회원 상세 조회
    @GetMapping("/user/{m_id}")
    public String userDetail(@PathVariable String m_id, Model model) {
        List<JoinHistoryVO> historyList = new ArrayList<>();
        List<JoinPointVO> pointList = new ArrayList<>();

        model.addAttribute("pointList",pointList);  // 회원 포인트 리스트
        model.addAttribute("historyList", historyList); // 이용 내역 리스트
        return "userDetail";
    }

    // 기사 승인 대기 목록
    @GetMapping("/approval")
    public String approval(Model model) {
        List<JoinApprovalVO> apList = new ArrayList<>();
        List<JoinDriverVO> dvList = new ArrayList<>();

        model.addAttribute("apList", apList);
        model.addAttribute("dvList", dvList);
        return "approvalList";
    }

    // 핫플레이스 목록 관리
    @GetMapping("/hotplace")
    public String hotplace(Model model) {
        List<PlaceDTO> placeList = new ArrayList<>();

        model.addAttribute("placeList", placeList);
        return "hotplace";
    }
}
