package com.tmate.web;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.EventDTO;
import com.tmate.service.BoardService;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class EventApiController {


    private final BoardService boardService;


    // 이벤트 글 작성
    @PostMapping("/eventwrite")
    public boolean eventWrite(@RequestBody BoardDTO event) {
        System.out.println("PostMapping eventWrite() event : " + event.toString());
        return boardService.eRegister(event);
    }


    // 이벤트 글 수정
    @PutMapping("/eventmodify")
    public boolean eventModify(@RequestBody BoardDTO event) {
        System.out.println("PutMapping eventModify() event : " + event.toString());
        return boardService.eModify(event);
    }


    // 이벤트 글 비공개 처리
    @DeleteMapping("/eventremove/{bd_id}")
    public boolean eventRemove(@PathVariable("bd_id") String bd_id) {
        System.out.println("PutMapping eventRemove() event No : " + bd_id);
        return boardService.remove(bd_id);
    }
}
