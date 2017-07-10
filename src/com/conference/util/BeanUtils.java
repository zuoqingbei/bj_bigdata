package com.conference.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/** 
* 类说明 ：javaBean之间值或者javaBean与jfinal的model之间的相互转换,相互复制
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年8月3日 下午2:05:32 
*/
public class BeanUtils {
	
	
	
	/** 
	 * @Desc:  把普通的javaBean里面的值转换为jfinal的model得值
	 * @param javaBean
	 * @param model
	 * @param isPutAll	是否把model表中没有的字段值也放进去,true都放,false不放
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年8月3日 下午2:18:26 
	 */
	public static void beanToModel(Object javaBean,Model model,Boolean isPutAll){
		try {
			Class<? extends Object> tClass =javaBean.getClass();
//			1.循环bean中的所有get方法或属性,
			Field[] fields = tClass.getDeclaredFields();
			for(Field field:fields){
//				2.获取bean中的属性名称
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
				//获得set方法
//				Method method = pd.getWriteMethod();
//				method.invoke(javaBean, new Object[]{"123"});
				//获得get方法
				Method get = pd.getReadMethod();
				Object getValue = get.invoke(javaBean, new Object[]{});
				String fieldName=field.getName();
//				3.如果bean中的属性名称在model的属性中存在,就set进去
				Table table = TableMapping.me().getTable(model.getClass());
				if(table.hasColumnLabel(fieldName)){
					model.set(fieldName, getValue);
				}else if(isPutAll){
//				4.如果不存在就put进去
					model.put(fieldName, getValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * @Desc: 把jsonObject对象转换为model
	 * @param jsonObj
	 * @param model
	 * @param isPutAll
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年8月10日 上午10:18:05 
	 */
	public static void jsonObjectToModel(JSONObject jsonObj,Model model,Boolean isPutAll){
		try {
			Set<Entry<String, Object>> sets = jsonObj.entrySet();
			for (Entry<String, Object> entry : sets) {
//				3.如果bean中的属性名称在model的属性中存在,就set进去
				Table table = TableMapping.me().getTable(model.getClass());
				if(table.hasColumnLabel(entry.getKey())){
					model.set(entry.getKey(), entry.getValue());
				}else if(isPutAll){
//				4.如果不存在就put进去
					model.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @Desc: 把jsonObject对象转换为model
	 * @param jsonObj
	 * @param model
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年8月10日 上午10:18:05 
	 */
	public static void jsonObjectToRecord(JSONObject jsonObj,Record model){
		try {
			Set<Entry<String, Object>> sets = jsonObj.entrySet();
			for (Entry<String, Object> entry : sets) {
				model.set(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * @Desc: map变成model
	 * @param map
	 * @param model
	 * @param isPutAll
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年9月23日 下午4:35:12 
	 */
	public static void mapToModel(Map<String , Object> map,Model model,Boolean isPutAll){
		try {
			Set<Entry<String, Object>> sets = map.entrySet();
			for (Entry<String, Object> entry : sets) {
//				3.如果bean中的属性名称在model的属性中存在,就set进去
				Table table = TableMapping.me().getTable(model.getClass());
				if(table.hasColumnLabel(entry.getKey())){
					model.set(entry.getKey(), entry.getValue());
				}else if(isPutAll){
//				4.如果不存在就put进去
					model.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**将menu sourcelist 根据父子 进行排序
	 * 1.选获取根ID下的子集
	 * 2.如果级联,就对每个子集下的子集,并放入childs属性里面
	 * @param list			排序好的列表
	 * @param sourcelist	数据源列表
	 * @param parentId		树的根ID
	 * @param cascade		是否级联
	 * @author longjunfeng
	 * @date   2016年4月19日上午10:15:45
	 */
	public static void sortList(List<Record>  list, List<Record>  sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			Record e = sourcelist.get(i);
			
			if (e.get("parent_id")!=null&& e.get("parent_id").toString().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						Record child = sourcelist.get(j);
						if (child.get("parent_id")!=null
								&&  child.get("parent_id").toString().equals(e.get("id").toString())){
							ArrayList<Record> childs = new ArrayList<Record>();
							sortList(childs, sourcelist, e.get("id").toString(), true);
							e.set("childs", childs);
							break;
						}
					}
				}
			}
		}
	}
	
	
	/** 
	 * @Desc:  nestable 插件从前台获取排序的数据,解析成带有idx排序的list record
	 * @param returnList	返回的列表
	 * @param dataList		.nestable('serialize')的格式数据
	 * @param sort			sort值,从多少开始
	 * @param parentId		父Id的值
	 * @return
	 * @return: List<Record>
	 * @author: longjunfeng   
	 * @date: 2016年11月15日 下午4:24:53 
	 */
	public static List<Record> getSort(List<Record> returnList,List<Map<String, Object>> dataList,int sort,String parentId){
		for (int i = 0; i < dataList.size(); i++) {
			Record oneRec=new Record();
			Map<String, Object> one = dataList.get(i);
			oneRec.set("id", one.get("id"));
			oneRec.set("idx",sort);
			oneRec.set("parent_id",parentId);
			returnList.add(oneRec);
			sort++;
			if(one.get("children")!=null){
				List<Map<String, Object>> childrens =(List<Map<String, Object>>) one.get("children");
				getSort(returnList, childrens, sort,one.get("id").toString());
			}
		}
		return returnList;
	}
	
	
	/** 
	 * @Desc: 将数组中的项转换为以逗号隔开的字符串
	 * @param models
	 * @param column
	 * @return
	 * @return: String
	 * @author: longjunfeng   
	 * @date: 2016年11月28日 下午1:48:20 
	 */
	public static <T extends Model> String joinColumWithQ(List<T> models,String column){
		if(models==null||models.size()==0){
			return "";
		}
		StringBuffer result=new StringBuffer();
		for (int i = 0; i < models.size(); i++) {
			result.append(models.get(i).getStr(column));
			if(i!=models.size()-1){
				result.append(",");
			}
		}
		return result.toString();
	}
	
	/*public static void main(String[] args) {
		AccountSocialBean userInfo =new AccountSocialBean();
		AccountSocial accountSocial=new AccountSocial();
		BeanUtils.beanToModel(userInfo, accountSocial, true);
	}*/
	

}
