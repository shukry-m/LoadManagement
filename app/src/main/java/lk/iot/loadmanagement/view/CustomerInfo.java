package lk.iot.loadmanagement.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.helper.CustomMessage;

public class CustomerInfo extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023 ;
    EditText fullName,email,phone,street,city,province, zip;
    TextView verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button resendCode;
    Button resetPassLocal,changeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        street    = findViewById(R.id.profileStreet);
        province    = findViewById(R.id.profileProvince);
        zip = findViewById(R.id.profileZip);
        city = findViewById(R.id.profileCity);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        user = fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));

                    if(documentSnapshot.getString("street")!=null)
                        street.setText(documentSnapshot.getString("street"));

                    if(documentSnapshot.getString("province")!=null)
                        province.setText(documentSnapshot.getString("province"));

                    if(documentSnapshot.getString("zip")!=null)
                        zip.setText(documentSnapshot.getString("zip"));

                    if(documentSnapshot.getString("city")!=null)
                        city.setText(documentSnapshot.getString("city"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

    }

    public void Save(View view) {

        if(fullName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()){
           CustomMessage.displayStatusMessage("One or Many fields are empty",3,3,CustomerInfo.this);
            return;
        }

        final String email_ = email.getText().toString();
        user.updateEmail(email_).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                DocumentReference docRef = fStore.collection("users").document(user.getUid());

                Map<String,Object> edited = new HashMap<>();

                edited.put("email",email_);
                edited.put("fName",fullName.getText().toString());
                edited.put("phone",phone.getText().toString());
                edited.put("city",city.getText().toString());
                edited.put("street",street.getText().toString());
                edited.put("province",province.getText().toString());
                edited.put("zip", zip.getText().toString());

                docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerInfo.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                });
                Toast.makeText(CustomerInfo.this, "Email is changed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerInfo.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Cancel(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
