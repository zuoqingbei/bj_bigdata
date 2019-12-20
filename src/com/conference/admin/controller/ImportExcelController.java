package com.conference.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

import com.conference.admin.model.BigDataDRef;
import com.conference.admin.model.Creator;
import com.conference.admin.model.Dept;
import com.conference.admin.model.FctEval;
import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.FctOriginImport;
import com.conference.admin.model.FctZb;
import com.conference.admin.model.FctZbImport;
import com.conference.admin.model.RZbOrigin;
import com.conference.common.BaseController;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
public class ImportExcelController extends BaseController {
	public void downOrgTemp() {
		String str = new SimpleDateFormat("yyyy-MM-ddHHmmss").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "素材导入模板(" + time + ").xls";
        //excel模板路径  
        File fi = new File(PathKit.getWebRootPath() +File.separator+"bigdata"+ File.separator + "tpl" + File.separator + "素材导入模板.xls");
        renderFile(fi);
    }
	public void downFctZbTemp() {
		String str = new SimpleDateFormat("yyyy-MM-ddHHmmss").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "新闻导入模板(" + time + ").xls";
        //excel模板路径  
        File fi = new File(PathKit.getWebRootPath() +File.separator+"bigdata"+ File.separator + "tpl" + File.separator + "新闻导入模板.xls");
        renderFile(fi);
    }
	/**
	 * 原始素材导入
	 */
	public void importFctOrgin() {
		UploadFile  up= getFile("file");
		File file=up.getFile();
		Map<String,Object> map=new HashMap<String, Object>();
	    ImportParams importParams = new ImportParams();
	    // 因为Excel是从第3行开始才有正式数据,所以我们这里从第2行开始读取数据
	    importParams.setHeadRows(1);
	    // 这里是做非空验证,DTO中的数组必须不为空
	    importParams.setImportFields(FctOriginImport.MUST_FILL);

	    // 获取模板
	    ExcelImportResult<FctOriginImport> data=new ExcelImportResult<FctOriginImport>();
	    try {
	        data = ExcelImportUtil.importExcelMore(new FileInputStream(file), FctOriginImport.class, importParams);
	    } catch (Exception e) {
	        map.put("success", false);
	        map.put("msg", "解析异常");
	        renderJson(map);
	    }

	    // 获取模板数据
	    List<FctOriginImport> successList = data.getList();
	    int total = successList.size();
	    int successNum=0;
	    if (total == 0) {
	   		// 如果数据量为0直接返回错误信息,不继续执行
	    	 map.put("success", false);
		     map.put("msg", "暂无数据");
		     renderJson(map);
	    }else{
	    	List<Dept> companys=Dept.dao.getAllCompanys();//大单位
	    	List<Dept> depts=Dept.dao.getAllDepts();
	    	List<Creator> creators=Creator.dao.findAll();
	    	// 循环数据
	    	for (FctOriginImport excelDTO : successList) {
	    		// 取数据方式
	    		FctOrigin org=new FctOrigin();
	    		org.put("name", excelDTO.getName());
	    		org.put("created_time", new Date());
	    		//处理大单位
	    		String company=excelDTO.getCompany();
	    		Map<String,Object> m1=checkContainCompany(companys, company);
	    		org.put("from_dept", m1.get("id"));
	    		companys=(List<Dept>) m1.get("companys");
	    		
	    		//处理采集部门
	    		String dept=excelDTO.getDept();
	    		Map<String,Object> m2=checkContainDept(depts, dept,m1.get("id")+"");
	    		org.put("dept_id", m2.get("id"));
	    		depts=(List<Dept>) m2.get("depts");
	    		
	    		//处理发稿人
	    		String creator=excelDTO.getCreator();
	    		Map<String,Object> m3=checkContainCreator(creators, creator);
	    		org.put("creator_id", m3.get("id"));
	    		creators=(List<Creator>) m3.get("creators");
	    		
	    		//前方记者
	    		String sourceCode=excelDTO.getSourceCode();
	    		Map<String,Object> m4=checkContainCreator(creators, sourceCode);
	    		org.put("sourceCode", m4.get("id"));
	    		creators=(List<Creator>) m4.get("creators");
	    		org.save();
	    		successNum++;
	    	}
	    	// 看情况返回信息
	    	map.put("success", true);
		    map.put("msg", "导入成功，总计"+total+"条，成功"+successNum+"条");
	    	renderJson(map);
	    	
	    }
	    
	}
	
	/**
	 * 新闻导入
	 */
	public void importFctZb() {
		UploadFile  up= getFile("file");
		File file=up.getFile();
		Map<String,Object> map=new HashMap<String, Object>();
	    ImportParams importParams = new ImportParams();
	    // 因为Excel是从第3行开始才有正式数据,所以我们这里从第2行开始读取数据
	    importParams.setHeadRows(1);
	    // 这里是做非空验证,DTO中的数组必须不为空
	    importParams.setImportFields(FctZbImport.MUST_FILL);

	    // 获取模板
	    ExcelImportResult<FctZbImport> data=new ExcelImportResult<FctZbImport>();
	    try {
	        data = ExcelImportUtil.importExcelMore(new FileInputStream(file), FctZbImport.class, importParams);
	    } catch (Exception e) {
	        map.put("success", false);
	        map.put("msg", "解析异常");
	        renderJson(map);
	    }

	    // 获取模板数据
	    List<FctZbImport> successList = data.getList();
	    int total = successList.size();
	    int successNum=0;
	    if (total == 0) {
	   		// 如果数据量为0直接返回错误信息,不继续执行
	    	 map.put("success", false);
		     map.put("msg", "暂无数据");
		     renderJson(map);
	    }else{
	    	List<Dept> companys=Dept.dao.getAllCompanys();//大单位
	    	List<Dept> depts=Dept.dao.getAllDepts();
	    	List<Creator> creators=Creator.dao.findAll();
	    	List<BigDataDRef> refs=BigDataDRef.dao.getBigDataDRefByType("方向");
	    	List<BigDataDRef> pros=BigDataDRef.dao.getBigDataDRefByType("性质");
	    	List<BigDataDRef> fs=BigDataDRef.dao.getBigDataDRefByType("使用方式");
	    	List<BigDataDRef> levels=BigDataDRef.dao.getBigDataDRefByType("评价级别");
	    	// 循环数据
	    	for (FctZbImport excelDTO : successList) {
	    		String firstDept="";
	    		// 取数据方式
	    		Date now=new Date();
	    		FctZb org=new FctZb();
	    		org.put("zb_name", excelDTO.getName());
	    		org.put("issue_number", now.getTime());
	    		org.put("created_time", excelDTO.getTime());
	    		//处理大单位
	    		String company=excelDTO.getCompany();
	    		Map<String,Object> m1=checkContainCompany(companys, company);
	    		companys=(List<Dept>) m1.get("companys");
	    		
	    		//处理采集部门
	    		String dept=excelDTO.getDept();
	    		if(StringUtils.isNotBlank(dept)){
	    			String deptids=",";
	    			for(String d:dept.split(";")){
	    				Map<String,Object> m2=checkContainDept(depts, d,m1.get("id")+"");
	    				deptids+=m2.get("id")+",";
	    				if(StringUtils.isBlank(firstDept))
	    				firstDept=m2.get("id")+"";
	    				depts=(List<Dept>) m2.get("depts");
	    			}
	    			org.put("source_dept_ids", deptids);
	    		}
	    		
	    		//处理发稿人
	    		String creator=excelDTO.getCreator();
	    		if(StringUtils.isNotBlank(creator)){
	    			String creids=",";
	    			for(String d:creator.split(";")){
	    				Map<String,Object> m3=checkContainCreator(creators, d);
	    				creids+=m3.get("id")+",";
	    	    		creators=(List<Creator>) m3.get("creators");
	    			}
	    			org.put("creator_ids", creids);
	    		}
	    		
	    		//前方记者
	    		String sourceCode=excelDTO.getSourceCode();
	    		Map<String,Object> m4=checkContainCreator(creators, sourceCode);
	    		org.put("source_code", m4.get("id"));
	    		creators=(List<Creator>) m4.get("creators");
	    		
	    		//方向
	    		Map<String,Object> m5=checkRefs(refs, excelDTO.getDirectionIds(),"方向");
	    		org.put("direction_ids",excelDTO.getDirectionIds());
	    		refs=(List<BigDataDRef>) m5.get("refs");
	    		
	    		//性质
	    		Map<String,Object> m6=checkRefs(pros, excelDTO.getPropertyIds(),"性质");
	    		org.put("property_ids",excelDTO.getPropertyIds());
	    		pros=(List<BigDataDRef>) m6.get("refs");
	    		org.put("load_time",now);
	    		org.save();
	    		//原始素材编码
	    		RZbOrigin rZbOrg=new RZbOrigin();
	    		rZbOrg.put("zb_id", org.get("zb_id"));
	    		rZbOrg.put("origin_id", excelDTO.getOrgIds());
	    		rZbOrg.save();
	    		//保存评价
	    		FctEval eval=new FctEval();
	    		eval.put("zb_id", org.get("zb_id"));
	    		eval.put("origin_id", excelDTO.getOrgIds());
	    		//Map<String,Object> mm=checkContainDept(depts, excelDTO.getCompany(),m1.get("id")+"");
	    		eval.put("office_id", excelDTO.getDept());
	    		//depts=(List<Dept>) mm.get("depts");
	    		if("有".equals(excelDTO.getPishi())){
	    			eval.put("have_pishi", 1);
	    		}else{
	    			eval.put("have_pishi", 0);
	    		}
	    		//使用方式
	    		Map<String,Object> m7=checkRefs(fs, excelDTO.getUseType(),"使用方式");
	    		eval.put("use_type_id",excelDTO.getUseType());
	    		fs=(List<BigDataDRef>) m7.get("refs");
	    		//评价级别
	    		Map<String,Object> m8=checkRefs(levels, excelDTO.getLevel(),"评价级别");
	    		eval.put("use_level_id",excelDTO.getLevel());
	    		levels=(List<BigDataDRef>) m8.get("refs");
	    		eval.save();
	    		successNum++;
	    	}
	    	// 看情况返回信息
	    	map.put("success", true);
		    map.put("msg", "导入成功，总计"+total+"条，成功"+successNum+"条");
	    	renderJson(map);
	    	
	    }
	    
	}
	//处理方向、性质
	public Map<String,Object> checkRefs(List<BigDataDRef> refs,String name,String type){
		Map<String,Object> map=new HashMap<String, Object>();
		boolean contain=false;
		String id="";
		map.put("id", id);
		map.put("refs", refs);
		if(StringUtils.isBlank(name)){
			return map;
		}
		for(BigDataDRef c:refs){
			if(name.equals(c.get("name")+"")){
				contain=true;
				id=c.get("id")+"";
				break;
			}
		}
		if(!contain){
			BigDataDRef d=new BigDataDRef();
			d.put("name", name);
			d.put("type", type);
			d.save();
			id=d.get("id")+"";
			refs.add(d);
		}
		map.put("id", id);
		map.put("refs", refs);
		return map;
	}
	//处理发稿人、前方记者
	public Map<String,Object> checkContainCreator(List<Creator> creators,String name){
		Map<String,Object> map=new HashMap<String, Object>();
		boolean contain=false;
		String id="";
		map.put("id", id);
		map.put("creators", creators);
		if(StringUtils.isBlank(name)){
			return map;
		}
		for(Creator c:creators){
			if(name.equals(c.get("name")+"")){
				contain=true;
				id=c.get("id")+"";
				break;
			}
		}
		if(!contain){
			Creator d=new Creator();
			d.put("name", name);
			d.save();
			id=d.get("id")+"";
			creators.add(d);
		}
		map.put("id", id);
		map.put("creators", creators);
		return map;
	}
	//处理采集部门
	public Map<String,Object> checkContainDept(List<Dept> depts,String name,String pid){
		Map<String,Object> map=new HashMap<String, Object>();
		boolean contain=false;
		String id="";
		map.put("id", id);
		map.put("depts", depts);
		if(StringUtils.isBlank(name)){
			return map;
		}
		for(Dept c:depts){
			if(name.equals(c.get("name")+"")){
				contain=true;
				id=c.get("id")+"";
				break;
			}
		}
		if(!contain){
			Dept d=new Dept();
			d.put("name", name);
			d.put("pid", pid);
			d.save();
			id=d.get("id")+"";
			depts.add(d);
		}
		map.put("id", id);
		map.put("depts", depts);
		return map;
	}
	//处理大单位
	public Map<String,Object> checkContainCompany(List<Dept> companys,String name){
		Map<String,Object> map=new HashMap<String, Object>();
		boolean contain=false;
		String id="";
		map.put("id", id);
		map.put("companys", companys);
		if(StringUtils.isBlank(name)){
			return map;
		}
		for(Dept c:companys){
			if(name.equals(c.get("name")+"")){
				contain=true;
				id=c.get("id")+"";
				break;
			}
		}
		if(!contain){
			Dept d=new Dept();
			d.put("name", name);
			d.put("pid", "0");
			d.save();
			id=d.get("id")+"";
			companys.add(d);
		}
		map.put("id", id);
		map.put("companys", companys);
		return map;
	}
}
