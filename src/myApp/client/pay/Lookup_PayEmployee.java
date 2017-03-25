package myApp.client.pay;

import myApp.client.emp.model.HireModel;
import myApp.client.emp.model.HireModelProperties;
import myApp.client.pay.model.PaydayModel;
import myApp.frame.LoginUser;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.ui.InterfaceLookupResult;
import myApp.frame.ui.SimpleMessage;
import myApp.frame.ui.builder.AbstractLookupWindow;
import myApp.frame.ui.builder.GridBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Lookup_PayEmployee extends AbstractLookupWindow  {
	
	private HireModelProperties properties = GWT.create(HireModelProperties.class);
	private Grid<HireModel> grid = this.buildGrid();
	private InterfaceLookupResult lookupParent ;
	private PaydayModel paydayModel; 
	private TextField userNameField = new TextField();
	
	public Lookup_PayEmployee(InterfaceLookupResult lookupParent, PaydayModel paydayModel){
		
		this.lookupParent = lookupParent; 
		this.paydayModel = paydayModel; 
		
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
		this.setInit("교원 선택 : 급여산정기간 ( " 
				+ dtf.format(paydayModel.getOnDate()) + " ~ " 
				+ dtf.format(paydayModel.getOffDate()) + " )", 600, 350);
		
		this.addLabel(userNameField, "교원명", 150, 50, true) ;
		userNameField.addKeyDownHandler(new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent arg0) {
				if(arg0.getNativeKeyCode() == 13){
					retrieve(); 
				}
			}
		}); 

		VerticalLayoutContainer vlc = new VerticalLayoutContainer(); 
		vlc.add(this.getSearchBar(), new VerticalLayoutData(1, 40)); // , new Margins(0, 0, 0, 5)));
		vlc.add(grid, new VerticalLayoutData(1, 1));
		this.add(vlc);
		this.retrieve();
	}
	
	private Grid<HireModel> buildGrid(){
		GridBuilder<HireModel> gridBuilder = new GridBuilder<HireModel>(properties.keyId());
		gridBuilder.setChecked(SelectionMode.SINGLE);
		
		gridBuilder.addText(properties.korName(), 80, "교원명") ;
		gridBuilder.addDate(properties.hireDate(), 90, "입사일") ;
		gridBuilder.addText(properties.hireName(), 120, "입사구분") ;
		gridBuilder.addText(properties.telNo1(), 120, "연락처") ;
		gridBuilder.addText(properties.ctzNo(), 100, "주민번호");
		gridBuilder.addDate(properties.retireDate(), 90, "퇴직일") ;
		gridBuilder.addText(properties.note(), 200, "비고" );
	
		return gridBuilder.getGrid(); 
	}
	
	@Override
	public void retrieve(){
		GridRetrieveData<HireModel> service = new GridRetrieveData<HireModel>(grid.getStore());
		service.addParam("companyId", LoginUser.getLoginUser().getCompanyId());
		service.addParam("korName", userNameField.getText());
		service.addParam("onDate", this.paydayModel.getOnDate()); // 급여시작일 
		service.addParam("offDate", this.paydayModel.getOffDate()); // 급여종료일 
		service.retrieve("emp.Hire.selectByPayTarget");
	}

	@Override
	public void confirm() {
		HireModel hireModel = this.grid.getSelectionModel().getSelectedItem(); 

		if(hireModel != null){
			lookupParent.setLookupResult(hireModel);
			this.hide(); 
		} 
		else {
			new SimpleMessage("등록할 교원을 선택해 주세요"); 
		}	}

	@Override
	public void cancel() {
		this.hide(); 
	}
	
}
