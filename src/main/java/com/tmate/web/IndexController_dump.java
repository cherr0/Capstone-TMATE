package com.tmate.web;

import com.tmate.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class IndexController_dump {


    // 로그인 전 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 메인화면
    @GetMapping("/main")
    public String main(MemberDTO member, HttpSession httpSession) {
        List<MonthlyUsersVO> monthlyUser = new ArrayList<>();

        // 더미 데이터
        for(int i=1 ; i<=12 ; i++) {
            MonthlyUsersVO user = new MonthlyUsersVO();
            user.setStandard(i + "월");
            user.setUsers(i * ((int) Math.random() * 100));
        }

        httpSession.setAttribute("member", member);
        return "main";
    }

    // 공지 목록
    @GetMapping("/notice")
    public String noticeList(Model model) {
        List<BoardDTO> boardList = new ArrayList<>();

        for(int i=1 ; i<=20 ; i++) {
            BoardDTO board = new BoardDTO();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            // 더미 데이터
            board.setBd_id("not" + i);
            board.setBd_status("0");
            board.setBd_contents("테스트 중 입니다. "  + i);
            board.setBd_title("테스트 제목 " + i);
            board.setBd_cre_date(timestamp);
            board.setM_id("a1010123456780");
            boardList.add(board);
        }

        model.addAttribute("noticeList", boardList);    // 공지 항목 보여주기
        return "noticeList";
    }

    // 공지 글 작성
    @GetMapping("/noticewrite")
    public String noticeWrite() {
        return "noticeWrite";
    }

    // 공지 글 수정
    @GetMapping("/noticemodify")
    public String noticeModify() {
        return "noticeModify";
    }

    // 공지 글 조회
    @GetMapping("/notice/{bd_id}")
    public String notceDetail(@PathVariable String bd_id, Model model) {
        BoardDTO notice = new BoardDTO();

        // 더미 데이터
        notice.setBd_id(bd_id);
        notice.setBd_status("0");
        notice.setBd_contents("테스트 중 입니다.");
        notice.setBd_title("테스트 제목 1");
        notice.setBd_cre_date(new Timestamp(Calendar.getInstance().getTime().getTime()));
        notice.setM_id("a1010123456780");

        model.addAttribute("notice", notice);   // 공지 내용
        return "noticeDetail";
    }

    // 이벤트 목록
    @GetMapping("/event")
    public String eventList(Model model) {
        List<BoardDTO> eventList = new ArrayList<>();

        for(int i=1 ; i<=10 ; i++) {
            BoardDTO event = new BoardDTO();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            // 더미 데이터
            event.setBd_id("not" + i);
            event.setBd_status("0");
            event.setBd_contents("테스트 중 입니다. "  + i);
            event.setBd_title("테스트 제목 " + i);
            event.setBd_cre_date(timestamp);
            event.setM_id("a1010123456780");
            eventList.add(event);
        }

        model.addAttribute("eventList", eventList);
        return "eventList";
    }

    // 이벤트 작성
    @GetMapping("/eventwrite")
    public String eventWrite() {
        return "eventWrite";
    }

    // 이벤트 수정
    @GetMapping("eventmodify")
    public String eventModify() {
        return "eventModify";
    }

    // 이벤트 글 조회
    @GetMapping("/event/{bd_id}")
    public String eventDetail(@PathVariable String bd_id, Model model) {
        BoardDTO event = new BoardDTO();

        // 더미 데이터
        event.setBd_id(bd_id);
        event.setBd_status("0");
        event.setBd_contents("테스트 중 입니다.");
        event.setBd_title("테스트 제목 1");
        event.setBd_cre_date(new Timestamp(Calendar.getInstance().getTime().getTime()));
        event.setM_id("a1010123456780");

        model.addAttribute("event", event);   // 이벤 내용
        return "eventDetail";
    }

    // 회원 목록
    @GetMapping("/user")
    public String memberList(Model model) {
        List<MemberDTO> memberList = new ArrayList<>();


        for(int i=1 ; i<=10 ; i++) {
            MemberDTO member = new MemberDTO();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            // 더미 데이터
            member.setM_id("u101023456789" + i);
            member.setM_birth(timestamp);
            member.setM_n_use((int) Math.random() * 100);
            member.setM_t_use((int) Math.random() * 100);
            member.setM_point((int) Math.random() * 10000);
            member.setM_regdate(timestamp);
            member.setM_email("anyong123" + i +"@gmail.com");
            member.setM_count((int) Math.random() * 100);
            member.setM_level(String.valueOf((int)Math.random() * 4));
            member.setM_status("0");
            member.setM_profile("default");
            member.setM_house("집주소");
            memberList.add(member);
        }

        model.addAttribute("memberList", memberList); // 멤버 목록
        return "userList";
    }

    // 회원 상세 조회
    @GetMapping("/user/{m_id}")
    public String userDetail(@PathVariable String m_id, Model model) {
        List<JoinHistoryVO> historyList = new ArrayList<>();
        List<JoinPointVO> pointList = new ArrayList<>();

        // 더미 데이터
        for(int i=1 ; i<=10 ; i++) {
            JoinHistoryVO history = new JoinHistoryVO();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            history.setMerchant_uid("merchant_" + timestamp + "0");
            history.setM_id(m_id);
            history.setH_s_lttd("출발지 " + i);
            history.setH_s_lngtd("도착지 " + i);
            history.setH_s_time(timestamp);
            history.setH_e_time(timestamp);
            history.setCar_no("123가567" + i);
            history.setCar_model("소나타");
            history.setM_name("장동빈");
            historyList.add(history);
        }

        // 더미 데이터
        for(int i=1 ; i<=10 ; i++) {
            JoinPointVO point = new JoinPointVO();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            point.setPo_exact("0");
            point.setPo_result((int) Math.random() * 1000);
            point.setPo_course("탑승 혜택 포인트");
            point.setPo_time(timestamp);
            point.setM_point((int) Math.random() * 1000 + 250);
            pointList.add(point);
        }

        model.addAttribute("pointList",pointList);  // 회원 포인트 리스트
        model.addAttribute("historyList", historyList); // 이용 내역 리스트
        return "userDetail";
    }

    // 기사 승인 대기 목록
    @GetMapping("/approval")
    public String approval(Model model) {
        List<JoinApprovalVO> apList = new ArrayList<>();
        List<JoinDriverVO> dvList = new ArrayList<>();

        // 더미 데이터
        for(int i=1 ; i<=5 ; i++) {
            JoinApprovalVO approval = new JoinApprovalVO();
            approval.setD_id("d101012345678" + i);
            approval.setCorp_code("i");
            approval.setD_license_no("17-대구-2345" + i);
            approval.setCar_model("소나타");
            apList.add(approval);
        }

        // 더미 데이터
        for(int i=1 ; i<=5 ; i++) {
            JoinDriverVO driver = new JoinDriverVO();
            driver.setD_id("d101012345678" + i);
            driver.setM_name("장동빈");
            driver.setD_license_no("17-대구-2345" + i);
            dvList.add(driver);
        }

        model.addAttribute("apList", apList);
        model.addAttribute("dvList", dvList);
        return "approvalList";
    }

    // 핫플레이스 목록 관리
    @GetMapping("/hotplace")
    public String hotplace(Model model) {
        List<PlaceDTO> placeList = new ArrayList<>();

        for(int i=1 ; i<=10 ; i++) {
            PlaceDTO place = new PlaceDTO();
            place.setPl_id("14458");
            place.setPl_name("중구 대포동 419-23");
            place.setPl_lttd(Math.random() * 200);
            place.setPl_lngtd(Math.random() * 200);
            place.setPl_start(0);
            place.setPl_finish(0);
            placeList.add(place);
        }

        model.addAttribute("placeList", placeList);
        return "hotplace";
    }
}
