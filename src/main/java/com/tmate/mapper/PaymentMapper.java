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
    int delete(String sid);

    // 카드 조회
    List<PaymentDTO> getPaymentList(String m_id);

    // 카드 상세
    PaymentDTO getPaymentByCustomer(String customer_uid);

    // 카카오페이 결제 준비 res 값 인서트
    int kakaoReady(KakaoDTO kakaoDTO);

    // 카카오페이 결제 준비 값 조회
    KakaoDTO kakaoReadyRes(String m_id);

    // 카카오페이 결제 승인 res 값 인서트
    int kakaoApprove(KakaoDTO kakaoDTO);

    // 카카오페이 카드 등록
    int paymentInsert(PaymentDTO paymentDTO);
}
