package com.tmate.web;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import com.tmate.domain.PageDTO;

import com.tmate.service.BoardService;
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

        int total = boardService.totalCount(cri);

        model.addAttribute("noticeList", boardList);    // 공지 항목 보여주기
        model.addAttribute("pageMaker", new PageDTO(cri, total));
        return "admin/noticeList";
    }


    // 공지 글 조회
    @GetMapping("/notice/{bd_id}")
    public String notceDetail(@PathVariable String bd_id, Model model) {
        BoardDTO notice = boardService.get(bd_id);
        System.out.println("공지 게시글 열람 bd_id : " + bd_id);

        boardService.viewCount(bd_id);

        model.addAttribute("notice", notice);   // 공지 내용
        return "admin/noticeDetail";
    }


    // 공지 글 작성 페이지
    @GetMapping("/noticewrite")
    public String noticeWrite() {
        return "/admin/noticeWrite";
    }


    // 공지 수정 페이지
    @GetMapping("/noticemodify/{bd_id}")
    public String noticeModify(Model model,@PathVariable String bd_id) {
        BoardDTO board = boardService.get(bd_id);

        model.addAttribute("board", board);
        return "/admin/noticeModify";
    }


}
