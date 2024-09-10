package com.leafresh.backend.ftp.controller;

import com.leafresh.backend.ftp.service.FtpImgLoaderUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ftp")
public class FTPController {

    private final FtpImgLoaderUtil2 ftpFileUploadService;

    @Autowired
    public FTPController(FtpImgLoaderUtil2 ftpFileUploadService) {
        this.ftpFileUploadService = ftpFileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String localFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
        File localFile = new File(localFilePath);

        try {
            // 로컬에 파일 저장
            file.transferTo(localFile);

            // FTP 서버에 파일 업로드
            String uploadedFilePath = ftpFileUploadService.uploadFile(localFile, file.getOriginalFilename());

            // JSON 형식으로 응답 반환
            return ResponseEntity.ok().body(Map.of("uploadedFilePath", uploadedFilePath));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "파일 업로드 실패", "message", e.getMessage()));
        } finally {
            // 로컬에 저장된 파일 삭제
            if (localFile.exists()) {
                localFile.delete();
            }
        }
    }


    @GetMapping("/image")
    public ResponseEntity<Resource> getImage(@RequestParam("path") String path) {
        try {
            Resource resource = ftpFileUploadService.download(path);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String path) {
        try {
            boolean isDeleted = ftpFileUploadService.delete(path);
            if (isDeleted) {
                return ResponseEntity.ok("파일 삭제 성공");
            } else {
                return ResponseEntity.status(404).body("파일 삭제 실패: 파일을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 삭제 중 오류 발생: " + e.getMessage());
        }
    }

}
