package com.tmate.service.android.user;

import com.tmate.domain.KakaoDTO;

import java.util.Map;

public interface AppPaymentService {
    // 결제 준비
    public Boolean kakaoReady(Map<String, String> map);
}
