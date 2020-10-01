package com.geek.fastdfs.controller;

import com.geek.fastdfs.model.FileSystem;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/filesystem")
@CrossOrigin
public class FileServerController {

    @Value("${geek-fastdfs.upload_location}")
    private String upload_location;

    @PostMapping("/upload")
    @ResponseBody
    public FileSystem upload(@RequestParam("file") MultipartFile multipartFile) {
        // 将文件存储在 web 服务器上，再调用 fastdfs 的 client 将文件上传到 fastdfs 服务器。
        FileSystem fileSystem = new FileSystem();
        // 文件原始名称。
        String originalFilename = multipartFile.getOriginalFilename();
        // 扩展名。
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameNew = UUID.randomUUID() + extension;

        try {
            File file = new File("G:\\home\\upload\\" + fileNameNew);
            // 先上传到本地服务器。
            multipartFile.transferTo(file);
            // 获取新上传文件的物理路径。
            String newFilePath = file.getAbsolutePath();

            // 加载配置文件。
            ClientGlobal.initByProperties("config/fdfs-client.properties");

            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            // 创建 Tracker 客户端。
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            // 定义 Storage 客户端。
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            // 文件元信息。
            NameValuePair[] metaList = new NameValuePair[1];
            metaList[0] = new NameValuePair("fileName", originalFilename);
            // 执行上传。
            System.out.println(newFilePath);
            String fileId = client.upload_file1(newFilePath, null, metaList);
            System.out.println("upload success. file id is: " + fileId);

            fileSystem.setFileId(fileId);
            fileSystem.setFilePath(fileId);
            fileSystem.setFileName(originalFilename);

            // 通过调用 service 和 dao 将文件路径存储到数据库。
            // 。。。

            int i = 0;
            while (i++ < 10) {
                byte[] result = client.download_file1(fileId);
                System.out.println(i + ", download result is: " + result.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return fileSystem;
    }

}
