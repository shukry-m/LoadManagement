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

public class Appliance_1 extends AppCompatActivity {

    RecyclerView rvTime5To8;
    TimeAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar tb_appliance1;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_1);
        rvTime5To8 = findViewById(R.id.rvTime5To8);
        tb_appliance1 = findViewById(R.id.tb_appliance1);

        tb_appliance1.setTitle("Home Appliances");
        fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        list = new HomeApplianceDAO(Appliance_1.this).getAll(userID);

        adapter = new TimeAdapter(Appliance_1.this,userID,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                HomeAppliance hm = list.get(position);
                System.out.println(hm);

                Toast.makeText(Appliance_1.this,list.get(position).getH_LABEL()+" : "+on,Toast.LENGTH_LONG).show();
                String res = on ? "1" : "0";
                System.out.println("* hm "+hm+" res "+res);

                new FirebaseDAO(Appliance_1.this).UpdateTimeToFirebase(1,list.get(position).getH_ID(),res);
            }

            @Override
            public void onPositionClicked(int position, View view) {

            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager( Appliance_1.this,  RecyclerView.VERTICAL, false );
        rvTime5To8.setLayoutManager( layoutManager );
        rvTime5To8.setHasFixedSize( true );
        rvTime5To8.setAdapter(adapter);

    }

    public void Next(View view) {
        Intent intent = new Intent(this, Appliance_2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), ApplianceActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( intent );
        finish();
    }


}
