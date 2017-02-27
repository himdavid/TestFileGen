package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class Functions {
    private WiniumDriver winiumDriver;
    private WebDriver webDriver;
    private String PE_TESTER_LOGIN_PAGE = "https://qa-tools.vrli.net/petester/user_mgt/login";
    private String PE_TESTER_USERNAME_LOCATOR = "input[name=\"username\"]";
    private String PE_TESTER_USERNAME_VALUE = "dhim";
    private String PE_TESTER_PASSWORD_LOCATOR = "input[name=\"password\"]";
    private String PE_TESTER_PASSWORD_VALUE = "Corp@123";
    private String PE_TESTER_LOGIN_BUTTON_LOCATOR = "input[type=\'submit\']";
    private String PE_TESTER_INPUTFILE_LINK_LOCATOR = "a[href=\'/petester/input/\']";
    //private String PE_TESTER_HOST_LOCATOR = "#hostname";
    private String PE_TESTER_CLIENTID_LOCATOR = "input#clientSysId";
    private String PE_TESTER_SOLUTIONNAME_LOCATOR = "#solutionId";
    private String PE_TESTER_SOLUTIONVERSION_LOCATOR = "#solutionVersionId";
    private String PE_TESTER_FILEMONITORTASK_LOCATOR = "#fileMonitorTask";
    private String PE_TESTER_TIMEOUT_LOCATOR = "#fileDropTimeOut";
    private String PE_TESTER_DROPFILE_BUTTON_LOCATOR = "#drop";
    private String PE_TESTER_CHOOSEFILE_LOCATOR = "#svp_file";
    private String PE_TESTER_UPLOADFILE_Button_LOCATOR = "button[value=\"Upload File\"]";
    private String PE_TESTER_INPUTID_TEXT_LOCATOR = "//*[contains(text(),\'InputId\')]";
    private String PE_TESTER_CLEAR_BUTTON_LOCATOR = "#clear_console";

    public Functions() {
    }

    /**
     * Create the WiniumDriver
     * @param applicationPath, the field path to the application 
     * @return winiumDriver, the winiumDriver
     * @throws MalformedURLException
     */
    public WiniumDriver getWiniumDriver(String applicationPath) throws MalformedURLException {
        DesktopOptions options = new DesktopOptions();
        options.setApplicationPath(applicationPath);
        WiniumDriver winiumDriver = new WiniumDriver(new URL("http://localhost:9999"), options);
        this.winiumDriver = winiumDriver;
        return winiumDriver;
    }

    public WebDriver getWebDriver(String browser) {
        if(browser.equals("Chrome")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            ChromeDriver webDriver = new ChromeDriver();
            this.webDriver = webDriver;
        }

        return this.webDriver;
    }

    public void waitForElement(String elementID) throws InterruptedException {
        while(this.winiumDriver.findElementsById(elementID).size() == 0) {
            System.out.println("Waiting for the phone to ring");
        }

        System.out.println("Element Found!!!");
    }

    public WebElement findElementByCssSelector(String css) throws InterruptedException {
        while(this.webDriver.findElements(By.cssSelector(css)).size() == 0) {
            Thread.sleep(1000L);
        }

        return this.webDriver.findElement(By.cssSelector(css));
    }

    public WebElement findElementByXpathSelector(String xpath) throws InterruptedException {
        while(this.webDriver.findElements(By.xpath(xpath)).size() == 0) {
            Thread.sleep(1000L);
            System.out.println("Waiting for element to appear");
        }

        return this.webDriver.findElement(By.xpath(xpath));
    }

    public void selectElementFromDropDown(String cssSelectorDropDown, String text) {
        WebElement dropDownElement = this.webDriver.findElement(By.cssSelector(cssSelectorDropDown));
        Select select = new Select(dropDownElement);
        select.selectByValue(text);
    }

    public String trimInputId(String inputIdString) {
        return inputIdString.replace("InputId:", "").trim();
    }

    public String dropFilePeTester(String host, String testFilePath, String clientId, String solutionName, String version, String fileMonitorTask, String timeOut) throws InterruptedException {
        this.webDriver.get(this.PE_TESTER_LOGIN_PAGE);
        if(this.webDriver.getCurrentUrl().equals(this.PE_TESTER_LOGIN_PAGE)) {
            this.findElementByCssSelector(this.PE_TESTER_USERNAME_LOCATOR).sendKeys(new CharSequence[]{this.PE_TESTER_USERNAME_VALUE});
            this.findElementByCssSelector(this.PE_TESTER_PASSWORD_LOCATOR).sendKeys(new CharSequence[]{this.PE_TESTER_PASSWORD_VALUE});
            this.findElementByCssSelector(this.PE_TESTER_LOGIN_BUTTON_LOCATOR).click();
        }

        this.findElementByCssSelector(this.PE_TESTER_INPUTFILE_LINK_LOCATOR).click();
        Thread.sleep(5000L);
        this.findElementByCssSelector("option[value=\"" + host + "\"]").click();
        this.findElementByCssSelector(this.PE_TESTER_CHOOSEFILE_LOCATOR).sendKeys(testFilePath);
        this.findElementByCssSelector(this.PE_TESTER_CLIENTID_LOCATOR).clear();
        this.findElementByCssSelector(this.PE_TESTER_CLIENTID_LOCATOR).sendKeys(clientId);
        this.findElementByCssSelector(this.PE_TESTER_SOLUTIONNAME_LOCATOR).clear();
        this.findElementByCssSelector(this.PE_TESTER_SOLUTIONNAME_LOCATOR).sendKeys(solutionName);
        this.findElementByCssSelector(this.PE_TESTER_SOLUTIONVERSION_LOCATOR).clear();
        this.findElementByCssSelector(this.PE_TESTER_SOLUTIONVERSION_LOCATOR).sendKeys(version);
        this.findElementByCssSelector(this.PE_TESTER_FILEMONITORTASK_LOCATOR).clear();
        this.findElementByCssSelector(this.PE_TESTER_FILEMONITORTASK_LOCATOR).sendKeys(fileMonitorTask);
        this.findElementByCssSelector(this.PE_TESTER_TIMEOUT_LOCATOR).clear();
        this.findElementByCssSelector(this.PE_TESTER_TIMEOUT_LOCATOR).sendKeys(timeOut);
        this.findElementByCssSelector(this.PE_TESTER_UPLOADFILE_Button_LOCATOR).click();
        this.findElementByCssSelector(this.PE_TESTER_DROPFILE_BUTTON_LOCATOR).click();
        String inputId = this.trimInputId(this.findElementByXpathSelector(this.PE_TESTER_INPUTID_TEXT_LOCATOR).getText());
        this.findElementByCssSelector(this.PE_TESTER_CLEAR_BUTTON_LOCATOR).click();
        System.out.println(inputId);
        return inputId;
    }

    public void JDBCconnection() {
        //String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
        String DB_URL = "jdbc:oracle:thin:@sst-bch01-vip.vrli.net:1580:bch01";
        String USER = "sst01_ch_appl";
        String PASS = "sst01_ch_appl";

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Connecting to a selected database...");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            System.out.println("Creating statement...");
            Statement stmt = conn.createStatement();
            String e = "SELECT di.DC_ALERT_SYSID, di.KEYWORD, di.VALUE  FROM DISPATCH_INTERACT di JOIN ALERT a ON a.DC_ALERT_SYSID = di.DC_ALERT_SYSID WHERE di.DC_ALERT_SYSID = (SELECT a.DC_ALERT_SYSID \t\t\t\t\t\tFROM RECORD r \t\t\t\t\t\tJOIN ALERT a ON a.TRACKING_ID = r.TRACKING_ID AND r.FIRST_NAME = \'Robo\' AND r.LAST_NAME = \'Cop\' AND r.WHEN_CREATED < trunc(sysdate) AND ROWNUM = 1) AND di.dispatch_seq = 1";
            ResultSet rs = stmt.executeQuery(e);

            while(rs.next()) {
                String sysid = rs.getString("DC_ALERT_SYSID");
                String keyword = rs.getString("KEYWORD");
                String value = rs.getString("VALUE");
                System.out.println("Alert SysID: " + sysid);
                System.out.println("Keyword: " + keyword);
                System.out.println("Value: " + value);
                System.out.println("==================================");
            }

            rs.close();
        } catch (SQLException ex) {
            ;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public String getTrackingID(String inputID) throws InterruptedException {
        String trackingId = null;
        //String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
        String DB_URL = "jdbc:oracle:thin:@sst-bch01-vip.vrli.net:1580:bch01";
        String USER = "sst01_ch_appl";
        String PASS = "sst01_ch_appl";

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Connecting to a selected database...");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            System.out.println("Creating statement...");
            Statement stmt = conn.createStatement();
            String e = "SELECT TRACKING_ID from RECORD WHERE INPUT_ID=\'" + inputID + "\'";
            System.out.println(e);
            Thread.sleep(10000);
            ResultSet rs = stmt.executeQuery(e);

            while(rs.next()) {
                trackingId = rs.getString("TRACKING_ID");
                System.out.println("Tracking ID is: " + trackingId);
            }
            rs.close();
        } catch (SQLException ex) {
            ;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return trackingId;
    }
}
