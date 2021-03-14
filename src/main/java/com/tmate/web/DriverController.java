package com.tmate.web;

import com.tmate.domain.*;
import com.tmate.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping("driver")
    public String driverList(Model model, Criteria cri) {
       log.info("기사리스트로 이동중");
        List<JoinDriversVO> drivers = driverService.getListDriverPagingList(cri);
        int total = driverService.countTotalDrivers();
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        model.addAttribute("drivers",drivers);
        return"/admin/driverList";
    }

    @Transactional
    @GetMapping("driver/{d_id}")
    public String driverDetail(@PathVariable("d_id") String d_id,Model model) {
        log.info("넘어오는 기사번호 : " + d_id);
        JoinDriverProfileVO driver = driverService.getDriver(d_id);
        log.info("넘어가는 기사정보: " + driver);
        int like = driverService.countLike(d_id);
        int dislike = driverService.countDislike(d_id);
        int history = driverService.getCountHistory(d_id);
        List<JoinBanVO> banList = driverService.getBanList(d_id);
        model.addAttribute("banList", banList);
        model.addAttribute("driver", driver);
        model.addAttribute("like", like);
        model.addAttribute("dislike", like);
        model.addAttribute("history", history);

        return "/admin/driverDetail";
    }
}
