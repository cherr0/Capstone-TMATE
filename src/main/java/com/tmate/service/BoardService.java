package com.tmate.service;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;

import java.util.List;

public interface BoardService {


    public boolean register(BoardDTO boardDTO);

    public BoardDTO get(String bd_id);

    public boolean modify(BoardDTO boardDTO);

    public boolean remove(String bd_id);

    // 글 목록 리스트
    public List<BoardDTO> getList(Criteria cri);

    // 페이지 처리용 totalCount
    public int totalCount(Criteria cri);

    public void viewCount(String bd_id);
}
