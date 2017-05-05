package myApp.client.tmc;

import myApp.client.tmc.model.CheckupModel;
import myApp.client.tmc.model.CheckupModelProperties;
import myApp.client.tmc.model.RequestModel;
import myApp.frame.service.GridDeleteData;
import myApp.frame.service.GridInsertRow;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.service.GridUpdateData;
import myApp.frame.ui.SimpleMessage;
import myApp.frame.ui.builder.ComboBoxField;
import myApp.frame.ui.builder.GridBuilder;

import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Page_Checkup extends ContentPanel  {
	
	private CheckupModelProperties properties = GWT.create(CheckupModelProperties.class);
	private Grid<CheckupModel> grid = this.buildGrid();
	private RequestModel requestModel; 
	
	public Page_Checkup(){
		this.setHeaderVisible(false);
		this.add(grid);
	}
	
	public Grid<CheckupModel> buildGrid(){

		GridBuilder<CheckupModel> gridBuilder = new GridBuilder<CheckupModel>(properties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);
		
		final ComboBoxField checkupTypeComboBox = new ComboBoxField("CheckupTypeCode");  
		checkupTypeComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				CheckupModel data = grid.getSelectionModel().getSelectedItem(); 
				grid.getStore().getRecord(data).addChange(properties.checkupCode(), checkupTypeComboBox.getCode());
			}
		}); 
		gridBuilder.addText(properties.checkupName(), 100, "검사종류", checkupTypeComboBox) ;
		gridBuilder.addText(properties.checkupOrder(), 200, "검사요청사항", new TextField()) ;

		
		final ComboBoxField checkupProcessComboBox = new ComboBoxField("CheckupProcessCode");  
		checkupProcessComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				CheckupModel data = grid.getSelectionModel().getSelectedItem(); 
				grid.getStore().getRecord(data).addChange(properties.processCode(), checkupProcessComboBox.getCode());
			}
		}); 
		gridBuilder.addText(properties.processName(), 80, "검사상태"); // , checkupProcessComboBox) ;

		gridBuilder.addText(properties.checkupResult(), 250, "검사결과"); //, new DateField()) ;

		ActionCell<String> fileUploadCell = new ActionCell<String>("첨부파일", new ActionCell.Delegate<String>(){
			@Override
			public void execute(String arg0) {
				fileUploadOpen(); 
			}
		});
		gridBuilder.addCell(properties.fileUpload(), 80, "첨부파일", fileUploadCell) ;

		gridBuilder.addDate(properties.checkupDate(), 100, "검사일"); // , new DateField()) ;
		gridBuilder.addText(properties.userKorName(), 80, "검사담당") ;
		
		return gridBuilder.getGrid(); 
	}

	private void fileUploadOpen(){
		CheckupModel checkupModel = grid.getSelectionModel().getSelectedItem(); 
		if(checkupModel != null){
			Lookup_File lookupFile = new Lookup_File();
			lookupFile.open(checkupModel.getCheckupId());
			lookupFile.show();
		}
	}

	
	public void retrieve(RequestModel requestModel){
		this.grid.getStore().clear(); 
		this.requestModel = requestModel;
		GridRetrieveData<CheckupModel> service = new GridRetrieveData<CheckupModel>(grid.getStore());
		service.addParam("requestId", this.requestModel.getRequestId());
		service.retrieve("tmc.Checkup.selectByRequestId");
	}

	public void insert(){
		if(this.requestModel == null){
			new SimpleMessage("선택된 진료요청 내역이 없습니다."); 
			return ; 
		}
		
		GridInsertRow<CheckupModel> service = new GridInsertRow<CheckupModel>(); 
		CheckupModel checkupModel = new CheckupModel();
		
		// 초기 데이터 설정 
		checkupModel.setProcessCode("010");
		checkupModel.setProcessName("검사요청");
		
		checkupModel.setRequestId(this.requestModel.getRequestId());
		service.insertRow(grid, checkupModel);
	}
	
	public void delete(){
		GridDeleteData<CheckupModel> service = new GridDeleteData<CheckupModel>();
		List<CheckupModel> checkedList = grid.getSelectionModel().getSelectedItems() ; 
		service.deleteRow(grid.getStore(), checkedList, "tmc.Checkup.delete");

	}
	
	public void update(){
		GridUpdateData<CheckupModel> service = new GridUpdateData<CheckupModel>(); 
		service.update(grid.getStore(), "tmc.Checkup.update"); 
	}
	
	public int getRowCount() {
		return grid.getStore().getAll().size(); 
	}
}

