package com.tmate.mapper;

import com.tmate.domain.BookmarkDTO;
import com.tmate.domain.PlaceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceMapper {


    /*
    *  핫플레이스 Mapper
    * */
    List<PlaceDTO> getList();

    int insert(PlaceDTO placeDTO);

    int delete(String pl_id);


    /*
    *
    * 즐겨 찾기 Mapper
    * */

    // 리스트 가져오기
    List<BookmarkDTO> findBookmarkList(String m_id);

    // 즐겨 찾기 지우기
    int deleteBookmark(@Param("bm_name") String bm_name, @Param("m_id") String m_id);

    // 즐겨찾기 등록
    int insertBookmark(BookmarkDTO bookmarkDTO);

}
