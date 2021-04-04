package com.tmate.service.android.user;

import com.tmate.domain.KakaoDTO;
import com.tmate.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppPaymentServiceImpl implements AppPaymentService {

    private final PaymentMapper paymentMapper;

    @Override
    public Boolean kakaoReady(Map<String, String> map) {

        log.info("결제 준비 날아오는 정보 " + map);

        KakaoDTO kakao = new KakaoDTO();

        long re_time = new Date(map.get("created_at")).getTime();

        kakao.setTid(map.get("mid"));
        kakao.setM_id(map.get("partner_user_id"));
        kakao.setD_id(map.get("partner_order_id"));
        kakao.setRe_time(new Timestamp(re_time));

        return paymentMapper.kakaoReady(kakao) == 1;
    }

    @Override
    public KakaoDTO kakaoReadyRes(String m_id) {
        return paymentMapper.kakaoReadyRes(m_id);
    }

    @Override
    public Boolean kakaoApprove(Map<String, String> map) {
        return null;
    }
}
