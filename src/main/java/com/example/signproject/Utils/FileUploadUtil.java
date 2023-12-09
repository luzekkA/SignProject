package com.example.signproject.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtil {
    private static final String UPLOAD_DIR = "uploads/";

    public static ResultJson<Object> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ResultJson.error("文件为空");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();

        // 获取文件大小
        long fileSize = file.getSize();

        // 检查文件格式是否为图片
        if (!isImageFile(file)) {
            return ResultJson.error("文件格式不是图片");
        }

        // 保存文件到服务器（这里只是简单地将文件保存到项目根目录下的uploads文件夹）
        try {
            file.transferTo(new File(UPLOAD_DIR + fileName));
            return ResultJson.success(UPLOAD_DIR + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultJson.error("文件上传失败");
        }
    }

    private static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
