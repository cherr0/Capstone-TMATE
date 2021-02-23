package com.tmate.service;

import com.tmate.domain.Criteria;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.JoinPointVO;
import com.tmate.domain.MemberDTO;
import com.tmate.mapper.JoinMapper;
import com.tmate.mapper.Membermapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final Membermapper membermapper;

    private final JoinMapper joinMapper;

    @Override
    public List<MemberDTO> getListMembers(Criteria cri) {
        return membermapper.getList(cri);
    }

    // 회원 상세 페이지 회원 정보
    @Override
    public MemberDTO getMember(String m_id) {
        MemberDTO member = membermapper.getMemberByM_id(m_id);
        return member;
    }

    // 회원 상세페이지 이용내역 리스트
    @Override
    public List<JoinHistoryVO> getHistoryList(String m_id) {
        List<JoinHistoryVO> history = joinMapper.getHistoryByMember(m_id);
        return history;
    }

    // 회원 상세페이지 포인트 리스트
    @Override
    public List<JoinPointVO> getPointList(String m_id) {
        List<JoinPointVO> point = joinMapper.getPointByMember(m_id);
        return point;
    }

    // 회원 상세페이지 리스트
    @Transactional
    @Override
    public Map<String, Object> getMemberDetail(String m_id) {
        Map<String, Object> memberInfo = new HashMap<>();
        // 회원
        memberInfo.put("member", (MemberDTO)membermapper.getMemberByM_id(m_id));
        // 이용내역
        memberInfo.put("history", (List<JoinHistoryVO>)joinMapper.getHistoryByMember(m_id));
        // 포인트
        memberInfo.put("point", (List<JoinPointVO>)joinMapper.getPointByMember(m_id));
        return memberInfo;
    }
}
