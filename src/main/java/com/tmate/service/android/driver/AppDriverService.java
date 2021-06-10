package com.tmate.service.android.driver;

import com.tmate.domain.*;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.domain.driver.SidebarProfileVO;

import java.util.List;
import java.util.Map;

public interface AppDriverService {

    // 기사용 어플 회원 가입 - 멤버, 드라이버 트랜잭션
    Boolean saveDriverProfile(Map<String, String> map);

    // 기사 승인 확인
    Boolean searchApprove(String d_id);

    // 운행 기록 확인
    List<DriverHistoryVO> historyList(String d_id);

    // 리뷰 보기 클릭
    List<ReviewDTO> getDriverReviewList(String merchant_uid);

    // 기사 사이드바 프로필
    SidebarProfileVO getSidebarProfileById(String d_id);

   // 기사 프로필 정보
    DriverProfileVO getDriverProfile(String d_id);

    // 기사 이메일 변경
    Boolean modifyDriverEmail(MemberDTO memberDTO);

    // 계좌 변경 - modify

    // 기사 차량 정보
    List<CarDTO> getDriverCarList(String d_id);

    // 기사 차량 추가
    Boolean insertDriverCar(CarDTO carDTO);

    // 기사 차량 선택
    Boolean selectDriverCar(String d_id, String car_no);

    // 블랙리스트 추가
    Boolean blacklistRegister(BanDTO banDTO);

    // 블랙리스트 삭제
    Boolean blacklistRemove(String d_id, String m_id);

    // 택시 기사 상태 바꾸기 - 대기 중, 운행 중, 휴식 중
    Boolean driverModStatus(String d_id, int d_status);

    // 콜대기 팝업 정보 전송

}
