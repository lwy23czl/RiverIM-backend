package cn.river.im.service;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiNiuService {

    private final String accessKey = "d0ux_HXMA9iRwofw9DVL45lYl9QvrIzlo9e8kHU8";
    private final String secretKey = "qoVyx2PJwXeOItMwJrGJ5XS-lYq7afe2gE7O2P07";
    private final String bucket = "wind-chat";
    private final String domain = "http://qn.lwy23czl.top";

    /**
     * 上传文件到七牛云
     *
     * @param file 待上传的文件
     * @return 返回文件在七牛云的URL地址
     */
    public String upload(MultipartFile file) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(file.getBytes(), null, upToken);
            // 解析上传成功的结果
            return domain + "/" + response.jsonToMap().get("key").toString();
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}

