package com.utfinancing.financehub.engine.file.service;

import com.utfinancing.financehub.engine.file.common.UnifiedException;

import java.io.IOException;
import java.io.InputStream;

public interface FilezService {

	/**
	 * 下载文件
	 * @param neid
	 * @param nsid
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
	InputStream downloadFile(Long neid, Integer nsid, String recordId) throws IOException, UnifiedException;

	/**
	 * 删除文件
	 * @param tenantCode
	 * @param fileNeid
	 * @param fileNsid
	 * @param recordId
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	void deleteFile(String tenantCode, Long fileNeid, Integer fileNsid, String recordId) throws IOException, UnifiedException;


	/**
	 * 上传附件
	 * @param tenantCode
	 * @param file
	 * @param fileName
	 * @param pathType
	 * @param path
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	FilezUploadVo uploadFile2Filez(String tenantCode, MultipartFile file, String fileName, String pathType, String path, String recordId) throws IOException, UnifiedException;

	/**
	 * 上传附件
	 * @param tenantCode
	 * @param file
	 * @param fileName
	 * @param pathType
	 * @param path
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	FilezUploadVo uploadFile2Filez(String tenantCode, File file, String fileName, String pathType, String path, String recordId) throws IOException, UnifiedException;

	/**
	 * 获取附件外链的url
	 * @param tenantCode
	 * @param nsid
	 * @param neid
	 * @param recordId
	 * @return
	 * @throws UnifiedException
	 * @throws IOException
	 */
//	String deliveryFile(String tenantCode, String nsid, String neid, String recordId) throws UnifiedException, IOException;


	/**
	 * 获取附件预览的url
	 * @param tenantCode
	 * @param neid
	 * @param nsid
	 * @param thumbtail
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	String getPreviewUrl(String tenantCode, Long neid, Integer nsid, boolean thumbtail, String recordId) throws IOException, UnifiedException;

	/**
	 * 批量获取附件的预览url
	 * @param tenantCode
	 * @param reqDtoList
	 * @param thumbtail
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	List<BatchPreviewUrlDto> getBatchPreviewUrl(String tenantCode, List<AttachmentPreviewReqDto> reqDtoList, boolean thumbtail, String recordId) throws IOException, UnifiedException;




	/**
	 * 获取附件在线编辑url
	 * @param tenantCode
	 * @param neid
	 * @param nsid
	 * @param path
	 * @param pathType
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	String getEditUrl(String tenantCode, Long neid, Integer nsid, String path, String pathType, String recordId) throws IOException, UnifiedException;

	/**
	 *
	 * @author ex-dingjie
	 * @date 2023/12/6
	 * @desc 根据文件路径批量获取文件列表
	 * @param tenantCode
	 * @param filePath
	 * @param pageNo
	 * @param pageSize
	 * @param recordId
	 * @return
	 * @throws IOException
	 *
	 */
//	BatchGetByFilePathVo getFileListByPath(String tenantCode, String filePath, Integer pageNo, Integer pageSize, String recordId) throws IOException, UnifiedException;

	/**
	 * 获取文件信息
	 *
	 * @param tenantCode 租户id
	 * @param filePath 文件路径
	 * @param recordId 记录id
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	FilezFileDto getFileByPath(String tenantCode, String filePath, String recordId) throws IOException, UnifiedException;

	/**
	 *
	 * @author ex-dingjie
	 * @date 2023/12/6
	 * @desc 根据文件id与命名空间id移动文件到指定目录
	 * @param tenantCode
	 * @param filePath
	 * @param nsid
	 * @param fromNeid
	 * @param recordId
	 * @return
	 * @throws IOException
	 *
	 */
//	String moveFileByNsidAndNeid(String tenantCode, String nsid, String fromNeid, String filePath, String pathType, String recordId) throws UnifiedException, IOException;

	/**
	 * 通过文件id和命名空间id获取文件信息
	 * @param tenantCode
	 * @param neid
	 * @param nsid
	 * @param recordId
	 * @return
	 * @throws UnifiedException
	 * @throws IOException
	 */
//	FilezFileDto getFileByNeidAndNsid(String tenantCode, String neid, String nsid, String recordId) throws UnifiedException, IOException;

	/**
	 * 文件重命名
	 * @param tenantCode
	 * @param nsid
	 * @param fromNeid
	 * @param toFileName
	 * @param recordId
	 * @return
	 */
//	String fileRename(String tenantCode, String nsid, String fromNeid, String toFileName, String recordId) throws UnifiedException, IOException;

	/**
	 * 创建文件夹
	 * @param tenantCode
	 * @param pathType
	 * @param path
	 * @param recordId
	 * @return
	 * @throws IOException
	 * @throws UnifiedException
	 */
//	FilezFileDto createFolder(String tenantCode, String pathType, String path, String recordId) throws IOException, UnifiedException;
}
