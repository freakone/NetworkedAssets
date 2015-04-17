/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;

/**
 *
 * @author ffrea_000
 */
public class NetworkedAssets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        try
        {
            Worker w = new Worker() {
              
            };
            
            
           while(true)
            {        
                w.Job();
                Thread.sleep(800);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
       
    }
    
}
