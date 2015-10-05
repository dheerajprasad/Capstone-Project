package sample.com.horoscope;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dheerajprasad on 28/9/2015.
 */
public class HoroscopeDbHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "horoscope.db";
    private static final int DATABASE_VERSION = 2;
    public HoroscopeDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.HOROSCOPE_TABLE + " ("
                + DatabaseContract.horoscope_table._ID + " INTEGER PRIMARY KEY,"
                + DatabaseContract.horoscope_table.ZODIACSIGN_COL + " TEXT UNIQUE NOT NULL,"
                + DatabaseContract.horoscope_table.TITLE_DAY + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.TITLE_WEEK + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.TITLE_MONTH + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.TITLE_YEAR + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.DATE_COL + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.DESCRIPTION_DAY + " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.DESCRIPTION_WEEK+ " TEXT NOT NULL,"
                + DatabaseContract.horoscope_table.DESCRIPTION_MONTH+ " TEXT NOT NULL,"
                +  DatabaseContract.horoscope_table.DESCRIPTION_YEAR + " TEXT NOT NULL"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.HOROSCOPE_TABLE);
        onCreate(db);
    }
}
