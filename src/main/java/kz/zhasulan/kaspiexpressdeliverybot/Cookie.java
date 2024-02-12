package kz.zhasulan.kaspiexpressdeliverybot;

import java.util.ArrayList;
import java.util.List;

public  class Cookie {
    public static String session = "";
    public static String info="";

    public static void normalize(){
        StringBuilder sessionSB= new StringBuilder(session);
        StringBuilder infoSB= new StringBuilder(info);
        sessionSB.delete(0,18);
        infoSB.delete(0,5);
        sessionSB.delete(sessionSB.indexOf(";"),sessionSB.length()-1);
        infoSB.delete(infoSB.indexOf(";"),infoSB.length()-1);
        session= sessionSB.toString();
        info= infoSB.toString();

    }
}
