package com.tmate.service.android.user;

import com.tmate.domain.KakaoDTO;
import com.tmate.domain.PaymentDTO;
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

    @Override   // 결제 준비 시 데이터 등록
    public Boolean kakaoReady(Map<String, String> map) {

        log.info("결제 준비 날아오는 정보 : " + map);

        KakaoDTO kakao = new KakaoDTO();

        long re_time = Long.parseLong(map.get("created_at"));



        kakao.setTid(map.get("tid"));
        kakao.setM_id(map.get("partner_user_id"));
        kakao.setD_id(map.get("partner_order_id"));
        kakao.setRe_time(new Timestamp(re_time));

        log.info("보내는 값 " + kakao);

        return paymentMapper.kakaoReady(kakao) == 1;
    }

    @Override   // 결제 승인 시 필요한 값 불러오기
    public KakaoDTO kakaoReadyRes(String m_id) {
        return paymentMapper.kakaoReadyRes(m_id);
    }

    @Override   // 결제 승인
    public Boolean kakaoApprove(KakaoDTO kakaoDTO) {

        log.info("결제 승인 날아오는 정보 : " + kakaoDTO);

        return paymentMapper.kakaoApprove(kakaoDTO) == 1;
    }

    @Override   // 간편결제 카드 등록
    public Boolean kakaoCardInsert(PaymentDTO paymentDTO) {
        log.info("카드 등록 정보 : " + paymentDTO);
        return paymentMapper.paymentInsert(paymentDTO) == 1;
    }
}
