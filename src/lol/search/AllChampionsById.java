
package lol.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * If you have a long list of champion id's and you want the name of them, this is the place to do it. 
 * The class grabs a large list of all champions by their id and returns one file. Therefore all names can be obtained
 * without massive calls to the internet. 
 * @author Oscar
 */
public class AllChampionsById {
    private boolean isResponsive;
    private final String apiKey;
    private final String region;
    private JSONObject allChampionsObject;
    public AllChampionsById(String region){
        this.region = region;
        LoLSearch objLoL = new LoLSearch();
        apiKey = objLoL.getApiKey();
        String jsonResponse = getJSONResponse();
        allChampionsObject = getJSONObjFromResponse(jsonResponse);
        
    }
    
    
    private String getJSONResponse(){
        String jsonResponse = null; //unparsed json response
        try {
            //URL
            URL url = new URL(
                    "https://global.api.pvp.net/api/lol/static-data/"+region+"/v1.2/champion?dataById=true&champData=tags&api_key="+apiKey);
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            this.isResponsive = true;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Game_ById.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.isResponsive = false;
        }
        return jsonResponse;
    }
    
    
    private JSONObject getJSONObjFromResponse(String response){
        JSONObject dataObj = null;
        try {
            JSONObject jsonObj = new JSONObject(response); //object of the JSON
            dataObj = jsonObj.getJSONObject("data"); //holds all champion objects
        } catch (JSONException ex) {
            Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataObj;
    }
    
    
    public String getChampNameFromId(int id){
        String name = null;
        if(id == 0){
            name = "Overall";
        }
        else{
            Iterator<?> keys = allChampionsObject.keys(); //iterator to traverse through entire champion list
            while(keys.hasNext()){ //operations per instance of each champion object
                try {
                    String key = (String)keys.next();
                    if(allChampionsObject.get(key) instanceof JSONObject){
                        JSONObject champObject = allChampionsObject.getJSONObject(key);
                        //compare the id of the current champion to the id of the champion played by the player
                        if(champObject.getInt("id") == id){
                            name = champObject.getString("name");
                            break;
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        return name;
    }
    public String getChampTitleFromId(int id){
        String title = null;
        if(id == 0){
            title = "";
        }
        else{
            Iterator<?> keys = allChampionsObject.keys(); //iterator to traverse through entire champion list
            while(keys.hasNext()){ //operations per instance of each champion object
                try {
                    String key = (String)keys.next();
                    if(allChampionsObject.get(key) instanceof JSONObject){
                        JSONObject champObject = allChampionsObject.getJSONObject(key);
                        //compare the id of the current champion to the id of the champion played by the player
                        if(champObject.getInt("id") == id){
                            title = champObject.getString("title");
                            break;
                        }
                        
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        return title;
    }
    
    public String getChampKeyFromId(int id){
        String keyString = null;
        Iterator<?> keys = allChampionsObject.keys(); //iterator to traverse through entire champion list
            while(keys.hasNext()){ //operations per instance of each champion object
                try {
                    String key = (String)keys.next();
                    if(allChampionsObject.get(key) instanceof JSONObject){
                        JSONObject champObject = allChampionsObject.getJSONObject(key);
                        //compare the id of the current champion to the id of the champion played by the player
                        if(champObject.getInt("id") == id){
                            keyString = champObject.getString("key");
                            break;
                        }
                        
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(LoLStaticData_AllChampions.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        return keyString;
    }
    
    
    public boolean getIsResponsive(){
        return this.isResponsive;
    }
}
