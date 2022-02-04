package com.wzy.loan.oss.servce.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.wzy.loan.oss.servce.fileService;
import com.wzy.loan.oss.util.CosProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author：wzy
 * @date：2022/2/4-02-04-17:50
 */
@Service
public class fileServiceImpl implements fileService {
    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

//        创建client实例
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(
                new BasicCOSCredentials(CosProperties.KEY_ID, CosProperties.KEY_SECRET),
                clientConfig
        );


//        先判断一下桶子存不存在，若不存在就创建一个
        if(!cosClient.doesBucketExist(CosProperties.BUCKET_NAME)){
            cosClient.createBucket(CosProperties.BUCKET_NAME);
            cosClient.setBucketAcl(CosProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }


//        上传文件流
//        文件目录需要根据日期进行改变
//        例：”pic/2022/02/04/wzyaljx.jpg“

//        构建日期格式
        String timeFolderName = new DateTime().toString("/yyyy-MM-dd/");
//        文件名生成
        fileName =
                UUID.randomUUID().toString()
//              文件名
                +
                fileName.substring(fileName.lastIndexOf("."));
//              文件后缀

//        整合key，为从桶中取数据的标识
        String key=module+timeFolderName+fileName;

//        将文件上传
        cosClient.putObject(CosProperties.BUCKET_NAME,key,inputStream,new ObjectMetadata());

//        cos服务
        cosClient.shutdown();
        //文件的url地址
        //https:// bucketname   .  endpoint / + key
//        https://wzy-srb-1305802616.cos.ap-guangzhou.myqcloud.com/006IKX0Kly8gyoltnhc89j30mi0mi43a.jpg
        return "https://" + CosProperties.BUCKET_NAME  +"."+ CosProperties.ENDPOINT + "/" + key;
    }

    @Override
    public void removeFile(String url) {

//        创建client实例
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(
                new BasicCOSCredentials(CosProperties.KEY_ID, CosProperties.KEY_SECRET),
                clientConfig
        );

//        删除只需要key，因此需要将url中key的部分截取出来即可
        String host="https://" + CosProperties.BUCKET_NAME  +"."+ CosProperties.ENDPOINT + "/";
//        从host长度的位置开始截取
        String key = url.substring(host.length());


        cosClient.deleteObject(CosProperties.BUCKET_NAME,key);
        //        cos服务
        cosClient.shutdown();
    }
}
