package com.tmate.mapper;

import com.tmate.domain.PlaceDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlaceMapper {

    List<PlaceDTO> getList();

    int insert(PlaceDTO placeDTO);

    int delete(String pl_id);


}
