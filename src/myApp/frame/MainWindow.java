package myApp.frame;

import myApp.client.sys.model.MenuModel;
import myApp.frame.ui.img.ResourceIcon;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class MainWindow extends BorderLayoutContainer {
	
	private PlainTabPanel tabPanel = new PlainTabPanel();	
 
	public MainWindow getMainWindow() {
		
		// North Layout setting 
		BorderLayoutData northLayoutData = new BorderLayoutData(34);
		northLayoutData.setMargins(new Margins(0,0,3,0));
		northLayoutData.setSplit(false);
		this.setNorthWidget(this.getNorthLayout(), northLayoutData); 
		
		// West Layout setting 
		BorderLayoutData westLayoutData = new BorderLayoutData(250);
		westLayoutData.setSplit(true);
		westLayoutData.setMargins(new Margins(0, 1, 0, 0)); // 앞쪽에 보이는 가로 줄을 없애준다
		this.setWestWidget(this.getWestLayout(), westLayoutData);
		
		tabPanel.setTabScroll(true);
		tabPanel.add(new Tab_Main(), "My Page"); // my page setting
		
		ContentPanel tabContent = new ContentPanel(); 
		tabContent.setHeaderVisible(false);
		VerticalLayoutContainer vlc = new VerticalLayoutContainer(); 
		vlc.add(tabPanel, new VerticalLayoutData(1, 1, new Margins(2))); //, 2, 2, 2)));
		tabContent.add(vlc);
		
		this.setCenterWidget(tabContent);

		//Viewport viewport = new Viewport();
//		viewport.add(this);
		
		//RootPanel.get().add(viewport);
		
		return this; 
	}
	
	private ContentPanel getNorthLayout(){
		String headerMessage = "반갑습니다. " + LoginUser.getLoginUser().getCompanyModel().getCompanyName()
				+ " " + LoginUser.getLoginUser().getKorName()+ "님!" ; ; 

		Label label = new Label(headerMessage); 
		label.getElement().getStyle().setProperty("color", "silver"); // font color 변경
		label.getElement().getStyle().setProperty("fontWeight", "bold"); // font color 변경
		label.getElement().getStyle().setProperty("fontSize", "14px"); // font color 변경
				
		HBoxLayoutContainer header = new HBoxLayoutContainer();
		header.add(label, new BoxLayoutData(new Margins(8, 3, 3, 20))); 
		
		ToolButton question = new ToolButton(ToolButton.QUESTION); 
		header.add(question, new BoxLayoutData(new Margins(11, 3, 3, 10))); 
	
		ToolButton close = new ToolButton(ToolButton.CLOSE); 
		header.add(close, new BoxLayoutData(new Margins(11, 3, 3, 5))); 
		
		ContentPanel cp = new ContentPanel();
		cp.setBodyStyle("backgroundColor:#000033"); // http://www.w3schools.com/colors/colors_names.asp 페이지 참조 
		
		cp.add(header);
		cp.setHeaderVisible(false);
		return cp ; 
	}
	
	private ContentPanel getWestLayout(){
		
		AccordionLayoutAppearance accordianLayout = GWT.create(AccordionLayoutAppearance.class); 
		ContentPanel treeAccordianPanel = new ContentPanel(accordianLayout); 
		treeAccordianPanel.setHeading("Navigation");			
		treeAccordianPanel.add(this.getMenuTree()); 
		
		//treeAccordianPanel.setBodyStyle("style='backgroundColor:white; color:red;'");
		
		ContentPanel newsAccordianPanel = new ContentPanel(accordianLayout); 
		newsAccordianPanel.setHeading("News & Board");
		newsAccordianPanel.add(new Label("공지사항 및 게시판입니다.")); 
		
		ContentPanel tempAccordianPanel = new ContentPanel(accordianLayout); 
		tempAccordianPanel.setHeading("공사 중...");
		tempAccordianPanel.add(new Label("준비중입니다."));
		
		AccordionLayoutContainer accordianContainer = new AccordionLayoutContainer();
		
		accordianContainer.setExpandMode(ExpandMode.SINGLE_FILL);
		accordianContainer.add(treeAccordianPanel);
		accordianContainer.setActiveWidget(treeAccordianPanel);
		accordianContainer.add(newsAccordianPanel);
		accordianContainer.add(tempAccordianPanel);
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer(); 
		vlc.add(accordianContainer, new VerticalLayoutData(1, 1, new Margins(3, 0, 0, 0)));
		
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(vlc);
		
		return cp ; 
		
	}
	
	public Tree<MenuModel, String> getMenuTree(){
		
		TreeStore<MenuModel> menuTreeStore = new TreeStore<MenuModel>(new ModelKeyProvider<MenuModel> () {
			@Override
			public String getKey(MenuModel roleObject) {
				return roleObject.getMenuId() + "";
			}
		});
		
		ValueProvider<MenuModel, String> treeMenuValueProvider = new ValueProvider<MenuModel, String>() {
			@Override
			public String getValue(MenuModel object) {
				return object.getMenuName();
			}
			@Override
			public void setValue(MenuModel object, String value) {
			}
			@Override
			public String getPath() {
				return "path";
			}
		} ; 

		Tree<MenuModel, String> menuTree = new Tree<MenuModel, String>(menuTreeStore, treeMenuValueProvider) {
			@Override
			protected void onClick(Event event) { // onDoubleClick event도 있으나...
				TreeNode<MenuModel> node = findNode(event.getEventTarget().<Element> cast());

				if(node == null) {
					return; // 선택된 메뉴가 없다. 
				}
		        
				if(node.getModel().getMenuId() != null && node.getModel().getChildList().size() == 0 ){
					String className = node.getModel().getClassName();  
					String pageName = node.getModel().getMenuName();
					
					OpenTab openTab = new OpenTab();
					openTab.openTab(tabPanel, className, pageName);
				}
				
		        super.onDoubleClick(event); // tree node를 one-click으로 열기위해 사용한다. 
			}
		};

		retrieveMenuTree(menuTreeStore, LoginUser.getLoginUser().getUserId());
		menuTree.getStyle().setLeafIcon(ResourceIcon.INSTANCE.textButton());
		return menuTree; 
	}
	
	public void retrieveMenuTree(TreeStore<MenuModel> menuTreeStore, Long userId){
		Tree_MenuRetrieve retrieve = new Tree_MenuRetrieve(menuTreeStore);
		retrieve.retrieveByUserId(userId);
	}
}
