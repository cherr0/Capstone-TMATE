package com.tmate.mapper;

import com.tmate.domain.*;
import com.tmate.domain.user.UserYearsCapacityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMainMapper {


    public List<JoinHistoryVO> getMainWeeklyHistoryList(String m_id);

    // joinMapper에서 사용
    public List<JoinPointVO> getMainWeeklyPointList(String m_id);

    // usermainmapper에서 사용
    public List<UserYearsCapacityVO> getUsersYearsCount(String m_id);

    // 좋아요 카운트
    public int getCountLike(String m_id);

    // 싫어요 카운트
    public int getCountDisLike(String m_id);

    // 블랙리스트 횟수
    public int getCountBan(String m_id);

    // 카드 리스트
    public List<PaymentDTO> getPaymentByM_id(String m_id);

    // 알림 전송 리스트
    public List<NotificationDTO> getNotifiByM_id(String m_id);

    // 알림 전송 삭제
    public int deleteNotifi(String n_phone);

    // 결제 이력 리스트
    public List<ReceiptDTO> getReceiptByM_id(String m_id);

    // 결제 이력 총 카운트
    public int getCountReceipt(String m_id);

    // 결제 이력 페이징 처리 리스트
    public List<JoinReceiptVO> getReceiptListPaging(@Param("cri") Criteria cri, String m_id);
}
