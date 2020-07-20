package lk.iot.loadmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import lk.iot.loadmanagement.db.DatabaseHelper;
import lk.iot.loadmanagement.model.HomeAppliance;
import lk.iot.loadmanagement.model.ManualControlItem;
import lk.iot.loadmanagement.view.ManualControl;

public class ManualControlDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;

    public ManualControlDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public int insert(int h_id,int status) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL + " WHERE " + dbHelper.MA_HA_ID + " = " + h_id;

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert
                ContentValues cv = new ContentValues();

                cv.put(dbHelper.MA_HA_ID, h_id);
                cv.put(dbHelper.MA_HA_STATUS, status);

                count = (int) dB.insert(dbHelper.TABLE_MANUAL_CONTROL, null, cv);

            }else{
                //update
                ContentValues cv = new ContentValues();

                cv.put(dbHelper.MA_HA_STATUS, status);

                count = (int) dB.update(dbHelper.TABLE_MANUAL_CONTROL, cv, dbHelper.MA_HA_ID + " =?", new String[]{h_id +""});

            }


        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }


//mm
    public ArrayList<ManualControlItem> getAll(){

        ArrayList<ManualControlItem> list = new ArrayList<>();



        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{
            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL+" WHERE "
                    +dbHelper.MA_HA_ID +" IN (SELECT "+dbHelper.HA_ID+" FROM "+dbHelper.TABLE_HOME_APPLIANCE+" )";

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                String HomeApplianceName = "";
                int h_id = cur.getInt(cur.getColumnIndex(dbHelper.MA_HA_ID));
                int status = cur.getInt(cur.getColumnIndex(dbHelper.MA_HA_STATUS));


                String select2 = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE+" WHERE "+dbHelper.HA_ID+" = "+h_id;
                Cursor c = dB.rawQuery(select2, null);
                if (c != null && c.moveToFirst()) {

                    HomeApplianceName = c.getString(c.getColumnIndex(dbHelper.HA_LABEL));
                }

                list.add(new ManualControlItem(HomeApplianceName,h_id,status));
            }


        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }



}
