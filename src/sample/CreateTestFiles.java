package sample;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class CreateTestFiles {
    private BufferedReader br;
    private String delimiter;

    public CreateTestFiles() {
    }

    /**
     *
     * @param filePath
     * @param delimiter
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param baseLineFile
     * @param configFile
     * @param header
     * @param footer
     * @return
     * @throws IOException
     */
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
            String configFieldName = configFields[0].trim();
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

    /**
     *
     * @param map
     * @param testCase
     * @return
     * @throws IOException
     */
    public HashMap<String, ConfigData> updateFieldValueFixedWidth(HashMap<String, ConfigData> map, List<String> testCase) throws IOException {
        for(int i = 0; i < testCase.size(); ++i) {
            String field = ((String)testCase.get(i)).split("=")[0];
            String value = ((String)testCase.get(i)).split("=")[1].replaceAll("\"", "");
            //System.out.println("Found field \n Field Name:" + value + "\n Old Value:" + ((ConfigData)map.get(field)).getValue());
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

    /**
     *
     * @param map
     * @param testCase
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param delimiterType
     * @param testCaseFilePath
     * @param testDataFilePath
     * @param configFilePath
     * @param delimiter
     * @param fileNameFormat
     * @param multipleFilesFlag
     * @param header
     * @param footer
     * @return
     * @throws IOException
     */
    public ArrayList<String> createOutput(String delimiterType, String testCaseFilePath, String testDataFilePath,
                                          String configFilePath, String delimiter, String fileNameFormat,
                                          String multipleFilesFlag, boolean header, boolean footer) throws IOException {

        ArrayList testCasesLists = new ArrayList();
        ArrayList testFileList = new ArrayList();
        File testFile = null;
        File tempFile = null;
        FileOutputStream fops1 = null;
        FileOutputStream fops2 = null;
        Boolean createHeader = header;
        Boolean createFooter = footer;
        String headerString = "";
        String footerString = "";



        /**Get the file path of the test case file */
        String filePath = testCaseFilePath.substring(0, testCaseFilePath.lastIndexOf("\\"))
                + "\\" + "Input File " +this.getDateTime() + "\\";

        new File(filePath).mkdir();

        /** Read the test case file */
        BufferedReader br = new BufferedReader(new FileReader(testCaseFilePath));
        String content;
        while((content = br.readLine()) != null) {

            HashMap map;
            HashMap updatedMap = null;

            /** Split the contents of the test case file by the a comma */
            if(content.contains(",")) {
                testCasesLists = new ArrayList(Arrays.asList(content.split(",")));
            } else {
                testCasesLists.add(content);
            }
            /** Put the test case into the file name */
            String fileName = filePath + fileNameFormat.replace("[FieldName]", content.replace("\"", ""));
            fileName = fileName.substring(0, Math.min(fileName.length(), 260));

            testFileList.add(fileName);

            /** Only create the temp file once if the desired output type is a single file */
            if(multipleFilesFlag.equals("Single") && testFile == null && tempFile == null) {
                testFile = new File(fileName);
                fops1 = new FileOutputStream(testFile);
                tempFile = new File(filePath +"temp.dat");
                fops2 = new FileOutputStream(tempFile);
            } else if(multipleFilesFlag.equals("Multiple")) {
                testFile = new File(fileName);
                fops1 = new FileOutputStream(testFile);
                tempFile = new File(filePath +"temp.dat");
                fops2 = new FileOutputStream(tempFile);
            }

            if(delimiterType.equals("Delimited")) {
                map = this.parseFieldsDelimited(testDataFilePath, delimiter);
                updatedMap = this.updateFieldValue(map, testCasesLists);
            } else if(delimiterType.equals("Fixed width")) {
                map = this.parseFieldsFixedWidth(testDataFilePath, configFilePath, header, footer);
                updatedMap = this.updateFieldValueFixedWidth(map, testCasesLists);
            }

            int setSize = updatedMap.entrySet().size();
            int count = 1;

            for(Iterator br2 = updatedMap.entrySet().iterator(); br2.hasNext(); ++count) {
                Entry copyString = (Entry)br2.next();
                String field = (String)copyString.getKey();
                String value;
                if(delimiterType.equals("Delimited")) {
                    value = (String) copyString.getValue();
                } else {
                    ConfigData configData = (ConfigData) copyString.getValue();
                    value = configData.getValue();
                }
                if(count < setSize) {
                    if(createHeader == true && delimiterType.equals("Delimited")) {
                        fops1.write(field.getBytes());
                        fops1.write(delimiter.getBytes());
                    }
                    fops2.write(value.getBytes());
                    if(delimiterType.equals("Delimited")) {
                        fops2.write(delimiter.getBytes());
                    }
                }
            }
            if(createHeader == true && delimiterType.equals("Fixed width")){
                headerString = this.getHeader(testDataFilePath);
                if(createFooter == true) {
                    footerString = this.getFooter(testDataFilePath, true);
                }
                fops1.write(headerString.getBytes());
            }


            fops2.write("\n".getBytes());
            if(multipleFilesFlag.equals("Single")) {
                createHeader = false;
            }

            testCasesLists.clear();
            if(multipleFilesFlag.equals("Multiple")) {
                BufferedReader var25 = new BufferedReader(new FileReader(tempFile));
                String var24 = "";
                if(delimiterType.equals("Delimited")) {
                    var24 = var25.readLine() + "\n" + footerString;
                } else {
                    if(!headerString.equals("")) {
                        var24 = var24 + "\n";
                    }
                    var24 = var24 + var25.readLine();
                    if(!footerString.equals("")) {
                        var24 = var24 + "\n" + footerString;
                    }
                }
                fops1.write(var24.getBytes());
                var25.close();
                fops1.close();
                fops2.close();
                tempFile.delete();

            }
        }
        if(multipleFilesFlag.equals("Single")) {
            fops2.close();
            BufferedReader var25 = new BufferedReader(new FileReader(tempFile));
            String line;
            //String var24 = "";
            while((line = var25.readLine()) != null) {
                    if(header == true) {
                        fops1.write(("\n" + line).getBytes());
                    } else {
                        fops1.write(line.getBytes());
                        //if((var25.readLine()) != null) {
                            fops1.write("\n".getBytes());
                       // }
                    }

                }
            if(!footerString.equals("")) {
                fops1.write(("\n" + footerString).getBytes());
            }
            var25.close();
            fops1.close();
            //tempFile.delete();
        }

        br.close();

        /**
         * Open folder with newly created test files
         */
        Desktop.getDesktop().open(new File(filePath));

        return testFileList;
    }

    /**
     *
     * @param baseLineFile
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param baseLineFile
     * @param header
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-YYYY_HHmmss");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }

    /**
     *
     * @return
     */
    public String getDelimiter() {
        return this.delimiter;
    }

    public static void main(String[] args) throws IOException {
        CreateTestFiles ctf = new CreateTestFiles();
        Time time = new Time();
        String todaysDate = time.getTodaysDate("yyyyMMdd");
        //ctf.createOutputFixedWidth("C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/testCase.txt", "C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/PROD.CM.DIALER.AUTO.NUANCE.20161026", "C:/Users/david_him/Documents/Projects/ADS/17451/testfiles/config.txt", "PROD.CM.DIALER.AUTO.NUANCE." + todaysDate + "_[FieldName]", false, false);
    }
}
