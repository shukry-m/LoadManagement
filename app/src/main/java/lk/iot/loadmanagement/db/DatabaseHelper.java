package lk.iot.loadmanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lk.iot.loadmanagement.model.HomeAppliance;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "load_management.db";
    private static final int DATABASE_VERSION = 1;

    DatabaseReference ref;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //#################### HOME APPLIANCE TABLE ######################
    // TABLE
    public static final String TABLE_HOME_APPLIANCE = "home_appliance_tbl";
    // TABLE ATTRIBUTES
    public static final String HA_ID = "ha_Id";
    public static final String HA_LABEL = "ha_label_name";

    //TABLE CREATING STRING
    private static final String CREATE_HOME_APPLIANCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_HOME_APPLIANCE + " ("
            + HA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HA_LABEL + " TEXT ); ";

    //#################### HOME APPLIANCE   TABLE ######################
    // TABLE
    public static final String TABLE_HOME_APPLIANCE_TIME = "home_appliance_time_tbl";
    // TABLE ATTRIBUTES
    public static final String T_ID = "t_Id";
    public static final String T_HA_ID = "t_ha_id";
    public static final String T_5_TO_8 = "t_5_to_8";
    public static final String T_8_TO_17 = "t_8_to_17";
    public static final String T_17_TO_22 = "t_17_to_22";
    public static final String T_22_TO_5 = "t_22_to_5";

    //TABLE CREATING STRING
    private static final String CREATE_HOME_APPLIANCE_TIME_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_HOME_APPLIANCE_TIME + " ("
            + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T_HA_ID + " INTEGER, "
            + T_5_TO_8 + " INTEGER,"
            + T_8_TO_17 + " INTEGER, "
            + T_17_TO_22 + " INTEGER, "
            + T_22_TO_5 + " INTEGER ); ";

    //#################### MANUAL CONTROL TABLE ######################
    // TABLE
    public static final String TABLE_MANUAL_CONTROL = "manual_control_tbl";
    // TABLE ATTRIBUTES
    public static final String MA_ID = "ma_Id";
    public static final String MA_HA_ID = "ma_ha_id";
    public static final String MA_HA_STATUS = "ma_ha_status";

    //TABLE CREATING STRING
    private static final String CREATE_MANUAL_CONTROL_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_MANUAL_CONTROL + " ("
            + MA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MA_HA_ID + " INTEGER, "
            + MA_HA_STATUS + " INTEGER ); ";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOME_APPLIANCE_TABLE);
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(1,\"Kitchen Lights\")");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(2,\"Kitchen Fan\")");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(3,\"Dining Room Lights\")");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(4,\"Dining Room Fans\")");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(5,\"Living Room Lights\")");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE+" VALUES(6,\"Living Room Fans\")");

        db.execSQL(CREATE_HOME_APPLIANCE_TIME_TABLE);
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(1,1,0,0,0,0)");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(2,2,0,0,0,0)");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(3,3,0,0,0,0)");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(4,4,0,0,0,0)");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(5,5,0,0,0,0)");
        db.execSQL("INSERT INTO "+TABLE_HOME_APPLIANCE_TIME+" VALUES(6,6,0,0,0,0)");

        db.execSQL(CREATE_MANUAL_CONTROL_TABLE);
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(1,1,0)");
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(2,2,0)");
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(3,3,0)");
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(4,4,0)");
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(5,5,0)");
        db.execSQL("INSERT INTO "+TABLE_MANUAL_CONTROL+" VALUES(6,6,0)");

       // insertDataToFirebase();

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  this.onCreate(db);

    }
}
