package com.tmate.service;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;

import java.util.List;

public interface BoardService {


    public boolean register(BoardDTO boardDTO);

    public BoardDTO get(String bd_id);

    public boolean modify(BoardDTO boardDTO);

    public boolean remove(String bd_id);

    // 이벤트 글 삽입
    public boolean eRegister(BoardDTO boardDTO);

    // 이벤트 글 수정
    public boolean eModify(BoardDTO boardDTO);

    // 관리자 -  글 목록 리스트
    public List<BoardDTO> getList(Criteria cri);

    // 관리자 - 이벤트 리스트
    public List<BoardDTO> getEventList(Criteria cri);

    // 페이지 처리용 totalCount
    public int totalCount(Criteria cri);

    // 페이지 처리용 - 관리자, 사용자 이벤트
    public int totalECount(Criteria cri);

    public void viewCount(String bd_id);


    /*
     *  사용자 페이지
     * 공지사항은 - 공개 된것만 보여준다.
     * */
    public List<BoardDTO> getUserBoardList(Criteria cri);

    // 개수
    public int getUserBoardCount();

    public BoardDTO getN(String bd_id);
}
