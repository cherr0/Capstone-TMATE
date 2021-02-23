package com.tmate.service;


import com.tmate.domain.PlaceDTO;

import java.util.List;

public interface PlaceService {

    public List<PlaceDTO>  getHotPlaceList();

    public boolean remove(String pl_id);
}
