package lk.iot.loadmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.data.HomeApplianceDAO;

public class RegisterActivity extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn,textView2;
    ProgressBar progressBar;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mPhone      = findViewById(R.id.phone);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn   = findViewById(R.id.createText);
        textView2   = findViewById(R.id.textView2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

             //   if(Network.isNetworkAvailable(this)){
                progressBar.setVisibility(View.VISIBLE);




                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                    insertDataToFirebase();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }


    private void insertDataToFirebase() {
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        DocumentReference documentReference1 = fStore.collection("users").document(userID).collection("Appliances").document("1");
        Map<String,Object> appliance1 = new HashMap<>();
        appliance1.put("H_ID","1");
        appliance1.put("H_LABEL","Kitchen Lights");
        appliance1.put("T_5_TO_8","0");
        appliance1.put("T_8_TO_17","0");
        appliance1.put("T_17_TO_22","0");
        appliance1.put("T_22_TO_5","0");
        appliance1.put("M_STATUS","0");
        documentReference1.set(appliance1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Appliances 1 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("1",userID,"Kitchen Lights",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        DocumentReference documentReference2 = fStore.collection("users").document(userID).collection("Appliances").document("2");
        Map<String,Object> appliance2 = new HashMap<>();
        appliance2.put("H_ID","2");
        appliance2.put("H_LABEL","Kitchen Fan");
        appliance2.put("T_5_TO_8","0");
        appliance2.put("T_8_TO_17","0");
        appliance2.put("T_17_TO_22","0");
        appliance2.put("T_22_TO_5","0");
        appliance2.put("M_STATUS","0");
        documentReference2.set(appliance2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Appliances 2 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("2",userID,"Kitchen Fan",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        DocumentReference documentReference3 = fStore.collection("users").document(userID).collection("Appliances").document("3");
        Map<String,Object> appliance3 = new HashMap<>();
        appliance3.put("H_ID","3");
        appliance3.put("H_LABEL","Dining Room Lights");
        appliance3.put("T_5_TO_8","0");
        appliance3.put("T_8_TO_17","0");
        appliance3.put("T_17_TO_22","0");
        appliance3.put("T_22_TO_5","0");
        appliance3.put("M_STATUS","0");
        documentReference3.set(appliance3).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  Log.d(TAG, "onSuccess: Appliances 3 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("3",userID,"Dining Room Lights",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        DocumentReference documentReference4 = fStore.collection("users").document(userID).collection("Appliances").document("4");
        Map<String,Object> appliance4 = new HashMap<>();
        appliance4.put("H_ID","4");
        appliance4.put("H_LABEL","Dining Room Fans");
        appliance4.put("T_5_TO_8","0");
        appliance4.put("T_8_TO_17","0");
        appliance4.put("T_17_TO_22","0");
        appliance4.put("T_22_TO_5","0");
        appliance4.put("M_STATUS","0");
        documentReference4.set(appliance4).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 Log.d(TAG, "onSuccess: Appliances 4 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("4",userID,"Dining Room Fans",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        DocumentReference documentReference5 = fStore.collection("users").document(userID).collection("Appliances").document("5");
        Map<String,Object> appliance5 = new HashMap<>();
        appliance5.put("H_ID","5");
        appliance5.put("H_LABEL","Living Room Lights");
        appliance5.put("T_5_TO_8","0");
        appliance5.put("T_8_TO_17","0");
        appliance5.put("T_17_TO_22","0");
        appliance5.put("T_22_TO_5","0");
        appliance5.put("M_STATUS","0");
        documentReference5.set(appliance5).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  Log.d(TAG, "onSuccess: Appliances 5 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("5",userID,"Living Room Lights",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        DocumentReference documentReference6 = fStore.collection("users").document(userID).collection("Appliances").document("6");
        Map<String,Object> appliance6 = new HashMap<>();
        appliance6.put("H_ID","6");
        appliance6.put("H_LABEL","Living Room Fans");
        appliance6.put("T_5_TO_8","0");
        appliance6.put("T_8_TO_17","0");
        appliance6.put("T_17_TO_22","0");
        appliance6.put("T_22_TO_5","0");
        appliance6.put("M_STATUS","0");
        documentReference6.set(appliance6).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  Log.d(TAG, "onSuccess: Appliances 6 created for "+ userID);
                new HomeApplianceDAO(RegisterActivity.this).insert("6",userID,"Living Room Fans",
                        "0","0","0","0","0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {

        System.exit(0);
        finish();

    }
}
