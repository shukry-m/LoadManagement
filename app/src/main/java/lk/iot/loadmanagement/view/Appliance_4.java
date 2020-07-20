package lk.iot.loadmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.adapter.TimeAdapter;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.data.HomeApplianceTimeDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAplianceTime;
import lk.iot.loadmanagement.model.HomeAppliance;

public class Appliance_4 extends AppCompatActivity {

    RecyclerView rvTime22To5;
    TimeAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar tb_appliance1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_4);
        rvTime22To5 = findViewById(R.id.rvTime22To5);
        tb_appliance1 = findViewById(R.id.tb_appliance1);

        tb_appliance1.setTitle("Home Appliances");
        list = new HomeApplianceDAO(Appliance_4.this).getAll();

        adapter = new TimeAdapter(Appliance_4.this,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                Toast.makeText(Appliance_4.this,list.get(position).getName()+" : "+on,Toast.LENGTH_LONG).show();
                int res = on ? 1 : 0;
                new HomeApplianceTimeDAO(Appliance_4.this).insert(4,list.get(position).getId(),res);
            }

            @Override
            public void onPositionClicked(int position, View view) {

            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }


        });
        LinearLayoutManager layoutManager = new LinearLayoutManager( Appliance_4.this,  RecyclerView.VERTICAL, false );
        rvTime22To5.setLayoutManager( layoutManager );
        rvTime22To5.setHasFixedSize( true );
        rvTime22To5.setAdapter(adapter);
    }

    public void Save(View view) {

        ArrayList<HomeAplianceTime> list = new HomeApplianceTimeDAO(Appliance_4.this).getAll();

        for(HomeAplianceTime t: list){
            System.out.println("* "+ t);
        }

        displayStatusMessage("Do You Want To Save? ",3,0);


    }

    private void displayStatusMessage(String s, int colorValue, final int id) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView imageView;
        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3

        int success = R.drawable.ic_success;
        int error_image = R.drawable.ic_error;
        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = defaultColor;
        int img = success;
        if (colorValue == 1) {
            color = successColor;
            img = success;

        } else if (colorValue == 2) {
            color = errorColor;
            img = error_image;

        } else if (colorValue == 3) {
            color = warningColor;
            img = warning_image;
        }

        builder = new AlertDialog.Builder(Appliance_4.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){
                    Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity( intent );
                    finish();

                    alertDialog.dismiss();
                }else{
                    alertDialog.dismiss();
                }


            }
        });

    }
}
