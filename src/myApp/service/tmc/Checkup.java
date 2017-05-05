package myApp.service.tmc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import myApp.client.tmc.model.CheckupModel;
import myApp.frame.service.ServiceRequest;
import myApp.frame.service.ServiceResult;
import myApp.frame.ui.AbstractDataModel;
import myApp.server.data.UpdateDataModel;

public class Checkup {

	private String mapperName  = "tmc03_checkup"; 
	
	public void selectById(SqlSession sqlSession, ServiceRequest request, ServiceResult result){
		Long selectId = request.getLong("checkupId");
		List<AbstractDataModel> list = sqlSession.selectList(mapperName + ".selectById", selectId);
		result.setRetrieveResult(1, "select ok", list);
	}
	
	public void selectByRequestId(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		Long requestId = request.getLong("requestId"); 
		
		System.out.println("requestId is " + requestId); 
		
		List<AbstractDataModel> list = sqlSession.selectList(mapperName + ".selectByRequestId", requestId);
		result.setRetrieveResult(1, "select ok", list);
	}
	
	public void update(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		
		Long userId = request.getLong("userId");
		System.out.println("login id si " + userId); 
		
		List<AbstractDataModel> list = new ArrayList<AbstractDataModel>();  
		
		for(AbstractDataModel data : request.getList()){
			CheckupModel checkupModel = (CheckupModel)data; 
			checkupModel.setCheckupDate(new Date());
			checkupModel.setCheckupUserId(userId);
		
			System.out.println("checkup user id is " + checkupModel.getCheckupUserId()); 
			
			list.add(checkupModel); 
		}
		
		UpdateDataModel<AbstractDataModel> updateModel = new UpdateDataModel<AbstractDataModel>(); 
		updateModel.updateModel(sqlSession, list, mapperName, result);
	}

	public void selectByCheckupList(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		Long companyId = request.getLong("companyId"); 
		Date startDate = request.getDate("startDate"); 
		Date endDate = request.getDate("endDate"); 
		String patientName = request.getString("patientName");
		patientName = "%" + patientName + "%";
		
		System.out.println("company id is "+companyId); 
		System.out.println("start date is " + startDate);
		System.out.println("end date is " + endDate);
		System.out.println("epatientName " + patientName);
		
		Map<String, Object> param = new HashMap<String, Object>(); 
		param.put("companyId", companyId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("patientName", patientName); 
		List<AbstractDataModel> list = sqlSession.selectList(mapperName + ".selectByCheckupList", param);
		
		System.out.println("list count is "+ list.size()); 
		
		result.setRetrieveResult(1, "select ok", list);
	}
	
	public void delete(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		UpdateDataModel<AbstractDataModel> updateModel = new UpdateDataModel<AbstractDataModel>(); 
		updateModel.deleteModel(sqlSession, request.getList(), mapperName, result);
	}
}
