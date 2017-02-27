package test;

import com.jcraft.jsch.Session;
import org.json.simple.parser.ParseException;
import sample.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Hello world!
 *
 */
public class TestSandBox {


	 public void test() throws IOException{
	  Functions functions = new Functions();


	  /** try{
		  functions.getWebDriver("Chrome");

		  CreateTestFiles ctf = new CreateTestFiles();
		  Time time = new Time();
		  String todaysDate = time.getTodaysDate("yyyyMMdd");
		  ArrayList<String> testFile = ctf.createOutput("C:/Users/david_him/Documents/Projects/BofA/WO15157/testfiles/testCaseMisc.txt",
				  "C:/Users/david_him/Documents/Projects/BofA/WO15157/testfiles/varolii.std.in.20161207.misc.dat",
				  ",", "varolii.std.in."+ todaysDate + ".misc.[FieldName].dat", true);

		  for(String fileName: testFile) {

			  HashMap<String,String> testFileMap = ctf.parseFieldsDelimited(fileName, ",");

			  String inputId = functions.dropFilePeTester("sst01", fileName, "2695", "BAC_MISC_COLL", "v20",
					"FileMonitor", "50000");
			  String trackingId = functions.getTrackingID(inputId);
			  SSHFactory ssh = new SSHFactory();
			  JSONFactory json = new JSONFactory();

			  Session sshSession = ssh.connectToSSH("svc-app03.vrli.net", 22);
			  String jsonText = ssh.sendCommand(sshSession,"curl http://sst-chiweb01:8080/api/rest/records/" + trackingId + "|python -m json.tool");

			  HashMap<String,String> JSONMap = json.parseJSONMap(jsonText);

			  ValidationFactory validate = new ValidationFactory();
			  validate.validatePETesterInputMapping(testFileMap, JSONMap);
		  }
	  }
	  catch(Exception e){
	   System.out.println(e.getMessage());
	   **/
	 }

	public static void main(String[] args) throws IOException, ParseException {
		TestSandBox app = new TestSandBox();
		app.test();
	}
}