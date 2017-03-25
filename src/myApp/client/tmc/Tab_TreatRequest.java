package myApp.client.tmc;

import java.util.Date;
import java.util.List;

import myApp.client.sys.Lookup_Company;
import myApp.client.sys.model.CompanyModel;
import myApp.client.sys.model.UserModel;
import myApp.client.tmc.model.PatientModel;
import myApp.client.tmc.model.RequestModel;
import myApp.client.tmc.model.RequestModelProperties;
import myApp.frame.LoginUser;
import myApp.frame.service.GridDeleteData;
import myApp.frame.service.GridInsertRow;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.service.GridUpdateData;
import myApp.frame.service.InterfaceCallback;
import myApp.frame.ui.InterfaceLookupResult;
import myApp.frame.ui.SimpleMessage;
import myApp.frame.ui.builder.GridBuilder;
import myApp.frame.ui.builder.InterfaceGridOperate;
import myApp.frame.ui.builder.SearchBarBuilder;
import myApp.frame.ui.field.LookupTriggerField;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

/*
 * 전문의 처방 등록
 * 검사오더와 처방내역만 등록이 가능하다.  
 */

public class Tab_TreatRequest extends BorderLayoutContainer implements InterfaceGridOperate {
	
	private RequestModelProperties properties = GWT.create(RequestModelProperties.class);
	private Grid<RequestModel> grid = this.buildGrid();
	private Grid<RequestModel> gridHistory = this.buildGridHistory();
	private TextField patientNameField = new TextField();
	private CompanyModel companyModel = LoginUser.getLoginUser().getCompanyModel(); 
	
	public Tab_TreatRequest() {

		final LookupTriggerField lookupCompanyField = new LookupTriggerField(); 
		//final Lookup_Company lookupCompany = new Lookup_Company();
		
		lookupCompanyField.setEditable(false);
		lookupCompanyField.addTriggerClickHandler(new TriggerClickHandler(){
   	 		@Override
			public void onTriggerClick(TriggerClickEvent event) {
   	 			Lookup_Company lookupCompany = new Lookup_Company();
   	 			lookupCompany.setCallback(new InterfaceLookupResult(){
	   				@Override
	   				public void setLookupResult(Object result) {
	   					companyModel = (CompanyModel)result;
	   					lookupCompanyField.setText(companyModel.getCompanyName());
	   				}
	   			});	
   	 			lookupCompany.show();
			}
   	 	}); 

		this.companyModel = LoginUser.getLoginUser().getCompanyModel(); 
		lookupCompanyField.setText(companyModel.getCompanyName());
		
		SearchBarBuilder searchBarBuilder = new SearchBarBuilder(this);
		searchBarBuilder.addLookupTriggerField(lookupCompanyField, "기관명", 250, 48);
		searchBarBuilder.addTextField(patientNameField, "환자명", 150, 46, true); 
		searchBarBuilder.addRetrieveButton(); 
		searchBarBuilder.addUpdateButton();
		searchBarBuilder.addInsertButton();
		searchBarBuilder.addDeleteButton();

		VerticalLayoutContainer vlc = new VerticalLayoutContainer(); 
		vlc.add(searchBarBuilder.getSearchBar(), new VerticalLayoutData(1, 48));
		vlc.add(grid, new VerticalLayoutData(1, 1));
		
		grid.addRowClickHandler(new RowClickHandler(){
			@Override
			public void onRowClick(RowClickEvent event) {

				RequestModel requestModel = grid.getSelectionModel().getSelectedItem(); 
				if(requestModel != null){
					retrieveHistory(requestModel); 
				}
			}
		}); 

		gridHistory.addRowClickHandler(new RowClickHandler(){
			@Override
			public void onRowClick(RowClickEvent event) {

//				RequestModel requestModel = gridHistory.getSelectionModel().getSelectedItem(); 
//				editRequestModel(requestModel); 
			}
		}); 
		
		BorderLayoutData northLayoutData = new BorderLayoutData(300);
		northLayoutData.setMargins(new Margins(2,0,0,0));
		northLayoutData.setSplit(true);
		northLayoutData.setMaxSize(1000);
		this.setNorthWidget(vlc, northLayoutData); 

		BorderLayoutData westLayoutData = new BorderLayoutData(400);
		westLayoutData.setMargins(new Margins(2,0,0,0));
		westLayoutData.setSplit(true);
		westLayoutData.setMaxSize(1000);
		this.setWestWidget(this.gridHistory, westLayoutData);

		BorderLayoutData centerLayoutData = new BorderLayoutData();
		centerLayoutData.setMargins(new Margins(2,2,0,2));
		centerLayoutData.setMaxSize(1000);
//		this.setCenterWidget(pageTreat, centerLayoutData);
	}
	
	public Grid<RequestModel> buildGrid(){
		
		LookupTriggerField lookupPatientField = new LookupTriggerField(); // 환자 찾기
		lookupPatientField.addTriggerClickHandler(new TriggerClickHandler(){
			@Override
			public void onTriggerClick(TriggerClickEvent event) {

				Lookup_Patient lookupPatient = new Lookup_Patient(companyModel);
				lookupPatient.setCallback(new InterfaceLookupResult(){
					@Override
					public void setLookupResult(Object result) {
						RequestModel data = grid.getSelectionModel().getSelectedItem();
						PatientModel patientModel = (PatientModel)result;
						grid.getStore().getRecord(data).addChange(properties.patientId(), patientModel.getPatientId());
						grid.getStore().getRecord(data).addChange(properties.patientKorName(), patientModel.getKorName());
						grid.getStore().getRecord(data).addChange(properties.insNo(), patientModel.getInsNo());
					}
				});

				lookupPatient.show();
			}
		}); 
		
		LookupTriggerField lookupRequestUserField = new LookupTriggerField();
		lookupRequestUserField.addTriggerClickHandler(new TriggerClickHandler(){
			@Override
			public void onTriggerClick(TriggerClickEvent event) {
				
				if(companyModel == null){
					new SimpleMessage("선택된 기관명이 없습니다."); 
					return ; 
				}

				Lookup_RequestUser lookupRequestUser= new Lookup_RequestUser(); // 선택된 기관정보를 넘겨준다.
				lookupRequestUser.setCallback(new InterfaceLookupResult(){
					@Override
					public void setLookupResult(Object result) {
						RequestModel data = grid.getSelectionModel().getSelectedItem();
						UserModel userModel = (UserModel)result; 
						grid.getStore().getRecord(data).addChange(properties.requestUserId(), userModel.getUserId());
						grid.getStore().getRecord(data).addChange(properties.korName(), userModel.getKorName());
					}
				});
				
				lookupRequestUser.show(); 
			}
		}); 
		
		GridBuilder<RequestModel> gridBuilder = new GridBuilder<RequestModel>(properties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);

		gridBuilder.addDate(properties.requestDate(), 100, "진료예정일", new DateField());
		gridBuilder.addText(properties.insNo(), 100, "보험번호"); //, new TextField()) ;
		gridBuilder.addText(properties.patientKorName(), 80, "환자명", lookupPatientField) ;
		
		gridBuilder.addText(properties.korName(), 100, "보건의", lookupRequestUserField);
		gridBuilder.addText(properties.requestNote(), 200, "진료요청내용", new TextField()) ;
		gridBuilder.addDate(properties.treatDate(), 100, "진료일"); //, new DateField());
		gridBuilder.addText(properties.treatKorName(), 80, "진료의"); //, lookUpTreatUserField) ;
		gridBuilder.addText(properties.treatNote(), 200, "처방내역"); //, new TextField()) ;
		gridBuilder.addText(properties.note(), 400, "특기사항", new TextField()) ;		
		gridBuilder.addText(properties.regKorName(), 80, "등록자"); //, new TextField()) ;
		gridBuilder.addDate(properties.regDate(), 100, "등록일"); //, new DateField()) ;
		return gridBuilder.getGrid(); 
	}

	public Grid<RequestModel> buildGridHistory(){

		GridBuilder<RequestModel> gridBuilder = new GridBuilder<RequestModel>(properties.keyId());  
		// gridBuilder.setChecked(null);
		gridBuilder.addDate(properties.treatDate(), 	100, "진료일"); //, new DateField());
		gridBuilder.addText(properties.treatKorName(), 	80, "진료의"); //, lookUpTreatUserField) ;
		gridBuilder.addDate(properties.requestDate(), 	100, "진료예정일");
		gridBuilder.addText(properties.korName(), 		80, "보건의");
		
		return gridBuilder.getGrid(); 
	}
	
//	private void editRequestModel(RequestModel requestModel){
//		this.pageTreat.retrieve(requestModel);
//	}
	
	private void retrieveHistory(RequestModel requestModel){
		GridRetrieveData<RequestModel> service = new GridRetrieveData<RequestModel>(gridHistory.getStore());
		service.addCallback(new InterfaceCallback(){
			@Override
			public void callback() {
				gridHistory.getSelectionModel().select(0, false);
//				editRequestModel(gridHistory.getSelectionModel().getSelectedItem()); 
			}
		});

		service.addParam("patientId", requestModel.getPatientId());
		service.retrieve("tmc.Request.selectByPatientId");
	}
	
	@Override
	public void retrieve() {
		
		if(this.companyModel.getCompanyId() == null){
			new SimpleMessage("기관명 확인", "조회조건의 기관명은 반드시 입력하세요. ");
			return ; 
		} 
		
		GridRetrieveData<RequestModel> service = new GridRetrieveData<RequestModel>(grid.getStore());
		service.addParam("companyId", this.companyModel.getCompanyId());
		service.addParam("patientName", patientNameField.getText());
		service.retrieve("tmc.Request.selectByCompanyId");
	}
	
	@Override
	public void update(){
		GridUpdateData<RequestModel> service = new GridUpdateData<RequestModel>(); 
		service.update(grid.getStore(), "tmc.Request.update"); 
	}
	
	@Override
	public void insertRow(){
		if(this.companyModel.getCompanyId() == null){
			new SimpleMessage("기관선택", "등록하고자 하는 담당자의 기관을 먼저 선택하여 주세요"); 
			return ; 
		}
		
		GridInsertRow<RequestModel> service = new GridInsertRow<RequestModel>(); 

		RequestModel requestModel= new RequestModel();
		// 초기 데이터 설정 
		requestModel.setRegUserModel(LoginUser.getLoginUser());
		requestModel.setRegUserId(LoginUser.getLoginUser().getUserId());
		requestModel.setRegDate(new Date());
		requestModel.setRequestUserModel(LoginUser.getLoginUser());
		requestModel.setRequestUserId(LoginUser.getLoginUser().getUserId());
		
		requestModel.setRegDate(new Date());
		
		service.insertRow(grid, requestModel);
	}
	
	@Override
	public void deleteRow(){
		GridDeleteData<RequestModel> service = new GridDeleteData<RequestModel>();
		List<RequestModel> checkedList = grid.getSelectionModel().getSelectedItems() ; 
		service.deleteRow(grid.getStore(), checkedList, "tmc.Request.delete");
	}
}