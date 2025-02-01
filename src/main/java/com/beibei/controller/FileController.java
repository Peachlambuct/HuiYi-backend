package com.beibei.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Users;
import com.beibei.service.UsersService;
import com.beibei.utils.Const;
import com.beibei.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Resource
    private UsersService userService;

    @Resource
    private UsersService usersService;

    @Resource
    private JwtUtils jwtUtils;

    @PostMapping("/upload")
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

    @PostMapping("/upload-avatar-create")
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

    @GetMapping("/avatar")
    public void getAvatar(@RequestParam("token") String token, HttpServletResponse response) {
        DecodedJWT decodedJWT = jwtUtils.resolveJwt(token);
        if (decodedJWT == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Integer id = jwtUtils.toId(decodedJWT);
        try {
            Users user = usersService.getById(id);
            if (user == null || user.getAvatar() == null) {
                // 不要返回 JSON，而是返回 404 状态码
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            File file = new File(uploadDir, user.getAvatar());
            if (!file.exists()) {
                // 同样，只返回状态码
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 确保设置正确的 Content-Type
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null || !mimeType.startsWith("image/")) {
                mimeType = "image/jpeg"; // 设置默认的图片类型
            }
            response.setContentType(mimeType);

            // 添加一些缓存相关的头信息
            response.setHeader("Cache-Control", "public, max-age=31536000");

            try (FileInputStream fis = new FileInputStream(file);
                    ServletOutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
        } catch (Exception e) {
            log.error("Error serving avatar", e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 出错时返回 404，而不是 JSON
        }
    }
}
