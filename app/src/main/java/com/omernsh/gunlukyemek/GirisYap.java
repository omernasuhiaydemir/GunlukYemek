package com.omernsh.gunlukyemek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

public class GirisYap extends AppCompatActivity {

    Button giris_yap_button,forgot_pass_button, eye_button;

    EditText e_mail_edittext,sifre_edittext;




    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    String e_mail_adresi_str, şifre_str;



    int goz_kontrol=0;

    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_yap);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        sharedPref = this.getSharedPreferences("sharedPref_login", Context.MODE_PRIVATE);


        if (user != null) {

            Intent intent = new Intent(getApplicationContext(), AnaSayfa.class);
            startActivity(intent);
            finish();
        }



        giris_yap_button = findViewById(R.id.giris_yap_button);
        forgot_pass_button = findViewById(R.id.forgot_pass_button);
        eye_button = findViewById(R.id.eye_button);
        e_mail_edittext = findViewById(R.id.e_mail_edittext);
        sifre_edittext = findViewById(R.id.sifre_edittext);


        forgot_pass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ForgotPass.class);
                startActivity(intent);


            }
        });



        eye_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (goz_kontrol==0){
                    goz_kontrol=1;


                    sifre_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    eye_button.setBackgroundResource(R.drawable.eye_red);


                }
                else if(goz_kontrol==1){
                    goz_kontrol=0;

                    sifre_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    eye_button.setBackgroundResource(R.drawable.eye);



                }


            }
        });



        giris_yap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                e_mail_adresi_str=e_mail_edittext.getText().toString();
                şifre_str=sifre_edittext.getText().toString();





                if ((e_mail_adresi_str.matches(""))||(şifre_str.matches(""))){



                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurunuz...", Toast.LENGTH_SHORT).show();



                }
                else {


                    progressDialog = new ProgressDialog(GirisYap.this);
                    progressDialog.setMessage("Giriş yapılıyor...");
                    progressDialog.show();



                    mAuth.signInWithEmailAndPassword(e_mail_adresi_str, şifre_str)
                            .addOnCompleteListener(GirisYap.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        SharedPreferences.Editor prefsEditor = sharedPref.edit();
                                        prefsEditor.putString("e_mail_key", e_mail_adresi_str);
                                        prefsEditor.apply();


                                        Intent intent = new Intent(GirisYap.this,AnaSayfa.class);
                                        startActivity(intent);
                                        finish();


                                    }

                                    else {

                                        Toast.makeText(getApplicationContext(),"Bilgilerinizi kontrol edin...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });




                }


            }
        });



    }


}



