package com.alejandrafajardo.dasix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Activity_Registar extends AppCompatActivity {

    EditText Rusuario, Rclave, confirmacion;
    Button RegistrarUsu, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Rusuario=findViewById(R.id.Rusuario);
        Rclave=findViewById(R.id.Rclave);
        confirmacion=findViewById(R.id.confirmacion);
        RegistrarUsu=findViewById(R.id.RegistrarUsu);
        RegistrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtengo los valores
                String usuario=Rusuario.getText().toString();
                String clave=Rclave.getText().toString();
                String confirmacionClave=confirmacion.getText().toString();

                // Valido que ninguno este vacio
                if (usuario.isEmpty() || clave.isEmpty() || confirmacionClave.isEmpty()){
                    if (usuario.isEmpty()){
                        Rusuario.setError("El usuario no puede estar vacio");
                    }
                    if (clave.isEmpty()){
                        Rclave.setError("La contraseña no puede estar vacia");
                    }
                    if (confirmacionClave.isEmpty()){
                        confirmacion.setError("La confirmacion no puede estar vacio");
                    }
                }else {
                    // Valido que las contraseñas coincidan
                    if (!clave.equals(confirmacionClave)){
                        confirmacion.setError("La contraseña no coincide");
                    }else{
                        // Si todo_ es correcto entonces envio los valores para registrar el usuario
                        RequestParams parametros = new RequestParams();
                        parametros.put("usuario", usuario);
                        parametros.put("password", clave);
                        parametros.put("passwordConfirmacion", confirmacionClave);
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.post(Api.urlRegistrar(Activity_Registar.this), parametros, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String respuesta = new String(responseBody);
                                try {
                                    registrarUsuario(statusCode,respuesta);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(Activity_Registar.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        cancelar=findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento= new Intent(Activity_Registar.this,MainActivity.class);
                startActivity(intento);
            }
        });
    }

    private void registrarUsuario(int statusCode, String r) throws JSONException {
        JSONObject respuesta=new JSONObject(r);
        try{
            String resApi=respuesta.getString("respuesta");
            Toast.makeText(this, "El usuario se ha creado", Toast.LENGTH_LONG).show();
            Intent intento =  new Intent(this, MainActivity.class);
            startActivity(intento);
        }catch (JSONException e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}