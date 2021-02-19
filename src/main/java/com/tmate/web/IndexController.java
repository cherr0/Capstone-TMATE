package com.tmate.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {


    // 로그인 전 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 메인화면
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    // 공지 목록
    @GetMapping("/notice")
    public String noticeList() {
        return "noticeList";
    }

    // 공지 글 조회
    @GetMapping("/notice/{bd_id}")
    public String notceDetail(@PathVariable String bd_id) {

        return "noticeDetail";
    }

    // 이벤트 목록
    @GetMapping("/event")
    public String eventList() {
        return "eventList";
    }

    // 이벤트 글 조회
    @GetMapping("/event/{bd_id}")
    public String eventDetail(@PathVariable String bd_id) {
        return "eventDetail";
    }

    // 회원 목록
    @GetMapping("/user")
    public String userList() {
        return "userList";
    }

    // 회원 상세 조회
    @GetMapping("/user/{u_id}")
    public String userDetail(@PathVariable String u_id) {
        return "userDetail";
    }

    // 기사 승인 대기 목록
    @GetMapping("/approval")
    public String approval() {
        return "approval";
    }

    // 핫플레이스 목록 관리
    @GetMapping("hotplace")
    public String hotplace() {
        return "hotplace";
    }
}
