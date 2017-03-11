package myApp.client.bbs;

import java.util.List;

import myApp.client.sys.model.FileModel;
import myApp.client.sys.model.FileModelProperties;
import myApp.frame.service.GridDeleteData;
import myApp.frame.service.GridRetrieveData;
import myApp.frame.ui.builder.GridBuilder;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Grid_FileDownload {

	private FileModelProperties properties = GWT.create(FileModelProperties.class);
	private Grid<FileModel> grid = this.buildGrid(); 
	public FormPanel form = new FormPanel();
	
	public Grid<FileModel> buildGrid(){
		
		
		GridBuilder<FileModel> gridBuilder = new GridBuilder<FileModel>(properties.keyId());  
		gridBuilder.setChecked(SelectionMode.SINGLE);

		gridBuilder.addDateTime(properties.regDate(), 160, "등록일", null);
		gridBuilder.addText(properties.fileName(), 360, "파일명");
		gridBuilder.addDouble(properties.size(), 80, "크기"); 
		
		ActionCell<String> downloadCell = new ActionCell<String>("다운로드", new ActionCell.Delegate<String>(){
			@Override
			public void execute(String arg0) {
				FileModel fileModel = grid.getSelectionModel().getSelectedItem(); 
				String url = "FileDownload?fileId=" + fileModel.getFileId();
				Window.open(url, "download File", "status=0,toolbar=0,menubar=0,location=0");
			}
		});
		gridBuilder.addCell(properties.downloadCell(), 100, "다운로드", downloadCell) ;

		ActionCell<String> deleteCell = new ActionCell<String>("삭제", new ActionCell.Delegate<String>(){
			@Override
			public void execute(String arg0) {
				deleteFile(); 
			}
		});
		gridBuilder.addCell(properties.downloadCell(), 80, "삭제", deleteCell) ;
		return gridBuilder.getGrid(); 
	}
	
	public void deleteFile(){
		GridDeleteData<FileModel> service = new GridDeleteData<FileModel>();
		List<FileModel> checkedList = grid.getSelectionModel().getSelectedItems() ; 
		service.deleteRow(this.grid.getStore(), checkedList, "sys.File.delete");
	}
	
	public void retrieve(Long parentId){
		GridRetrieveData<FileModel> service = new GridRetrieveData<FileModel>(grid.getStore());
		service.addParam("parentId", parentId);
		service.retrieve("sys.File.selectByParentId");
	} 
	
	public Grid<FileModel> getGrid(){
		this.grid.setBorders(false);
		return this.grid; 
	}

}
