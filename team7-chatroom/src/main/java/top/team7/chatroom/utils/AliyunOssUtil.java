package top.team7.chatroom.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.team7.chatroom.config.AliyunOssConfig;

import java.io.InputStream;
import java.util.UUID;

@Component
public class AliyunOssUtil {

    @Autowired
    AliyunOssConfig aliyunOssConfig;

    public String upload(InputStream inputStream, String module, String originalFilename) {

        String endpoint = aliyunOssConfig.getEndpoint();
        String keyId = aliyunOssConfig.getKeyid();
        String keySecret = aliyunOssConfig.getKeysecret();
        String bucketName = aliyunOssConfig.getBucketname();

        OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            String folder = new DateTime().toString("yyyy/MM/dd");
            String fileName = UUID.randomUUID().toString();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String key = module + "/" + folder + "/" + fileName + fileExtension;
            ossClient.putObject(aliyunOssConfig.getBucketname(), key, inputStream);
            return "https://" + bucketName + "." + endpoint + "/" + key;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
