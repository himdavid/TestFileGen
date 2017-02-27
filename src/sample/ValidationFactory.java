package sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ValidationFactory {
    public ValidationFactory() {
    }

    public boolean validatePETesterInputMapping(HashMap<String, String> mapTestFile, HashMap<String, String> mapJSON) {
        boolean testStatus = false;
        String keyJSON = null;
        int countFail = 0;
        Iterator iter = mapTestFile.entrySet().iterator();

        while(true) {
            while(iter.hasNext()) {
                Entry entry1 = (Entry)iter.next();
                String keyTestFile = ((String)entry1.getKey()).toLowerCase().replace("_", "");
                String valueTestFile = (String)entry1.getValue();
                String valueJSON;
                if(!mapJSON.containsKey(keyTestFile)) {
                    System.out.println("Did not find a JSON field for: " + keyTestFile);
                    if(mapJSON.containsValue(valueTestFile)) {
                        Iterator var11 = mapJSON.entrySet().iterator();
                        while(var11.hasNext()) {
                            Entry entry2 = (Entry)var11.next();
                            keyJSON = (String)entry2.getKey();
                            valueJSON = (String)entry2.getValue();
                            if(valueTestFile.equals(valueJSON)) {
                                System.out.println("\tBut found the value: " + valueJSON + " correponding to JSON field: " + keyJSON);
                            }
                        }
                    }
                    ++countFail;
                } else {
                    valueJSON = (String)mapJSON.get(keyTestFile);
                    System.out.println("Comparing test file field:" + keyTestFile + " \n\t value: " + valueTestFile);
                    System.out.println("Found JSON value:" + valueJSON);
                }
            }
            if(countFail == 0) {
                testStatus = true;
            }
            return testStatus;
        }
    }
}
