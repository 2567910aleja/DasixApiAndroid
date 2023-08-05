package com.alejandrafajardo.dasix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alejandrafajardo.dasix.Api.Api;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alejandrafajardo.dasix.databinding.ActivityMain2Binding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity2 extends AppCompatActivity {

    Button volverinicio;
    TextView carga;
    Spinner selUsuario;
    SharedPreferences session;
    SharedPreferences.Editor editorSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        volverinicio=findViewById(R.id.volverinicio);
        carga=findViewById(R.id.carga);
        selUsuario=findViewById(R.id.selUsuarios);
        session = getSharedPreferences("session", MainActivity2.MODE_PRIVATE);
        editorSesion = session.edit();

        //peticion para los tipos de usuario
        //obtenemos el session_id
        String session_id = session.getString("session_id","");
        //verficamos que exista
        if (session_id.isEmpty()){
            Intent intento = new Intent(this, MainActivity.class);
            startActivity(intento);
        }
        //parametros
        RequestParams parametros = new RequestParams();
        parametros.put("session_id", session_id);
        parametros.put("accion", "tiposUsuarios");

        //enviar petición
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(Api.urlCargarDatos(MainActivity2.this), parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String res=new String(responseBody);
                        try {
                            cargaSpinnerU(statusCode,res);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MainActivity2.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        volverinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cerrar sesión
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

    private void cargaSpinnerU(int statusCode, String r) throws JSONException {
        JSONObject respuesta=new JSONObject(r);
        Log.e("datos",r);
        try {
            String tipos=respuesta.getString("tiposUsuarios");
            JSONArray usuarios=new JSONArray(tipos);

            //crear lista
            String [] tiposArray=new String[usuarios.length()+1];
            tiposArray[0]="Seleccione";
            for (int i=0; i<usuarios.length();i++){
                tiposArray[i+1]=usuarios.get(i).toString();
            }

            //crear adaptador
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tiposArray);
            selUsuario.setAdapter(adaptador);

        }catch (JSONException e){
            String error = respuesta.getString("error");
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }


    }
}
