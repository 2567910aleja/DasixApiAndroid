package com.alejandrafajardo.dasix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Registar extends AppCompatActivity {

    EditText Rusuario, Rclave;
    Button RegistrarUsu, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Rusuario=findViewById(R.id.Rusuario);
        Rclave=findViewById(R.id.Rclave);
        RegistrarUsu=findViewById(R.id.RegistrarUsu);
        RegistrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento=new Intent(Activity_Registar.this,MainActivity.class);
                startActivity(intento);
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
}