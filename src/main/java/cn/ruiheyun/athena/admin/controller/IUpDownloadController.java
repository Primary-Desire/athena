package cn.ruiheyun.athena.admin.controller;

import org.springframework.http.codec.multipart.FilePart;

public interface IUpDownloadController {

    Object upload(FilePart filePart);

}
