package com.gb.soa.omp.export.util;

import com.gb.soa.omp.ccommon.api.exception.BusinessException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO（描述类的职责）
 *
 * @author zy
 * @date 2020/10/13 10:00
 */
@Component
public class MinioClientUtil {

	private static final Logger log = LoggerFactory.getLogger(MinioClientUtil.class);

	/**
	 * 连接url
	 */
	@Value("${minio.intranet.endpoint}")
	private String endpoint;
	/**
	 * 用户名
	 */
	@Value("${minio.accesskey}")
	private String accesskey;
	/**
	 * 密码
	 */
	@Value("${minio.secretkey}")
	private String secretKey;


	/**
	 * 上传文件
	 * @param filePath 获取文件地址
	 * @param fileName 文件名称
	 * @param bucketName 存储桶-上传文件地址
	 * @return
	 */
	public JSONObject uploadFile(String filePath, String fileName, String bucketName){
		JSONObject res = new JSONObject();
		res.put("code", -1);
		try {

			//获取 MinioClient
			MinioClient minioClient = MinioClient.builder()
					.endpoint(endpoint)
					.credentials(accesskey, secretKey)
					.build();
			// 判断存储桶是否存在
			boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (isExist) {
				log.debug("根文件夹已存在");
			} else {
				// 创建文件夹
				log.debug("创建根文件夹");
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
			}
			// 开始上传
			log.debug("开始上传文件");
//			minioClient.putObject(bucketName, fileName, filePath, null);

			minioClient.uploadObject(UploadObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.filename(filePath)
					.build());
			res.put("code", 0);
			res.put("msg", endpoint + "/" + bucketName + "/" + fileName);
			log.debug("上传文件成功");

		}catch (Throwable ex){
			log.error(ex.getMessage(),ex);
			res.put("code", -1);
			res.put("msg", ex.getMessage());
		}
		return res;
	}


	/**
	 * 获取文件URL
	 * @param fileName 文件名称
	 * @param bucketName 存储桶-上传文件地址
	 * @return
	 */
	public String getFileUrl(String fileName, String bucketName){
		String url = null;
		try {
			//获取 MinioClient
			MinioClient minioClient = MinioClient.builder()
					.endpoint(endpoint)
					.credentials(accesskey, secretKey)
					.build();

			url = minioClient.getObjectUrl(bucketName, fileName);
			log.debug("文件路径获取成功!");
		}catch (Throwable ex){
			throw new BusinessException(Constant.SUB_SYSTEM, ExceptionType.BE40203,
					"文件路径获取失败!" + ex.getMessage());
		}
		return url;
	}




}
