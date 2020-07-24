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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.adapter.HomeApplianceAdapter;
import lk.iot.loadmanagement.data.FirebaseDAO;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.helper.Network;
import lk.iot.loadmanagement.model.HomeAppliance;

public class HomeApplianceActivity extends AppCompatActivity {
    RecyclerView rvHomeItem;
    HomeApplianceAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar toolbar_home_app;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_appliance);
        final EditText Data = findViewById(R.id.Data);
        Button Add = findViewById(R.id.btnAdd);
         rvHomeItem = findViewById(R.id.rvHomeApplItem);
         toolbar_home_app = findViewById(R.id.tb_home_appl);

         toolbar_home_app.setTitle("Home Appliance");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
     //   list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll();

        list =new HomeApplianceDAO(HomeApplianceActivity.this).getAll(userID);


         adapter = new HomeApplianceAdapter(HomeApplianceActivity.this,list,new ClickListener(){

             @Override
             public void onPositionClicked(int position, View view) {
                 HomeAppliance hm = list.get(position);
                 displayStatusMessage("Do you want to remove this Item",3,0,hm,position);

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


        class Task implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    final int value = i;
                    System.out.println("* "+i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }

        }


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getText().toString().equals("")){
                    displayStatusMessage("Please Enter a Home Appliance",2,2,null,0);
                }else {
                    if(Network.isNetworkAvailable(HomeApplianceActivity.this)){
                          insertData(Data.getText().toString(),list.size());
                        Data.setText("");

                        //new Thread(new Task()).start();
//
//                        try {
//                            Thread.sleep(1000);
//                            setAdapter();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                     //   setAdapter();
                        //rvHomeItem.setAdapter(adapter);
//                        adapter.notify();
                        adapter.notifyDataSetChanged();
  //                      adapter.notifyAll();
                    }else{
                        displayStatusMessage("Please Check your Internet Connection",3,3,null,0);
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), ApplianceActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( intent );
        finish();
    }



    private void setAdapter() {


        String userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
      //  list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll();

        list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll(userId);
        adapter = new HomeApplianceAdapter(HomeApplianceActivity.this,list,new ClickListener(){

            @Override
            public void onPositionClicked(int position, View view) {
                HomeAppliance hm = list.get(position);
                displayStatusMessage("Do you want to remove this Item",3,0,hm,position);
            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

            }
        });
       // adapter.notifyItemInserted(list.size()-1);

     //   rvHomeItem.removeAllViews();
        rvHomeItem.setAdapter(adapter);

       // rvHomeItem.invalidate();

           adapter.notifyDataSetChanged();
    }

    private void insertData(String DataItem,int listSize) {

         int id  =new FirebaseDAO(HomeApplianceActivity.this).insertToFirebase(DataItem);
      //  System.out.println(y);
        adapter.notifyDataSetChanged();
        if(id>0){
            setAdapter();
        }
       // getData();
    }

    private void getData() {
        ArrayList<HomeAppliance> list = new HomeApplianceDAO(HomeApplianceActivity.this).getAll(userID);
        for(HomeAppliance h:list){
            System.out.println("****************");
            System.out.println(h);
            System.out.println("****************");
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
//        adapter.notify();
        adapter.notifyDataSetChanged();
//        adapter.notifyAll();
    }

    private void displayStatusMessage(String s, int colorValue, final int id, final HomeAppliance hm, final int position) {

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

                      new FirebaseDAO(HomeApplianceActivity.this).deleteFirebaseHomeAppliance(hm.getH_ID());

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
