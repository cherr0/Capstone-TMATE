package com.tmate.service.android.user;

import com.tmate.domain.KakaoDTO;
import com.tmate.domain.PaymentDTO;

import java.util.Map;

public interface AppPaymentService {
    // 결제 준비
    public Boolean kakaoReady(Map<String, String> map);

    // 결제 준비 후 저장된 값 불러오기
    public KakaoDTO kakaoReadyRes(String m_id);

    // 결제 승인
    public Boolean kakaoApprove(KakaoDTO kakaoDTO);

    // 결제 카드 등록
    public Boolean kakaoCardInsert(PaymentDTO paymentDTO);
}
