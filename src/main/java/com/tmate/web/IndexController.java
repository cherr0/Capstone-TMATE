package com.tmate.web;

import com.tmate.domain.*;
import com.tmate.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final MainService mainService;

    // 로그인 전 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 메인화면
    @GetMapping("/main")
    public String main(Model model) {
        int members = mainService.countMembers();       // 총 유저 수
        int drivers = mainService.countDrivers();       // 총 기사 수
        int weeklyUsers = mainService.countWeeklyUsers();   // 주간 이용자 수
        List<MonthlyUsersVO> monthlyUsers = mainService.countMonthlyUsers(); // 월간 이용자 수
        List<UsersByAgeVO> usersByAge = mainService.countUsersByAge();  // 연령별 이용자 수
        List<PlaceDTO> placeByStart = mainService.rankHotplaceByStart();    // 핫플레이스 이용 순위

        model.addAttribute("members", members);
        model.addAttribute("drivers", drivers);
        model.addAttribute("monthlyUsers", monthlyUsers);
        model.addAttribute("usersByAge", usersByAge);
        model.addAttribute("placeByStart", placeByStart);
        return "main";
    }

}
