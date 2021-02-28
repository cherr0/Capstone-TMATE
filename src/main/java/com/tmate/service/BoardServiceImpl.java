package com.tmate.service;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import com.tmate.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    @Override
    public boolean register(BoardDTO boardDTO) {
        return boardMapper.insert(boardDTO) == 1;
    }

    @Override
    public BoardDTO get(String bd_id) {
        return boardMapper.read(bd_id);
    }

    @Override
    public boolean modify(BoardDTO boardDTO) {

        return boardMapper.update(boardDTO) == 1;
    }

    @Override
    public boolean remove(String bd_id) {
        return boardMapper.delete(bd_id) == 1;
    }

    @Override
    public List<BoardDTO> getList(Criteria cri) {

        return boardMapper.getBoardList(cri);
    }
}
