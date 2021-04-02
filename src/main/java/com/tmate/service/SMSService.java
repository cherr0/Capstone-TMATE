package com.tmate.service;

import com.tmate.domain.MemberDTO;
import com.tmate.domain.PhoneDTO;
import com.tmate.mapper.Membermapper;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final Membermapper membermapper;
    
    private static final String api_key = "NCSILQQGTJKYU1AL";  // coolsms api key
    private static final String api_searet = "1DODZAILXFPRRRDWVAEU8GPSEBUMLBM7";  // coolsms api secret key

//    public final String confirmNumber = createKey();
    public String confirmNumber;
    public static final int CONFIRM = 1;    // 인증 확인
    public static final int REJECT = 0;     // 인증 실패

    public boolean certifiedPhoneNumber(PhoneDTO phone) {

        Message coolsms = new Message(api_key, api_searet);

        confirmNumber = createKey();
        phone.setConfirm(confirmNumber);

        System.out.println("수신자 번호 : " + phone.getPhoneNumber());
        System.out.println("인증 번호 : " + phone.getConfirm());

        HashMap<String, String> params = new HashMap<>();
        params.put("to", phone.getPhoneNumber());    // 수신 전화번호
        params.put("from", "01067501664");  // 발신 전화번호
        params.put("type", "SMS");  // 메시지 보내는 타입
        params.put("text", "휴대폰 인증 메시지 : 인증번호는 [" + phone.getConfirm() + "] 입니다."); // 메시지 내용
        params.put("app_version", "test app 1.2");  // 어플리케이션 이름 및 버전

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            return true;
        }catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
            return false;
        }


    }

    public MemberDTO getPermission(PhoneDTO phone) {
        return membermapper.searchPermission(phone);
    }

    //		인증코드 만들기 -> 휴대폰 인증 번호 및 초대 코드용
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(2) + 1; // 1~2 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        System.out.println("생성된 인증번호 : " + key.toString());
        return key.toString();
    }
}
