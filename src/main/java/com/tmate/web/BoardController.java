package com.tmate.web;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import com.tmate.service.BoardService;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // 공지 목록
    @GetMapping("/notice")
    public String noticeList(Model model, Criteria cri) {
        List<BoardDTO> boardList = boardService.getList(cri);

        model.addAttribute("noticeList", boardList);    // 공지 항목 보여주기
        return "noticeList";
    }


    // 공지 글 조회
    @GetMapping("/notice/{bd_id}")
    public String notceDetail(@PathVariable String bd_id, Model model) {
        BoardDTO notice = boardService.get(bd_id);

        model.addAttribute("notice", notice);   // 공지 내용
        return "noticeDetail";
    }


    // 공지 글 작성 페이지
    @GetMapping("/noticewrite")
    public String noticeWrite() {
        return "noticeWrite";
    }


    // 공지 글 작성 전송
    @PostMapping("/api/noticewrite")
    public void noticeWrite(BoardDTO board) {
        System.out.println("PostMapping noticeWrite() board : " + board.toString());

        boardService.register(board);
    }

    // 공지 수정 페이지
    @GetMapping("/noticemodify")
    public String noticeModify() {
        return "noticeModify";
    }


    // 공지 수정 전송
    @PutMapping("/api/noticemodify")
    public boolean noticeModify(BoardDTO board) {
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
