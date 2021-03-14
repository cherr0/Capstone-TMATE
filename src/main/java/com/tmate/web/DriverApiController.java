package com.tmate.web;

import com.tmate.domain.Criteria;
import com.tmate.domain.HisotryDPageDTO;
import com.tmate.domain.HistoryPageDTO;
import com.tmate.domain.ReviewDPageDTO;
import com.tmate.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DriverApiController {

    private final DriverService driverService;

    @Transactional
    @GetMapping(value = "/pagesh/{m_id}/{page}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HisotryDPageDTO> getHList(@PathVariable("m_id") String m_id, @PathVariable("page") int page) {

        Criteria cri = new Criteria(page,10);

        log.info("get history List m_id: " + m_id);
        log.info(cri);


        return new ResponseEntity<>(driverService.getHistoryList(cri,m_id), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/pagesr/{m_id}/{page}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReviewDPageDTO> getRList(@PathVariable("m_id") String m_id, @PathVariable("page") int page) {

        Criteria cri = new Criteria(page,10);

        log.info("get Review List m_id: " + m_id);
        log.info(cri);
        log.info("리뷰 개수 : " + driverService.getCountReview(m_id));

        return new ResponseEntity<>(driverService.getReviewList(cri,m_id), HttpStatus.OK);
    }
}
