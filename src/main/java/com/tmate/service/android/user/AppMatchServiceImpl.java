package com.tmate.service.android.user;

import com.tmate.domain.AttendDTO;
import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.mapper.DispatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppMatchServiceImpl implements AppMatchService {

    private final DispatchMapper dispatchMapper;

    /*
     *  -------------
     *     일반호출
     *  -------------
     * */

    @Transactional
    @Override
    public String registerNormalMatch(DispatchDTO dispatchDTO) {
        String dp_id = "";

        if(dispatchMapper.insertNormalMatch(dispatchDTO) == 1)
            dp_id = dispatchDTO.getDp_id();

        // 참여 테이블
        dispatchMapper.insertNormalAttend(dispatchDTO);

        return dp_id;
    }

    @Override
    public String getD_idDuringCall(String dp_id) {
        return dispatchMapper.selectDriver(dp_id);
    }

    @Override
    public Boolean removeNowCall(String dp_id) {

        int flag = 0;

        if(dispatchMapper.deleteNormalAttend(dp_id) == 1){
            flag = dispatchMapper.deleteNormalMatch(dp_id);
        }

        return flag == 1;
    }

    @Override
    public DispatchDTO getCurrentCallInfo(String m_id) {
        DispatchDTO dispatchInfo = dispatchMapper.getCurrentDispatchInfo(m_id);
        if(dispatchInfo != null)
            return dispatchInfo;
        else
            return dispatchMapper.getCurrentPassengerDispatchInfo(m_id);

    }

    @Override
    public DispatchDTO getDetailCurrentCallInfo(String dp_id) {
        return dispatchMapper.getDetailCurrentDispatchInfo(dp_id);
    }

    @Override
    public DispatchDTO getCurrentDriverLocation(String dp_id) {
        return dispatchMapper.getRealTimeDriverPosition(dp_id);
    }

    /*
    *  -------------
    *     동승호출
    *  -------------
    * */

    @Override
    public List<DispatchDTO> getNearMatchList(double s_lat, double s_lng, double f_lat, double f_lng) {
        return dispatchMapper.selectNearMatchList(s_lat, s_lng, f_lat, f_lng);
    }

    @Transactional
    @Override
    public String registerMatch(DispatchDTO dispatchDTO, AttendDTO attendDTO) {
        String dp_id = "";
        if (dispatchMapper.insertTogetherDispatch(dispatchDTO) == 1) {
            attendDTO.setDp_id(dispatchDTO.getDp_id());
            attendDTO.setM_id(dispatchDTO.getM_id());
            dispatchMapper.insertTogehterAttend(attendDTO);
            dp_id = dispatchDTO.getDp_id();
        }


        return dp_id;
    }

    @Transactional
    @Override
    public Boolean removeMatch(String dp_id) {
        int flag = 0;
        if(dispatchMapper.deleteTogetherAttend(dp_id) == 1) {
            flag = dispatchMapper.deleteTogetherDispatch(dp_id);
        }

        return flag == 1;
    }

    @Override
    public Boolean registerApplyButton(AttendDTO attendDTO) {
        return dispatchMapper.insertAttendApply(attendDTO) == 1;
    }

    @Override
    public Boolean modifyAggreeMatching(String dp_id, String m_id) {
        return dispatchMapper.agreeApply(dp_id, m_id) == 1;
    }

    @Override
    public Boolean modifyRejectMatching(String dp_id, String m_id) {
        return dispatchMapper.rejectApply(dp_id, m_id) == 1;
    }

    @Override
    public List<AttendDTO> getApplyerList(String dp_id) {
        return dispatchMapper.getApplyTogetherList(dp_id);
    }

    @Override
    public List<AttendDTO> getPassengerList(String dp_id) {
        return dispatchMapper.getPassengerList(dp_id);
    }

    @Override
    public List<AttendDTO> alreadyChoiceSeatNO(String dp_id) {
        return dispatchMapper.getJoinSeat(dp_id);
    }
}
