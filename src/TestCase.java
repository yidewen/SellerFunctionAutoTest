import java.util.ArrayList;

public class TestCase {
	ArrayList<String[]> case1 = new ArrayList<String[]>();
	
	String[] step1 = {"sendkeys","xpath","//input[@id='loginName']","wesper"};
	String[] step2 = {"sendkeys","xpath","//input[@id='loginPwd']","abc123"};
	String[] step3 = {"click","xpath","//input[@id='btnSubmit']"};
	String[] step4 = {"click","xpath","//a[@class='close right_top']"};
	String[] step5 = {"check","xpath","text","µêÆÌ×ÜÀÀ"};
	String[] step6 = {"mouseoverandclick","xpath","//a[contains(text(),'ÉÌÆ·')]"};
	
	public TestCase() {
		// TODO Auto-generated constructor stub
		case1.add(step1);
		case1.add(step2);
		case1.add(step3);
		case1.add(step4);
		case1.add(step5);
		case1.add(step6);
	}
}
