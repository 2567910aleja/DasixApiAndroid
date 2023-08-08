package com.alejandrafajardo.dasix.Api;

import android.content.Context;

public class Api {
    static public String url="https://dasix.azurewebsites.net";
    //static public String url="http://192.168.0.2:8000";
    static public String urlApi=url+"/api/";

    static public String urlIniciarSesion(Context context){
        String respuesta=urlApi+"iniciosesion/";
        return respuesta;
    }
    static  public  String urlRegistrar(Context context){
    String respuesta=urlApi+"registrar/";
        return respuesta;
    }
    static  public  String urlCargarDatos(Context context){
    String respuesta=urlApi+"cargardatos/";
        return respuesta;
    }


}
