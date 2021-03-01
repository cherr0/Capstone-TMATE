package com.tmate.service;

import com.tmate.domain.*;
import com.tmate.mapper.JoinMapper;
import com.tmate.mapper.Membermapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<JoinHistoryVO> getHistoryList(Criteria cri, String m_id) {
        List<JoinHistoryVO> history = joinMapper.getHistoryByMember(cri,m_id);
        return history;
    }

    // 회원 페이지 처리 가능한 이용내역
    @Override
    public HistoryPageDTO getListPage(Criteria cri, String m_id) {
        return new HistoryPageDTO(joinMapper.getTotalHistoryCount(m_id),
                joinMapper.getHistoryByMember(cri,m_id));
    }

    // 회원 상세페이지 포인트 리스트
    @Override
    public List<JoinPointVO> getPointList(Criteria cri,String m_id) {
        List<JoinPointVO> point = joinMapper.getPointByMember(cri, m_id);
        return point;
    }

    // 회원 페이지 처리 가능한 포인트 내역


    @Override
    public PointPageDTO getPointListPage(Criteria cri, String m_id) {
        return new PointPageDTO(joinMapper.getTotalPointCount(m_id),
                joinMapper.getPointByMember(cri,m_id));
    }

    // 페이지 네이션 처리를 위한 total 카운트 구하는 로직
    @Override
    public int getTotalCount(Criteria cri) {
        return membermapper.getTotalCount(cri);
    }

    // 페이지 네이션 처리를 위한 totalCount 구하는 로직
    @Override
    public int getTotalHistoryCount(String m_id) {
        return joinMapper.getTotalHistoryCount(m_id);
    }

    @Override
    public int getTotalPointCount(String m_id) {
        return joinMapper.getTotalPointCount(m_id);
    }
}
