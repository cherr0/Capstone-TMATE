package com.tmate.mapper;

import com.tmate.domain.BoardImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardImageMapper {

    public void insert(BoardImageDTO boardImageDTO);

    public void delete(String uuid);

    public List<BoardImageDTO> findByBd_id(String bd_id);

    public List<BoardImageDTO> findBoardImageList();
}
