package com.tmate.service.android.driver;

import com.tmate.domain.HistoryDTO;

import java.util.List;

public interface AppDriverMatchService {

    // 기사 상태 값 바꾸기 -> 휴식중, 대기중
    Boolean modifyDriver_status(String d_id, int flag);

    // 기사 2km 이내 콜 정보 리스트 가져오기
    List<HistoryDTO> getCallList(double m_lttd, double m_lngtd);
}
