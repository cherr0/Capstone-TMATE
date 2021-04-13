package com.tmate.service.android.driver;

import com.tmate.domain.BanDTO;
import com.tmate.domain.DriverDTO;

import java.util.List;

public interface AppDriverService {

    // 기사용 어플 회원 가입 - 멤버, 드라이버 트랜잭션
    Boolean getDriverProfile(String m_id);

    // 기사 정보 수정

    // 기사 탈퇴
    int disableDriver(String m_id);

    // 차량 선택
    // int modifyDriverCar(); 매개변수 값 정해지면 활성화


    // 계좌 변경 - modify


    // 운행 기록

    // 콜대기 팝업 정보 전송



    // 택시 기사 상태 바꾸기 - 대기 중, 운행 중, 휴식 중
    int DriverModStatus(String d_id, String d_status);

    // 블랙리스트 추가 -> modify
    int blacklistRegister(BanDTO banDTO);

    // 블랙리스트 삭제 -> remove
    int blacklistRemove(String d_id, String m_id);

    // 블랙리스트 보기 -> select
    List<BanDTO> getBlacklist(String d_id);

    // 평점 이력 보기 -> select

}
