
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.ymatou.browser.Browser;
import com.ymatou.io.ExcelOperator;


public class MainTestLogic {

	public static void main(String[] args) {
		//ҵ��������߼�.
		
		
		ExcelOperator eo = new ExcelOperator();
		Browser b = new Browser("ie");
		b.setUrl("http://seller.alpha.ymatou.com");
		MainTestLogic logic = new MainTestLogic();
//		logic.testXMLOperator();
		b.openUrl();
		b.maxWindow();
//		logic.runTestCase(eo,b);			
	}
	
	public void runTestCase(TestCase testCase){
		ArrayList<String[]>
	}
	
//	public void runTestCase(ExcelOperator eo,Browser b) {
//		LinkedHashMap<String, List<List<String>>> hMap = new LinkedHashMap<String, List<List<String>>>();
//		hMap = eo.generateAllTestCase();
//		List<String> testCaseName = eo.returnAllSheetName();
//		for (String string : testCaseName) {
//			System.out.println("��ǰ���е��� " + string);
//			List<List<String>> steps = hMap.get(string);
//			for (List<String> list : steps) {
//				System.out.println("��ǰִ�еĲ����ǣ� " + list.toString());
//				b.excuteStep(list);
//			}
//			String url = b.getUrl();
//			if (url.contains("https")) {
//				b.logout();
//			}
//			b.openUrl();
//			b.maxWindow();
//		}
//		b.quit();
//	}
}
