package lk.iot.lmsApp.data;
//lk.iot.lmsApp
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lk.iot.lmsApp.model.HomeAppliance;
import lk.iot.lmsApp.model.Profile;
import lk.iot.lmsApp.view.RegisterActivity;


public class FirebaseDAO {

    Context context;
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";
    final FirebaseDatabase database;

    public FirebaseDAO(Context context) {
        this.context = context;
        fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance();
    }
    //############################### DOWNLOAD ###########################

    public ArrayList<HomeAppliance> downloadAllFromFireBase(){

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        new HomeApplianceDAO(context).deleteAll(userID);

        DatabaseReference ref = database.getReference("users/"+userID+"/Appliances");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Post post = dataSnapshot.getValue(Post.class);

               // System.out.println(dataSnapshot);
                //System.out.println("***********");
                //System.out.println(dataSnapshot.getValue(HomeAppliance.class));
                //HomeAppliance hm = dataSnapshot.getValue(HomeAppliance.class);
                for (DataSnapshot children : dataSnapshot.getChildren()) {

                    HomeAppliance hm = children.getValue(HomeAppliance.class);
                    System.out.println("***********");
                    System.out.println(hm.getH_ID());
                    InsertToLocalDb(hm.getH_ID());
                    System.out.println("***********");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

//        mDatabase.child("users").child(userID).child("Appliances").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



        return new HomeApplianceDAO(context).getAll(userID);
    }

    private void InsertToLocalDb(final String id) {

        DatabaseReference ref = database.getReference("users/"+userID+"/Appliances/"+id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Post post = dataSnapshot.getValue(Post.class);
                System.out.println(dataSnapshot.getValue(HomeAppliance.class));
                HomeAppliance hm = dataSnapshot.getValue(HomeAppliance.class);
                if(Objects.requireNonNull(dataSnapshot).exists()){

                    new HomeApplianceDAO(context).insert(id,userID,
                            hm.getH_LABEL(),
                            hm.getT_5_TO_8(),
                            hm.getT_8_TO_17(),
                            hm.getT_17_TO_22(),
                            hm.getT_22_TO_5(),
                            hm.getM__STATUS()
                    );

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

       /* DocumentReference documentReference = fStore.collection("users").document(userID).collection("Appliances").document(id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


            }
        });*/

    }


    //############################### CHECK Active ##############################

    public String checkActive(){
       /* userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        final String[] isActive = new String[1];
        DatabaseReference ref = database.getReference("users/"+userID +"/profile");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Post post = dataSnapshot.getValue(Post.class);
                if(dataSnapshot.exists()){

                    Profile prof = dataSnapshot.getValue(Profile.class);
                    System.out.println(prof);
                    isActive[0] ="true";//prof.getIsActive();//dataSnapshot.getValue().toString();

                }else{
                    isActive[0] = "false";
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        System.out.println(isActive[0]);
        if(isActive[0]==null){
            isActive[0] = "false";
        }
        return isActive[0];*/
       return "true";
    }
    //############################### INSERT ##############################

    public int insertToFirebase(final String label_name) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        new HomeApplianceDAO(context).insert(label_name,userID);
        final int[] h_id = {0};
        final int[] id = {2};
        //insertToLocalDb

        DatabaseReference ref = database.getReference("users/"+userID+"/Appliances");

        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Integer> list = new ArrayList<>();
                int max = 0;
                boolean isDuplicate = false;
                if(Objects.requireNonNull(dataSnapshot).exists()){
                    for (DataSnapshot children : dataSnapshot.getChildren()) {


                        HomeAppliance hm = children.getValue(HomeAppliance.class);
                        System.out.println(label_name);
                        System.out.println(hm);
                        int id = Integer.parseInt(hm.getH_ID());
                        if(max <id){
                            max = id;
                            list.clear();
                            list.add(max+1);
                        }
                        if(hm.getH_LABEL().equals(label_name)){
                            isDuplicate= true;
                            break;
                        }
                        Log.d(TAG, "max "+hm.getH_ID());
                    }
                    System.out.println("Before insert Data "+isDuplicate);
                    if(!isDuplicate) {
                        id[0] = insertData(list.get(0), label_name);
                        Log.d(TAG, list.toString());
                    }

                }else {
                    Log.d("tag", "onEvent: Document do not exists insertToFirebase");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The insertToFirebase read failed: " + databaseError.getCode());
            }
        });
        return id[0];

    }

    private int insertData(final int newID , final String label_name) {
       // new HomeApplianceDAO(context).insert(label_name, newID+"",userID);
        System.out.println("insertData");
        DatabaseReference ref = database.getReference();
       // ref.child(newID+"");

        Map<String,Object> appliance1 = new HashMap<>();
        appliance1.put("H_ID", newID+"");
        appliance1.put("H_LABEL",label_name);
        appliance1.put("T_5_TO_8","0");
        appliance1.put("T_8_TO_17","0");
        appliance1.put("T_17_TO_22","0");
        appliance1.put("T_22_TO_5","0");
        appliance1.put("M_STATUS","0");

        ref.child("users").child(userID).child("Appliances").child(newID+"").setValue(appliance1);
                 new HomeApplianceDAO(context).insert(label_name, newID+"",userID);
//        .onSuccessTask(new SuccessContinuation<Void, Object>() {
//            @NonNull
//            @Override
//            public Task<Object> then(@Nullable Void aVoid) throws Exception {
//
//
//                return  null;
//            }
//        });

        return newID;
    }
//        documentReference1.set(appliance1).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                new HomeApplianceDAO(context).insert(label_name, newID+"",userID);
//                Log.d(TAG, "onSuccess: Appliances  created for "+ newID);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: " + e.toString());
//            }
//        });



    //############################### UPDATE ################
    public void UpdateManualControlToFirebase(final String h_id, final String m__status) {
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
       // DocumentReference docRef = fStore.collection("users").document(userID).collection("Appliances").document(h_id);
        DatabaseReference ref = database.getReference("users/"+userID+"/Appliances/"+h_id+"/M_STATUS");

        Map<String,Object> edited = new HashMap<>();

        edited.put("M_STATUS",m__status);
        ref.setValue(m__status).onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
                new HomeApplianceDAO(context).updateManualControl(h_id,m__status,userID);
                return  null;
            }
        });
        /*docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        );*/
    }

    public void UpdateTimeToFirebase(final int place_id, final String h_id, final String status) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        DatabaseReference ref = database.getReference();
        //DocumentReference docRef = fStore.collection("users").document(userID).collection("Appliances").document(h_id);


        Map<String,Object> edited = new HashMap<>();


        switch (place_id) {
            case 1: {
                edited.put("T_5_TO_8", status);
                ref = database.getReference("users/"+userID+"/Appliances/"+h_id+"/T_5_TO_8");
            }
            break;
            case 2: {
                edited.put("T_8_TO_17", status);
                ref = database.getReference("users/"+userID+"/Appliances/"+h_id+"/T_8_TO_17");
            }
            break;
            case 3: {
                edited.put("T_17_TO_22", status);
                ref = database.getReference("users/"+userID+"/Appliances/"+h_id+"/T_17_TO_22");
            }
            break;
            case 4: {
                edited.put("T_22_TO_5", status);
                ref = database.getReference("users/"+userID+"/Appliances/"+h_id+"/T_22_TO_5");
            }
            break;
        }

        ref.setValue(status).onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
                new HomeApplianceDAO(context).updateTime(place_id,h_id,status,userID);
                return  null;
            }
        });

      /*  docRef.update(edited)
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
                );*/
    }

    ///############################ DELETE ##################

    public void deleteFirebaseHomeAppliance(final String h_id) {

        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        new HomeApplianceDAO(context).deleteHomeAppliance(h_id+"",userID);
        DatabaseReference ref = database.getReference("users/"+userID+"/Appliances");
        ref.child(h_id).removeValue().onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
                new HomeApplianceDAO(context).deleteHomeAppliance(h_id+"",userID);
                return  null;
            }
        });
      /*  fStore.collection("users").document(userID).collection("Appliances").document(h_id).delete().addOnSuccessListener(
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
        );*/
    }
}
