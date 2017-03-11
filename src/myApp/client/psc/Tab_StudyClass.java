package myApp.client.psc;

import java.util.List;

import myApp.client.psc.model.StudyClassModel;
import myApp.client.psc.model.StudyClassModelProperties;
import myApp.frame.LoginUser;
import myApp.frame.service.GridDeleteData;
import myApp.frame.service.GridInsertRow;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.service.GridUpdateData;
import myApp.frame.ui.builder.ComboBoxField;
import myApp.frame.ui.builder.GridBuilder;
import myApp.frame.ui.builder.InterfaceGridOperate;
import myApp.frame.ui.builder.SearchBarBuilder;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Tab_StudyClass extends VerticalLayoutContainer implements InterfaceGridOperate {
	
	private StudyClassModelProperties properties = GWT.create(StudyClassModelProperties.class);
	private Grid<StudyClassModel> grid = this.buildGrid();
	private TextField className = new TextField();
	
	public Tab_StudyClass() {
		
		this.setBorders(false); 
		
		SearchBarBuilder searchBarBuilder = new SearchBarBuilder(this);
		searchBarBuilder.addTextField(className, "반 이름", 150, 50, true); 
		searchBarBuilder.addRetrieveButton(); 
		searchBarBuilder.addUpdateButton();
		searchBarBuilder.addInsertButton();
		searchBarBuilder.addDeleteButton();
		
		this.add(searchBarBuilder.getSearchBar(), new VerticalLayoutData(1, 40));
		this.add(grid, new VerticalLayoutData(1, 1));
	}
	
	@Override
	public void retrieve(){
		GridRetrieveData<StudyClassModel> service = new GridRetrieveData<StudyClassModel>(grid.getStore());
//		service.addParam("companyId", LoginUser.getLoginUser().getCompanyId());
		service.addParam("companyId", LoginUser.getLoginUser().getCompanyModel().getCompanyId());
		service.retrieve("psc.StudyClass.selectByCompanyId");
	}
	
	@Override
	public void update(){
		GridUpdateData<StudyClassModel> service = new GridUpdateData<StudyClassModel>(); 
		service.update(grid.getStore(), "psc.StudyClass.update"); 
	}
	
	@Override
	public void insertRow(){
		GridInsertRow<StudyClassModel> service = new GridInsertRow<StudyClassModel>(); 
		StudyClassModel studyClassModel = new StudyClassModel();
//		studyClassModel.setCompanyId(LoginUser.getLoginUser().getCompanyId());
		studyClassModel.setCompanyId(LoginUser.getLoginUser().getCompanyModel().getCompanyId());
		service.insertRow(grid, studyClassModel);
	}
	
	@Override
	public void deleteRow(){
		GridDeleteData<StudyClassModel> service = new GridDeleteData<StudyClassModel>();
		List<StudyClassModel> checkedList = grid.getSelectionModel().getSelectedItems() ; 
		service.deleteRow(grid.getStore(), checkedList, "psc.StudyClass.delete");
	}
	
	public Grid<StudyClassModel> buildGrid(){
			
		GridBuilder<StudyClassModel> gridBuilder = new GridBuilder<StudyClassModel>(properties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);
		
		gridBuilder.addText(properties.studyClassCode(), 80, "반코드", new TextField()) ;
		gridBuilder.addText(properties.studyClassName(), 150, "반 이름", new TextField()) ;
	
		final ComboBoxField studyClassTypeComboBox = new ComboBoxField("StudyClassTypeCode");  
		studyClassTypeComboBox.addCollapseHandler(new CollapseHandler(){
			@Override
			public void onCollapse(CollapseEvent event) {
				StudyClassModel data = grid.getSelectionModel().getSelectedItem(); 
				grid.getStore().getRecord(data).addChange(properties.studyClassTypeCode(), studyClassTypeComboBox.getCode());
			}
		}); 
		gridBuilder.addText(properties.studyClassTypeName(), 100, "반 구분", studyClassTypeComboBox) ;
		
		gridBuilder.addBoolean(properties.useYnFlag(), 90, "사용여부") ;
		gridBuilder.addText(properties.note(), 400, "비고", new TextField());

		return gridBuilder.getGrid(); 
	}

}