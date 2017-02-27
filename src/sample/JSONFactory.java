package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JSONFactory {
    public JSONFactory() {
    }

    public String parseJSON(String json) {
        Object jsonString = null;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject)jsonParser.parse(json);
        } catch (ParseException var39) {
            var39.printStackTrace();
        }

        String trackingID = (String)jsonObject.get("trackingID");
        String accountNumber = (String)jsonObject.get("accountNumber");
        String alertID = (String)jsonObject.get("alertID");
        String batchID = (String)jsonObject.get("batchID");
        Boolean billableInd = (Boolean)jsonObject.get("billableInd");
        String billingDivision = (String)jsonObject.get("billingDivision");
        String campaignID = (String)jsonObject.get("campaignID");
        String campaignName = (String)jsonObject.get("campaignName");
        String city = (String)jsonObject.get("city");
        Long clientID = (Long)jsonObject.get("clientID");
        String consumerID = (String)jsonObject.get("consumerID");
        String conversationID = (String)jsonObject.get("conversationID");
        String country = (String)jsonObject.get("country");
        String county = (String)jsonObject.get("county");
        String dateOfBirth = (String)jsonObject.get("dateOfBirth");
        Boolean dialerDispatched = (Boolean)jsonObject.get("dialerDispatched");
        String dialerList = (String)jsonObject.get("dialerList");
        String dupeHash = (String)jsonObject.get("dupeHash");
        String encryptionKey = (String)jsonObject.get("encryptionKey");
        String expirationDate = (String)jsonObject.get("expirationDate");
        String expirationSource = (String)jsonObject.get("expirationSource");
        Boolean fieldLengthsScrubbed = (Boolean)jsonObject.get("fieldLengthsScrubbed");
        String firstName = (String)jsonObject.get("firstName");
        String gender = (String)jsonObject.get("gender");
        String inputID = (String)jsonObject.get("inputID");
        String installationID = (String)jsonObject.get("installationID");
        String lastName = (String)jsonObject.get("lastName");
        Long loadDate = (Long)jsonObject.get("loadDate");
        String locateStatus = (String)jsonObject.get("locateStatus");
        String personID = (String)jsonObject.get("personID");
        String productID = (String)jsonObject.get("productID");
        Long resultDate = (Long)jsonObject.get("resultDate");
        String[] medias = new String[]{"mediaId", "mediaAddress", "status", "seqNo", "reasonsInvalid", "secondaryStateCode"};
        ArrayList JSONValues = this.getJSONArray(jsonObject, "medias", medias);
        this.printArray(JSONValues);
        return (String)jsonString;
    }

    public static HashMap<String, String> parseJSONMap(String JSONString) throws ParseException {
        HashMap map = new HashMap();
        JSONParser JSONParser = new JSONParser();
        JSONObject jObject = (JSONObject)JSONParser.parse(JSONString);

        String key;
        String val;
        for(Iterator iter = jObject.keySet().iterator(); iter.hasNext(); map.put(key.toLowerCase(), val)) {
            key = iter.next().toString();
            if(jObject.get(key) == null) {
                val = "null";
            } else {
                val = jObject.get(key).toString();
            }
        }

        return map;
    }

    public ArrayList<ArrayList<String>> getJSONArray(JSONObject jsonObject, String array, String[] arrayValues) {
        JSONArray JSONChildObject = (JSONArray)jsonObject.get(array);
        ArrayList JSONSubList = new ArrayList();
        ArrayList JSONMainList = new ArrayList();

        for(int i = 0; i < JSONChildObject.size(); ++i) {
            JSONObject JSONNest = (JSONObject)JSONChildObject.get(i);

            for(int a = 0; a < arrayValues.length; ++a) {
                String JSONValue;
                if(JSONNest.get(arrayValues[a]) == null) {
                    JSONValue = "null";
                } else {
                    JSONValue = JSONNest.get(arrayValues[a]).toString();
                }

                JSONSubList.add(JSONValue);
            }

            JSONMainList.add(JSONSubList);
            JSONSubList = new ArrayList();
        }

        return JSONMainList;
    }

    public void printArray(ArrayList<ArrayList<String>> list) {
        for(int i = 0; i < list.size(); ++i) {
            ArrayList mainObject = (ArrayList)list.get(i);

            for(int a = 0; a < mainObject.size(); ++a) {
                System.out.println("Object " + i + ": " + (String)mainObject.get(a));
            }
        }

    }

    public void printJSONMap(HashMap<String, String> map) {
        Iterator var2 = map.keySet().iterator();

        while(var2.hasNext()) {
            String json = (String)var2.next();
            String key = json.toString();
            String value = ((String)map.get(key)).toString();
            System.out.println("Key: " + key + "\nValue: " + value);
        }

    }
}
