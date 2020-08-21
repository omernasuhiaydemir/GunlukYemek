package com.omernsh.gunlukyemek;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

public class AnaSayfa extends AppCompatActivity {


    Button kayıtlı_veriler, kaydet_button,log_out_button;
    EditText bayii_kodu_editText, sıcak_editText, soguk_editText;

    String bayii_koduStr, sıcakStr, sogukStr;

    TextView e_mail_textview,tarih_textview;



    SharedPreferences sharedPref,sharedPref_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ana_sayfa);


        kayıtlı_veriler = findViewById(R.id.kayıtlı_veriler);

        kaydet_button = findViewById(R.id.kaydet_button);

        log_out_button = findViewById(R.id.log_out_button);

        bayii_kodu_editText = findViewById(R.id.bayii_kodu_editText);
        sıcak_editText = findViewById(R.id.sıcak_editText);
        soguk_editText = findViewById(R.id.soguk_editText);

        e_mail_textview = findViewById(R.id.e_mail_textview);
        tarih_textview = findViewById(R.id.tarih_textview);



         sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);

        sharedPref_login = this.getSharedPreferences("sharedPref_login",Context.MODE_PRIVATE);



        String kullanıcı_e_mail_str = sharedPref_login.getString("e_mail_key","Kullanıcı e-maili bulunamadı");
        e_mail_textview.setText(kullanıcı_e_mail_str);

        Date d = new Date();
        CharSequence s = android.text.format.DateFormat.format("MM/dd/yy",d.getTime());

        tarih_textview.setText(s.toString());


        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(AnaSayfa.this);
                builder.setTitle("Hesabınızdan çıkış yapmak istediğinizden emin misiniz?");

                builder.setNegativeButton("İptal", null);

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent intent = new Intent(AnaSayfa.this, GirisYap.class);
                        startActivity(intent);
                        FirebaseAuth.getInstance().signOut();





                    }
                });
                builder.show();



            }
        });



        kayıtlı_veriler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(AnaSayfa.this, KayıtlıVeriler.class);
                startActivity(intent);


            }
        });

        kaydet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             bayii_koduStr =   bayii_kodu_editText.getText().toString();
             sogukStr = soguk_editText.getText().toString();
             sıcakStr =  sıcak_editText.getText().toString();




             if (bayii_koduStr.equals("") ||  sogukStr.equals("") || sıcakStr.equals("")  ){

                 Toast.makeText(getApplicationContext(),"Lütfen bütün bilgileri eksiksiz giriniz!",Toast.LENGTH_LONG).show();

             }else {


                 String time= String.valueOf(System.currentTimeMillis());

                 Data data = new Data(bayii_koduStr,sogukStr,sıcakStr);

                 SharedPreferences.Editor prefsEditor = sharedPref.edit();
                 Gson gson = new Gson();
                 String json = gson.toJson(data);
                 prefsEditor.putString(time, json);
                 prefsEditor.apply();

                 System.out.println(json);


                 bayii_kodu_editText.setText("");
                 sıcak_editText.setText("");
                 soguk_editText.setText("");

                 Toast.makeText(getApplicationContext(),"Veri kaydedildi!",Toast.LENGTH_LONG).show();



             }


            }
        });


    }


}
