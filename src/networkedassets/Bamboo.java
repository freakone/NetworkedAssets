/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;

import java.io.IOException;
import org.json.JSONObject;
import java.util.*;
/**
 *
 * @author ffrea_000
 */
public class Bamboo {
    
    private String bambooUrl = "";
    private String bambooUser = "";
    private String bambooPass = "";
    private String bambooKey = "";
    private JSONObject lastResponse;
    private List<BambooInterface> listeners = new ArrayList<BambooInterface>();
    private boolean bNextRunning = false;
    private int nextBuildNumber = 0;
    
    public Bamboo(String url, String user, String pass, String key){
        
        bambooUrl = url;
        bambooUser = user;
        bambooPass = pass;      
        bambooKey = key;
    }
    
    public void fetchBamboo() throws IOException
    {
        JSONObject resp = Useful.readJsonFromUrl(String.format("%sbamboo/rest/api/latest/result/%s.json?expand=results.result&os_authType=basic", bambooUrl, bambooKey), bambooUser, bambooPass);     
        int next_temp = nextBuildNumber;
        
        if(resp.getJSONObject("results").getInt("size") > 0){        
        lastResponse = resp.getJSONObject("results").getJSONArray("result").getJSONObject(0);
        nextBuildNumber = lastResponse.getInt("buildNumber") + 1;           
         
        } else
            nextBuildNumber = 1;
        
        boolean build = fetchNextBuild();
        
        if(build)
            for(BambooInterface in : listeners)
                in.projectIsBuilding();
        else if(nextBuildNumber != next_temp)
            for(BambooInterface in : listeners)
                in.projectChanged(lastResponse);
        
    }
    
    public boolean fetchNextBuild() throws IOException
    {        
        try
        {
            if(nextBuildNumber > 0)
            {
                 JSONObject resp = Useful.readJsonFromUrl(String.format("%sbamboo/rest/api/latest/result/status/%s.json?expand=results.result&os_authType=basic", bambooUrl, bambooKey+"-"+String.valueOf(nextBuildNumber)), bambooUser, bambooPass);
                 return resp.has("progress");    
            }
        }
        catch(java.io.FileNotFoundException ex)
        {
            return false;
        }
        
        return false;
    }
    
    public void addListener(BambooInterface toAdd) {
        listeners.add(toAdd);
    }
    
    
    
    
    
}
