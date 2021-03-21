package com.tmate.web;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.BoardImageDTO;
import com.tmate.domain.EventDTO;
import com.tmate.service.BoardService;
import com.tmate.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Log4j2
public class EventApiController {

    @Value("${com.tmate.upload.path}") // application.properties의 변수
    private String uploadPath;

    private final BoardService boardService;


    // 이벤트 글 작성
    @PostMapping(value = "/eventwrite",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public boolean eventWrite(@RequestBody BoardDTO event) {
        System.out.println("PostMapping eventWrite() event : " + event.toString());
        boardService.eRegister(event);
        return true;
    }


    // 이벤트 글 수정
    @PutMapping("/eventmodify")
    public boolean eventModify(@RequestBody BoardDTO event) {
        System.out.println("PutMapping eventModify() event : " + event.toString());
        boardService.eModify(event);
        return true;
    }


    // 이벤트 글 비공개 처리
    @DeleteMapping("/eventremove/{bd_id}")
    public boolean eventRemove(@PathVariable("bd_id") String bd_id) {
        System.out.println("PutMapping eventRemove() event No : " + bd_id);
        return boardService.remove(bd_id);
    }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<BoardImageDTO>> uploadFile(MultipartFile[] uploadFiles) {

        log.info("uploadFiles : " + uploadFiles);

        List<BoardImageDTO> boardImageDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {


            // 이미지 파일만 업로드 가능
            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어가므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);
            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);

                // 섬네일 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;

                // 섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(thumbnailSaveName);

                // 섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);
                boardImageDTOList.add(new BoardImageDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // end for

        return new ResponseEntity<>(boardImageDTOList, HttpStatus.OK);
    }


    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {


        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF8");

            log.info("fileName: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            // MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName, @Nullable String uuid) {

        log.info("uuid: "+uuid);
        String srcFileName = null;

        try {

            if (uuid != null && uuid.length() != 0) {
                boardService.removeImage(uuid);
            }

            srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        log.info("str = " + str);

        String folderPath = str.replace("//", File.separator);

        // make folder

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
