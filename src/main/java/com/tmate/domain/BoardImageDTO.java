package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {

    // 파일 시퀀스
    private int inum;

    // 파일 이름
    private String img_name;

    // 고유 uuid
    private String uuid;

    // 경로
    private String path;

    // 글 외래키
    private String bd_id;

    public BoardImageDTO(String img_name, String uuid, String path) {
        this.img_name = img_name;
        this.uuid = uuid;
        this.path = path;
    }



    public String getImageURL() {
        try {
            return URLEncoder.encode(path + "/" + uuid + "_" + img_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(path + "/s_" + uuid + "_" + img_name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return "";
    }
}
