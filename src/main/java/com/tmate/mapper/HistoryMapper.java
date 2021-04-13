package com.tmate.mapper;

import com.tmate.domain.HistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMapper {


    List<HistoryDTO> findTogetherList(@Param("slttd") double lttd, @Param("slngtd") double lngtd,
                                      @Param("flttd") double flttd, @Param("flngtd") double flngtd);


    HistoryDTO findMatchingDetail(@Param("merchant_uid") String merchant_uid);


}
