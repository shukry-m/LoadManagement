package lk.iot.loadmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import lk.iot.loadmanagement.db.DatabaseHelper;
import lk.iot.loadmanagement.model.HomeAplianceTime;
import lk.iot.loadmanagement.model.HomeAppliance;

public class HomeApplianceTimeDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;

    public HomeApplianceTimeDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public int insert(int place_id,int h_id,int status) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE_TIME + " WHERE " + dbHelper.T_HA_ID + " = " + h_id;

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {

                //insert
                ContentValues cv = new ContentValues();
                switch (place_id) {
                    case 1: {

                        cv.put(dbHelper.T_22_TO_5, 0);
                        cv.put(dbHelper.T_17_TO_22, 0);
                        cv.put(dbHelper.T_8_TO_17, 0);
                        cv.put(dbHelper.T_5_TO_8, status);
                    }
                    break;
                    case 2: {

                        cv.put(dbHelper.T_22_TO_5, 0);
                        cv.put(dbHelper.T_17_TO_22, 0);
                        cv.put(dbHelper.T_8_TO_17, status);
                        cv.put(dbHelper.T_5_TO_8, 0);
                    }
                    break;

                    case 3: {

                        cv.put(dbHelper.T_22_TO_5, 0);
                        cv.put(dbHelper.T_17_TO_22, status);
                        cv.put(dbHelper.T_8_TO_17, 0);
                        cv.put(dbHelper.T_5_TO_8, 0);
                    }
                    break;
                    case 4: {
                        cv.put(dbHelper.T_22_TO_5, status);
                        cv.put(dbHelper.T_17_TO_22, 0);
                        cv.put(dbHelper.T_8_TO_17, 0);
                        cv.put(dbHelper.T_5_TO_8, 0);
                    }
                    break;
                }
                cv.put(dbHelper.T_HA_ID, h_id);
                count = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE_TIME, null, cv);

            } else {

            //update

            ContentValues values = new ContentValues();

            switch (place_id) {
                case 1: {
                    values.put(dbHelper.T_5_TO_8, status);
                }
                break;
                case 2: {
                    values.put(dbHelper.T_8_TO_17, status);
                }
                break;
                case 3: {
                    values.put(dbHelper.T_17_TO_22, status);
                }
                break;
                case 4: {
                    values.put(dbHelper.T_22_TO_5, status);
                }
                break;
            }
            count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE_TIME, values, dbHelper.T_HA_ID + " =?", new String[]{h_id +""});

            if (count > 0) {
                System.out.println(" * update Time " + count);
            }
        }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }


    public ArrayList<HomeAplianceTime> getAll(){

        ArrayList<HomeAplianceTime> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE_TIME+" WHERE "
                    +dbHelper.T_HA_ID +" IN (SELECT "+dbHelper.HA_ID+" FROM "+dbHelper.TABLE_HOME_APPLIANCE+" )";



            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                String h_name="";
                int h_id = cur.getInt(cur.getColumnIndex(dbHelper.T_HA_ID));
                int t_5_8 = cur.getInt(cur.getColumnIndex(dbHelper.T_5_TO_8));
                int t_8_17 = cur.getInt(cur.getColumnIndex(dbHelper.T_8_TO_17));
                int t_17_22 = cur.getInt(cur.getColumnIndex(dbHelper.T_17_TO_22));
                int t_22_5 = cur.getInt(cur.getColumnIndex(dbHelper.T_22_TO_5));

                String select2 = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE+" WHERE "+dbHelper.HA_ID+" = "+h_id;
                Cursor c = dB.rawQuery(select2, null);
                if (c != null && c.moveToFirst()) {
                    h_name = c.getString(c.getColumnIndex(dbHelper.HA_LABEL));
                }

                list.add(new HomeAplianceTime(h_name,h_id,t_5_8,t_8_17,t_17_22,t_22_5));
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }


    public HomeAplianceTime getHomeAppliance(int h_id){

        HomeAplianceTime homeAplianceTime = new HomeAplianceTime();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE_TIME+" WHERE "+dbHelper.T_HA_ID +" = "+h_id;

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            if (cur!= null && cur.moveToFirst()) {
                String h_name="";
                 homeAplianceTime.setT_5_TO_8(cur.getInt(cur.getColumnIndex(dbHelper.T_5_TO_8)));
                homeAplianceTime.setT_8_TO_17(cur.getInt(cur.getColumnIndex(dbHelper.T_8_TO_17)));
                homeAplianceTime.setT_17_TO_22(cur.getInt(cur.getColumnIndex(dbHelper.T_17_TO_22)));
                homeAplianceTime.setT_22_TO_5(cur.getInt(cur.getColumnIndex(dbHelper.T_22_TO_5)));

                String select2 = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE+" WHERE "+dbHelper.HA_ID+" = "+h_id;
                Cursor c = dB.rawQuery(select2, null);
                if (c != null && c.moveToFirst()) {
                    homeAplianceTime.setHomeApplianceName(c.getString(c.getColumnIndex(dbHelper.HA_LABEL)));
                }

            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return homeAplianceTime;

    }
}
