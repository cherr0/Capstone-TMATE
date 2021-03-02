package com.tmate.web;

import com.tmate.domain.*;

import com.tmate.service.MainService;
import com.tmate.service.MemberService;
import com.tmate.service.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final MainService mainService;

    private final SMSService smsService;

    // 로그인 전 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 메인화면
    @GetMapping("/main")
    public String main(Model model, HttpSession httpSession) {
        int members = mainService.countMembers();       // 총 유저 수
        int drivers = mainService.countDrivers();       // 총 기사 수
        int weeklyUsers = mainService.countWeeklyUsers();   // 주간 이용자 수
        List<MonthlyUsersVO> monthlyUsers = mainService.countMonthlyUsers(); // 월간 이용자 수
        List<UsersByAgeVO> usersByAge = mainService.countUsersByAge();  // 연령별 이용자 수
        List<PlaceDTO> placeByStart = mainService.rankHotplaceByStart();    // 핫플레이스 이용 순위


        System.out.println("members = " + members);
        System.out.println("drivers = " + drivers);
        System.out.println("weeklyUsers = " + weeklyUsers);
        System.out.println("monthlyUsers = " + monthlyUsers);
        System.out.println("placeByStart = " + placeByStart);
        System.out.println("usersByAge = " + usersByAge);

        PhoneDTO phone = new PhoneDTO("박중원","01067501664","");

        MemberDTO member = smsService.getPermission(phone);
        httpSession.setAttribute("member",member);


        model.addAttribute("members", members);
        model.addAttribute("drivers", drivers);
        model.addAttribute("weeklyUsers", weeklyUsers);
        model.addAttribute("monthlyUsers", monthlyUsers);
        model.addAttribute("usersByAge", usersByAge);
        model.addAttribute("placeByStart", placeByStart);
        return "main";
    }

    // 유저 메인화면
    @GetMapping("/usermain")
    public String userMain() {
        return "userMain";
    }

    // 회원가입 QR 코드 사용하여 모바일 화면 연결
    @GetMapping("/registerqrcode")
    public String qrCode() {
        return "registerQRcode";
    }

}
