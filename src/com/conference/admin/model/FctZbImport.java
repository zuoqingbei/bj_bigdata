package com.conference.admin.model;

import org.hibernate.validator.constraints.NotBlank;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class FctZbImport {
	@Excel(name = "新闻标题")
    @NotBlank(message = "新闻标题不能为空")
    private String name;

    @Excel(name = "发稿人")
    @NotBlank(message = "发稿人不能为空")
    private String creator;
    @Excel(name = "大单位")
    @NotBlank(message = "大单位不能为空")
    private String company;
    @Excel(name = "采集部门")
    @NotBlank(message = "采集部门不能为空")
    private String dept;
    @Excel(name = "前方记者")
    @NotBlank(message = "前方记者不能为空")
    private String sourceCode;
    
    @Excel(name = "性质")
    @NotBlank(message = "性质不能为空")
    private String propertyIds;
    @Excel(name = "方向")
    @NotBlank(message = "方向不能为空")
    private String directionIds;
    @Excel(name = "原始素材编码")
    @NotBlank(message = "原始素材编码不能为空")
    private String orgIds;
    @Excel(name = "时间")
    private String time;
    
    @Excel(name = "使用方式")
    private String useType;
    @Excel(name = "评价级别")
    private String level;
    @Excel(name = "是否有批示")//（0代表无，1代表有）
    private String pishi;
    public static final String[] MUST_FILL = new String[]{"新闻标题", "发稿人", "采集部门", "前方记者","原始素材编码","性质","方向"};
	public String getName() {
		return name;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPishi() {
		return pishi;
	}
	public void setPishi(String pishi) {
		this.pishi = pishi;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDirectionIds() {
		return directionIds;
	}
	public void setDirectionIds(String directionIds) {
		this.directionIds = directionIds;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPropertyIds() {
		return propertyIds;
	}
	public void setPropertyIds(String propertyIds) {
		this.propertyIds = propertyIds;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
    
}
