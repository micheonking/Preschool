package myApp.client.acc;

import java.util.List;

import myApp.client.acc.model.AccountModelProperties;
import myApp.frame.PDFViewer;
import myApp.frame.service.GridDeleteData;
import myApp.frame.service.GridInsertRow;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.service.GridUpdateData;
import myApp.frame.ui.SimpleMessage;
import myApp.frame.ui.builder.ComboBoxField;
import myApp.frame.ui.builder.GridBuilder;
import myApp.frame.ui.builder.InterfaceGridOperate;
import myApp.frame.ui.builder.SearchBarBuilder;
import myApp.client.acc.model.AccountModel;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Tab_CommonAccount extends VerticalLayoutContainer implements InterfaceGridOperate {
	
	private AccountModelProperties properties = GWT.create(AccountModelProperties.class);
	private Grid<AccountModel> grid = this.buildGrid();
	
	ComboBoxField eduOfficeName = new ComboBoxField("EduOfficeCode");
	
	public Tab_CommonAccount() {
		
		this.setBorders(false); 
		SearchBarBuilder searchBarBuilder = new SearchBarBuilder(this);
		searchBarBuilder.addComboBox(eduOfficeName, "회계구분코드", 300, 100);
		searchBarBuilder.addRetrieveButton(); 
		searchBarBuilder.addUpdateButton();
		searchBarBuilder.addInsertButton();
		searchBarBuilder.addDeleteButton();
	
	    TextButton retrievePDFButton = new TextButton("PDF출력");
	    retrievePDFButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				// PDF 호출하기 
				PDFViewer viewer = new PDFViewer(); 
				// 호출하려면 className과 기타 Parameter를 String으로 붙여서 넘겨주어야 한다. 
				viewer.open("className=acc.AccountPDF");
			}
		});
	    searchBarBuilder.getSearchBar().add(retrievePDFButton); 
		
		this.add(searchBarBuilder.getSearchBar(), new VerticalLayoutData(1, 40));
		this.add(grid, new VerticalLayoutData(1, 1));
		
	}
	
	@Override
	public void retrieve(){
		
		String eduOfficeCode = eduOfficeName.getCode(); 
		
		if(eduOfficeCode == null){
			new SimpleMessage("조회할 회계구분코드를 먼저 선택하여야 합니다.");
			return ; 
		}
		
		GridRetrieveData<AccountModel> service = new GridRetrieveData<AccountModel>(grid.getStore());
		service.addParam("eduOfficeCode", eduOfficeName.getCode());
		service.retrieve("acc.Account.selectByEduOfficeCode");
	}
	
	@Override
	public void update(){
		GridUpdateData<AccountModel> service = new GridUpdateData<AccountModel>(); 
		service.update(grid.getStore(), "acc.Account.update"); 
	}
	
	@Override
	public void insertRow(){
		GridInsertRow<AccountModel> service = new GridInsertRow<AccountModel>(); 
		AccountModel accountModel = new AccountModel();
		accountModel.setEduOfficeCode(eduOfficeName.getCode());
		accountModel.setCompanyId(Long.parseLong("0"));
		accountModel.setHangCode("000");
		accountModel.setGmokCode("000");
		accountModel.setSmokCode("000");
		accountModel.setUseYnFlag(false);
		accountModel.setSumYnFlag(false);
		accountModel.setTransYnFlag(false);
		accountModel.setBudgetYnFlag(false);
		
		service.insertRow(grid, accountModel);
	}
	
	@Override
	public void deleteRow(){
		GridDeleteData<AccountModel> service = new GridDeleteData<AccountModel>();
		List<AccountModel> checkedList = grid.getSelectionModel().getSelectedItems() ; 
		service.deleteRow(grid.getStore(), checkedList, "acc.Account.delete");
	}
	
	public Grid<AccountModel> buildGrid(){
			
		GridBuilder<AccountModel> gridBuilder = new GridBuilder<AccountModel>(properties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);
		
		gridBuilder.addText(properties.gwonCode(), 60, "관코드", new TextField()); 
		gridBuilder.addText(properties.gwonName(), 100, "관이름", new TextField()); 
		gridBuilder.addText(properties.hangCode(), 60, "항코드", new TextField()); 
		gridBuilder.addText(properties.hangName(), 100, "항이름", new TextField()); 
		gridBuilder.addText(properties.gmokCode(), 60, "목코드", new TextField()); 
		gridBuilder.addText(properties.gmokName(), 100, "목이름", new TextField()); 
//		gridBuilder.addText(properties.smokCode(), 80, "세목코드", new TextField()); 
//		gridBuilder.addText(properties.smokName(), 100, "세목이름", new TextField()); 
		gridBuilder.addText(properties.printName(), 200, "출력명", new TextField()); 

		gridBuilder.addBoolean(properties.sumYnFlag(), 50, "합계"); 
		 
		
		
		final ComboBoxField inExpComboBox = new ComboBoxField("InExpCode");  
		inExpComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				AccountModel data = grid.getSelectionModel().getSelectedItem(); 
				grid.getStore().getRecord(data).addChange(properties.inExpCode(), inExpComboBox.getCode());
			}
		}); 
		gridBuilder.addText(properties.inExpName(), 100, "입출구분", inExpComboBox) ;
		gridBuilder.addBoolean(properties.budgetYnFlag(), 50, "예산");
		
		gridBuilder.addBoolean(properties.resolutionYnFlag(), 60, "결의서"); 	
		
		gridBuilder.addBoolean(properties.transYnFlag(), 50, "거래");
		gridBuilder.addBoolean(properties.useYnFlag(), 50, "제외"); 
	
		gridBuilder.addText(properties.note(), 400, "비고", new TextField()); 

//		// grid를 return 하기 전에 설정해야 한다. 
//		gridBuilder.setBeforeStartEditHandler(new BeforeStartEditHandler<AccountModel>(){
//			@Override
//			public void onBeforeStartEdit(BeforeStartEditEvent<AccountModel> event) {
//				// TODO Auto-generated method stub
//				
//				AccountModel accountModel = grid.getSelectionModel().getSelectedItem(); 
//				if(accountModel != null){
//					if("000".equals(accountModel.getHangCode())){
//						event.setCancelled(true);		
//					}
//					else {
//						event.setCancelled(false);
//					}
//				}
//			}
//		}); 
		
		return gridBuilder.getGrid();  
	}

}