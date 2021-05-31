package com.tmate.service.android.driver;

import com.tmate.domain.BanDTO;
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

    @Transactional
    @Override
    public DispatchDTO modifyDispatchBoarding(String d_id) {

        // 1. 기사가 운행중인 코드 가져온다.
        String dp_id = dispatchMapper.getDrivingDp_id(d_id);

        // 운행 중인 호출정보 상태 -> 탑승중으로 변경
        dispatchMapper.updateDispatchBoarding(dp_id);

        // 목적지를 가져온다.
        return dispatchMapper.getDestination(dp_id);
    }

    @Override
    public Boolean modifyDispatchBoardingEnds(String dp_id) {
        return dispatchMapper.updateDisptchBoardingEnds(dp_id) == 1;
    }

    @Override
    public DispatchDTO getUsingServiceM_id(String d_id) {
        return dispatchMapper.getUseDispatchM_id(d_id);
    }

    @Override
    public Boolean modifyFareDuringPayment(String dp_id, int all_fare, String dp_status) {
        return dispatchMapper.updateFareDuringPayment(dp_id, all_fare, dp_status) == 1;
    }

    @Override
    public Boolean registerBlackList(BanDTO banDTO) {
        return dispatchMapper.insertBlacklist(banDTO) == 1;
    }
}
