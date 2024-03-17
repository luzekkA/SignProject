package com.example.signproject.Utils;

import com.example.signproject.Enum.RolesEnum;
import com.example.signproject.ViewModel.exportSheetViewModel;
import com.example.signproject.entity.User;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class FileUtil {
    private static final String MINIO_URL = "http://121.40.35.81";
    private static final String MINIO_ACCESS_KEY = "admin";
    private static final String MINIO_SECRET_KEY = "adminadminadmin";
    private static final String BUCKET_NAME = "sign-project";
    private static final MinioClient minioClient = MinioClient.builder()
            .endpoint(MINIO_URL, 9000, false)
            .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
            .build();

//    public static boolean isImageFile(File file) {
//        try {
//            return ImageIO.read(file) != null;
//        } catch (IOException e) {
//            return false;
//        }
//    }

//    public static boolean isImageExtension(String filename) {
//        List<String> imageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
//        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
//        return imageExtensions.contains(extension);
//    }

    public static ResultJson<Object> isImage(MultipartFile multipartFile) {
//        return ResultJson.success();
//        try {
        // 检查文件是否为空
        if (multipartFile.isEmpty()) {
            return ResultJson.error("文件为空");
        }

        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            return ResultJson.error("文件名为空");
        }

        // 获取文件扩展名
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 检查文件扩展名是否为图片格式
        if (!isImageExtension(fileExtension)) {
            return ResultJson.error("文件扩展名不是图片格式");
        }

//            // 创建临时文件
//            File file = File.createTempFile("temp", fileExtension);
//
//            // 将上传的文件转存到临时文件中
//            multipartFile.transferTo(file);
//
//            // 检查临时文件是否为有效的图片文件
//            if (isImageFile(file)) {
//                return ResultJson.success();
//            } else {
//                return ResultJson.error("文件不是有效的图片");
//            }
        return ResultJson.success();
//        }
//        catch (IOException e) {
//            return ResultJson.error("Error occurred is image: " + e.getMessage());
//        }
    }

    // 检查文件扩展名是否为图片格式
    private static boolean isImageExtension(String fileExtension) {
        // 这里你需要实现对图片扩展名的判断逻辑，例如：JPEG、JPG、PNG、GIF等
        // 你可以根据实际需求来扩展支持的图片格式
        return fileExtension.equalsIgnoreCase(".jpg") ||
                fileExtension.equalsIgnoreCase(".jpeg") ||
                fileExtension.equalsIgnoreCase(".png") ||
                fileExtension.equalsIgnoreCase(".gif");
    }

    // 检查临时文件是否为有效的图片文件
    private static boolean isImageFile(File file) {
        // 这里你需要实现检查文件是否为图片文件的逻辑
        // 可以使用图片处理库来验证文件的有效性，或者根据文件头部信息进行判断
        // 例如，使用 ImageIO 或者 Apache Commons Imaging 库
        // 下面是一个简单的示例：
        try {
            ImageIO.read(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static ResultJson<Object> uploadFileToMinio(MultipartFile file, String fileName) {
        try {
//            MinioClient minioClient = MinioClient.builder()
//                    .endpoint("121.40.35.81", 9000, false)
//                    .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
//                    .build();

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }

            try (InputStream stream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(fileName)
                                .stream(stream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
            System.out.println("File '" + file.getOriginalFilename() + "' is uploaded successfully to MinIO server.");
            return ResultJson.success();
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            return ResultJson.error("Error occurred: " + e);
        }
    }

    public static void removeFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .build());
    }

    public static String getObjectUrl(String objectName) {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .method(Method.GET).build();
            return minioClient.getPresignedObjectUrl(args);
        } catch (Exception e) {
            return null;
        }
    }

    public static ResultJson<Object> isPdf(MultipartFile multipartFile) {
        String filename = Objects.requireNonNull(multipartFile.getOriginalFilename());
        if (!filename.toLowerCase().endsWith(".pdf")) {
            return ResultJson.error("文件后缀不是.pdf");
        }

        try (PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
            // If the document has no pages, it is not a valid PDF
            if (document.getNumberOfPages() < 1) {
                return ResultJson.error("文件不是有效的PDF文件");
            }
        } catch (IOException e) {
            return ResultJson.error("文件不是有效的PDF文件");
        }

        return ResultJson.success();
    }

    public static Workbook setExcel(List<User> userList) {
        Workbook workbook = new XSSFWorkbook();
        String[] excelHeader = {"姓名", "身份证", "手机号"};
        Sheet sheet = workbook.createSheet("人员名单");
        for (int i = 0; i < excelHeader.length; i++) {
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            sheet.autoSizeColumn(i);
            //设置指定列的列宽，256 * 50这种写法是因为width参数单位是单个字符的256分之一
            sheet.setColumnWidth(cell.getColumnIndex(), 100 * 50);
        }

        Row r = sheet.createRow(0);
        r.createCell(0).setCellValue(excelHeader[0]);
        r.createCell(1).setCellValue(excelHeader[1]);
        r.createCell(2).setCellValue(excelHeader[2]);

        // 使用List的数据填充工作表的行和列
        int rowNum = 1;
        for (User item : userList) {
            if (item.getRoles() == RolesEnum.player) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getUsername());
                row.createCell(1).setCellValue(item.getId());
                row.createCell(2).setCellValue(item.getPhone());
            }
        }
        return workbook;
    }

    public static Workbook setExcel_t(List<exportSheetViewModel> exportSheetViewModelList) {
        Workbook workbook = new XSSFWorkbook();
        String[] excelHeader = {"序号", "姓名", "性别", "组别", "比赛小项", "代表队", "身份证号", "注册编号","签号"};
        Sheet sheet = workbook.createSheet("抽签检录");
        for (int i = 0; i < excelHeader.length; i++) {
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            sheet.autoSizeColumn(i);
            //设置指定列的列宽，256 * 50这种写法是因为width参数单位是单个字符的256分之一
            sheet.setColumnWidth(cell.getColumnIndex(), 100 * 50);
        }

        Row r = sheet.createRow(0);
        r.createCell(0).setCellValue(excelHeader[0]);
        r.createCell(1).setCellValue(excelHeader[1]);
        r.createCell(2).setCellValue(excelHeader[2]);
        r.createCell(3).setCellValue(excelHeader[3]);
        r.createCell(4).setCellValue(excelHeader[4]);
        r.createCell(5).setCellValue(excelHeader[5]);
        r.createCell(6).setCellValue(excelHeader[6]);
        r.createCell(7).setCellValue(excelHeader[7]);
        r.createCell(8).setCellValue(excelHeader[8]);

        // 使用List的数据填充工作表的行和列
        int rowNum = 1;
        for (exportSheetViewModel item : exportSheetViewModelList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getUserName());
            row.createCell(2).setCellValue(item.getGender());
            row.createCell(3).setCellValue(item.getGroup());
            row.createCell(4).setCellValue(item.getType().toString());
            row.createCell(5).setCellValue(item.getTeam());
            row.createCell(6).setCellValue(item.getUserid());
            row.createCell(7).setCellValue("");
            row.createCell(8).setCellValue(item.getSort());
        }
        return workbook;
    }
}
