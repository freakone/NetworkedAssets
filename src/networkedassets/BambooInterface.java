/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;
import org.json.JSONObject;
/**
 *
 * @author ffrea_000
 */
public interface BambooInterface {
    public void projectChanged(JSONObject obj);
    public void projectIsBuilding();
}
