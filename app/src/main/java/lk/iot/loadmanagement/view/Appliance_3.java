package lk.iot.loadmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.adapter.TimeAdapter;
import lk.iot.loadmanagement.data.FirebaseDAO;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAppliance;

public class Appliance_3 extends AppCompatActivity {

    RecyclerView rvTime17To22;
    TimeAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar tb_appliance1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_3);
        rvTime17To22 = findViewById(R.id.rvTime17To22);
        tb_appliance1 = findViewById(R.id.tb_appliance1);
        tb_appliance1.setTitle("Home Appliances");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new HomeApplianceDAO(Appliance_3.this).getAll(userID);

        adapter = new TimeAdapter(Appliance_3.this,userID,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                Toast.makeText(Appliance_3.this,list.get(position).getH_LABEL()+" : "+on,Toast.LENGTH_LONG).show();
                String res = on ? "1" : "0";

                new FirebaseDAO(Appliance_3.this).UpdateTimeToFirebase(3,list.get(position).getH_ID(),res);
            }

            @Override
            public void onPositionClicked(int position, View view) {

            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }


        });
        LinearLayoutManager layoutManager = new LinearLayoutManager( Appliance_3.this,  RecyclerView.VERTICAL, false );
        rvTime17To22.setLayoutManager( layoutManager );
        rvTime17To22.setHasFixedSize( true );
        rvTime17To22.setAdapter(adapter);

    }

    public void Next(View view) {
        Intent intent = new Intent(this, Appliance_4.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), Appliance_2.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( intent );
        finish();
    }

}
