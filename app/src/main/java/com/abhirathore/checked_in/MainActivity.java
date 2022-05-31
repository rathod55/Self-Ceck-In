package com.abhirathore.checked_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button image_button;
    SharedPreferences sp;
    Button  register;
    TextView forgot_password;
    ProgressDialog progressDialog;
    EditText ed1,ed2;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int READ_CODE=100;
    private static final int WRITE_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_CODE);

        sp=getSharedPreferences("image_button",MODE_PRIVATE);

        image_button=(Button) findViewById(R.id.imageButton1);
        register=(Button)findViewById(R.id.register_button1);
        ed1=(EditText)findViewById(R.id.editText1);
        ed2=(EditText)findViewById(R.id.editText2);
        forgot_password=(TextView)findViewById(R.id.forgot_password_TV);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals(null))
                {
                    Toast.makeText(getApplicationContext(), "Someting went wrong", Toast.LENGTH_SHORT).show();
                }
                else if (ed1.getText().toString().equals(""))
                {
                    ed1.setError("Enter the Email Address");
                    Toast.makeText(getApplicationContext(), "Someting went wrong", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(ed1.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            ed1.setEnabled(true);
            ed2.setEnabled(true);
            image_button.setEnabled(true);
            register.setEnabled(true);



            authStateListener= new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    if(firebaseUser!=null)
                    {

                        Toast.makeText(MainActivity.this,"You have successfully logged in",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));



                    }
                    else
                    {

                        Toast.makeText(MainActivity.this,"Please logged in",Toast.LENGTH_LONG).show();
                    }
                }
            };
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Register.class));
                }
            });

            image_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressDialog=new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Loading");
                    progressDialog.setMessage("Authenicating User");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    final String email=ed1.getText().toString();
                    String password=ed2.getText().toString();
                    if(email.isEmpty())
                    {

                        ed1.setError("Please Provide Email");
                        ed1.requestFocus();
                    }
                    else if(password.isEmpty())
                    {

                        ed2.setError("Enter the New Password");
                        ed2.requestFocus();
                    }
                    else if(email.isEmpty() && password.isEmpty())
                    {

                        Toast.makeText(MainActivity.this,"Plaese Fill the Details",Toast.LENGTH_LONG).show();
                    }
                    else if(!(email.isEmpty() && password.isEmpty()))
                    {

                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                {

                                    Toast.makeText(MainActivity.this,"loggin unsuccesfull",Toast.LENGTH_LONG).show();

                                }
                                else
                                {

                                    SharedPref.saveSharedSetting(MainActivity.this,"CaptainCode","false");
                                    Toast.makeText(MainActivity.this,"Login Successfull",Toast.LENGTH_LONG).show();
                                    Intent loggedin=new Intent(getApplicationContext(),HomeActivity.class);
                                    startActivity(loggedin);
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Failed to Login",Toast.LENGTH_LONG).show();

                    }
                }
            });

        }



        else

        {
            ed1.setEnabled(false);
            ed2.setEnabled(false);
            image_button.setEnabled(false);
            register.setEnabled(false);
            Toast.makeText(getApplicationContext(),"Not connected to Internet",Toast.LENGTH_LONG).show();
        }




    }
    public void checkPermission(String permission, int request_code)
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, permission)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission},request_code);

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Already Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==READ_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Read Storage Permision Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Read Storage Permision Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==WRITE_CODE)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Write Storage Permision Granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Write Storage Permision Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}

