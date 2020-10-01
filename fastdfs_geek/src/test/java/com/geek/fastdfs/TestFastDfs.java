package com.geek.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestFastDfs {

    private String conf_filename = "config/fdfs-client.properties";

    @Test
    public void testUpload() {

        System.out.println("java.version=" + System.getProperty("java.version"));

        try {
            // 加载配置文件。
            ClientGlobal.initByProperties(conf_filename);

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
            metaList[0] = new NameValuePair("fileName", "hello.txt");
            // 执行上传。
            String fileId = client.upload_file1("C:\\Users\\geek\\Desktop\\hello.txt", "txt", metaList);
            System.out.println("upload success. file id is: " + fileId);

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
    }

    @Test
    public void testQuery() {

        System.out.println("java.version=" + System.getProperty("java.version"));

        try {
            // 加载配置文件。
            ClientGlobal.initByProperties(conf_filename);

            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            // 创建 Tracker 客户端。
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            // 定义 Storage 客户端。
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            FileInfo fileInfo = client.query_file_info("group1", "M00/00/00/wKiOoV74416Ab7YEAAAAETi0qDc272.txt");
            System.out.println("fileInfo = " + fileInfo);
            // fileInfo = source_ip_addr = 192.168.142.161, file_size = 17, create_timestamp = 2020-06-29 02:37:18, crc32 = 951363639

            // 根据文件 id 查询。
            FileInfo fileInfo1 = client.query_file_info1("group1/M00/00/00/wKiOoV74416Ab7YEAAAAETi0qDc272.txt");
            System.out.println("fileInfo1 = " + fileInfo1);

            // 查询元信息。
            NameValuePair[] metadata1 = client.get_metadata1("group1/M00/00/00/wKiOoV74416Ab7YEAAAAETi0qDc272.txt");
            System.out.println("metadata1 = " + metadata1);

            // 关闭。
            trackerServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDown() {

        System.out.println("java.version=" + System.getProperty("java.version"));

        try {
            // 加载配置文件。
            ClientGlobal.initByProperties(conf_filename);

            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            // 创建 Tracker 客户端。
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            // 定义 Storage 客户端。
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            byte[] bytes = client.download_file1("group1/M00/00/00/wKiOoV74416Ab7YEAAAAETi0qDc272.txt");

            File file = new File("./a.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            // 关闭。
            trackerServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

}


/*
java.version=1.8.0_241
network_timeout=30000ms
charset=UTF-8
upload success. file id is: group1/M00/00/00/wKiOoV74416Ab7YEAAAAETi0qDc272.txt
1, download result is: 17
2, download result is: 17
3, download result is: 17
4, download result is: 17
5, download result is: 17
6, download result is: 17
7, download result is: 17
8, download result is: 17
9, download result is: 17
10, download result is: 17

Process finished with exit code 0

 */
