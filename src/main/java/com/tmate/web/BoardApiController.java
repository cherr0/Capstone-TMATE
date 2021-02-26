package com.tmate.web;

import com.tmate.domain.BoardDTO;
import com.tmate.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardService boardService;

    // 공지 글 작성 전송
    @PostMapping("/api/noticewrite")
    public boolean noticeWrite(@RequestBody BoardDTO board) {
        System.out.println("PostMapping noticeWrite() board : " + board.toString());

        return boardService.register(board);
    }


    // 공지 수정 전송
    @PutMapping("/api/noticemodify")
    public boolean noticeModify(@RequestBody BoardDTO board) {
        System.out.println("PutMapping noticeModify() board : " + board.toString());

        return boardService.modify(board);
    }


    // 공지 삭제
    @DeleteMapping("/api/noticeremove")
    public boolean noticeRemove(String bd_id) {
        System.out.println("DeleteMapping noticeRemove() board No : " + bd_id);

        return boardService.remove(bd_id);
    }
}
