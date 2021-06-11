package com.tmate.mapper;

import com.tmate.domain.MemberDTO;
import com.tmate.domain.NotificationDTO;
import com.tmate.domain.user.ApprovalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {

    // 검색
    List<MemberDTO> findByPhone(String phone);

    // 나의 지인
    List<NotificationDTO> findListMyFriends(String m_id);

    // 알림 활성화 여부
    void updateFlag(NotificationDTO notificationDTO);

    // 지인 번호 삭제
    int deleteFriendPhoneNo(@Param("m_id") String m_id, @Param("n_name") String n_name);

    // 승인요청 클릭시
    void insertMyApproval(ApprovalDTO approvalDTO);

    // 나에게 승인요청한 리스트
    List<ApprovalDTO> findListMyApproval(String m_id);

    // 승인 시
    int insertNotifi(NotificationDTO notificationDTO);

    // 거절 시
    void deleteApproval(@Param("id") String id,@Param("m_id") String m_id);

    // 승인 요청 개수
    int countApproval(String m_id);

    // 승인 대기중인 리스트
    List<ApprovalDTO> findWaitingList(String m_id);


}
