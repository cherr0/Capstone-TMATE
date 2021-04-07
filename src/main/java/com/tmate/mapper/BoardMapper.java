package com.tmate.mapper;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시판 리스트 -- 관리자에게 보이는 공지사항
    List<BoardDTO> getBoardList(Criteria cri);

    // 공지사항 리스트 - 사용자에게 보여지는 것
    List<BoardDTO> getBoardUserList(Criteria cri);

    // 이벤트 리스트
    List<BoardDTO> getEventList(Criteria cri);

    // 게시판 삭제
    int delete(String bd_id);

    // 게시판 업데이트 -> 상태도 같이 하고 내용도 같이 바뀐다.
    int update(BoardDTO boardDTO);

    // 이벤트 업데이트
    int updateE(BoardDTO boardDTO);

    // 게시판 추가
    int insert(BoardDTO boardDTO);

    // 게시판 조회
    BoardDTO read(String bd_id);

    // 게시판 조회
    BoardDTO readE(String bd_id);

    // 공지사항 리스트 총 글 갯수
    int totalCount(Criteria cri);

    // 공지사항 공개 리스트 글 갯수
    int totalUserCount();

    // 이벤트 글 개수
    int totalEventCount();

    // 게시판 조회수 증가 --> 사용자에게만 넣자
    void viewCount(String bd_id);

    // 이벤트 글 삽입
    int insertEvent(BoardDTO boardDTO);


    /* ------------------------
            App 관련 mapper
       ------------------------ */
    List<BoardDTO> getNoticeList();
}
