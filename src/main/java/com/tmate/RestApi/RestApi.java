package com.tmate.RestApi;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Log4j2
@RestController
public class RestApi {

    @PostMapping("/kakao/approveSub")
    public String approveSub(@RequestParam HashMap<String, String> map) {
        log.info("data :: {}", map);

        String result = "";


        try {

            // URL 설정하고 접속하기
            URL url = new URL("https://kapi.kakao.com/v1/payment/approve"); // URL 설정
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            /* ------------------------------
                    전송 모드 설정 - 기본 설정
               ------------------------------ */
            con.setDoInput(true);           // 서버에서 읽기 모드 지정
            con.setDoOutput(true);          // POST 데이터를 OutputStream으로 넘겨주는 설정
            con.setRequestMethod("POST");   // 전송 방식 POST 설정
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);



            /*  ------------------------------
                           헤더 세팅
                ------------------------------ */
            con.setRequestProperty("content-Type","application/x-www-form-urlencoded");
            con.setRequestProperty("authorization","KakaoAK e24eec29f82748733f7a2be2de93c236");


            /* -------------------------------
                         서버 값 전송
               ------------------------------- */
            StringBuffer buffer = new StringBuffer();

            // HashMap으로 전달 받은 파라미터가 null이 아닌 경우 버퍼에 넣음
            if(map != null) {
                Set key = map.keySet();

                for(Iterator iterator = key.iterator(); iterator.hasNext();) {
                    String keyName = (String) iterator.next();
                    String valueName = map.get(keyName);
                    buffer.append(keyName).append("=").append(valueName + "&");

                    System.out.println(valueName);
                }
            }
            System.out.println(buffer.toString());

            // 서버 전송
            OutputStreamWriter outStream = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            BufferedWriter writer = new BufferedWriter(outStream);

            writer.write(buffer.toString());
            writer.flush();


            /* -------------------------------
                       서버에서 전송 받기
               ------------------------------- */
            // 상태 200 전송되면 서버에서 전송 받은 값을 읽음
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Stream을 처리해줌
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
                result = sb.toString();
                return result;

            }else {
                // 정상 처리가 아닌 경우 에러 확인
                System.out.println(con.getResponseCode());
                System.out.println(new String(con.getErrorStream().readAllBytes()));
            }

        }catch(Exception e) {
            System.out.println(e.toString());
        }

        return result;
    }

}
