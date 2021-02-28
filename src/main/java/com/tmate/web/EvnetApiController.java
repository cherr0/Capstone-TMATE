package com.tmate.web;

import com.tmate.domain.EventDTO;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class EvnetApiController {

    private final EventService eventService;


    // 이벤트 글 작성
    @PostMapping("/eventwrite")
    public boolean eventWrite(@RequestBody EventDTO event) {
        System.out.println("PostMapping eventWrite() event : " + event.toString());
        return eventService.register(event);
    }


    // 이벤트 글 수정
    @PutMapping("/eventmodify")
    public boolean eventModify(@RequestBody EventDTO event) {
        System.out.println("PutMapping eventModify() event : " + event.toString());
        return eventService.modify(event);
    }


    // 이벤트 글 삭제
    @DeleteMapping("/eventremove")
    public boolean eventRemove(String e_id) {
        System.out.println("DeleteMapping eventRemove() event No : " + e_id);
        return eventService.remove(e_id);
    }
}
