package myApp.client.sys;

import myApp.client.sys.model.MenuModel;
import myApp.frame.Tree_MenuRetrieve;
import myApp.frame.service.TreeGridUpdate;
import myApp.frame.ui.img.ResourceIcon;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

public class Page_TreeMenu extends VerticalLayoutContainer {
	
	//private TreeGrid<MenuModel> treeGrid = this.buildTreeGrid();
	private Long roleId;
	private Tree<MenuModel, String> menuTree ; //= this.getMenuTree(); 
	
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

		Tree<MenuModel, String> menuTree = new Tree<MenuModel, String>(menuTreeStore, treeMenuValueProvider) ; 
		
//		{
//			@Override
//			protected void onClick(Event event) { // onDoubleClick event도 있으나...
//				TreeNode<MenuModel> node = findNode(event.getEventTarget().<Element> cast());
//
//				if(node == null) {
//					return; // 선택된 메뉴가 없다. 
//				}
//		        
//				if(node.getModel().getMenuId() != null && node.getModel().getChildList().size() == 0 ){
//					String className = node.getModel().getClassName();  
//					String pageName = node.getModel().getMenuName();
//					
//					OpenTab openTab = new OpenTab();
//					openTab.openTab(tabPanel, className, pageName);
//				}
//				
//		        super.onDoubleClick(event); // tree node를 one-click으로 열기위해 사용한다. 
//			}
//		};

//		retrieveMenuTree(menuTreeStore, LoginUser.getLoginUser().getUserId());
		menuTree.getStyle().setLeafIcon(ResourceIcon.INSTANCE.textButton());
		menuTree.setCheckable(true);
		menuTree.setCheckStyle(CheckCascade.TRI);
		
		return menuTree; 
	}



//	public TreeGrid<MenuModel> buildTreeGrid(){
//		
//		MenuModelProperties properties = GWT.create(MenuModelProperties.class);
//		final GridBuilder<MenuModel> gridBuilder = new GridBuilder<MenuModel>(properties.keyId());  
//		
//		gridBuilder.setChecked(SelectionMode.SINGLE);
//
//		gridBuilder.addText(properties.menuName(), 300, "메뉴명", new TextField());
//		gridBuilder.addText(properties.seq(), 50, "순서", new TextField()) ;
//		gridBuilder.addText(properties.className(), 200, "클래스명", new TextField()) ;
//		
//		gridBuilder.addBoolean(properties.useYnFlag(), 50, "숨김") ;
//		gridBuilder.addText(properties.note(), 300, "상세설명", new TextField());
//	
//		return gridBuilder.getTreeGrid();  
//	}

	public Page_TreeMenu(){

		ButtonBar buttonBar = new ButtonBar();
		TextButton updateButton = new TextButton("저장");
		updateButton.setWidth(60);
		updateButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				update();  
			}
		}); 
		buttonBar.add(updateButton);

		
//		TextButton refreshButton = new TextButton("조회");
//		
//		refreshButton.addSelectHandler(new SelectHandler(){
//			@Override
//			public void onSelect(SelectEvent event) {
//				 refresh(); 
//			}
//		}); 
//		buttonBar.add(refreshButton);
		
//		TextButton expandAll = new TextButton("펼치기"); 
//		expandAll.setWidth(60);
//		expandAll.addSelectHandler(new SelectHandler(){
//			@Override
//			public void onSelect(SelectEvent event) {
//				 treeGrid.expandAll();
//			}
//		}); 
//		buttonBar.add(expandAll);
//		
//		TextButton collapseAll = new TextButton("감추기");
//		collapseAll.setWidth(60);
//		collapseAll.addSelectHandler(new SelectHandler(){
//			@Override
//			public void onSelect(SelectEvent event) {
//				 treeGrid.collapseAll();
//			}
//		}); 
//		buttonBar.add(collapseAll);
		
		this.add(buttonBar);

		this.menuTree = this.getMenuTree(); 
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(this.menuTree);
		
		this.add(cp, new VerticalLayoutData(1, 1, new Margins(1, 0, 0, 0)));
	}

	public void refresh(){
		if(this.roleId != null){
			retrieve(this.roleId);
		}
	}
	
	public void retrieve(Long roleId){
		this.roleId = roleId;
		Tree_MenuRetrieve retrieve = new Tree_MenuRetrieve(menuTree.getStore());
		retrieve.retrieveTree(roleId);
	}
	
	private void update(){
		
		menuTree.getCheckedSelection();
		
		for(MenuModel menu : menuTree.getCheckedSelection()){
			menuTree.setChecked(menu, Tree.CheckState.UNCHECKED);	
		}
		
		TreeGridUpdate<MenuModel> service = new TreeGridUpdate<MenuModel>(); 
		//service.update(treeGrid.getTreeStore(), "sys.Menu.update"); 
	}
	
	private void deleteRow(){
//		TreeGridDeleteRow<MenuModel> service = new TreeGridDeleteRow<MenuModel>();
//		List<MenuModel> checkedList = treeGrid.getSelectionModel().getSelectedItems() ; 
//		service.deleteRow(treeGrid.getTreeStore(), checkedList, "sys.Menu.delete");
	}
}
