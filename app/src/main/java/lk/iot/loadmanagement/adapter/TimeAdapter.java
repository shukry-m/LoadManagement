package lk.iot.loadmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lk.iot.loadmanagement.R;
import lk.iot.loadmanagement.data.HomeApplianceTimeDAO;
import lk.iot.loadmanagement.helper.ClickListener;
import lk.iot.loadmanagement.model.HomeAplianceTime;
import lk.iot.loadmanagement.model.HomeAppliance;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeAdapterViewHolder> {

        Context context;
        ArrayList<HomeAppliance> list;
        ClickListener listener;

        public TimeAdapter(Context context, ArrayList<HomeAppliance>list, ClickListener listener){
            this.context = context;
            this.list = list;
            this.listener=listener;
        }

        @NonNull
        @Override
        public TimeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_for_appliance_time, parent, false );
            return new TimeAdapterViewHolder( view,listener );
        }

        @Override
        public void onBindViewHolder(TimeAdapterViewHolder holder, int position) {
            HomeAppliance homeAppliance = list.get(position);
            holder.txtRoom.setText(homeAppliance.getName());
            HomeAplianceTime hm = new HomeApplianceTimeDAO(this.context).getHomeAppliance(homeAppliance.getId());
            holder.check.setChecked(false);

            System.out.println("* "+this.context.getClass().getSimpleName());

            switch (this.context.getClass().getSimpleName()){
                case "Appliance_1":{
                    if(hm.getT_5_TO_8()==1){
                        holder.check.setChecked(true);
                    }
                }
                break;
                case "Appliance_2":
                {
                    if(hm.getT_8_TO_17()==1){
                        holder.check.setChecked(true);
                    }
                }
                break;
                case "Appliance_3":
                {
                    if(hm.getT_17_TO_22()==1){
                        holder.check.setChecked(true);
                    }
                }
                break;
                case "Appliance_4":
                {
                    if(hm.getT_22_TO_5()==1){
                        holder.check.setChecked(true);
                    }
                }
                break;
            }



        }

        @Override
        public int getItemCount() {
            if(list != null){
                return list.size();
            }
            return 0;
        }

        public static class TimeAdapterViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

            CheckBox check;
            TextView txtRoom;
            private WeakReference<ClickListener> listenerRef;
            public TimeAdapterViewHolder(@NonNull View itemView,ClickListener listener) {
                super(itemView);

                check = itemView.findViewById(R.id.check);
                txtRoom = itemView.findViewById(R.id.txtRoom);
                check.setOnCheckedChangeListener(this);
                listenerRef = new WeakReference<>(listener);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listenerRef.get().onCheckedChanged(getAdapterPosition(),buttonView,isChecked);
            }
        }
    }
