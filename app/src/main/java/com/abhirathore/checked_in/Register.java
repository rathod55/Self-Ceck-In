package com.abhirathore.checked_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText ed1,ed2;
    Button submit;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

            ed1=(EditText)findViewById(R.id.editText1);
            ed2=(EditText)findViewById(R.id.editText2);
            submit=(Button)findViewById(R.id.register_button);

            firebaseAuth=FirebaseAuth.getInstance();

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog=new ProgressDialog(Register.this);
                    progressDialog.setMessage("Registering User");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    String email=ed1.getText().toString();
                    String password=ed2.getText().toString();
                    if(email.isEmpty())
                    {

                        ed1.setError("Please Provide Email");
                        ed1.requestFocus();
                        progressDialog.dismiss();
                    }
                    else if(password.isEmpty())
                    {

                        ed2.setError("Enter the New Password");
                        ed2.requestFocus();
                        progressDialog.dismiss();
                    }
                    else if(email.isEmpty() && password.isEmpty())
                    {

                        Toast.makeText(Register.this,"Plaese Fill the Details",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    else if(!(email.isEmpty() && password.isEmpty()))
                    {

                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(Register.this,"not registered try again",Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    Toast.makeText(Register.this,"registered to the account",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Register.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Register.this,"Failed to Register",Toast.LENGTH_LONG).show();

                    }
                }
            });


        }

    }

