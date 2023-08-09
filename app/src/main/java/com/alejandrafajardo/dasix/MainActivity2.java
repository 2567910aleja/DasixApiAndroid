package com.alejandrafajardo.dasix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alejandrafajardo.dasix.Api.Api;
import com.alejandrafajardo.dasix.adaptadores.AdaptadorClientes;
import com.alejandrafajardo.dasix.adaptadores.AdaptadorUsuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView recyUsu;
    SharedPreferences session;
    SharedPreferences.Editor editorSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        volverinicio=findViewById(R.id.volverinicio);
        carga=findViewById(R.id.carga);
        selUsuario=findViewById(R.id.selUsuarios);
        recyUsu=findViewById(R.id.recyUsu);
        recyUsu.setLayoutManager(new LinearLayoutManager(this));
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
            selUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String seleccionado=adapterView.getItemAtPosition(i).toString();
                    cargarRecycler(seleccionado);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //no se hace nada, porque si
                }
            });


        }catch (JSONException e){
            String error = respuesta.getString("error");
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }


    }

    @SuppressLint("SetTextI18n")
    private void cargarRecycler(String seleccionado) {
        if (!seleccionado.equals("Seleccione")){
                carga.setText("Cargando...");
                RequestParams parametros = new RequestParams();
                if (seleccionado.equals("usuario")){
                    parametros.put("accion", "cargarUsuarios");

                }else{
                    parametros.put("accion", "cargarClientes");
                }
                parametros.put("session_id", session.getString("session_id",""));
                AsyncHttpClient httpClient = new AsyncHttpClient();
                httpClient.post(Api.urlCargarDatos(MainActivity2.this), parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String res=new String(responseBody);
                        Log.e("datosUsuario",res);
                        try {
                            JSONObject usuariosObj=new JSONObject(res);
                            JSONArray jsonArray;

                            //creamos adaptador
                            if (seleccionado.equals("usuario")){
                                jsonArray=usuariosObj.getJSONArray("usuarios");
                                AdaptadorUsuarios adapterUsuarios = new AdaptadorUsuarios(MainActivity2.this,jsonArray);
                                recyUsu.setAdapter(adapterUsuarios);
                            } else if (seleccionado.equals("cliente")) {
                                jsonArray=usuariosObj.getJSONArray("clientes");
                                AdaptadorClientes adapterClientes = new AdaptadorClientes(MainActivity2.this,jsonArray);
                                recyUsu.setAdapter(adapterClientes);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        carga.setText("");

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MainActivity2.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        carga.setText("");
                    }
                });

        }
    }
}
