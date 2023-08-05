package com.alejandrafajardo.dasix.Api;

import android.content.Context;

public class Api {
    static String urlApi="http://192.168.0.2:8000/api/";

    static public String urlIniciarSesion(Context context){
        String respuesta=urlApi+"iniciosesion/";
        return respuesta;
    }
    static  public  String urlRegistrar(Context context){
    String respuesta=urlApi+"registrar/";
        return respuesta;
    }

}
