package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.IUpDownloadController;
import cn.ruiheyun.athena.common.util.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(value = {"/api/v1/admin/upDownload"})
public class UpDownloadControllerImpl implements IUpDownloadController {

    @Autowired
    private StringRedisUtils stringRedisUtils;

    @Override
    @RequestMapping(value = {"/upload"})
    public Object upload(@RequestPart("file") FilePart filePart) {

        byte[] cache = new byte[1024 * 8];

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Path temp = Files.createTempFile(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-")), filePart.filename());
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(temp, StandardOpenOption.WRITE);
            DataBufferUtils.write(filePart.content(), channel, 0).doOnComplete(() -> log.info("Upload Finnish...")).subscribe();

            try (FileInputStream inputStream = new FileInputStream(temp.toFile())) {
                int length;
                while ((length = inputStream.read(cache)) != -1) {
                    outputStream.write(cache, 0, length);
                }
            }
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("捕获异常, 错误类型: {}, 错误提示: {}", e.getClass(), e.getMessage());
        }
        return null;
    }
}
