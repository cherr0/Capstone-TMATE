package com.tmate.service;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import com.tmate.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean eModify(BoardDTO boardDTO) {
        return boardMapper.updateE(boardDTO) == 1;
    }

    @Override
    public boolean remove(String bd_id) {
        return boardMapper.delete(bd_id) == 1;
    }

    @Override
    public List<BoardDTO> getList(Criteria cri) {

        return boardMapper.getBoardList(cri);
    }

    @Override
    public int totalCount(Criteria cri) {
        return boardMapper.totalCount(cri);
    }

    @Override
    public void viewCount(String bd_id) {
        boardMapper.viewCount(bd_id);
    }


    @Override
    public List<BoardDTO> getEventList(Criteria cri) {
        return boardMapper.getEventList(cri);
    }

    @Override
    public int totalECount(Criteria cri) {
        return boardMapper.totalEventCount();
    }

    @Override
    public boolean eRegister(BoardDTO boardDTO) {
        return boardMapper.insertEvent(boardDTO) == 1;
    }

    /*
    * 사용자 서비스
    * */

    @Override
    public List<BoardDTO> getUserBoardList(Criteria cri) {
        return boardMapper.getBoardUserList(cri);
    }

    @Override
    public int getUserBoardCount() {
        return boardMapper.totalUserCount();
    }

    @Transactional
    @Override
    public BoardDTO getN(String bd_id) {
        BoardDTO boardDTO = boardMapper.read(bd_id);
        boardMapper.viewCount(bd_id);

        return boardDTO;
    }
}
