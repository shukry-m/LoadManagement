package lk.iot.loadmanagement.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.adapter.TimeAdapter;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.data.HomeApplianceTimeDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAppliance;

public class Appliance_1 extends AppCompatActivity {

    RecyclerView rvTime5To8;
    TimeAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar tb_appliance1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_1);
        rvTime5To8 = findViewById(R.id.rvTime5To8);
        tb_appliance1 = findViewById(R.id.tb_appliance1);

        tb_appliance1.setTitle("Home Appliances");
        list = new HomeApplianceDAO(Appliance_1.this).getAll();

        adapter = new TimeAdapter(Appliance_1.this,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                HomeAppliance hm = list.get(position);
                System.out.println(hm);

                Toast.makeText(Appliance_1.this,list.get(position).getName()+" : "+on,Toast.LENGTH_LONG).show();
                int res = on ? 1 : 0;
                System.out.println("* hm "+hm+" res "+res);
                new HomeApplianceTimeDAO(Appliance_1.this).insert(1,list.get(position).getId(),res);
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

}
