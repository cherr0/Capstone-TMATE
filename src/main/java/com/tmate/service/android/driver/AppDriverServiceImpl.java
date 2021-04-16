package com.tmate.service.android.driver;

import com.tmate.domain.*;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.mapper.DriverMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class AppDriverServiceImpl implements AppDriverService{

    private final DriverMapper driverMapper;

    @Transactional
    @Override   // 기사용 어플 회원가입 - 회원, 기사 테이블
    public Boolean saveDriverProfile(MemberDTO memberDTO, DriverDTO driverDTO) {
        log.info("AppDriverService 기사 회원가입 멤버 데이터 : " + memberDTO);
        driverMapper.registerDriver(memberDTO);

        log.info("AppDriverSErvice 기사 회원가입 기사 데이터 : " + driverDTO);
        return driverMapper.addDriverLicense(driverDTO) == 1;
    }

    @Override   // 기사 운행이력
    public List<DriverHistoryVO> historyList(String d_id) {
        log.info("AppDriverService 기사 운행이력 d_id : " + d_id);
        return driverMapper.getDriverHistoryList(d_id);
    }

    @Override   // 운행이력 클릭 시 나오는 리뷰
    public List<ReviewDTO> getDriverReviewList(String merchant_uid) {
        log.info("AppDriverService 기사 이력 리뷰 매칭 코드 : " + merchant_uid);
        return driverMapper.getDriverReviewList(merchant_uid);
    }

    @Override   // 기사 프로필
    public DriverProfileVO getDriverProfile(String d_id) {
        log.info("AppDriverService 기사 프로필 가져오기 d_id : " + d_id);
        return driverMapper.getDriverDetail(d_id);
    }

    @Override   // 기사 이메일 수정
    public Boolean modifyDriverEmail(MemberDTO memberDTO) {
        log.info("AppDriverService 기사 이메일 수정 memberDTO :" + memberDTO);
        return driverMapper.emailModify(memberDTO) == 1;
    }

    @Override   // 기사 차량 정보
    public List<CarDTO> getDriverCarList(String d_id) {
        log.info("AppDriverService 기사 차량 정보 d_id : " + d_id);
        return driverMapper.getCarList(d_id);
    }

    @Override   // 기사 차량 추가
    public Boolean insertDriverCar(CarDTO carDTO) {
        log.info("AppDriverService 기사 차량 추가 carDTO : " + carDTO);
        return driverMapper.insertCar(carDTO) == 1;
    }

    @Override   // 블랙리스트 추가
    public Boolean blacklistRegister(BanDTO banDTO) {
        log.info("AppDriverService 블랙리스트 추가 : " + banDTO);
        return driverMapper.insertBlacklist(banDTO) == 1;
    }

    @Override   // 블랙리스트 제거
    public Boolean blacklistRemove(BanDTO banDTO) {
        log.info("AppDriverService 블랙리스트 제거 : " + banDTO);
        return driverMapper.deleteBlacklist(banDTO) == 1;
    }

    @Override   // 기사 상태 변경
    public Boolean driverModStatus(DriverDTO driverDTO) {
        log.info("AppDriverService 기사 상태 변경 : " + driverDTO);
        return driverMapper.setDriverStatus(driverDTO) == 1;
    }


}
