package com.abaskan.evkuaforum.BarberClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UserAdressDataHandler {


    public UserAdressDataHandler() {

    }

    public String getAdressData (String requestURL){
        URL url;
        String response = "";

        try{
            url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            int responsceCode = connection.getResponseCode();
            if(responsceCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((line = bufferedReader.readLine()) != null){
                    response += line;
                }
            }
            else{
                response = "";
            }


        }catch (ProtocolException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
