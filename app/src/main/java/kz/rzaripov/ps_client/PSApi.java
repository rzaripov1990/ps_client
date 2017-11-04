package kz.rzaripov.ps_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by ZuBy on 28.10.2017.
 */

public class PSApi {

    private String username = "";
    private String password = "";

    public PSApi(String ausername, String apassword) {
        username = ausername;
        password = apassword;
    }

    public String getURL(String amethod) {
        return "https://api.ps.kz/client/" + amethod + "?username=" + username + "&password=" + password +
                "&input_format=http&output_format=json";
    }

    public String getResponseFromUrl(String url) throws IOException {
        String json = "";
        String line;
        URL request = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(request.openStream()));
        while ((line = in.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            json += line;
        }
        in.close();
        return json;
    }

    public String requestTo(String url){
        if (url.isEmpty()) {
            return "{\"ps_result\":\"error\", \"error_text\":\"Request string is empty\"}";
        }

        try {
            return getResponseFromUrl(url);
        } catch (IOException e) {
            //return  e.getMessage();
            return "{\"ps_result\":\"error\", \"error_text\":\"Response is empty\"}";
        }
    }
}
