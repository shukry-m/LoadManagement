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

public class HomeApplianceDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;

    public HomeApplianceDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public int insert(String label_name) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(dbHelper.HA_LABEL, label_name);

            count = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE, null, values);

            if(count>0){
                String query = "SELECT "+dbHelper.HA_ID+" FROM "+dbHelper.TABLE_HOME_APPLIANCE+" ORDER BY "+dbHelper.HA_ID+" DESC LIMIT 1";
                Cursor c = dB.rawQuery(query,null);
                if (c != null && c.moveToFirst()) {
                    int lastId = c.getInt(0);

                    ContentValues cv = new ContentValues();
                    cv.put(dbHelper.T_HA_ID,lastId);
                    cv.put(dbHelper.T_22_TO_5,0);
                    cv.put(dbHelper.T_17_TO_22,0);
                    cv.put(dbHelper.T_8_TO_17,0);
                    cv.put(dbHelper.T_5_TO_8,0);

                    int count2 = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE_TIME, null, cv);
                    if(count2>0){
                        System.out.println(dbHelper.TABLE_HOME_APPLIANCE_TIME+" inserted");
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    public int update(HomeAppliance homeAppliance) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(dbHelper.HA_LABEL, homeAppliance.getName());

            count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE, values, dbHelper.HA_ID+" =?",new String[] {homeAppliance.getId()+""});

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    public ArrayList<HomeAppliance> getAll(){

        ArrayList<HomeAppliance> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE+" ORDER BY "+dbHelper.HA_ID+" DESC";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                int id = cur.getInt(cur.getColumnIndex(dbHelper.HA_ID));
                String lbl = cur.getString(cur.getColumnIndex(dbHelper.HA_LABEL));
                list.add(new HomeAppliance(id,lbl));
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }

    public int deleteHomeAppliance(int ha_id) {

        int count =0;
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_HOME_APPLIANCE
                    +" WHERE "+dbHelper.HA_ID+" = '"+ha_id+"'";

            System.out.println("* delete query "+deleteQuery);

            Cursor cur = dB.rawQuery(deleteQuery, null);

            count = cur.getCount();
            if(count>0){
                String deleteQuery2 = "DELETE FROM "+dbHelper.TABLE_HOME_APPLIANCE_TIME
                        +" WHERE "+dbHelper.T_HA_ID+" = '"+ha_id+"'";

                System.out.println("* delete query "+deleteQuery2);

                Cursor cur2 = dB.rawQuery(deleteQuery2, null);

                if(cur2.getCount()>0){
                    System.out.println("Deleted 2"+ha_id);
                }

                //TABLE_MANUAL_CONTROL
                String deleteQuery3 = "DELETE FROM "+dbHelper.TABLE_MANUAL_CONTROL
                        +" WHERE "+dbHelper.MA_HA_ID+" = '"+ha_id+"'";

                System.out.println("* delete query "+deleteQuery3);

                Cursor cur3 = dB.rawQuery(deleteQuery3, null);

                if(cur3.getCount()>0){
                    System.out.println("Deleted 3"+ha_id);
                }

            }

        } catch (Exception e) {

            Log.v(" Exception", e.toString());

        } finally {
            if (cursor !=null) {
                cursor.close();
            }
            dB.close();
        }
        return count;
    }

}
