package com.leafresh.backend.ftp.controller;

import com.leafresh.backend.ftp.service.FtpImgLoaderUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/ftp")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")  // application.yml에서 가져온 값 사용

public class FTPController {

    private final FtpImgLoaderUtil2 ftpFileUploadService;

    @Autowired
    public FTPController(FtpImgLoaderUtil2 ftpFileUploadService) {
        this.ftpFileUploadService = ftpFileUploadService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String localFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
        File localFile = new File(localFilePath);

        try {
            // 로컬에 파일 저장
            file.transferTo(localFile);

            // FTP 서버에 파일 업로드
            String result = ftpFileUploadService.uploadFile(localFile, file.getOriginalFilename());
            return "파일 업로드 성공: " + result;

        } catch (IOException e) {
            e.printStackTrace();
            return "파일 업로드 실패: " + e.getMessage();
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
            Resource resource = ftpFileUploadService.download(path);  // FTP 서버에서 이미지 다운로드
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 이미지 타입 지정 (필요시 다른 타입으로 변경)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
