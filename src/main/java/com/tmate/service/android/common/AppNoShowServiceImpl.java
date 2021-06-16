package com.tmate.service.android.common;

import com.tmate.mapper.NoShowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppNoShowServiceImpl implements AppNoShowService {

    private final NoShowMapper noShowMapper;

    @Transactional
    @Override
    public Boolean MasterClickNoShowBtn(String m_id, String dp_id) {

        noShowMapper.updateAttendStatus(m_id, dp_id);

        noShowMapper.updateCurPeople(dp_id);

        return noShowMapper.updateUserCnt(m_id) == 1;
    }

    @Transactional
    @Override
    public Boolean DriverClickNoShowBtn(String m_id, String dp_id) {

        noShowMapper.updateAttendStatus(m_id, dp_id);

        noShowMapper.updateUserCnt(m_id);

        return noShowMapper.updateNoshowDpStatus(dp_id) == 1;
    }

    @Override
    public String getMemberStatus(String m_id) {
        return noShowMapper.selectMemberStatus(m_id);
    }

    @Override
    public Boolean modifyMemberNormalStatus(String m_id) {
        return noShowMapper.updateUserNormalStatus(m_id) == 1;
    }
}
