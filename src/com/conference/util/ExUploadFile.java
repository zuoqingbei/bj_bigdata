/**
 * 
 */
package com.conference.util;

import java.io.File;

/**
 * 
 * @author yongtree
 */
public class ExUploadFile {

	/**
	 * @fieldName: suffix
	 * @fieldType: String
	 * @Description: 文件后缀
	 */
	private String suffix;

	/**
	 * @fieldName: path
	 * @fieldType: String
	 * @Description: 文件云路径
	 */
	private String path;

	/**
	 * @fieldName: fileName
	 * @fieldType: String
	 * @Description: 文件存放的名称
	 */
	private String fileName;

	/**
	 * @fieldName: originalFileName
	 * @fieldType: String
	 * @Description: 文件原始名称
	 */
	private String originalFileName;

	/**
	 * @fieldName: saveDirectory
	 * @fieldType: String
	 * @Description: 文件本地存放文件夹
	 */
	private String saveDirectory;

	/**
	 * @fieldName: file
	 * @fieldType: File
	 * @Description: 上传文件
	 */
	private File file;
	
	
	/**
	 * @fieldName: isCloud
	 * @fieldType: boolean
	 * @Description: 是否上传到了云端
	 */
	private boolean isCloud=true;
	
	/**
	 * @fieldName: cloudType
	 * @fieldType: boolean
	 * @Description: 云存储类型  （七牛、腾讯云）
	 */
	private String cloudType;
	
	/**
	 * @fieldName: cms_content_file_id
	 * @fieldType: String
	 * @Description: 内容模块-内容附件表的ID
	 */
	private String cms_content_file_id;
	
	
	
	

	public ExUploadFile(String suffix, String path, String fileName,
			String originalFileName, String saveDirectory, File file) {
		super();
		this.suffix = suffix;
		this.path = path;
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.saveDirectory = saveDirectory;
		this.file = file;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public String getSaveDirectory() {
		return saveDirectory;
	}

	public File getFile() {
		return file;
	}

	public void delete() {
		file.delete();
	}

	public boolean isCloud() {
		return isCloud;
	}

	public void setCloud(boolean isCloud) {
		this.isCloud = isCloud;
	}

	public String getCloudType() {
		return cloudType;
	}

	public void setCloudType(String cloudType) {
		this.cloudType = cloudType;
	}

	public String getCms_content_file_id() {
		return cms_content_file_id;
	}

	public void setCms_content_file_id(String cms_content_file_id) {
		this.cms_content_file_id = cms_content_file_id;
	}
	
	

}
