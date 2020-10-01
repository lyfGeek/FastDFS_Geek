package com.geek.fastdfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSystem {

    private String fileId;
    private String filePath;
    private long fileSize;
    private String fileName;
    private String fileType;

}
