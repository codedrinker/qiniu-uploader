package com.codedrinker.adapter;

import com.codedrinker.dto.QiniuFile;
import com.codedrinker.exception.ErrorCodeException;
import com.codedrinker.error.ErrorCode;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;


/**
 * Created by codedrinker on 2018/10/12.
 */
@Service
public class QiniuAdapter {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    private String bucket = "assets";

    private String path = "images/";

    public String upload(QiniuFile qiniuFile) {
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);//...生成上传凭证，然后准备上传
        String[] files = StringUtils.split(qiniuFile.getFileName(), ".");
        if (files.length < 2) {
            throw new ErrorCodeException(ErrorCode.UPLOAD_FAIL);
        }
        String ext = files[files.length-1];
        String key = path + StringUtils.substring(UUID.randomUUID().toString(), 0, 10) + "." + ext;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(qiniuFile.getUploadBytes());
        String token = getToken();
        try {
            Response response = uploadManager.put(byteInputStream, key, token, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                throw new ErrorCodeException(ErrorCode.UPLOAD_FAIL);
            }
        }
        return null;
    }

    private String getToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucket);
        return token;
    }
}
