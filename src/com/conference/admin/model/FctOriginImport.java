package com.conference.admin.model;

import org.hibernate.validator.constraints.NotBlank;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class FctOriginImport {
	@Excel(name = "素材标题")
    @NotBlank(message = "素材标题不能为空")
    private String name;

    @Excel(name = "发稿人")
    @NotBlank(message = "发稿人不能为空")
    private String creator;
    @Excel(name = "采集部门")
    @NotBlank(message = "采集部门不能为空")
    private String dept;
    @Excel(name = "大单位")
    @NotBlank(message = "大单位不能为空")
    private String company;
    @Excel(name = "前方记者")
    @NotBlank(message = "前方记者不能为空")
    private String sourceCode;
    public static final String[] MUST_FILL = new String[]{"素材标题", "发稿人", "采集部门", "大单位", "前方记者"};
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
    
}
