package com.tmate.mapper;

import com.tmate.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // 삽입
    void insert(MemberDTO memberDTO);

    // 삭제
    void delete(String m_id);

    // 수정
    void update(MemberDTO memberDTO);

    // 읽기
    void read(String m_id);

}
