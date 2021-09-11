package com.java.qitianliang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import android.util.Log;
import android.widget.Toast;

public class PostUtil {

    public static String Post(String url,String data) {
        String msg = "";
        try {

            // this ip config always changes
            // you should modify it before building APK and running
            // your phone(run android app) and PC(run server) should share Internet connection
            // and change it into your PC(server) Internet IPv4 address

            HttpURLConnection conn = (HttpURLConnection) new URL("http://183.172.177.155:8081/AndroidWeb/" + url).openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer response = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                msg = response.toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
            msg = "Service Error";
            return msg;
        }
        return msg;
    }
}
