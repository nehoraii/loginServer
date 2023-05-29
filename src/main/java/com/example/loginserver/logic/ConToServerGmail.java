package com.example.loginserver.logic;
import com.example.loginserver.enums.ErrorsEnumConnection;
import com.example.loginserver.enums.ErrorsEnumForUser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConToServerGmail {
    private static URL url;
    private static HttpURLConnection connection;
    public  static ErrorsEnumForUser connectToServer(String path, String status){
        try {
            url=new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(status.toUpperCase());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            return ErrorsEnumForUser.GOOD;
        } catch (MalformedURLException e) {
            return ErrorsEnumForUser.URLError;
        } catch (IOException e) {
            return ErrorsEnumForUser.OpenConnectionError;
        }catch (Exception e){
            return ErrorsEnumForUser.ElseError;
        }
    }

    public static ErrorsEnumForUser sendToServer(String gmail,String message){
        String jsonInputString="sendTo:"+gmail + ",massage:" +message;
        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
            return ErrorsEnumForUser.GOOD;
        } catch (IOException e) {
            return ErrorsEnumForUser.OpenConnectionError;
        }
    }
    public static ErrorsEnumForUser getFromServer(){
        BufferedReader in;
        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            return ErrorsEnumForUser.OpenConnectionError;
        }
        try {

            while ((inputLine=in.readLine())!=null){
                response.append(inputLine);
            }
        } catch (IOException e) {
            return ErrorsEnumForUser.ReadError;
        }
        System.out.println("answer from service:  " + response);
        return ErrorsEnumForUser.GOOD;
    }
}
