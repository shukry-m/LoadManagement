package lk.iot.lmsApp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "load_management.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //#################### HOME APPLIANCE TABLE ######################
    // TABLE
    public static final String TABLE_HOME_APPLIANCE = "home_appliance_tbl";
    // TABLE ATTRIBUTES
    public static final String H_ID = "h_Id";
    public static final String USER_ID = "user_id";
    public static final String H_LABEL = "h_label_name";
    public static final String T_5_TO_8  =  "t_5_to_8";
    public static final String T_8_TO_17 =  "t_8_to_17";
    public static final String T_17_TO_22 = "t_17_to_22";
    public static final String T_22_TO_5 = "t_22_to_5";
    public static final String M_STATUS = "m_status";

    //TABLE CREATING STRING
    private static final String CREATE_HOME_APPLIANCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_HOME_APPLIANCE + " ("
            + H_ID + " TEXT PRIMARY KEY, "
            + USER_ID + " TEXT, "
            + H_LABEL + " TEXT, "
            + T_5_TO_8 + " TEXT, "
            + T_8_TO_17 + " TEXT, "
            + T_17_TO_22 + " TEXT, "
            + T_22_TO_5 + " TEXT, "
            + M_STATUS + " TEXT ); ";




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOME_APPLIANCE_TABLE);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
