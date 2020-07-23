package lk.iot.loadmanagement.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import lk.iot.loadmanagement.model.HomeAppliance;
import lk.iot.loadmanagement.view.CustomerInfo;
import lk.iot.loadmanagement.view.MainActivity;


public class FirebaseDAO {

    Context context;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public FirebaseDAO(Context context) {
        this.context = context;
        this.context = context;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }
    //############################### DOWNLOAD ###########################

    public ArrayList<HomeAppliance> downloadAllFromFireBase(){

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        new HomeApplianceDAO(context).deleteAll(userID);

        fStore.collection("users").document(userID).collection("Appliances").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                InsertToLocalDb(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        return new HomeApplianceDAO(context).getAll(userID);
    }

    private void InsertToLocalDb(final String id) {


        DocumentReference documentReference = fStore.collection("users").document(userID).collection("Appliances").document(id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(Objects.requireNonNull(documentSnapshot).exists()){

                    new HomeApplianceDAO(context).insert(id,userID,
                            documentSnapshot.getString("H_LABEL"),
                            documentSnapshot.getString("T_5_TO_8"),
                            documentSnapshot.getString("T_8_TO_17"),
                            documentSnapshot.getString("T_17_TO_22"),
                            documentSnapshot.getString("T_22_TO_5"),
                            documentSnapshot.getString("M_STATUS")
                            );

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

    }


    //############################### INSERT ##############################

    public void insertToFirebase(final String label_name) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        final int[] h_id = {0};
        fStore.collection("users").document(userID).collection("Appliances").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            List<Integer> list = new ArrayList<>();
                            int max = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                int id = Integer.parseInt(document.getId());
                                if(max <id){
                                    max = id;
                                    list.clear();
                                    list.add(max+1);
                                }
                                Log.d(TAG, "max "+document.getId());

                            }
                           // Log.d(TAG, "max "+ max);
                            insertData(list.get(0),label_name);
                            Log.d(TAG, list.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void insertData(final int newID , final String label_name) {
        DocumentReference documentReference1 = fStore.collection("users").document(userID).collection("Appliances").document( newID+"");
        Map<String,Object> appliance1 = new HashMap<>();
        appliance1.put("H_ID", newID+"");
        appliance1.put("H_LABEL",label_name);
        appliance1.put("T_5_TO_8","0");
        appliance1.put("T_8_TO_17","0");
        appliance1.put("T_17_TO_22","0");
        appliance1.put("T_22_TO_5","0");
        appliance1.put("M_STATUS","0");
        documentReference1.set(appliance1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                new HomeApplianceDAO(context).insert(label_name, newID+"",userID);
                Log.d(TAG, "onSuccess: Appliances  created for "+ newID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    //############################### UPDATE ################
    public void UpdateManualControlToFirebase(final String h_id, final String m__status) {
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        DocumentReference docRef = fStore.collection("users").document(userID).collection("Appliances").document(h_id);

        Map<String,Object> edited = new HashMap<>();

        edited.put("M_STATUS",m__status);

        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new HomeApplianceDAO(context).updateManualControl(h_id,m__status,userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "update Manual status onFailure: " + e.toString());
                                    }
                                }
        );
    }

    public void UpdateTimeToFirebase(final int place_id, final String h_id, final String status) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        DocumentReference docRef = fStore.collection("users").document(userID).collection("Appliances").document(h_id);


        Map<String,Object> edited = new HashMap<>();


        switch (place_id) {
            case 1: {
                edited.put("T_5_TO_8", status);
            }
            break;
            case 2: {
                edited.put("T_8_TO_17", status);
            }
            break;
            case 3: {
                edited.put("T_17_TO_22", status);
            }
            break;
            case 4: {
                edited.put("T_22_TO_5", status);
            }
            break;
        }

        docRef.update(edited)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new HomeApplianceDAO(context).updateTime(place_id,h_id,status,userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Log.d(TAG, "update Time onFailure: " + e.toString());
                                          }
                                      }
                );
    }

    ///############################ DELETE ##################

    public void deleteFirebaseHomeAppliance(final String h_id) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        fStore.collection("users").document(userID).collection("Appliances").document(h_id).delete().addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new HomeApplianceDAO(context).deleteHomeAppliance(h_id+"",userID);
                        Log.d(TAG, "onSuccess: Appliances  deleted for "+ h_id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Delete onFailure: " + e.toString());
                                            }
                                        }
        );
    }
}
