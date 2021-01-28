package com.finwin.mythri.custmate.SupportingClass;

import com.finwin.mythri.custmate.home.transfer.view_recent_transfers.pojo.transaction_list.TransactionListResponse;

import java.util.ArrayList;

public class ConstantClass {

//    public static final String ip_global = "http://192.168.0.221:5363";
//    public static final String ip_global = "http://209.126.76.226:5363";
//    public static final String ip_global = "http://35.196.223.10:5363";
//    public static final String ip_global = "http://192.168.0.223:120";

      public static final String ip_global = "http://www.finwintechnologies.com:5363";
     // public static final String ip_global = "https://custmateuvnl.digicob.in";
    //public static final String ip_global = "http://192.168.225.221:81";
    //public static final String ip_global = "http://192.16.0.200:81";

    public static final String url_news = "https://newsapi.org/v2/top-headlines?sources=medical-news-today&apiKey=880ea11837b94135b565e6cbca5fe20a";
    public static boolean NEWS_FIRSTRUN = true;
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_URL = "url";
    public static final String KEY_URLTOIMAGE = "urlToImage";
    public static final String KEY_PUBLISHEDAT = "publishedAt";

    //public static String[] const_acc_nos_array;
    public static ArrayList<String> listAccountNumbers;
    public static String const_password="const_password";
    public static final String mstrType = "M_TYPE";
    public static String[] masterTypArray;
    public static String[] masterTypArrayID;
    public static TransactionListResponse transactionListResponse;
    public static ArrayList<String> listScheme;
    public static String pinforQR = "qrpass";
    public static String accountNumber = "accountNumber";
    public static String custId = "custId";
    public static String userName = "userName";
    public static String phoneNumber = "phoneNumber";
    public static String mpinStatus = "mpinStatus";


    public static final String PREFS_DATA = "PrefesContent";
    //    public static String PREFS_MPIN = "";

//    public static String QR_SCND = "QRSCND";
//    public static String QR_ACCNO = "QRACCNO";

}
