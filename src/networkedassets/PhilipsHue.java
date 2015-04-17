/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;
/**
 *
 * @author ffrea_000
 */
public class PhilipsHue {
    
    private String hueIp = "";
    private List allLights = new ArrayList();
    private JSONObject reqSend = new JSONObject();
    
    public PhilipsHue(String ip) throws IOException{
        
        hueIp = ip;    
        fetchLightData();
        createGroup();
    }
    
    public void newRequest()
    {
        reqSend = new JSONObject();
    }
    
    public void setRequestHue(int hue)
    {
        reqSend.put("hue", hue);
    }
    
    public void setSat(int sat)
    {
        reqSend.put("sat", sat);
    }
    public void setTransistionTime(int time)
    {
        reqSend.put("transitiontime", time);
    }
    
    public void setRequestOn(boolean state)
    {
        reqSend.put("on", state);
    }
    
    public void setRequestBrightness(int sat)
    {
        reqSend.put("bri", sat);
    }
    
    public void sendRequest(int lightnum, boolean group) throws IOException
    {
        if(reqSend.length() > 0)
        {
            System.out.println(reqSend.toString());
            System.out.println(Useful.http(reqSend.toString(), hueIp + String.format("api/newdeveloper/%s/", group ? "groups" : "light") + String.valueOf(lightnum) + (group ? "/action" : "/state"), "PUT"));
        }
    }
    
    public void effectOk() throws IOException
    {
      newRequest();
      setRequestHue(25500);
      setRequestOn(true);
      setTransistionTime(2);
      setSat(255);
      setRequestBrightness(254);
      sendRequest(1, true);         
    }
    
    public void effectError() throws IOException
    {
      newRequest();
      setRequestHue(65535);
      setRequestOn(true);
      setTransistionTime(2);
      setSat(255);
      setRequestBrightness(254);
      sendRequest(1, true); 
    }
    
    private final int[] buildingColors = {25500, 65535, 46920};
    private int buildingColorIndex = 0;
    public void effectBuilding() throws IOException
    {
        newRequest();
        setRequestHue(buildingColors[buildingColorIndex]);
        setRequestOn(true);
        setTransistionTime(5);
        setSat(255);
        setRequestBrightness(254);
        sendRequest(1, true); 
        
        buildingColorIndex++;
        if(buildingColorIndex > 2)
            buildingColorIndex = 0;
    }
    
    
    public void createGroup() throws IOException
    {
       
       JSONObject groups =  Useful.readJsonFromUrl(hueIp + "api/newdeveloper/groups/");
       Iterator<String> keys = groups.keys();

        while( keys.hasNext() ) {            
            String id = keys.next();
            System.out.println(Useful.http("", hueIp + "api/newdeveloper/groups/" + id, "DELETE"));
            System.out.println(id);
        
        }
           
        JSONObject obj = new JSONObject();
        obj.put("name", "blah");
        List indexes = new ArrayList();
        for(Object pl : allLights){
            int id = ((PhilipsLight)pl).id;
            indexes.add(String.valueOf(id));        
        }
        obj.put("lights", indexes);
        System.out.println(obj.toString());
        
        
        System.out.println(Useful.http(obj.toString(), hueIp + "api/newdeveloper/groups/", "POST"));
       
    }
    
    private void fetchLightData() throws IOException{
        allLights.clear();
        JSONObject lights = Useful.readJsonFromUrl(hueIp + "api/newdeveloper/lights");
        Iterator<String> keys = lights.keys();

        while( keys.hasNext() ) {
            String key = keys.next();
            JSONObject onelight = ( JSONObject)lights.get(key); 
            
    
            PhilipsLight newlight = new PhilipsLight();
            newlight.modelid = onelight.getString("modelid");
            newlight.name = onelight.getString("name");
            newlight.type = onelight.getString("type"); 
            newlight.id = Integer.parseInt(key);
            allLights.add(newlight);

            System.out.println(key + " " + newlight.type);
            
        }
    }    
    
    
    
    
    
}
