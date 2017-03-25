package myApp.frame;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.info.Info;

public class OpenTab {

	private Widget createTab(String className){

		if("CompanyTab.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.sys.Tab_Company.class) ;
		}

<<<<<<< HEAD
=======
		if("Tab_MenuTree".equals(className)) {
			return (Widget) GWT.create(myApp.client.sys.Tab_MenuTree.class) ;
		}
		
>>>>>>> branch 'master' of https://github.com/micheonking/Preschool
		// 검사결과 등록 
		if("Tab_Checkup".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_Checkup.class) ;
		}
		
		if("GridAdminUser".equals(className)) {
			return (Widget) GWT.create(myApp.client.sys.Tab_Admin.class) ;
		}

		if("RoleTab.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.sys.Tab_Role.class) ;
		}
		
		if("CodeKindTab.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.code.Tab_CodeKind.class) ;
		}
		
		if("Tab_LicenseCode.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.code.Tab_LicenseCode.class) ;
		}
		
		if("Tab_User".equals(className)) {
			return (Widget) GWT.create(myApp.client.sys.Tab_User.class) ;
		}

		if("Tab_Patient".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_Patient.class) ;
		}

		if("Tab_Payday".equals(className)) {
			return (Widget) GWT.create(myApp.client.pay.Tab_Payday.class) ;
		}

		if("Tab_LicenseList".equals(className)) {
			return (Widget) GWT.create(myApp.client.emp.Tab_LicenseList.class) ;
		}

		if("Tab_StudyClass".equals(className)) {
			return (Widget) GWT.create(myApp.client.psc.Tab_StudyClass.class) ;
		}
		if("Tab_Teacher".equals(className)) {
			return (Widget) GWT.create(myApp.client.psc.Tab_Teacher.class) ;
		}
		if("Tab_Student".equals(className)) {
			return (Widget) GWT.create(myApp.client.psc.Tab_Student.class) ;
		}
		if("Tab_Board".equals(className)) {
			return (Widget) GWT.create(myApp.client.bbs.Tab_Board.class) ;
		}
		if("Tab_Season.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.acc.Tab_Season.class) ;
		}
		if("Tab_AccountCommon.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.acc.Tab_CommonAccount.class) ;
		}
		if("Tab_CompanyAccount.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.acc.Tab_CompanyAccount.class) ;
		}

		if("Tab_Client.class".equals(className)) {
			return (Widget) GWT.create(myApp.client.acc.Tab_Client.class) ;
		}

		if("Tab_Request".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_Request.class) ;
		}

		if("Tab_RequestList".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_RequestList.class) ;
		}

		if("Tab_CheckupList".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_CheckupList.class) ;
		}

		if("Tab_TreatList".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_TreatList.class) ;
		}

		if("Tab_TreatRequest".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_TreatRequest.class) ;
		}

		if("Tab_Prescribe".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_Prescribe.class) ;
		}
		
		if("Tab_TreatResult".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_TreatResult.class) ;
		}
		
		if("Tab_Stats".equals(className)) {
			return (Widget) GWT.create(myApp.client.tmc.Tab_Stats.class) ;
		}
		
		
		return null; 
	}
	
	public void openTab(TabPanel tabPanel, String className, String pageName){

		TabItemConfig tabItemConfig = new TabItemConfig(pageName, true);
		Widget tabPage = tabPanel.findItem(pageName, true); 

		if(tabPage != null) {
			tabPanel.setActiveWidget(tabPage);
			return ; 
		}

		// not found tab page  
		tabPage = createTab(className);
				
		if(tabPage != null){
			tabPanel.add(tabPage, tabItemConfig);
			tabPanel.setActiveWidget(tabPage);
		} 
		else {
			Info.display(pageName, "해당 오브젝트가 시스템에 등록되어 있지 않습니다."); 
		}
	}
}
