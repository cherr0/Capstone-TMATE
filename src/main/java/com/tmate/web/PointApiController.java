package com.tmate.web;

import com.tmate.domain.Criteria;
import com.tmate.domain.PointPageDTO;
import com.tmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/point/*")
@RestController
@Log4j2
@RequiredArgsConstructor
public class PointApiController {

    private final MemberService memberService;

    @Transactional
    @GetMapping(value = "/pages/{m_id}/{page}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PointPageDTO> getList(@PathVariable("m_id") String m_id, @PathVariable("page") int page) {

        Criteria cri = new Criteria(page,10);

        log.info("get point List m_id: " + m_id);
        log.info(cri);


        return new ResponseEntity<>(memberService.getPointListPage(cri,m_id), HttpStatus.OK);
    }
}
