package com.beibei.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Users;
import com.beibei.service.UsersService;
import com.beibei.utils.Const;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Resource
    private UsersService userService;

    @PostMapping("/upload-avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
            @RequestAttribute(Const.ATTR_USER_ID) int userId) {
        if (file.isEmpty()) {
            return RestBean.error(new IOException("File is empty"));
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            if (!mkdir)
                return RestBean.error(new IOException("Failed to create directory"));
        }

        String originalFilename = file.getOriginalFilename();
        String extension = null;
        if (originalFilename != null) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String fileName = UUID.randomUUID() + extension;
        File dest = new File(dir, fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error(e.getMessage());
            return RestBean.error(new IOException("File upload failed"));
        }

        Users user = userService.getById(userId);
        if (user == null) {
            return RestBean.error(new IOException("User not found"));
        }
        user.setAvatar(dest.getName());
        userService.updateById(user);

        return RestBean.success(dest.getName());
    }

    @PostMapping("/upload")
    public RestBean<String> Create(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RestBean.error(new IOException("File is empty"));
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            if (!mkdir)
                return RestBean.error(new IOException("Failed to create directory"));
        }

        String originalFilename = file.getOriginalFilename();
        String extension = null;
        if (originalFilename != null) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String fileName = UUID.randomUUID() + extension;
        File dest = new File(dir, fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error(e.getMessage());
            return RestBean.error(new IOException("File upload failed"));
        }
        return RestBean.success(dest.getName());
    }

    @GetMapping("/images/{filename}")
    public void getImage(@PathVariable String filename, HttpServletResponse response) {
        File file = new File(uploadDir, filename);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);

        try (FileInputStream fis = new FileInputStream(file);
                ServletOutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("Error reading or writing image file", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
