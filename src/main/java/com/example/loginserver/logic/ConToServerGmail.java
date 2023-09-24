package com.example.loginserver.logic;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.vo.GmailVo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConToServerGmail {
    private static URL url;
    private static boolean answer;
    private static HttpURLConnection connection;
    public  static ErrorsEnum connectToServer(String path, String status){
        try {
            url=new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(status.toUpperCase());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            return ErrorsEnum.GOOD;
        } catch (MalformedURLException e) {
            System.out.println(e);
            return ErrorsEnum.URL_ERROR;
        } catch (IOException e) {
            System.out.println(e);
            return ErrorsEnum.OPEN_CONNECTION_ERROR;
        }catch (Exception e){
            System.out.println(e);
            return ErrorsEnum.ELSE_ERROR;
        }
    }

    public static ErrorsEnum sendToServer(String gmail,String message){
        GmailVo gmailVo=new GmailVo();
        gmailVo.setMassage(message);
        gmailVo.setSendTo(gmail);
        Gson json=new Gson();
        String jsonString=json.toJson(gmailVo);
        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonString);
            outputStream.flush();
            return ErrorsEnum.GOOD;
        } catch (IOException e) {
            System.out.println(e);
            return ErrorsEnum.OPEN_CONNECTION_ERROR;
        }
    }
    public static ErrorsEnum getFromServer(){
        BufferedReader in;
        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            return ErrorsEnum.OPEN_CONNECTION_ERROR;
        }
        try {

            while ((inputLine=in.readLine())!=null){
                response.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println(e);
            return ErrorsEnum.READ_ERROR;
        }
        String resString=response.toString();
        if(resString.equals("true")){
            answer=true;
        }
        else {
            answer=false;
        }
        System.out.println("answer from service:  " + response);
        return ErrorsEnum.GOOD;
    }
    public static boolean getAnswer(){
        return answer;
    }
}
