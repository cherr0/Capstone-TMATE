package com.tmate.web;

import com.tmate.domain.Criteria;
import com.tmate.domain.EventDTO;
import com.tmate.domain.PageDTO;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class EventController {

    private final EventService eventService;

    // 이벤트 목록
    @GetMapping("/event")
    public String eventList(Model model, Criteria cri) {
        List<EventDTO> eventList = eventService.getListEvent(cri);
        int total = eventService.getTotalCount(cri);

        model.addAttribute("pageMaker", new PageDTO(cri, total));
        model.addAttribute("eventList", eventList);     // 이벤트 목록
        return "/admin/eventList";
    }


    // 이벤트 글 조회
    @GetMapping("/event/{e_id}")
    public String eventDetail(@PathVariable String e_id, Model model) {
        EventDTO event = eventService.get(e_id);

        eventService.viewCount(e_id);
        model.addAttribute("event", event);   // 이벤트 내용
        return "/admin/eventDetail";
    }


    // 이벤트 글 작성 페이지
    @GetMapping("/eventwrite")
    public String eventWrite() {
        return "/admin/eventWrite";
    }


    // 이벤트 글 수정 페이지
    @GetMapping("/eventmodify/{e_id}")
    public String eventModify(Model model,@PathVariable String e_id) {
        EventDTO event = eventService.get(e_id);

        model.addAttribute("event", event);
        return "/admin/eventModify";
    }

}
