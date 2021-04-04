package com.tmate.mapper;

import com.tmate.domain.KakaoDTO;
import com.tmate.domain.PaymentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {


    // 카드 추가
    int insert(PaymentDTO paymentDTO);

    // 카드 삭제
    int delete(String customer_uid);

    // 카드 조회
    List<PaymentDTO> getPaymentList(String m_id);

    // 카드 상세
    PaymentDTO getPaymentByCustomer(String customer_uid);

    // 대표카드 지정
    int updateRep(String customer_uid);

    // 대표 카드 비활성화 -> 활성화 된거 찾고 비활성화 시킨다.
    int updateDRep(String custoemr_uid);

    // 대표 카드 -> 찾기->
    String findPayment(String m_id);

    // 카카오페이 결제 준비 res 값 인서트
    int kakaoReady(KakaoDTO kakaoDTO);
}
