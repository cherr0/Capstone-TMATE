package com.tmate.service.android.user;

import com.tmate.domain.KakaoDTO;

import java.util.Map;

public interface AppPaymentService {
    // 결제 준비
    public Boolean kakaoReady(Map<String, String> map);

    // 결제 준비 후 저장된 값 불러오기
    public KakaoDTO kakaoReadyRes(String m_id);

    // 결제 승인
    public Boolean kakaoApprove(Map<String, String> map);
}
