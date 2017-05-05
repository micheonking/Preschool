package myApp.service.psc;
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import myApp.client.psc.model.StudentModel;
import myApp.server.DatabaseFactory;
import myApp.server.pdf.CellLayout;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class StudentPDF {

	private StudentModel getStudentModel(String studentId){
		SqlSession sqlSession = DatabaseFactory.openSession();
		StudentModel studentModel = sqlSession.selectOne("psc03_student.selectById", Long.parseLong(studentId) ); 
		return studentModel ; 
	}
	
	private PdfPCell createImageCell(String studentId) throws DocumentException, IOException {
		String filePath = "D:\\WebFiles\\" ;
		String imageFile = filePath + (Long.parseLong(studentId)/100) + "\\"  + studentId ;
		File file = new File(imageFile); 
		
		if(!file.exists()){
			// 사진이 없는 경우 
			imageFile = filePath + "default.jpg" ;
		}
		
		Image img = Image.getInstance(imageFile);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setPadding(3);
        return cell;
        
    }
	
	public void getDocument(BufferedOutputStream bufferedOutputStream, HttpServletRequest request) throws DocumentException, IOException{
		
		
		StudentModel studentModel = this.getStudentModel(request.getParameter("studentId")); 
		
		Document document = new Document(PageSize.A4, 0, 0, 50, 0);
		PdfWriter.getInstance(document, bufferedOutputStream);
		document.open();
		
		CellLayout cellLayout = new CellLayout(10);
		
		PdfPTable table = new PdfPTable(7); // column count 
		table.setWidthPercentage(90);
		table.setWidths(new int[]{15, 10, 15, 10, 15, 10, 15});
		
		PdfPCell cell;
		
		cell = createImageCell(studentModel.getStudentId().toString()); 
		cell.setRowspan(7);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		
		table.addCell(cellLayout.getCell("원생번호"));
		table.addCell(cellLayout.getCell(studentModel.getStudentNo()));

		table.addCell(cellLayout.getCell("한글이름")); 
		table.addCell(cellLayout.getCell(studentModel.getKorName()));
		
		table.addCell(cellLayout.getCell("주민번호")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("반이름")); 
		table.addCell(cellLayout.getCell(""));
		
		table.addCell(cellLayout.getCell("배정일"));
		table.addCell(cellLayout.getCell(""));
		
		table.addCell(cellLayout.getCell("남녀구분")); 
		table.addCell(cellLayout.getCell(""));
		
		table.addCell(cellLayout.getCell("생일")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("영문명")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("한자명")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("헨드폰")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("집전화")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("이메일")); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("성격")); 

		cell = cellLayout.getCell("아주좋음"); 
		cell.setColspan(3);
//	    cell.setFixedHeight(20f);
//	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    
	    table.addCell(cell);
		
		table.addCell(cellLayout.getCell("특기")); 
		table.addCell(cellLayout.getCell(""));
		
		table.addCell(cellLayout.getCell("버릇")); 
		table.addCell(cellLayout.getCell(""));
		
		cell = cellLayout.getCell("좋아하는\r음식"); 
		cell.setFixedHeight(28f);
		table.addCell(cell); 
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("싫어하는\r음식"));
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("희망학교"));
		table.addCell(cellLayout.getCell(""));

		table.addCell(cellLayout.getCell("이전학력")); 
		cell = cellLayout.getCell(""); 
		cell.setColspan(3);
		table.addCell(cell);
		
		cell = cellLayout.getCell("특기사항"); 
		cell.setFixedHeight(42f);
		table.addCell(cell);
		
		
		cell = cellLayout.getCell("");
		cell.setColspan(6);
		table.addCell(cell);
		
		document.add(table);

        
        Paragraph p = new Paragraph(" ");
        document.add(p); 
          
		PdfPTable table2 = new PdfPTable(8);
		table2.setWidthPercentage(90);

		table2.setWidths(new int[]{10, 15, 10, 15, 10, 15, 10, 15});
		
		table2.addCell(cellLayout.getCell("헨드폰")); 
		table2.addCell(cellLayout.getCell(""));

		table2.addCell(cellLayout.getCell("집전화")); 
		table2.addCell(cellLayout.getCell(""));

		table2.addCell(cellLayout.getCell("이메일")); 
		table2.addCell(cellLayout.getCell(""));
		
		table2.addCell(cellLayout.getCell("이메일"));
		table2.addCell(cellLayout.getCell(""));
		
		
		document.add(table2);
		
		document.close();
	}
	

}