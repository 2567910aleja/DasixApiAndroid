package com.alejandrafajardo.dasix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alejandrafajardo.dasix.Api.Api;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alejandrafajardo.dasix.databinding.ActivityMain2Binding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class MainActivity2 extends AppCompatActivity {

    Button volverinicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        volverinicio=findViewById(R.id.volverinicio);
        //verificar
        volverinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cerrar sesión
                SharedPreferences session = getSharedPreferences("session", MainActivity2.MODE_PRIVATE);
                String session_id=session.getString("session_id","");
                if (!session_id.isEmpty()){
                    RequestParams parametros = new RequestParams();
                    parametros.put("session_id", session_id);
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.post(Api.urlIniciarSesion(MainActivity2.this), parametros, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String respuesta=new String(responseBody);
                            //borro el session_id
                            SharedPreferences.Editor sessionEditor=session.edit();
                            sessionEditor.clear();
                            sessionEditor.apply();
                            //lo mando al inicio
                            Intent intento=new Intent(MainActivity2.this,MainActivity.class);
                            startActivity(intento);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(MainActivity2.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Intent intento = new Intent(MainActivity2.this,MainActivity.class);
                    startActivity(intento);
                }

            }
        });
    }
}
