package lk.iot.loadmanagement.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.model.HomeAppliance;

public class CostAndPower extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_and_power);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
/// db.collection("cities").document("DC").delete();

        // db.collection("cities").document("DC").delete();
       // fStore.collection("users").document(userID).collection("Appliances").document(6+"").delete();
        //System.out.println("* delete");
    }
}
