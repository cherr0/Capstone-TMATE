package com.tmate.service.android.driver;

import com.tmate.domain.HistoryDTO;
import com.tmate.mapper.HistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppDriverMatchServiceImpl implements AppDriverMatchService {

    private final HistoryMapper historyMapper;

    // 기사 상태 바꾸기 -> 휴식중, 대기중
    // flag = 0 휴식으로 가게 flag = 1 대기중
    @Override
    public Boolean modifyDriver_status(String d_id, int flag) {

        // 휴식중으로 변경
        if(flag == 0)
            return historyMapper.update_statusR(d_id) == 1;
        if(flag == 1)
        return historyMapper.update_statusW(d_id) == 1;

        return false;
    }

    // 기사 2km 이내 콜정보 가져오기

    @Override
    public List<HistoryDTO> getCallList(double m_lttd, double m_lngtd) {

        return historyMapper.selectCallInfo(m_lttd,m_lngtd);
    }
}
