package com.tmate.mapper;

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
}
