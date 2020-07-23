package lk.iot.loadmanagement.view;

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
import lk.iot.loadmanagement.adapter.ManualControlAdapter;
import lk.iot.loadmanagement.data.FirebaseDAO;
import lk.iot.loadmanagement.data.HomeApplianceDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAppliance;

public class ManualControl extends AppCompatActivity {

    RecyclerView rvMCItems;
    ManualControlAdapter adapter;
    ArrayList<HomeAppliance> list;
    Toolbar MCToolBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        MCToolBar = findViewById(R.id.tb_manual_control);
        rvMCItems = findViewById(R.id.rvMCItems);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new HomeApplianceDAO(ManualControl.this).getAll(userID);

        MCToolBar.setTitle("Manual Control");
        adapter = new ManualControlAdapter(ManualControl.this,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                HomeAppliance hm = list.get(position);
                Toast.makeText(ManualControl.this,hm.getH_ID()+" : "+on,Toast.LENGTH_LONG).show();

                if(on) hm.setM__STATUS("1");
                else hm.setM__STATUS("0");

                new FirebaseDAO(ManualControl.this).UpdateManualControlToFirebase(hm.getH_ID(),hm.getM__STATUS());

            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {}
            @Override
            public void onPositionClicked(int position, View view) {}

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager( ManualControl.this,  RecyclerView.VERTICAL, false );
        rvMCItems.setLayoutManager( layoutManager );
        rvMCItems.setHasFixedSize( true );
        rvMCItems.setAdapter(adapter);
    }
}
