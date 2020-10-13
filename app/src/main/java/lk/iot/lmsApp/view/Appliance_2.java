package lk.iot.lmsApp.view;

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

import java.util.ArrayList;

import lk.iot.lmsApp.R;
import lk.iot.lmsApp.adapter.TimeAdapter;
import lk.iot.lmsApp.data.FirebaseDAO;
import lk.iot.lmsApp.data.HomeApplianceDAO;
import lk.iot.lmsApp.helper.ClickListener;
import lk.iot.lmsApp.model.HomeAppliance;

public class Appliance_2 extends AppCompatActivity {

    RecyclerView rvTime8To17;
    TimeAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar tb_appliance1;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_2);
        rvTime8To17 = findViewById(R.id.rvTime8To17);
        tb_appliance1 = findViewById(R.id.tb_appliance1);

        tb_appliance1.setTitle("Home Appliances");
        fAuth = FirebaseAuth.getInstance();
      //  fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new HomeApplianceDAO(Appliance_2.this).getAll(userID);

        adapter = new TimeAdapter(Appliance_2.this,userID,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

                Toast.makeText(Appliance_2.this,list.get(position).getH_LABEL()+" : "+on,Toast.LENGTH_LONG).show();
                String res = on ? "1" : "0";

                new FirebaseDAO(Appliance_2.this).UpdateTimeToFirebase(2,list.get(position).getH_ID(),res);

            }

            @Override
            public void onPositionClicked(int position, View view) {

            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }


        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager( Appliance_2.this,  RecyclerView.VERTICAL, false );
        rvTime8To17.setLayoutManager( layoutManager2 );
        rvTime8To17.setHasFixedSize( true );
        rvTime8To17.setAdapter(adapter);
    }

    public void Next(View view) {
        Intent intent = new Intent(this, Appliance_3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), Appliance_1.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( intent );
        finish();
    }

}
