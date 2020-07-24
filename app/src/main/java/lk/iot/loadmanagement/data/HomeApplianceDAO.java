package lk.iot.loadmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import lk.iot.loadmanagement.db.DatabaseHelper;
import lk.iot.loadmanagement.model.HomeAppliance;

public class HomeApplianceDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public HomeApplianceDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    //##################################### INSERT ###################################
    public int insert(String h_id,
                      String userID,
                      String label_name,
                      String t_5_to_8,
                      String t_8_to_17,
                      String t_17_to_22,
                      String t_22_to_5,
                      String m_status) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE + " WHERE " + dbHelper.H_ID + " = " + h_id;

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert
                ContentValues values = new ContentValues();
                values.put(dbHelper.H_ID, h_id);
                values.put(dbHelper.H_LABEL, label_name);
                values.put(dbHelper.USER_ID, userID);
                values.put(dbHelper.T_5_TO_8, t_5_to_8);
                values.put(dbHelper.T_8_TO_17, t_8_to_17);
                values.put(dbHelper.T_17_TO_22, t_17_to_22);
                values.put(dbHelper.T_22_TO_5, t_22_to_5);
                values.put(dbHelper.M_STATUS, m_status);

                count = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.H_LABEL, label_name);
                values.put(dbHelper.USER_ID, userID);
                values.put(dbHelper.T_5_TO_8, t_5_to_8);
                values.put(dbHelper.T_8_TO_17, t_8_to_17);
                values.put(dbHelper.T_17_TO_22, t_17_to_22);
                values.put(dbHelper.T_22_TO_5, t_22_to_5);
                values.put(dbHelper.M_STATUS, m_status);

                count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE, values, dbHelper.H_ID + " =?", new String[]{h_id });

                if(count>0){
                    System.out.println("* updated "+count);
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    public int insert(String label_name,String h_id,String userID) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE + " WHERE " + dbHelper.H_ID + " = " + h_id;

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert
                ContentValues values = new ContentValues();
                values.put(dbHelper.H_ID, h_id);
                values.put(dbHelper.H_LABEL, label_name);
                values.put(dbHelper.USER_ID, userID);
                values.put(dbHelper.T_5_TO_8, "0");
                values.put(dbHelper.T_8_TO_17, "0");
                values.put(dbHelper.T_17_TO_22, "0");
                values.put(dbHelper.T_22_TO_5, "0");
                values.put(dbHelper.M_STATUS, "0");

                count = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();
                values.put(dbHelper.H_LABEL, label_name);
                values.put(dbHelper.USER_ID, userID);

                count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE, values, dbHelper.H_ID + " =?", new String[]{h_id });

                if(count>0){
                    System.out.println("* updated");
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    public int insert(String label_name,String userID) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String query = "SELECT "+dbHelper.H_ID+" FROM "+dbHelper.TABLE_HOME_APPLIANCE+" ORDER BY "+dbHelper.H_ID+" DESC LIMIT 1";
            Cursor c = dB.rawQuery(query,null);
            if (c != null && c.moveToFirst()) {
                String lastId = c.getString(0);
                int h_id = Integer.parseInt(lastId)+1;


                //insert
                ContentValues values = new ContentValues();
                values.put(dbHelper.H_ID, h_id);
                values.put(dbHelper.H_LABEL, label_name);
                values.put(dbHelper.USER_ID, userID);
                values.put(dbHelper.T_5_TO_8, "0");
                values.put(dbHelper.T_8_TO_17, "0");
                values.put(dbHelper.T_17_TO_22, "0");
                values.put(dbHelper.T_22_TO_5, "0");
                values.put(dbHelper.M_STATUS, "0");

                count = (int) dB.insert(dbHelper.TABLE_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }


//########################## UPDATE ############################

    public int updateTime(int place_id,String h_id,String status,String userID) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {
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
                count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE, values, dbHelper.H_ID + " =? AND "+dbHelper.USER_ID+" =?", new String[]{h_id , userID});

                if (count > 0) {
                    System.out.println(" * update Time " + count);
                }


        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    public int updateManualControl(String h_id,String status,String userID) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {


            //update
            ContentValues cv = new ContentValues();

            cv.put(dbHelper.M_STATUS, status);

            count = (int) dB.update(dbHelper.TABLE_HOME_APPLIANCE, cv, dbHelper.M_STATUS +" =? AND "+
                    dbHelper.USER_ID + " =?", new String[]{h_id ,userID});

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    //##################### GET ####################

    public ArrayList<HomeAppliance> getAll(String userID){

        ArrayList<HomeAppliance> list = new ArrayList<>();


        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE +" WHERE "+dbHelper.USER_ID+" = '"+userID+"' ORDER BY "
                    +dbHelper.H_ID+" DESC";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                HomeAppliance h = new HomeAppliance();
                h.setH_ID(cur.getString(cur.getColumnIndex(dbHelper.H_ID)));
                h.setH_LABEL(cur.getString(cur.getColumnIndex(dbHelper.H_LABEL)));
                h.setUSER_ID(cur.getString(cur.getColumnIndex(dbHelper.USER_ID)));
                h.setT_5_TO_8(cur.getString(cur.getColumnIndex(dbHelper.T_5_TO_8)));
                h.setT_8_TO_17(cur.getString(cur.getColumnIndex(dbHelper.T_8_TO_17)));
                h.setT_17_TO_22(cur.getString(cur.getColumnIndex(dbHelper.T_17_TO_22)));
                h.setT_22_TO_5(cur.getString(cur.getColumnIndex(dbHelper.T_22_TO_5)));
                h.setM__STATUS(cur.getString(cur.getColumnIndex(dbHelper.M_STATUS)));
                list.add(h);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }

    public HomeAppliance get(String h_id,String userID){


        HomeAppliance h = new HomeAppliance();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_HOME_APPLIANCE+
                    " WHERE "+dbHelper.USER_ID+" = '"+userID+"' AND "+dbHelper.H_ID+" = '"+h_id+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                h.setH_ID(cur.getString(cur.getColumnIndex(dbHelper.H_ID)));
                h.setH_LABEL(cur.getString(cur.getColumnIndex(dbHelper.H_LABEL)));
                h.setUSER_ID(cur.getString(cur.getColumnIndex(dbHelper.USER_ID)));
                h.setT_5_TO_8(cur.getString(cur.getColumnIndex(dbHelper.T_5_TO_8)));
                h.setT_8_TO_17(cur.getString(cur.getColumnIndex(dbHelper.T_8_TO_17)));
                h.setT_17_TO_22(cur.getString(cur.getColumnIndex(dbHelper.T_17_TO_22)));
                h.setT_22_TO_5(cur.getString(cur.getColumnIndex(dbHelper.T_22_TO_5)));
                h.setM__STATUS(cur.getString(cur.getColumnIndex(dbHelper.M_STATUS)));

            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }

        return h;

    }
    //################################## DELETE ########################

    public  int deleteAll(String userID){
        int count = 0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_HOME_APPLIANCE
                    +" WHERE "+dbHelper.USER_ID+" = '"+userID+"'";

            System.out.println("* delete query "+deleteQuery);

            Cursor cur = dB.rawQuery(deleteQuery, null);

            count = cur.getCount();
            if(count>0){
                System.out.println("Deleted ");
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



    public int deleteHomeAppliance(String h_id,String userID) {

        int count =0;
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_HOME_APPLIANCE
                    +" WHERE "+dbHelper.H_ID+" = '"+h_id+"' AND "+dbHelper.USER_ID+" = '"+userID+"'";

            System.out.println("* delete query "+deleteQuery);

            Cursor cur = dB.rawQuery(deleteQuery, null);

            count = cur.getCount();
            if(count>0){
                System.out.println("* deleted tbl "+count);

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
