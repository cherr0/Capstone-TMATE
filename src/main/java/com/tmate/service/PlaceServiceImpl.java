package com.tmate.service;

import com.tmate.domain.PlaceDTO;
import com.tmate.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{

    private PlaceMapper placeMapper;

    @Override
    public List<PlaceDTO> getHotPlaceList() {
        return placeMapper.getList();
    }

    @Override
    public boolean remove(String pl_id) {
        return placeMapper.delete(pl_id) == 1;
    }
}
