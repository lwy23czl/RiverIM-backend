package cn.river.im.controller;

import cn.river.im.service.QiNiuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private QiNiuService qiNiuService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return qiNiuService.upload(file);
    }
}

