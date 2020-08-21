package com.omernsh.gunlukyemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KayıtlıVeriler extends AppCompatActivity {



    private List<Data> dataList = new ArrayList<>();
    private RecyclerView rv;
    private DataAdapter mAdapter;

    Context context = this;

    Button veri_tabanına_kaydet;





    SharedPreferences sharedPref,sharedPref_login;

    Data obj;

    String ipAddress;

    private DatabaseReference mRef,dbRef1;
    private FirebaseDatabase db;

    String IP_ADRESS;

    String kullanıcı_e_mail_str;


    private List<String> bayii_kodları_listesi = new ArrayList<>();
    private List<String> soguk_listesi = new ArrayList<>();
    private List<String> sıcak_listesi = new ArrayList<>();



    TextView veri_yok_textview;

    String kullanıcı_adı;

    List<String> gelen_liste;

    int veri_sayısı;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kayitli_veriler);


        veri_yok_textview = findViewById(R.id.veri_yok_text);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
         ipAddress = Formatter.formatIpAddress(ip);

        System.out.println("IP ADRESİ "+ipAddress);



        veri_tabanına_kaydet = findViewById(R.id.veri_tabanına_kaydet);
        veri_tabanına_kaydet.setEnabled(false);

        sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);

        sharedPref_login = this.getSharedPreferences("sharedPref_login",Context.MODE_PRIVATE);

        kullanıcı_e_mail_str = sharedPref_login.getString("e_mail_key","Kullanıcı e-maili bulunamadı");


        int index = kullanıcı_e_mail_str.indexOf("@");

         kullanıcı_adı = kullanıcı_e_mail_str.substring(0, index);

        System.out.println("KULLANICI ADI "+ kullanıcı_adı);






        Map<String, ?> prefsMap = sharedPref.getAll();


        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {

            String json =  entry.getValue().toString();

            System.out.println("Veriler "+json);

            Gson gson = new Gson();

            obj = gson.fromJson(json, Data.class);

            bayii_kodları_listesi.add(obj.bayii_kodu);
            sıcak_listesi.add(obj.sıcak);
            soguk_listesi.add(obj.soguk);

            System.out.println("Bayi kodları listesi "+bayii_kodları_listesi);
            System.out.println("sıcak listesi "+sıcak_listesi);
            System.out.println("soguk listesi "+soguk_listesi);

            if (bayii_kodları_listesi.size() !=  0){

                veri_yok_textview.setVisibility(View.INVISIBLE);
                veri_tabanına_kaydet.setEnabled(true);


            }

            dataList.add(obj);

        }

        final ProgressDialog progressDialog = new ProgressDialog(KayıtlıVeriler.this);
        progressDialog.setMessage("IP adresiniz kontrol ediliyor, lütfen bekleyin...");
        progressDialog.show();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("IP_ADRESS");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                IP_ADRESS = dataSnapshot.getValue(String.class);
                System.out.println(IP_ADRESS);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Veriler").child(kullanıcı_adı);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                gelen_liste = new ArrayList<>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String value1 = String.valueOf(dataSnapshot1.getKey());
                    gelen_liste.add(value1);

                    veri_sayısı = gelen_liste.size() + 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });




        rv = findViewById(R.id.recycler_view);



        mAdapter = new DataAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);


        prepareMovieData();



        veri_tabanına_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ipAddress.equals(IP_ADRESS)){


                    Date d = new Date();
                    CharSequence s = android.text.format.DateFormat.format("MM-dd-yy",d.getTime());
                    String gunun_tarihi = s.toString();
                    System.out.println(gunun_tarihi);

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Veriler").child(kullanıcı_adı);

                    for (int i = 0; i < bayii_kodları_listesi.size(); i++) {

                        mRef.child(String.valueOf(veri_sayısı + i)).child("Bayi").setValue(bayii_kodları_listesi.get(i));
                        mRef.child(String.valueOf(veri_sayısı + i )).child("Tarih").setValue(gunun_tarihi);
                        mRef.child(String.valueOf(veri_sayısı + i)).child("Sicaklar").setValue(sıcak_listesi.get(i));
                        mRef.child(String.valueOf(veri_sayısı + i)).child("Soguklar").setValue(soguk_listesi.get(i));

                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(KayıtlıVeriler.this);
                    builder.setTitle("Veriler başarıyla kaydedildi!");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            Intent intent = new Intent(KayıtlıVeriler.this, AnaSayfa.class);
                            startActivity(intent);

                            sharedPref.edit().clear().apply();


                        }
                    });
                    builder.show();



                    Toast.makeText(getApplicationContext(),"Veriler başarı ile kaydedildi...",Toast.LENGTH_LONG).show();
                   // sharedPref.edit().clear().apply();

                    prepareMovieData();



                }else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(KayıtlıVeriler.this);
                    builder.setTitle("Wi-Fi ağınızı kontrol edin!");
                    builder.setMessage("Verilerinizi yalnızca belirlenen wi-fi ağı üzerinden veri tabanına kaydedebilirsiniz.");
                    builder.setNegativeButton("Tamam", null);

                    builder.show();


                }





            }
        });






    }
    private void prepareMovieData() {

        mAdapter.notifyDataSetChanged();
    }
}

