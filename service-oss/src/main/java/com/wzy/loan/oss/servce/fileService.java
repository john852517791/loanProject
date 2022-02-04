package com.wzy.loan.oss.servce;

import java.io.InputStream;

/**
 * @author：wzy
 * @date：2022/2/4-02-04-17:49
 */
public interface fileService {

//    上传文件到腾讯云
    String upload(InputStream inputStream,String module,String fileName);


    void removeFile(String url);

}
