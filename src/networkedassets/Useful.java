/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkedassets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.Reader;
import java.nio.charset.Charset;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ffrea_000
 */
public class Useful {
    
    private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
    }
    
    public static JSONObject readJsonFromUrl(String urlString) throws IOException{
        
        return readJsonFromUrl(urlString, "", "");
    }
    
    public static JSONObject readJsonFromUrl(String urlString, String username, String password) throws IOException, JSONException {
       
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        
        if(username.length() > 0 && password.length() > 0){
        String encoded = Base64.encodeToString( (username+":"+password).getBytes(), false);
        conn.setRequestProperty("Authorization", "Basic "+ encoded);
        }
        
        InputStream is = conn.getInputStream();
       
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
    }
    
    public static String http(String obj, String addr, String method) throws IOException, JSONException {
        
        URL url = new URL(addr);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod(method);
        OutputStreamWriter out = new OutputStreamWriter(
            httpCon.getOutputStream());
        out.write(obj);
        out.close();
        InputStream is = httpCon.getInputStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          
          return readAll(rd);
        } finally {
          is.close();
        }
    }
    
   
    
}
