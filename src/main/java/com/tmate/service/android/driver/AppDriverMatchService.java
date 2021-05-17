package com.tmate.service.android.driver;

import com.tmate.domain.HistoryDTO;

import java.util.List;

public interface AppDriverMatchService {

    // 기사 상태 값 바꾸기 -> 휴식중, 대기중
    Boolean modifyDriver_status(String d_id, int flag);

    // 기사 2km 이내 콜 정보 리스트 가져오기
    List<HistoryDTO> getCallList(double m_lttd, double m_lngtd);

    // 기사가 콜을 수락 할시
    /*
     *  @Transactional
     *  1. 기사코드를 그 호출 정보에 넣어준다.
     *  2. 이용정보의 상태를 바꿔준다. -> 3
     *  3. 기사의 실시간 위치를 넣어준다.
     *  4. 기사의 상태도 운행 중으로 바꿔준다.
     * */
    Boolean driverCallAgree(String merchant_uid, String d_id, double m_lttd, double m_lngtd);
}
