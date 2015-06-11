package com.ymatou.browser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ymatou.io.XmlOperator;


public class Browser {
	final static String filepath = "E:\\workspace\\SellerAutoTestFunction\\res\\number.xml";
	public static WebDriver driver;
	//private String number;
	private String url;
	private XmlOperator x;
	
	public String getThisUrl(){
		return url;
	}
	
	//设置url
	public void setUrl(String url){
		this.url = url;
	}
	
	/**
	 * 根据输入来选择浏览器的种类
	 * @param browserType [可选值：firefox/ie/chrome/safari]
	 */
	public Browser(String browserType){
		x = new XmlOperator(filepath);
		//this.number = x.getElementValueByName("number");
		x.setValeByName("number", "");
		this.url = x.getElementValueByName("url");
		switch (browserType) {
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "ie":
			File file = new File("C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");		
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver();
			break;
		case "chrome":
			driver = new ChromeDriver();
			break;
		case "safari":
			driver = new SafariDriver();
			break;
		}
	}
	
	
	//logut
	public void logout(){
		
	}
	
	/**
	 * 执行测试步骤
	 * @param step
	 */
	public void excuteStep(List<String> step){
		String operateType = step.get(0).trim().toUpperCase();
		String identifyBy = step.get(1).trim().toUpperCase();
		String locator = step.get(2).trim();
		String value = "";
			switch (operateType) {
			//点击
			case "CLICK":					
				clickElement(identifyBy, locator);
				break;
			//文本框输入内容
			case "SENDKEYS":
				value = step.get(3).trim();
				System.out.println(value);
//				if (!value.contains("@huisky.com") && !value.contains("12345678")) {
//					value = step.get(3).trim() + number;
//					System.out.println(value);
//				}
				setValue(identifyBy, locator, value);
				break;
			//检查点
			case "CHECK":
				if (identifyBy.equals("TEXT")) {
					System.out.println(existText(locator));
				}
				break;
			//等待时间
			case "WAIT":
				wait(5);
				break;
			//下拉框选择值
			case "DSELECT":
				value = step.get(3).trim();
				selectValueByItem(identifyBy,locator,value);
				break;
			//悬停鼠标，并点击悬停菜单中的值
			case "MOUSEOVERANDCLICK":
				System.out.println(identifyBy+locator+value);
				value = step.get(3).trim();
				ClickItemInMouseOver(identifyBy, locator,value);
				//System.out.println("执行退出操作啦！");
				break;
			case "SWITCHTOWINDOW":
				value  = step.get(3).trim();
				switchToWindow(driver, value);
				break;
			case "SWITCHTOFRAME":
				value  = step.get(3).trim();
				switchToFrame(identifyBy, locator);
				break;
			case "SWITCHTO":
				driver.switchTo().defaultContent();
//			case "LINKPROCESS":
//				linkProcess(identifyBy, locator);
//				break;
			case "JUMPTO":
				this.url = locator;
				openUrl();
				break;
			default:
				System.out.println("这是测试数据" + step.toString());
				break;
			}
	}
	
	//操作cookie
	
	
	/**
	 * 浏览器窗口最大化
	 */
	public void maxWindow(){
		driver.manage().window().maximize();
	}

	//打开链接
	public void openUrl(){
		driver.navigate().to(url);
	}
	
	
	// 动态等待控件加载完成（略等于页面加载完成）
	public WebElement waitForLoaded(String identifyBy, String locator)
    {
		identifyBy = identifyBy.toUpperCase();
		WebElement element = null;		
		switch (identifyBy) {
			case "XPATH":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
				break;
			case "ID":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
				break;
			case "NAME":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
				break;
			case "TAGNAME":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.tagName(locator)));
				break;
			case "LINKTEXT":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(locator)));
				break;
			case "CLASSNAME":
				element = (new WebDriverWait(driver,20)).until(ExpectedConditions.presenceOfElementLocated(By.className(locator)));
				break;
		}
	    return element;
    }
	
	
	//返回符合条件的weblement的List
	public List<WebElement>  returnListOfWebElement(String identifyBy,String locator){
		List<WebElement> listOfWebElement = null;
		try {
			listOfWebElement = driver.findElements(By.xpath(locator));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfWebElement;
	}
	
	//根据下拉框的下拉显示的值来选择选项
	public void selectValueByItem(String identifyBy,String locator, String item){
		try {
			WebElement element = waitForLoaded(identifyBy,locator);
			element.findElement(By.xpath("//option[contains(text(),'" + item + "')]")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//检查是否存在list中。
	public boolean checkIsExistInList(String identifyBy,String listLocator,String targetString){
		boolean result = false;
		try {
			List<WebElement> list = returnListOfWebElement(identifyBy,listLocator);
			List<String> strings = new LinkedList<>();
			for (WebElement element : list) {
				String s = element.getText();
				strings.add(s);
			}					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	//clickItem
	public void clickElement(String identifyBy, String locator){
		try {
			identifyBy = identifyBy.toUpperCase();
			WebElement element = waitForLoaded(identifyBy, locator);
			element.click();
		} catch (Exception e) {
			
		}
	}
	
	//元素是否存在
	public boolean existElement(String identifyBy,String locator){
		boolean flag = false;
		identifyBy = identifyBy.toUpperCase();
        try {
            switch (identifyBy) {
                case "XPATH":
                    driver.findElement(By.xpath(locator));
                    flag = true;
                    break;
                case "ID":
                	driver.findElement(By.id(locator));
                    flag = true;
                    break;
                case "NAME":
                	driver.findElement(By.name(locator));
                    flag = true;
                    break;
                case "LINKTEXT":
                	driver.findElement(By.linkText(locator));
                    flag = true;
                    break;
                case "CLASSNAME":
                	driver.findElement(By.className(locator));
                    flag = true;
                    break;
                case "DIVTEXT":
                    List<WebElement> divelements = driver.findElements(By.tagName("div"));
                    for (WebElement divelement : divelements) {
                        if (divelement.getText().contains(locator)) {
                            flag = true;
                        }
                    }
                    break;
                case "OTHER":
                	driver.findElement(By.linkText("xxxxxxxxxxxxxxxxxxxxxxx"));
                    break;
            }
        }catch(NoSuchElementException e){
        	flag=false;
        }
		return flag;
      }
	
	//等待加载，适用于提交数据之后等待数据同步到表格
	public WebElement returnMatchElement(String identifyBy,String expectString,String listLocator,String refreshLocator){
		WebElement element = null;
		try {
			List<WebElement> list = returnListOfWebElement(identifyBy,listLocator);
			for (WebElement webElement : list) {
				String targetString = webElement.getText();
				if (targetString.equals(expectString)) {
					element = webElement;
					break;
				}
				wait(1);
				WebElement refresh = waitForLoaded(identifyBy,refreshLocator);
				refresh.click();
				list = returnListOfWebElement(identifyBy,listLocator);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}
	
	//subelement is existed or not
	public boolean existSubElement(WebElement parent,String identifyBy,String locator){
		boolean flag = false;
		identifyBy = identifyBy.toUpperCase();
        try {
            switch (identifyBy) {
                case "XPATH":
                    parent.findElement(By.xpath(locator));
                    flag=true;
                    break;
                case "ID":
                    parent.findElement(By.id(locator));
                    flag=true;
                    break;
                case "NAME":
                    parent.findElement(By.name(locator));
                    flag=true;
                    break;
                case "LINKTEXT":
                    parent.findElement(By.linkText(locator));
                    flag=true;
                    break;
                case "TAGNAME":
                    parent.findElement(By.tagName(locator));
                    flag=true;
                    break;
                case "OTHER":
                    parent.findElement(By.id("xxxxx"));
                    flag=true;
                    break;
            }
        } catch (NoSuchElementException e) {
            flag = false;
        }
		return flag;
	}
	
	//文本框输入内容
	public void setValue(String identifyBy,String locator,String value){
		identifyBy = identifyBy.toUpperCase();
		WebElement element = waitForLoaded(identifyBy, locator);
		element.click();
		element.clear();
		element.click();
		element.sendKeys(value);
	}
	
	//wait
	public void wait(int seconds){
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	//close current window/page
	public  void close() {
		driver.close();
	}
	
	//quitRunning
	public void quit() {
		driver.quit();
	}
	
	//切换到iframe中
	public void switchToFrame(String identifyBy,String locator) {
		identifyBy = identifyBy.toUpperCase();
		try {
			WebElement element = waitForLoaded(identifyBy, locator);
			driver.switchTo().frame(element);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//切换窗口
	public boolean switchToWindow(WebDriver driver,String windowTitle) {
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle)) {
                    continue;
                } else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(windowTitle)) {
                        flag = true;
                        System.out.println("Switch to window: "
                                + windowTitle + " successfully!");
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } catch (NoSuchWindowException e) {
            System.out.println("Window: " + windowTitle
                    + " could not found!" + e.fillInStackTrace());
            flag = false;
        }
        return flag;
	}
	
	//Exist text in the page
	public boolean existText(String text){
		String pageSource = driver.getPageSource();
		return pageSource.contains(text);
	}
	
	//alert is diepayed or not
	public boolean isAlertPresent(){
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	//alert, ok, cancel
	public void alertOperation(String operateType){
		operateType = operateType.toUpperCase();
		Alert alert = driver.switchTo().alert();
		switch (operateType) {
		case "OK":
			alert.accept();
			break;
		case "CANCEL":
			alert.dismiss();
			break;
		}
	}
	
	//deal potential alert
	public boolean dealPotentialAlert(String option){
		boolean flag = false;
		try {
			if (option == "OK") {
				driver.switchTo().alert().accept();
			}
			else {
				driver.switchTo().alert().dismiss();
			}
		} catch (NoSuchElementException e) {
			System.out.println(e.toString());
		}
		return flag;
	}
	
	//run javascript
	public void runJS(String jsstring){
		JavascriptExecutor js;
		js = (JavascriptExecutor) driver;
		js.executeScript(jsstring);
	}
	
	//Screenshot
	public static void screenShot(String filename, String savedPath){
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			System.out.println("save snapshot path is:/" + filename);
			FileUtils.copyFile(scrFile, new File(savedPath + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		}finally{
			System.out.println("screen shot finished");
		}
	}
	
	/**
	 * Table related
	 */
	public String getTableText(String identifyBy,String locator){
		String  s = "";
		return s;
	}
	
	//get url;
	public String getUrl(){
		return driver.getCurrentUrl();
	}
	
	public String getTitle(){
		return driver.getTitle();
	}
	
	//get link href
	public String getLinkHref(String identifyBy, String locator){
		String href="";
        try {
            identifyBy = identifyBy.toUpperCase();
            WebElement element = waitForLoaded(identifyBy, locator);
            href= element.getAttribute("href");
        } catch (NoSuchElementException e) {
        	System.out.println(e.toString());
        }
        return href;
	}
	
	//下拉框选择值
	public String getSelectedValueOfSelector(WebElement selector){
		String selected="";
		List<WebElement> options = selector.findElements(By.tagName("option"));
		for (WebElement webElement : options) {
			if(webElement.isSelected()){
				selected = webElement.getText();
			}
		}
		return selected;
	}
	
	//获得控件的相关属性值
	public String getElemntValue(String identifyBy,String locator){
		identifyBy = identifyBy.toUpperCase();
		String value = "";
		WebElement element = waitForLoaded(identifyBy, locator);
		String tagName="";
            //最好是根据获取到的对象来判断存在哪些属性值，然后来取对应的属性值
            //控件还不一定存在tagName
            tagName = element.getAttribute("tagName");
            if (tagName.equalsIgnoreCase("input")) {
                value = element.getAttribute("value");
            }
            if (tagName.equalsIgnoreCase("div")) {
                value = element.getText();
            }
            if (tagName.equalsIgnoreCase("span")) {
                value = element.getAttribute("innerHTML");
            }
            if (tagName.equalsIgnoreCase("select")) {
                value = getSelectedValueOfSelector(element);
            }
            if (tagName.equalsIgnoreCase("a")) {
                value = locator;
            }
        return value;
	}
	
	
	//模拟鼠标悬停操作
	public void ClickItemInMouseOver(String identifyBy,String locator,String value){
		identifyBy = identifyBy.toUpperCase();
		Actions ac = new Actions(driver);
		WebElement element = waitForLoaded(identifyBy,locator);
        ac.moveToElement(element).perform();
        clickElement(identifyBy, value);
	}
	
	
	//获取图片路径
	public String getImgSrc(String identifyBy,String locator){
		identifyBy = identifyBy.toUpperCase();
		WebElement element = waitForLoaded(identifyBy,locator);
    return element.getAttribute("src");
	}
	
	//选人控件通用方法，总是选择第一个高亮的人选
	public void chooseFirstMan(String identifyBy, String locator, String name){
		try {
			WebElement element = waitForLoaded(identifyBy,locator);
			element.click();
			element.clear();
			element.sendKeys(name);
			wait(3);
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void chooseColorForProudct(String locator,String targetName){
		List<WebElement> elements = driver.findElements(By.xpath(locator));
		//List<String> elementsName = new ArrayList<String>();
 		for (WebElement element : elements) {
			//elementsName.add(element.getText());
 			if (element.getText().equals(targetName)) {
 				//System.out.println(targetName);
 				//System.out.println(element.getText());
				element.click();
				List<WebElement> colorElements = element.findElements(By.xpath("//b[@class='label' and contains(text(),'"+ targetName +"')]//parent::div//parent::div/a"));
				System.out.println("选择的第一个颜色/尺码： "+colorElements.get(0).getText());
				colorElements.get(0).click();
			}
 			element = null;
		}
	}

}

