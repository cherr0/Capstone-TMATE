package com.tmate.service.android.driver;

import com.tmate.domain.*;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;

import java.util.List;

public interface AppDriverService {

    // 기사용 어플 회원 가입 - 멤버, 드라이버 트랜잭션
    Boolean saveDriverProfile(MemberDTO memberDTO, DriverDTO driverDTO);

    // 운행 기록 확인
    List<DriverHistoryVO> historyList(String d_id);

    // 리뷰 보기 클릭
    List<ReviewDTO> getDriverReviewList(String merchant_uid);

    // 기사 프로필 정보
    DriverProfileVO getDriverProfile(String d_id);

    // 기사 이메일 변경
    Boolean modifyDriverEmail(MemberDTO memberDTO);

    // 계좌 변경 - modify

    // 기사 차량 정보
    List<CarDTO> getDriverCarList(String d_id);

    // 기사 차량 추가
    Boolean insertDriverCar(CarDTO carDTO);

    // 블랙리스트 추가
    Boolean blacklistRegister(BanDTO banDTO);

    // 블랙리스트 삭제
    Boolean blacklistRemove(BanDTO banDTO);

    // 택시 기사 상태 바꾸기 - 대기 중, 운행 중, 휴식 중
    Boolean driverModStatus(DriverDTO driverDTO);

    // 콜대기 팝업 정보 전송

}
