package com.tmate.web;

import com.tmate.domain.*;
import com.tmate.service.BoardService;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class EventController {

    private final EventService eventService;
    private final BoardService boardService;

    // 이벤트 목록
    @GetMapping("/event")
    public String eventList(Model model, Criteria cri) {
        log.info("이벤트 리스트 컨트롤러");
        List<BoardDTO> eventList = boardService.getEventList(cri);
        int total = boardService.totalECount(cri);

        log.info("이벤트 리스트 : " + eventList);
        log.info("총 개수 : " + total);

        model.addAttribute("pageMaker", new PageDTO(cri, total));
        model.addAttribute("eventList", eventList);     // 이벤트 목록
        return "admin/eventList";
    }


    // 이벤트 글 조회
    @Transactional
    @GetMapping("/event/{bd_id}")
    public String eventDetail(@PathVariable("bd_id") String bd_id, Model model) {
//        EventDTO event = eventService.get(e_id);

//        eventService.viewCount(e_id);

        BoardDTO event = boardService.get(bd_id);
        List<BoardImageDTO> boardImageList = boardService.getBoardImageList(bd_id);
        model.addAttribute("event", event);
        model.addAttribute("eimages", boardImageList);
        return "admin/eventDetail";
    }


    // 이벤트 글 작성 페이지
    @GetMapping("/eventwrite")
    public String eventWrite() {
        return "/admin/eventWrite";
    }


    // 이벤트 글 수정 페이지
    @GetMapping("/eventmodify/{bd_id}")
    public String eventModify(Model model,@PathVariable("bd_id") String bd_id) {
        BoardDTO event = boardService.get(bd_id);
        List<BoardImageDTO> boardImageList = boardService.getBoardImageList(bd_id);

        model.addAttribute("event", event);
        model.addAttribute("eimages", boardImageList);
        return "/admin/eventModify";
    }

}
