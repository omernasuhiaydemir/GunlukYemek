package com.omernsh.gunlukyemek;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ForgotPass extends AppCompatActivity {


    EditText mail_edittext;
    Button tamam_button;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    String e_mail_adresi_str, şifre_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mail_edittext = findViewById(R.id.e_mail_edittext);
        tamam_button = findViewById(R.id.tamam_button);




        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                e_mail_adresi_str=mail_edittext.getText().toString();
                tamam_button.setEnabled(false);


                mAuth.sendPasswordResetEmail(e_mail_adresi_str)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {





                                    Toast.makeText(getApplicationContext(), "Mail gönderildi, lütfen mail adresinizi kontrol edin!", Toast.LENGTH_SHORT).show();


                                }else {


                                    Toast.makeText(getApplicationContext(), "Hata, lütfen girdiğiniz mail adresinizi kontrol edin!", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

            }
        });




    }


}



