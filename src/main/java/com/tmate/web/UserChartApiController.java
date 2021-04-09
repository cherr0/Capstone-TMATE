package com.tmate.web;

import com.tmate.domain.ChartDTO;
import com.tmate.domain.MonthlyUsersVO;
import com.tmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequestMapping("/chart/*")
@RequiredArgsConstructor
@RestController
public class UserChartApiController {

    private final UserService userService;

    //월간 사용자 포인트 - 사용
    @GetMapping(value = "/usepoint/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map<String,List<ChartDTO>>> getMonthlyUsePoint(@PathVariable("m_id") String m_id) {
        log.info("사용 포인트");
        Map<String, List<ChartDTO>> map = new HashMap<>();
        List<ChartDTO> use = userService.monthlyUsePoint(m_id);
        List<ChartDTO> get = userService.monthlyGetPoint(m_id);

        log.info("화면단에 넘어가는 사용포인트 : " + use);
        log.info("화면단에 넘어가는 적립포인트 : " + get);
        map.put("use", use);
        map.put("get", get);
        return new ResponseEntity<>(map, HttpStatus.OK) ;
    }

    // 월간 사용자 포인트 - 적립
    @GetMapping(value = "/getpoint/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<ChartDTO>> getMonthlyGetPoint(@PathVariable("m_id") String m_id) {
        log.info("적립 포인트");
        return new ResponseEntity<>(userService.monthlyGetPoint(m_id), HttpStatus.OK) ;
    }

    // 월간 사용자 이용 내역 - 동승 , 일반
    @GetMapping(value = "/together/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map<String,List<ChartDTO>>> getMonthlyTogether(@PathVariable("m_id") String m_id) {
        log.info("넘어오는 회원코드 : " + m_id);

        log.info("월별 동승 횟수");
        Map<String, List<ChartDTO>> map = new HashMap<>();
        map.put("together", userService.monthlyTogether(m_id));
        map.put("normal", userService.monthlyNormal(m_id));
        return new ResponseEntity<>(map, HttpStatus.OK) ;
    }

    // 월간 사용자 이용 내역 - 일반
    @GetMapping(value = "/normal/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<ChartDTO>> getMonthlyNormal(@PathVariable("m_id") String m_id) {
        log.info("월별 일반 횟수");
        return new ResponseEntity<>(userService.monthlyNormal(m_id), HttpStatus.OK) ;
    }

    // 월간 사용자 결제 - 카드, 현금
    @GetMapping(value = "/card/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map<String,List<ChartDTO>>> getMonthlyUseCard(@PathVariable("m_id") String m_id) {
        log.info("월별 카드 결제 횟수");
        Map<String, List<ChartDTO>> map = new HashMap<>();
        map.put("card", userService.monthlyUseCard(m_id));
        map.put("cash", userService.monthlyUseCash(m_id));
        return new ResponseEntity<>(map, HttpStatus.OK) ;
    }

    // 월간 사용자 결제 - 현금
    @GetMapping(value = "/cash/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<ChartDTO>> getMonthlyUseCash(@PathVariable("m_id") String m_id) {
        log.info("월별 현금 결제 횟수");
        return new ResponseEntity<>(userService.monthlyUseCash(m_id), HttpStatus.OK) ;
    }
}
