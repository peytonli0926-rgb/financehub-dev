package com.utfinancing.financehub.engine.file.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.file.common.UnifiedException;
import com.utfinancing.financehub.engine.file.config.AttachmentInfoConfig;
import com.utfinancing.financehub.engine.file.utils.MySSLSocketClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author iwen
 * @date 2023/11/6 13:57
 */
@RefreshScope
@Slf4j
@Service
public class FilezServiceImpl implements FilezService {

	@Value("${attachment.tenantCode}")
	private String tenantCode;

	@Value("${attachment.secret.slug}")
	private String loginName;

	@Value("${attachment.secret.app-key}")
	private String appKey;

	@Value("${attachment.secret.app-secret}")
	private String appSecret;

	@Value("${attachment.url.baseURL}")
	private String baseUrl;

	//获取token的地址
	@Value("${attachment.url.get-token}")
	private String getTokenUrl;

	//根据路径获取文件列表
//	@Value("${attachment.url.get-file}")
//	private String getFilesByFilePathUrl;

//	@Value("${attachment.url.get-file-info}")
//	private String getFileInfoByPathUrl;

	//下载文件地址
	@Value("${attachment.url.download-file}")
	private String downLoadFileUrl;

	//上传文件的地址
//	@Value("${attachment.url.upload-file}")
//	private String uploadFileUrl;

	// 预览地址
//	@Value("${attachment.url.preview-file}")
//	private String previewFileUrl;

	//在线编辑地址
//	@Value("${attachment.url.online-edit-file}")
//	private String onlineEditFileUrl;

	// 删除地址
//	@Value("${attachment.url.delete-file}")
//	private String deleteFileUrl;

	// 移动地址
//	@Value("${attachment.url.move-file}")
//	private String moveFileUrl;

	//外链接口地址
//	@Value("${attachment.url.delivery-file}")
//	private String deliveryFileUrl;

//	@Value("${attachment.url.rename-file}")
//	private String renameFileUrl;

//	@Value("${attachment.url.create-folder}")
//	private String createFolderUrl;

	@Resource
	private RedisService redisService;

	@Autowired
	private AttachmentInfoConfig attachmentInfoConfig;

	private static OkHttpClient mClient;

	//获取token的请求头key
	private final static String GRANT_TYPE = "grant_type";
	//获取token的请求头value
	private final static String CLIENT_WITH_SU = "client_with_su";
	//获取token的请求头key
	private final static String SCOPE = "scope";
	//获取token的请求头value
	private final static String ALL = "all";
	//获取token的请求头key
	private final static String SLUG = "slug";
	//Content-Type
	private final static String CONTENT_TYPE = "Content-Type";
	//Content-Type
	private final static String X_WWW_FORM = "application/x-www-form-urlencoded";
	//请求头加密参数
	private final static String AUTHORIZATION = "Authorization";
	//返回的token key
	private final static String ACCESS_TOKEN = "access_token";
	//请求头加密参数
	private final static String BEARER = "bearer ";
	//401代表认证失败
	private final static String ERR_401 = "401";
	//400代表参数异常
	private final static String ERR_400 = "400";
	//403代表参数异常
	private final static String ERR_403 = "403";
	//0代表请求成功
	private final static String SUCCESS = "0";
	private final static String FILE_EXTENSION_NOT_SUPPORT_400 = "400";

	//初始化客户端
	static {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(10L, TimeUnit.SECONDS);
		builder.readTimeout(10L, TimeUnit.MINUTES); // 设短了上传文件容易timeout
		builder.sslSocketFactory(MySSLSocketClient.getSSLSocketFactory(), MySSLSocketClient.X509);
		builder.hostnameVerifier(MySSLSocketClient.getHostnameVerifier());
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.setMaxRequestsPerHost(200);
		dispatcher.setMaxRequests(200);
		builder.dispatcher(dispatcher);
		mClient = builder.build();
	}

	@Override
	public InputStream downloadFile(Long neid, Integer nsid, String recordId) throws IOException, UnifiedException {
		String token = getToken(recordId);
		String url = baseUrl.concat(downLoadFileUrl + "?neid=" + neid + "&nsid=" + nsid);
		log.info("获取token成功,开始请求文件下载接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
		Request request = new Request.Builder()
				.addHeader(AUTHORIZATION, BEARER + token)
				.url(url)
				.get()
				.build();
		Response response = mClient.newCall(request).execute();
		log.info("开始请求文件下载接口成功, tenantCode:{},recordId:{}", tenantCode,recordId);
		return response.body().byteStream();
	}

//	@Override
//	public void deleteFile(String tenantCode,Long fileNeid, Integer fileNsid, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = deleteFileUrl + fileNeid;
//		log.info("获取token成功,开始请求删除文件接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//		FormBody formBody = new FormBody.Builder().add("nsid",fileNsid.toString()).build();
//		Request.Builder delete = new Request.Builder().url(url).delete(formBody);
//		delete.addHeader(AUTHORIZATION, BEARER + token);
//		delete.addHeader(CONTENT_TYPE, X_WWW_FORM);
//		Request request = delete.build();
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("删除文件接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("删除文件接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		}
//	}


//	@Override
//	public FilezUploadVo uploadFile2Filez(String tenantCode, MultipartFile file, String fileName, String pathType, String path, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//
//		log.info("获取token成功,开始请求上传接口,tenantCode:{}, uploadFileUrl:{}, recordId:{}", tenantCode,uploadFileUrl, recordId);
//		MediaType mediaType = okhttp3.MediaType.parse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//		RequestBody fileBody = RequestBody.create(mediaType, file.getBytes());
//
//		RequestBody requestBody = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("path", path)
//				.addFormDataPart("path_type", pathType)
//				.addFormDataPart("filedata", fileName, fileBody).build();
//
//		Request request = getTokenPostRequest(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE, token, uploadFileUrl, requestBody);
//		FilezUploadVo filezUploadVo = getResponse(request, FilezUploadVo.class, recordId);
//		log.info("上传接口请求成功, filezUploadVo:{}, tenantCode:{},recordId:{}", JSONObject.toJSONString(filezUploadVo), tenantCode,recordId);
//		if (StringUtils.equalsAny(filezUploadVo.getErrCode(), ERR_401, ERR_403)  || StrUtil.isBlank(filezUploadVo.getErrCode())){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, tenantCode:{},recordId:{}", tenantCode,recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenPostRequest(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE, token, uploadFileUrl, requestBody);
//			filezUploadVo = getResponse(request, FilezUploadVo.class, recordId);
//			log.info("上传接口再次请求成功, filezUploadVo:{}, tenantCode:{},recordId:{}", JSONObject.toJSONString(filezUploadVo), tenantCode,recordId);
//		}
//		return filezUploadVo;
//	}

	/**
	 * 附件上传云盘
	 //     * @param nasPath  附件在nas盘上的路径
	 * @param fileName 附件名称
	 * @param pathType
	 * @param path 上传路径
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	@Override
//	public FilezUploadVo uploadFile2Filez(String tenantCode,File file, String fileName, String pathType, String path, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//
//		log.info("获取token成功,开始请求上传接口, tenantCode:{},uploadFileUrl:{}, recordId:{}", tenantCode,uploadFileUrl, recordId);
//		MediaType mediaType = okhttp3.MediaType.parse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//		log.info("获取nas附件成功,tenantCode:{},附件名称:{},附件地址:{},附件是否存在:{},recordId:{}",tenantCode,file.getName(),file.getAbsolutePath(),file.exists(),recordId);
//
//		RequestBody fileBody = RequestBody.create(mediaType, file);
//		RequestBody requestBody = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("path", path)
//				.addFormDataPart("path_type", pathType)
//				.addFormDataPart("filedata", fileName, fileBody).build();
//
//		Request request = getTokenPostRequest(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE, token, uploadFileUrl, requestBody);
//		FilezUploadVo filezUploadVo = getResponse(request, FilezUploadVo.class, recordId);
//		log.info("uploadFile2Filez result: filezUploadVo={}, path={}, pathType={}, tenantCode={}, recordId={}", JSONObject.toJSONString(filezUploadVo), path, pathType, tenantCode, recordId);
//		if (StringUtils.equalsAny(filezUploadVo.getErrCode(), ERR_401, ERR_403) || StrUtil.isBlank(filezUploadVo.getErrCode())){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, tenantCode:{},recordId:{}", tenantCode,recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenPostRequest(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE, token, uploadFileUrl, requestBody);
//			filezUploadVo = getResponse(request, FilezUploadVo.class, recordId);
//			log.info("上传接口再次请求成功, filezUploadVo:{}, tenantCode:{},recordId:{}", JSONObject.toJSONString(filezUploadVo), tenantCode,recordId);
//		}
//
//		return filezUploadVo;
//	}


//	@Override
//	public String deliveryFile(String tenantCode,String nsid, String neid, String recordId) throws UnifiedException, IOException {
//		String token = getToken(tenantCode,recordId);
//		log.info("获取token成功,开始请求上传接口,tenantCode:{}, deliveryFileUrl:{}, recordId:{}", tenantCode,deliveryFileUrl, recordId);
//		DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//		RequestBody requestBody = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("expiration", dfDate.format(LocalDateTime.now().plusDays(1L)))
//				.addFormDataPart("mode", "r")
//				.addFormDataPart("nsid", nsid)
//				.addFormDataPart("neid",neid)
//				.build();
//		Request request = getTokenPostRequest("application/x-www-form-urlencoded",token,deliveryFileUrl,requestBody);
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("文件预览接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equals(errorCode, SUCCESS)) {
//			return jsonObject.get("url").toString();
//		}
//		return null;
//	}

//	@Override
//	public String getPreviewUrl(String tenantCode,Long neid, Integer nsid, boolean thumbtail, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = previewFileUrl + neid + "?nsid=" + nsid + "&thumbtail=" + thumbtail;
//		log.info("获取token成功,开始请求文件预览接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//
//		Request request = getTokenGetRequest(token, url);
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("文件预览接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, tenantCode:{},recordId:{}", tenantCode,recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("文件预览接口再次请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		}
//		// 错误码不为0表示不支持预览
//		if (!StringUtils.equals(errorCode, SUCCESS)) return "0";
//
//		return (String) jsonObject.get("previewUrl");
//	}

	/**
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	@Override
//	public List<BatchPreviewUrlDto> getBatchPreviewUrl(String tenantCode, List<AttachmentPreviewReqDto> reqDtoList, boolean thumbtail, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = previewFileUrl + "batch";
//		log.info("获取token成功,开始请求批量文件预览接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//		FormBody.Builder formBuilder = new FormBody.Builder();
//		formBuilder.add("file_array", JSONArray.toJSONString(reqDtoList));
//		RequestBody requestBody = formBuilder.build();
//
//		Request request = getTokenPostRequest(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,token, url,requestBody);
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("批量请求文件预览接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("批量请求文件预览接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		}
//
//		List<BatchPreviewUrlDto> returnDtoList = new ArrayList<>(reqDtoList.size());
//		// 获取失败,返回1
//		if (!StringUtils.equals(errorCode, SUCCESS)){
//			for (AttachmentPreviewReqDto reqDto : reqDtoList) {
//				BatchPreviewUrlDto returnDto = new BatchPreviewUrlDto();
//				returnDto.setNeid(reqDto.getNeid());
//				returnDto.setPreviewUrl("1");
//				returnDtoList.add(returnDto);
//			}
//			return returnDtoList;
//		}
//
//		JSONArray list = jsonObject.getJSONArray("list");
//		for (int i = 0; i < list.size(); i++) {
//			JSONObject item = list.getJSONObject(i);
//			BatchPreviewUrlDto returnDto = new BatchPreviewUrlDto();
//			returnDto.setNeid(item.getLong("neid"));
//			String itemErrCode = item.getString("errcode");
//
//			//不支持在线预览，返回0
//			if(!StringUtils.equals(itemErrCode, SUCCESS)){
//				returnDto.setPreviewUrl("0");
//			}else {
//				returnDto.setPreviewUrl(item.getString("previewUrl"));
//			}
//			returnDtoList.add(returnDto);
//		}
//
//		return returnDtoList;
//	}

//	@Override
//	public String getEditUrl(String tenantCode,Long neid, Integer nsid, String path, String pathType,String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = onlineEditFileUrl + "?neid="+ neid + "&nsid=" + nsid + "&path=" + path + "&path_type=" + pathType;
//		log.info("获取token成功,开始请求文件在线编辑url接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//		Request request = getTokenGetRequest(token, url);
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("文件在线编辑url接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, tenantCode:{},recordId:{}", tenantCode,recordId);
//			deleteToken(tenantCode);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("文件在线编辑url再次请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		}
//
//		//此文件不支持在线编辑,返回0
//		if (StringUtils.equals(errorCode,FILE_EXTENSION_NOT_SUPPORT_400)) return "0"; //{"errcode":400,"errmsg":"file extension not support"}
//
//		//获取失败，返回1
//		if (!StringUtils.equals(errorCode, SUCCESS)) return "1";
//
//		return (String) jsonObject.get("editUrl");
//	}




	private static <T> T getResponse(Request request, Class<T> tClass, String recordId) throws IOException, UnifiedException {
		Response response = mClient.newCall(request).execute();
		return JSONObject.parseObject(getResponseString(response, recordId), tClass);
	}

	/**
	 * 获取token
	 * @throws IOException
	 * @throws UnifiedException
	 */
	public String getToken(String recordId) throws IOException, UnifiedException {
		log.info("开始获取Token, tenantCode:{},recordId:{}", tenantCode,recordId);
//		String tokenKey = getTokenKey(tenantCode);
		String token = redisService.getCacheObject(RedisConstant.ATTACHMENT_TOKEN_KEY);
		if (!StringUtils.isBlank(token)){
			log.info("从缓存中取出token,直接返回, tenantCode:{},recordId:{}", tenantCode,recordId);
			return token;
		}
		log.info("缓存中没有token,请求云盘API获取token, tenantCode:{},recordId:{}", tenantCode,recordId);
//		TenantConfigDto tenantConfigDto = attachmentInfoConfig.getByTenantCode(tenantCode);
//		if(ObjectUtil.isEmpty(tenantConfigDto) || ObjectUtil.isEmpty(tenantConfigDto.getSecret())){
//			throw new RuntimeException("没有获取当前租户的配置信息,请先配置!");
//		}

		FormBody.Builder params = new FormBody.Builder();
		params.add(GRANT_TYPE, CLIENT_WITH_SU);
		params.add(SCOPE, ALL);
		params.add(SLUG, loginName);

		Response response = mClient.newCall(getAuthorizationPostFromRequest(appKey,appSecret,params.build(),
				baseUrl.concat(getTokenUrl))).execute();

		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
		log.info("请求云盘API获取token成功, 返回值:{},tenantCode:{}, recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
		Integer expiresIn = (Integer) jsonObject.get("expires_in");
		// token失效时间减去几秒
		int seconds = 2;
		if (expiresIn.compareTo(seconds) > 0) expiresIn -= seconds;

		redisService.setCacheObject(RedisConstant.ATTACHMENT_TOKEN_KEY, jsonObject.get(ACCESS_TOKEN), expiresIn.longValue(), TimeUnit.SECONDS);
		log.info("请求云盘API获取token成功, 将token放入缓存成功, expiresIn:{}, tenantCode:{},recordId:{}", expiresIn,tenantCode, recordId);
		return (String) jsonObject.get(ACCESS_TOKEN);
	}

	public String getAuthorization(String appKey,String appSecret) {
		String authorization = appKey + ":" + appSecret;
		String encode = Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8));
		return "Basic " + encode;
	}

	private  Request getAuthorizationPostFromRequest(String appKey,String appSecret,RequestBody requestBody, String url) {
		return new Request.Builder()
				.addHeader(CONTENT_TYPE, X_WWW_FORM)
				.addHeader(AUTHORIZATION, getAuthorization(appKey,appSecret))
				.url(url)
				.post(requestBody)
				.build();
	}

	private static Request getTokenPostRequest(String contentType, String token, String url, RequestBody requestBody ) {
		return new Request.Builder()
				.addHeader(CONTENT_TYPE, contentType)
				.addHeader(AUTHORIZATION, BEARER + token)
				.url(url)
				.post(requestBody)
				.build();
	}

	private static Request getTokenGetRequest(String token, String url) {
		return new Request.Builder()
				.addHeader(AUTHORIZATION, BEARER + token)
				.url(url)
				.get()
				.build();
	}



	public static String getResponseString(Response response, String recordId) throws IOException, UnifiedException {
		if (response == null) {
			log.info("云盘返回值为null,请求异常,recordId:{}", recordId);
			throw new UnifiedException(500,"请求异常");
		}
		String string = response.body().string();
		if (StringUtils.isBlank(string)) {
			log.info("云盘返回值为空,请求异常,recordId:{}", recordId);
			throw new UnifiedException(500,"请求异常");
		}
		return string;
	}


	//获取tokenKey
	public String getTokenKey(String tenantCode){
		return ACCESS_TOKEN + ":" + tenantCode;
	}

	//删除redis中的token
	public void deleteToken(String tenantCode){
		String tokenKey = getTokenKey(tenantCode);
		redisService.deleteObject(tokenKey);
	}
//	@Override
//	public BatchGetByFilePathVo getFileListByPath(String tenantCode, String filePath, Integer pageNo, Integer pageSize, String recordId) throws UnifiedException, IOException {
//		String token = getToken(tenantCode,recordId);
//		String url = getFilesByFilePathUrl;
//		log.info("获取token成功,开始请求根据路径获取文件列表接口, url:{}, tenantCode:{},recordId:{},filePath:{}", url, tenantCode,recordId,filePath);
//		String pathType = "ent";
//
//		FormBody.Builder params = new FormBody.Builder();
//		params.add("path", filePath);
//		params.add("path_type", pathType);
//		params.add("page_num", pageNo.toString());
//		params.add("page_size",pageSize.toString());
//		params.add("sort", "desc");
//		params.add("order_by","mtime");
//		Request request = getTokenPostRequest(X_WWW_FORM, token, getFilesByFilePathUrl, params.build());
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("请求根据路径获取文件列表接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("请求根据路径获取文件列表接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), tenantCode,recordId);
//		}
//
//		List<FilezFileDto> returnDtoList = new ArrayList<>();
//		Integer total = (Integer) jsonObject.get("total");
//		JSONArray list = jsonObject.getJSONArray("fileModelList");
//
//		if (Objects.nonNull(list)) {
//			for (int i = 0; i < list.size(); i++) {
//				FilezFileDto item = list.getObject(i, FilezFileDto.class);
//				returnDtoList.add(item);
//			}
//		}
//		BatchGetByFilePathVo batchGetByFilePathVo = new BatchGetByFilePathVo(returnDtoList,total);
//
//		return batchGetByFilePathVo;
//	}

//	@Override
//	public FilezFileDto getFileByPath(String tenantCode, String filePath, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = getFileInfoByPathUrl;
//		log.info("获取token成功,开始请求根据路径获取文件接口, url:{}, tenantCode:{},recordId:{},filePath:{}", url, tenantCode,recordId,filePath);
//		String pathType = "ent";
//
//		FormBody.Builder params = new FormBody.Builder();
//		params.add("path", filePath);
//		params.add("path_type", pathType);
//		Request request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("请求根据路径获取文件接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("请求根据路径获取文件接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		}else if (errorCode.equals(SUCCESS)){
//			return jsonObject.toJavaObject(FilezFileDto.class);
//		}
//		return null;
//	}

//	@Override
//	public String moveFileByNsidAndNeid(String tenantCode, String nsid, String fromNeid, String filePath, String pathType, String recordId) throws UnifiedException, IOException {
//		String token = getToken(tenantCode,recordId);
//		String url = moveFileUrl;
//		//log.info("获取token成功,开始请求批量文件预览接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//
//
//		FormBody.Builder params = new FormBody.Builder();
//		params.add("nsid", nsid);
//		params.add("from_neid", fromNeid);
//		params.add("to_path_type", pathType);
//		params.add("to_path",filePath);
//		Request request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("移动文件接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		String errmsg = String.valueOf(jsonObject.get("errmsg"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("移动文件接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		}
//
//
//		return errorCode;
//	}

//	@Override
//	public FilezFileDto getFileByNeidAndNsid(String tenantCode, String neid, String nsid, String recordId) throws UnifiedException, IOException {
//		String token = getToken(tenantCode,recordId);
//		String url = StrUtil.concat(true, getFilesByFilePathUrl, AttachmentConstants.SEPARATOR, neid, "?nsid=", nsid);
//		log.info("获取token成功,请求通过文件id获取文件信息接口参数, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//		Request request = getTokenGetRequest(token, url);
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("请求通过文件id获取文件信息接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenGetRequest(token, url);
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("请求通过文件id获取文件信息接口成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		}
//		return jsonObject.toJavaObject(FilezFileDto.class);
//	}

//	@Override
//	public String fileRename(String tenantCode, String nsid, String fromNeid, String toFileName, String recordId) throws UnifiedException, IOException {
//		String token = getToken(tenantCode,recordId);
//		String url = renameFileUrl;
//		log.info("获取token成功,开始请求文件重命名接口, url:{}, tenantCode:{},recordId:{}", url, tenantCode,recordId);
//		FormBody.Builder params = new FormBody.Builder();
//		params.add("nsid", nsid);
//		params.add("from_neid", fromNeid);
//		params.add("to_file_name", toFileName);
//		Request request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("文件重命名接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("文件重命名接口重试请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//			errorCode = String.valueOf(jsonObject.get("errcode"));
//		}
//		return errorCode;
//	}

//	@Override
//	public FilezFileDto createFolder(String tenantCode, String pathType, String path, String recordId) throws IOException, UnifiedException {
//		String token = getToken(tenantCode,recordId);
//		String url = createFolderUrl;
//		log.info("获取token成功,开始请求创建文件夹接口, url:{}, tenantCode:{}, recordId:{}, path:{}", url, tenantCode,recordId, path);
//		FormBody.Builder params = new FormBody.Builder();
//		params.add("path", path);
//		params.add("path_type", pathType);
//		Request request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//		Response response = mClient.newCall(request).execute();
//		JSONObject jsonObject = JSONObject.parseObject(getResponseString(response, recordId));
//		log.info("创建文件夹接口请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		String errorCode = String.valueOf(jsonObject.get("errcode"));
//		if (StringUtils.equalsAny(errorCode, ERR_401, ERR_403) || StrUtil.isBlank(errorCode)){
//			log.info("错误码401,鉴权失败,获取新token再请求一次, recordId:{}", recordId);
//			deleteToken(token);
//			token = getToken(tenantCode,recordId);
//			request = getTokenPostRequest(X_WWW_FORM, token, url, params.build());
//			Response response1 = mClient.newCall(request).execute();
//			jsonObject = JSONObject.parseObject(getResponseString(response1, recordId));
//			log.info("创建文件夹接口重试请求成功, 返回值:{}, tenantCode:{},recordId:{}", jsonObject.toJSONString(), token,recordId);
//		}
//		return jsonObject.toJavaObject(FilezFileDto.class);
//	}
}
