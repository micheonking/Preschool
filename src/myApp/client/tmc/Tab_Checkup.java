package myApp.client.tmc;

import java.util.Date;
import myApp.client.tmc.model.RequestModel;
import myApp.client.tmc.model.RequestModelProperties;
import myApp.client.tmc.model.CheckupModel;
import myApp.client.tmc.model.CheckupModelProperties;
import myApp.frame.LoginUser;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.service.GridUpdateData;
import myApp.frame.ui.SimpleMessage;
import myApp.frame.ui.builder.ComboBoxField;
import myApp.frame.ui.builder.GridBuilder;
import myApp.frame.ui.builder.InterfaceGridOperate;
import myApp.frame.ui.builder.SearchBarBuilder;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

/*
 * 검사오더에 따른 검사내용 등록 
 */

public class Tab_Checkup extends BorderLayoutContainer implements InterfaceGridOperate {
	
	private RequestModelProperties requestModelProperties = GWT.create(RequestModelProperties.class);
	private Grid<RequestModel> gridRequest = this.buildGridRequest();
	
	private CheckupModelProperties checkupModelProperties = GWT.create(CheckupModelProperties.class);
	private Grid<CheckupModel> gridCheckup = this.buildGridCheckup();

	private TextField patientNameField = new TextField();
	private DateField dateField = new DateField(); 
	private LookupCompanyField lookupCompanyField = new LookupCompanyField(); 
	
	public Tab_Checkup() {

		SearchBarBuilder searchBarBuilder = new SearchBarBuilder(this);
		searchBarBuilder.addLookupTriggerField(lookupCompanyField, "기관명", 210, 48);

		dateField.setValue(new Date());
		searchBarBuilder.addDateField(dateField, "진료예정일", 180, 70, true);
		searchBarBuilder.addTextField(patientNameField, "환자명", 130, 48, true); 
		searchBarBuilder.addRetrieveButton(); 

		VerticalLayoutContainer vlc = new VerticalLayoutContainer(); 
		vlc.add(searchBarBuilder.getSearchBar(), new VerticalLayoutData(1, 48));
		vlc.add(gridRequest, new VerticalLayoutData(1, 1));
		
		gridRequest.addRowClickHandler(new RowClickHandler(){
			@Override
			public void onRowClick(RowClickEvent event) {
				// event.getClass
				RequestModel requestModel = gridRequest.getSelectionModel().getSelectedItem(); 
				if(requestModel != null){
					retrieveCheckup(requestModel); 
				}
			}
		}); 
		
		BorderLayoutData northLayoutData = new BorderLayoutData(300);
		northLayoutData.setMargins(new Margins(0,0,1,0));
		northLayoutData.setSplit(true);
		northLayoutData.setMaxSize(1000);
		this.setNorthWidget(vlc, northLayoutData); 
		
		
		ContentPanel cp = new ContentPanel(); 
		cp.setHeaderVisible(false);
		cp.add(gridCheckup);
		
		TextButton buttonUpdate = new TextButton("임시저장");  
		buttonUpdate.setWidth(100);
		buttonUpdate.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				update(); 
			}
		}); 
		cp.addButton(buttonUpdate);
		
		TextButton buttonConfirm = new TextButton("검사완료");
		buttonConfirm.setWidth(100);
		cp.addButton(buttonConfirm);
		
		cp.setButtonAlign(BoxLayoutPack.CENTER);
		cp.getButtonBar().setPadding(new Padding(0, 0, 15, 0)); 
		this.setCenterWidget(cp); 
	}
	
	public Grid<RequestModel> buildGridRequest(){
		GridBuilder<RequestModel> gridBuilder = new GridBuilder<RequestModel>(requestModelProperties.keyId());  
		// gridBuilder.setChecked(SelectionMode.SINGLE);
		gridBuilder.addDate(requestModelProperties.requestDate(), 90, "진료예정일"); //, new DateField());
		gridBuilder.addText(requestModelProperties.treatStateName(), 80, "진료상태"); 
		gridBuilder.addText(requestModelProperties.insNo(), 80, "보험번호"); //, new TextField()) ;
		gridBuilder.addText(requestModelProperties.patientKorName(), 80, "환자명"); //, lookupPatientField) ;
		gridBuilder.addText(requestModelProperties.korName(), 80, "보건의"); //, lookupRequestUserField);
		gridBuilder.addText(requestModelProperties.requestNote(), 200, "진료요청내용"); //, new TextField()) ;
		gridBuilder.addDate(requestModelProperties.treatDate(), 85, "진료일"); //, new DateField());
		gridBuilder.addText(requestModelProperties.treatKorName(), 80, "진료의"); //, lookUpTreatUserField) ;
		gridBuilder.addText(requestModelProperties.treatNote(), 200, "처방내역"); //, new TextField()) ;
		gridBuilder.addText(requestModelProperties.note(), 400, "특기사항"); //, new TextField()) ;		
		gridBuilder.addText(requestModelProperties.regKorName(), 80, "등록자"); //, new TextField()) ;
		gridBuilder.addDate(requestModelProperties.regDate(), 85, "등록일"); //, new DateField()) ;
		return gridBuilder.getGrid(); 
	}

	public Grid<CheckupModel> buildGridCheckup(){

		GridBuilder<CheckupModel> gridBuilder = new GridBuilder<CheckupModel>(checkupModelProperties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);
		
		final ComboBoxField checkupTypeComboBox = new ComboBoxField("CheckupTypeCode");  
		checkupTypeComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				CheckupModel data = gridCheckup.getSelectionModel().getSelectedItem(); 
				gridCheckup.getStore().getRecord(data).addChange(checkupModelProperties.checkupCode(), checkupTypeComboBox.getCode());
			}
		}); 
		gridBuilder.addText(checkupModelProperties.checkupName(), 100, "검사종류"); // , checkupTypeComboBox) ;
		gridBuilder.addText(checkupModelProperties.checkupOrder(), 300, "검사요청사항"); //, new TextField()) ;

		final ComboBoxField checkupProcessComboBox = new ComboBoxField("CheckupProcessCode");  
		checkupProcessComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				CheckupModel data = gridCheckup.getSelectionModel().getSelectedItem(); 
				gridCheckup.getStore().getRecord(data).addChange(checkupModelProperties.processCode(), checkupProcessComboBox.getCode());
			}
		}); 
		gridBuilder.addText(checkupModelProperties.processName(), 80, "검사상태", checkupProcessComboBox) ;
		gridBuilder.addText(checkupModelProperties.checkupResult(), 400, "검사결과", new TextField()) ;
		
		ActionCell<String> fileUploadCell = new ActionCell<String>("첨부파일", new ActionCell.Delegate<String>(){
			@Override
			public void execute(String arg0) {
				fileUploadOpen(); 
			}
		});
		gridBuilder.addCell(checkupModelProperties.fileUpload(), 80, "첨부파일", fileUploadCell) ;
		gridBuilder.addDate(checkupModelProperties.checkupDate(), 100, "검사일"); // , new DateField()) ;
		gridBuilder.addText(checkupModelProperties.userKorName(), 80, "검사담당") ;
		return gridBuilder.getGrid(); 
	}

	private void fileUploadOpen(){
		CheckupModel checkupModel = gridCheckup.getSelectionModel().getSelectedItem(); 
		if(checkupModel != null){
			Lookup_File lookupFile = new Lookup_File();
			lookupFile.open(checkupModel.getCheckupId());
			lookupFile.show();
		}
	}
	
	private void retrieveCheckup(RequestModel requestModel){
		GridRetrieveData<CheckupModel> service = new GridRetrieveData<CheckupModel>(gridCheckup.getStore());
		service.addParam("requestId", requestModel.getRequestId());
		service.retrieve("tmc.Checkup.selectByRequestId");
	}
	
	@Override
	public void retrieve() {
		if(this.lookupCompanyField.getCompanyModel() == null){
			new SimpleMessage("기관명 확인", "조회조건의 기관명은 반드시 입력하세요. ");
			return ; 
		} 
		
		GridRetrieveData<RequestModel> service = new GridRetrieveData<RequestModel>(gridRequest.getStore());
		service.addParam("companyId", this.lookupCompanyField.getCompanyModel().getCompanyId());
		service.addParam("patientName", patientNameField.getText());
		service.addParam("requestDate", dateField.getValue());
		service.retrieve("tmc.Request.selectByCompanyId");
	}
	
	@Override
	public void update(){
		GridUpdateData<CheckupModel> service = new GridUpdateData<CheckupModel>();
		service.addParam("userId", LoginUser.getLoginUser().getUserId()); // 검사의사 등록. 
		service.update(gridCheckup.getStore(), "tmc.Checkup.update"); 
	}
	
	@Override
	public void insertRow(){
	}
	
	@Override
	public void deleteRow(){
	}
}