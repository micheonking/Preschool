package myApp.server.sys;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import myApp.client.sys.model.UserCompanyModel;
import myApp.frame.service.ServiceRequest;
import myApp.frame.service.ServiceResult;
import myApp.frame.ui.AbstractDataModel;
import myApp.service.data.UpdateDataModel;

public class UserCompany { 
	
	String mapperName = "sys07_user_company"; 
	
	public void selectByUserId(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		Long userId = request.getLong("userId"); 
		List<AbstractDataModel> userCompanyList = sqlSession.selectList(mapperName + ".selectByUserId", userId) ;
		result.setRetrieveResult(1, "사용자별 고객권한 조회", userCompanyList);
	}
	public void update(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		UpdateDataModel<UserCompanyModel> updateModel = new UpdateDataModel<UserCompanyModel>(); 
		updateModel.updateModel(sqlSession, request.getList(), mapperName, result);
	}
	public void delete(SqlSession sqlSession, ServiceRequest request, ServiceResult result) {
		UpdateDataModel<UserCompanyModel> updateModel = new UpdateDataModel<UserCompanyModel>(); 
		updateModel.deleteModel(sqlSession, request.getList(), mapperName, result);
	}
}