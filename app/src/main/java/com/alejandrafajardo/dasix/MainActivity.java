package com.alejandrafajardo.dasix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alejandrafajardo.dasix.Api.Api;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    EditText usuario, clave;
    Button ingresar, registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
        ingresar = findViewById(R.id.ingresar);
        registrar = findViewById(R.id.registrar);

        //verificar inicio sesión
        SharedPreferences session = getSharedPreferences("session", this.MODE_PRIVATE);
        String session_id=session.getString("session_id","");
        if (!session_id.isEmpty()){
            RequestParams parametros = new RequestParams();
            parametros.put("session_id", session_id);
            parametros.put("accion", "verificarsession");
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.post(Api.urlIniciarSesion(MainActivity.this), parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String respuesta = new String(responseBody);
                    try {
                        verificarSesion(statusCode,respuesta);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(MainActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioD = usuario.getText().toString();
                String claveD = clave.getText().toString();
                if (usuarioD.isEmpty() || claveD.isEmpty()) {
                    if (usuarioD.isEmpty()) {
                        usuario.setError("Debe ingresar un nombre de usuario");
                    }
                    if (claveD.isEmpty()) {
                        clave.setError("Debe ingresar una contraseña");
                    }
                } else {
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    RequestParams parametros = new RequestParams();
                    parametros.put("accion", "iniciarsesion");
                    parametros.put("usuario", usuarioD);
                    parametros.put("password", claveD);

                    httpClient.post(Api.urlIniciarSesion(MainActivity.this), parametros, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                            String respuesta = new String(responseBody);
                            try {
                                iniciarSesion(statusCode, respuesta);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                            try {
                                iniciarSesion(statusCode, "Error de conexion");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(MainActivity.this, Activity_Registar.class);
                startActivity(intento);
            }

        });
    }

    private void verificarSesion(int statusCode, String respuesta) throws JSONException {
        Log.e("Respuesta verificacion",respuesta);
        JSONObject res=new JSONObject(respuesta);
        String resS=res.getString("respuesta");
        if (resS.equals("valido")){
            Intent intento=new Intent(this,MainActivity2.class);
            startActivity(intento);
        }
    }

    private void iniciarSesion(int statusCode, String respuesta) throws JSONException {
        if (statusCode >= 200 && statusCode <= 299) {
            JSONObject jsonRes = new JSONObject(respuesta);
            try {
                String sessionid = jsonRes.getString("sessionid");
                SharedPreferences session = getSharedPreferences("session", this.MODE_PRIVATE);
                SharedPreferences.Editor sessioneditor = session.edit();
                Toast.makeText(this, "Inicio de Sesion correcta", Toast.LENGTH_SHORT).show();
                sessioneditor.putString("session_id", sessionid);
                sessioneditor.apply();
                Intent intento = new Intent(this, MainActivity2.class);
                startActivity(intento);
            } catch (JSONException e) {
                JSONArray error = new JSONArray(jsonRes.getString("error"));
                String errorV = error.get(0).toString();
                Toast.makeText(this, "" + errorV, Toast.LENGTH_LONG).show();
            }
        }
    }
}