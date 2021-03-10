package com.tmate.service;

import com.tmate.domain.*;
import com.tmate.mapper.JoinMapper;
import com.tmate.mapper.Membermapper;
import com.tmate.mapper.UserMainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMainMapper userMainMapper;
    private final Membermapper membermapper;
    private final JoinMapper joinMapper;

    @Override
    public Map<String, Object> getMainPage(String m_id) {

        Map<String, Object> mainMap = new HashMap<>();

        mainMap.put("member", membermapper.getMemberByM_id(m_id));
        mainMap.put("weeklyHistory", userMainMapper.getMainWeeklyHistoryList(m_id));
        mainMap.put("weeklyPoint", userMainMapper.getMainWeeklyPointList(m_id));
        mainMap.put("countBan",userMainMapper.getCountBan(m_id));
        mainMap.put("countGood", userMainMapper.getCountLike(m_id));
        mainMap.put("countBad", userMainMapper.getCountDisLike(m_id));

        return mainMap;
    }

    @Override
    public List<JoinHistoryVO> getMyHistoryList(Criteria cri, String m_id) {
        return null;
    }

    @Override
    public List<JoinPointVO> getMyPointList(Criteria cri, String m_id) {
        return null;
    }

    @Override
    public List<JoinReceiptVO> getMyReceiptList(Criteria cri, String m_id) {
        return null;
    }

    @Override
    public MemberDTO getMember(String m_id) {
        return null;
    }
}
