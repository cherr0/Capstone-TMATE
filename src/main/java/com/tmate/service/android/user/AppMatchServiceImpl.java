package com.tmate.service.android.user;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.mapper.DispatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppMatchServiceImpl implements AppMatchService {

    private final DispatchMapper dispatchMapper;

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
        return dispatchMapper.getCurrentDispatchInfo(m_id);
    }

    @Override
    public DispatchDTO getDetailCurrentCallInfo(String dp_id) {
        return dispatchMapper.getDetailCurrentDispatchInfo(dp_id);
    }

    @Override
    public DispatchDTO getCurrentDriverLocation(String dp_id) {
        return dispatchMapper.getRealTimeDriverPosition(dp_id);
    }
}
