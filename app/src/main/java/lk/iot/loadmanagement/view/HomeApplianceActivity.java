package lk.iot.loadmanagement.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.adapter.HomeApplianceAdapter;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAppliance;

public class HomeApplianceActivity extends AppCompatActivity {
    RecyclerView rvHomeItem;
    HomeApplianceAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar toolbar_home_app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_appliance);
        final EditText Data = findViewById(R.id.Data);
        Button Add = findViewById(R.id.btnAdd);
         rvHomeItem = findViewById(R.id.rvHomeApplItem);
         toolbar_home_app = findViewById(R.id.tb_home_appl);

         toolbar_home_app.setTitle("Home Appliance");

        list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll();

         adapter = new HomeApplianceAdapter(HomeApplianceActivity.this,list,new ClickListener(){

             @Override
             public void onPositionClicked(int position, View view) {
                 HomeAppliance hm = list.get(position);
                 displayStatusMessage("Do you want to remove this Item",3,0,hm);

             }

             @Override
             public void onCheckedChanged(CompoundButton cb, boolean on) {

             }

             @Override
             public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

             }
         });

        LinearLayoutManager layoutManager = new LinearLayoutManager( HomeApplianceActivity.this,  RecyclerView.VERTICAL, false );
        rvHomeItem.setLayoutManager( layoutManager );
        rvHomeItem.setHasFixedSize( true );
        rvHomeItem.setAdapter(adapter);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getText().toString().equals("")){
                    displayStatusMessage("Please Enter a Home Appliance",2,2,null);
                }else {
                    insertData(Data.getText().toString());
                    Data.setText("");
                    setAdapter();
                }
            }
        });
    }



    private void setAdapter() {
        list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll();
        adapter = new HomeApplianceAdapter(HomeApplianceActivity.this,list,new ClickListener(){

            @Override
            public void onPositionClicked(int position, View view) {
                HomeAppliance hm = list.get(position);
                displayStatusMessage("Do you want to remove this Item",3,0,hm);
            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

            }
        });
        rvHomeItem.setAdapter(adapter);
    }

    private void insertData(String DataItem) {
        int y = new HomeApplianceDAO(HomeApplianceActivity.this).insert(DataItem);
        System.out.println(y);
       // getData();
    }

   /* private void getData() {
        ArrayList<HomeAppliance> list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll();
        for(HomeAppliance h:list){
            System.out.println("****************");
            System.out.println(h);
            System.out.println("****************");
        }

    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void displayStatusMessage(String s, int colorValue, final int id, final HomeAppliance hm) {

        AlertDialog.Builder builder = null;
        View view = null;
        final TextView tvOk, tvMessage,tvCancel;
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

        builder = new AlertDialog.Builder(HomeApplianceActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);
        if (id==0) {
            tvCancel.setVisibility(View.VISIBLE);
        }else{
            tvCancel.setVisibility(View.INVISIBLE);
        }

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){

                      new HomeApplianceDAO(HomeApplianceActivity.this).deleteHomeAppliance(hm.getId());
                    setAdapter();

                    alertDialog.dismiss();
                }else{
                    tvCancel.setVisibility(View.INVISIBLE);
                    alertDialog.dismiss();
                }


            }
        });

    }
}
