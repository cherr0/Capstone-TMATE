package com.tmate.mapper;

import com.tmate.domain.Criteria;
import com.tmate.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Membermapper {

    // 관리자 페이지 회원리스트
    List<MemberDTO> getList(Criteria cri);

    // 회원 상세 정보
    MemberDTO getMemberByM_id(String m_id);

    // 회원 가입
    void insertMember(MemberDTO memberDTO);

    // 회원 수정
    void updateMember(MemberDTO memberDTO);

    // 회원 삭제 -> 회원 및 기사 관련
    int deleteMember(String m_id);

    // 승인 시 승인 날짜 변경
    int updateDate(String d_id);

    // 전체 유저 수
    int countMembers();

    // 전체 기사 수
    int countDrivers();

    // 멤버 수 토탈 카운트
    int getTotalCount(Criteria cri);
}
