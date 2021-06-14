package com.tmate.service;

import com.tmate.domain.PlaceDTO;
import com.tmate.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlaceServiceImpl implements PlaceService{

    private final PlaceMapper placeMapper;

    @Override
    public List<PlaceDTO> getHotPlaceList() {
        return placeMapper.getList();
    }

    @Override
    public boolean remove(String pl_id) {
        return placeMapper.delete(pl_id) == 1;
    }

    @Override
    public boolean register(PlaceDTO placeDTO) {

        int i = placeMapper.insert(placeDTO);
        return i == 1;
    }

    @Override
    public boolean updatePlaceCnt(String pl_id) {
        boolean finish = placeMapper.updateFinish(pl_id) == 1;
        log.info("PlaceServiceImpl 도착지 값 추가 : " + finish);
        return finish;
    }
}
