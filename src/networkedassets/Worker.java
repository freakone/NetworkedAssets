/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;

import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author ffrea_000
 */
public abstract class Worker implements BambooInterface {
    
    private PhilipsHue ph;
    private Bamboo bm;
    
    public Worker() throws IOException{
        
        ph = new PhilipsHue("http://192.168.16.50/");
        bm = new Bamboo("http://192.168.16.11:8085/", "user5", "motiveguideline", "PR5-PL5");
        bm.addListener(this);
    }
    
    
    
    public void Job(){
        
        try 
        {
            bm.fetchBamboo();
            
        } 
        catch (Exception ex)
        {
            System.out.println("someting went wrong" + ex.toString());
        }
    }
    
    @Override
    public void projectChanged(JSONObject obj)
    {
        System.out.println(obj.getString("state"));
        try
        {
            String state = obj.getString("state");
            if(state.contains("Successful")){
                 ph.effectOk();
            }
            else {
                ph.effectError();
            }
        } catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
    @Override
    public void projectIsBuilding(){
        
        System.out.println("building");
        try
        {
            ph.effectBuilding();
        } catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
            
    
}
