package com.tmate.service;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;

import java.util.List;

public interface BoardService {


    public void register(BoardDTO boardDTO);

    public BoardDTO get(String bd_id);

    public boolean modify(BoardDTO boardDTO);

    public boolean remove(String bd_id);

    // 글 목록 리스트
    public List<BoardDTO> getList(Criteria cri);
}
