package com.tmate.service;

import com.tmate.domain.Criteria;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.JoinPointVO;
import com.tmate.domain.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberService {

    // 회원 리스트
    public List<MemberDTO> getListMembers(Criteria cri);

    // 회원 눌렀을때 나오는 상세페이지 - 회원정보
    public MemberDTO getMember(String m_id);

    // 회원 눌렀을때 나오는 상세페이지 - 이용내역
    public List<JoinHistoryVO> getHistoryList(String m_id);

    // 회원 눌렀을때 나오는 상세페이지 - 포인트 내역
    public List<JoinPointVO> getPointList(String m_id);

    // 맵으로 해보기
    public Map<String, Object> getMemberDetail(String m_id);

}
