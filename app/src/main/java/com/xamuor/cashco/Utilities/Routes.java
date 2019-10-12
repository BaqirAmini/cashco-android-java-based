package com.xamuor.cashco.Utilities;

public class Routes {
//    Remote-server...
//    private static String IMG_PATH = "http://cashco.xamuor.com/uploads/";

//    Virtual-device IP
//    private static String IMG_PATH = "http://192.168.56.1:8000/uploads/";
//    NEW IP GENERATED
    private static String IMG_PATH = "http://192.168.232.1:8000/uploads/";
    public static String setUrl(String route) {
        //    Remote-server...
//        String url = "http://cashco.xamuor.com/api/" + route;
//        String url = "http://192.168.56.1:8000/api/" + route;
//        NEW IP GENERATED
        String url = "http://192.168.232.1:8000/api/" + route;
//        Virtual-device IP
        return url;

    }
    // To load images though their path
    public static String onLoadImage(String img) {
        return IMG_PATH.concat(img);
    }
}
