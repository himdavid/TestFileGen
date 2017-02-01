package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class CreateTestFiles {
    private BufferedReader br;
    private String delimiter;

    public CreateTestFiles() {
    }

    public HashMap<String, String> parseFieldsDelimited(String filePath, String delimiter) throws IOException {
        this.delimiter = delimiter;
        this.br = new BufferedReader(new FileReader(filePath));
        String[] column = null;
        String[] value = null;
        int line = 0;

        String parsedFields;
        LinkedHashMap map;
        for(map = new LinkedHashMap(); (parsedFields = this.br.readLine()) != null; ++line) {
            if(line == 0) {
                column = parsedFields.split(delimiter);
            } else if(line == 1) {
                value = parsedFields.split(delimiter);
            }
        }

        for(int i = 0; i <= column.length - 1; ++i) {
            map.put(column[i], value[i]);
        }

        return map;
    }

    public HashMap<String, ConfigData> parseFieldsFixedWidth(String baseLineFile, String configFile, boolean header, boolean footer) throws IOException {
        BufferedReader br1 = new BufferedReader(new FileReader(baseLineFile));
        BufferedReader br2 = new BufferedReader(new FileReader(configFile));
        String parsedBaseLineFileValue = null;
        LinkedHashMap map = new LinkedHashMap();

        String parsedBaseLineFile;
        for(int countLineBaseLineFile = 0; (parsedBaseLineFile = br1.readLine()) != null; ++countLineBaseLineFile) {
            if(header && footer && countLineBaseLineFile == 1) {
                parsedBaseLineFileValue = parsedBaseLineFile;
            }

            if(header && !footer && countLineBaseLineFile == 1) {
                parsedBaseLineFileValue = parsedBaseLineFile;
            }

            if(!header && footer && countLineBaseLineFile == 0) {
                parsedBaseLineFileValue = parsedBaseLineFile;
            }

            if(!header && !footer && countLineBaseLineFile == 0) {
                parsedBaseLineFileValue = parsedBaseLineFile;
            }
        }

        String parsedConfigFile;
        while((parsedConfigFile = br2.readLine()) != null) {
            String[] configFields = parsedConfigFile.split("\\t");
            String configFieldName = configFields[0];
            int configBeginIndex = Integer.parseInt(configFields[1]);
            int configEndIndex = Integer.parseInt(configFields[2]);
            String baseLineValue = parsedBaseLineFileValue.substring(configBeginIndex - 1, configEndIndex);
            ConfigData cd = new ConfigData(configFieldName, configBeginIndex, configEndIndex, baseLineValue);
            map.put(configFieldName, cd);
        }

        br1.close();
        br2.close();
        return map;
    }

    public HashMap<String, ConfigData> updateFieldValueFixedWidth(HashMap<String, ConfigData> map, List<String> testCase) throws IOException {
        for(int i = 0; i < testCase.size(); ++i) {
            String field = ((String)testCase.get(i)).split("=")[0];
            String value = ((String)testCase.get(i)).split("=")[1].replaceAll("\"", "");
            System.out.println("Found field \n Field Name:" + value + "\n Old Value:" + ((ConfigData)map.get(field)).getValue());
            ConfigData cd = (ConfigData)map.get(field);
            int beginIndex = cd.getBeginIndex();
            int endIndex = cd.getEndIndex();
            if(value.length() != endIndex - beginIndex - 1) {
                System.out.println("Not enough characters in test case");
            }

            cd.setValue(value);
            map.put(field, cd);
            System.out.println(" New Value:" + ((ConfigData)map.get(field)).getValue());
        }

        return map;
    }

    public HashMap<String, String> updateFieldValue(HashMap<String, String> map, List<String> testCase) throws IOException {
        for(int i = 0; i < testCase.size(); ++i) {
            String field = ((String)testCase.get(i)).split("=")[0];
            String value = ((String)testCase.get(i)).split("=")[1].replaceAll("\"", "");
            System.out.println("Found field \n Field Name:" + field + "\n Old Value:" + (String)map.get(field));
            map.put(field, value);
            System.out.println(" New Value:" + (String)map.get(field));
        }

        return map;
    }

    public File createOutputFixedWidth(String testCaseFilePath, String testDataFilePath, String configFilePath, String fileNameFormat, boolean header, boolean footer) throws IOException {
        ArrayList testCasesLists = new ArrayList();
        File testFile = null;
        BufferedReader br = new BufferedReader(new FileReader(testCaseFilePath));
        String content = null;
        String filePath = testCaseFilePath.substring(0, testCaseFilePath.lastIndexOf("/")) + "/";

        while((content = br.readLine()) != null) {
            if(content.contains(",")) {
                testCasesLists = new ArrayList(Arrays.asList(content.split(",")));
            } else {
                testCasesLists.add(content);
            }

            HashMap map = this.parseFieldsFixedWidth(testDataFilePath, configFilePath, header, footer);
            HashMap updatedMap = this.updateFieldValueFixedWidth(map, testCasesLists);
            String fileName = filePath + fileNameFormat.replace("[FieldName]", content.replace("\"", ""));
            fileName = fileName.substring(0, Math.min(fileName.length(), 260));
            testFile = new File(fileName);
            File tempFileValue = new File(filePath + "tempValue.dat");
            FileOutputStream fopsFinalFile = new FileOutputStream(testFile);
            FileOutputStream fopsValue = new FileOutputStream(tempFileValue);
            System.out.println(testFile.getAbsolutePath());
            System.out.println("file length is: " + testFile.getAbsolutePath().length());
            Iterator brValue = updatedMap.entrySet().iterator();

            String fopsFooter;
            while(brValue.hasNext()) {
                Entry copyValue = (Entry)brValue.next();
                String tempFileFooter = (String)copyValue.getKey();
                fopsFooter = ((ConfigData)copyValue.getValue()).getValue();
                fopsValue.write(fopsFooter.getBytes());
                System.out.println("Field:" + tempFileFooter + " \n Value:" + fopsFooter);
            }

            if(header) {
                File brValue1 = new File(filePath + "tempHeader.dat");
                FileOutputStream copyValue1 = new FileOutputStream(brValue1);
                copyValue1.write(this.getHeader(testDataFilePath).getBytes());
                BufferedReader tempFileFooter1 = new BufferedReader(new FileReader(brValue1));
                fopsFooter = tempFileFooter1.readLine() + "\n";
                fopsFinalFile.write(fopsFooter.getBytes());
                copyValue1.close();
                tempFileFooter1.close();
                brValue1.delete();
            }

            fopsValue.close();
            BufferedReader brValue2 = new BufferedReader(new FileReader(tempFileValue));
            String copyValue2 = brValue2.readLine();
            fopsFinalFile.write(copyValue2.getBytes());
            brValue2.close();
            tempFileValue.delete();
            if(footer) {
                File tempFileFooter2 = new File(filePath + "tempFooter.dat");
                FileOutputStream fopsFooter1 = new FileOutputStream(tempFileFooter2);
                fopsFooter1.write(this.getFooter(testDataFilePath, header).getBytes());
                BufferedReader brFooter = new BufferedReader(new FileReader(tempFileFooter2));
                String copyFooter = "\n" + brFooter.readLine();
                fopsFinalFile.write(copyFooter.getBytes());
                fopsFooter1.close();
                brFooter.close();
                tempFileFooter2.delete();
            }

            testCasesLists.clear();
            fopsFinalFile.close();
        }

        br.close();
        return testFile;
    }

    public ArrayList<String> createOutput(String testCaseFilePath, String testDataFilePath, String delimiter, String fileNameFormat, boolean multipleFilesFlag) throws IOException {
        ArrayList testCasesLists = null;
        File testFile = null;
        ArrayList testFileList = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(testCaseFilePath));
        String filePath = testCaseFilePath.substring(0, testCaseFilePath.lastIndexOf("/")) + "/";

        String content;
        while((content = br.readLine()) != null) {
            if(content.contains(",")) {
                testCasesLists = new ArrayList(Arrays.asList(content.split(",")));
            } else {
                testCasesLists.add(content);
            }

            HashMap map = this.parseFieldsDelimited(testDataFilePath, delimiter);
            HashMap updatedMap = this.updateFieldValue(map, testCasesLists);
            String fileName = filePath + fileNameFormat.replace("[FieldName]", content.replace("\"", ""));
            fileName = fileName.substring(0, Math.min(fileName.length(), 260));
            testFileList.add(fileName);
            if(multipleFilesFlag) {
                testFile = new File(fileName);
            } else {
                testFile = new File(filePath + fileNameFormat.replace("[FieldName]", "MultipleRecords"));
            }

            File tempFile = new File("temp.dat");
            FileOutputStream fops1 = new FileOutputStream(testFile);
            FileOutputStream fops2 = new FileOutputStream(tempFile);
            System.out.println(testFile.getAbsolutePath());
            System.out.println("file length is: " + testFile.getAbsolutePath().length());
            int setSize = updatedMap.entrySet().size();
            int count = 1;

            for(Iterator br2 = updatedMap.entrySet().iterator(); br2.hasNext(); ++count) {
                Entry copyString = (Entry)br2.next();
                String field = (String)copyString.getKey();
                String value = (String)copyString.getValue();
                fops1.write(field.getBytes());
                fops2.write(value.getBytes());
                if(count < setSize) {
                    fops1.write(delimiter.getBytes());
                    fops2.write(delimiter.getBytes());
                }
            }

            testCasesLists.clear();
            BufferedReader var25 = new BufferedReader(new FileReader(tempFile));
            String var24 = "\n" + var25.readLine();
            fops1.write(var24.getBytes());
            fops1.close();
            fops2.close();
            var25.close();
            tempFile.delete();
        }

        br.close();
        return testFileList;
    }

    public String getHeader(String baseLineFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(baseLineFile));
        String header = null;

        String parsedBaseLine;
        for(int countLine = 0; (parsedBaseLine = br.readLine()) != null; ++countLine) {
            if(countLine == 0) {
                header = parsedBaseLine;
            }
        }

        br.close();
        return header;
    }

    public String getFooter(String baseLineFile, boolean header) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(baseLineFile));
        String footer = null;

        String parsedBaseLine;
        for(int countLine = 0; (parsedBaseLine = br.readLine()) != null; ++countLine) {
            if(countLine == 2 && header) {
                footer = parsedBaseLine;
            }

            if(countLine == 1 && !header) {
                footer = parsedBaseLine;
            }
        }

        br.close();
        return footer;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public static void main(String[] args) throws IOException {
        CreateTestFiles ctf = new CreateTestFiles();
        Time time = new Time();
        String todaysDate = time.getTodaysDate("yyyyMMdd");
        ctf.createOutputFixedWidth("C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/testCase.txt", "C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/PROD.CM.DIALER.AUTO.NUANCE.20161026", "C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/config.txt", "PROD.CM.DIALER.AUTO.NUANCE." + todaysDate + "_[FieldName]", false, false);
    }
}
