package com.tmate.mapper;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시판 리스트
    List<BoardDTO> getBoardList(Criteria cri);

    // 게시판 삭제
    int delete(String bd_id);

    // 게시판 업데이트
    int update(BoardDTO boardDTO);

    // 게시판 추가
    int insert(BoardDTO boardDTO);

    // 게시판 조회
    BoardDTO read(String bd_id);

    // 공지사항 리스트 총 글 갯수
    int totalCount(Criteria cri);

    // 게시판 조회수 증가
    void viewCount(String bd_id);


}
