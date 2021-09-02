package com.tinatest.line_bot.service.lineNotify;

import com.tinatest.line_bot.service.LineNotifyService;

import java.io.IOException;

public class LineNotify {
    private String userToken;
    private LineParameter lineParameter;

    public LineNotify(String userToken) {
        this.userToken = userToken;
    }
    
    public LineNotify(String userToken, LineParameter lineParameter) {
        this.userToken = userToken;
        this.lineParameter = lineParameter;
    }
    
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
   
    public boolean notifyMe(String message) throws IOException {
        return notifyMe(message, 0, 0);
    }
    
    public boolean notifyMe(String message,int stickerPackageId,int stickerId) throws IOException{  
        if(this.lineParameter == null)
            this.lineParameter = new LineParameter(message, stickerPackageId, stickerId);
        int resStatus =  Connection.sendData(this.lineParameter,this.userToken);
        return resStatus==200;
    }

    public static void main(String[] args) {
        final String USER_TOKEN = "Your API Token";
        LineNotify ln = new LineNotify(USER_TOKEN);
        try{
            ln.notifyMe("Hello World");
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
    
}