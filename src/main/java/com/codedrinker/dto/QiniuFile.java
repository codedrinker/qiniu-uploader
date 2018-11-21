package com.codedrinker.dto;

import lombok.Data;

/**
 * Created by codedrinker on 2018/10/12.
 */
@Data
public class QiniuFile {
    private byte[] uploadBytes;
    private String fileName;

    public QiniuFile() {
    }
}
