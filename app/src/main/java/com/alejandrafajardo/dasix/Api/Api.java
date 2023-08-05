package com.alejandrafajardo.dasix.Api;

import android.content.Context;

public class Api {
    //este es para trabajar en local
    static String urlApi="http://192.168.0.2:8000/api/";

    //este es para utilizar la api del proyecto desplegado
    //static String urlApi="https://dasix.azurewebsites.net/api/";
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
