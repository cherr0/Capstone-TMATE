package com.tmate.service.android.driver;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.DriverDTO;
import com.tmate.mapper.DispatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppCallServiceImpl implements AppCallService {

    private final DispatchMapper dispatchMapper;

    @Override
    public Boolean modifyDriverStatus(DriverDTO driverDTO) {
        return dispatchMapper.updateDriverStatus(driverDTO) == 1;
    }

    @Override
    public List<DispatchDTO> getCallList(double m_lat, double m_lng) {
        return dispatchMapper.selectCallInfo(m_lat,m_lng);
    }

    @Transactional
    @Override
    public Boolean driverCallAgree(String dp_id, String d_id, double m_lat, double m_lng) {
        // 1. 기사코드를 그 호출 정보에 넣어준다.
        // 2. 이용정보의 상태를 바꿔준다.
        dispatchMapper.updateNormalMatchCall(dp_id, d_id);
        // 3. 기사의 실시간 위치를 넣어준다.
        dispatchMapper.updateDriverLocation(m_lat, m_lng, d_id);
        // 4. 기사의 상태도 운행 중으로 바꿔준다. -> 이것이 Return 값이 될것이다.
        return dispatchMapper.updateDriverDriving(d_id) == 1;
    }

    @Override
    public Boolean modifyRealTimeDriverLocation(double m_lat, double m_lng, String d_id) {
        return dispatchMapper.updateDriverLocation(m_lat, m_lng, d_id) == 1;
    }

    @Override
    public Boolean modifyDispatchBoarding(String dp_id) {
        return dispatchMapper.updateDispatchBoarding(dp_id) == 1;
    }

    @Override
    public Boolean modifyDispatchBoardingEnds(String dp_id) {
        return dispatchMapper.updateDisptchBoardingEnds(dp_id) == 1;
    }
}
