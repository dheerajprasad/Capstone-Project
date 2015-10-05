package sample.com.horoscope;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private List<ZodiacSign> ZodiacSign;
    public static final int SCORES_LOADER = 0;
    Cursor weatherCursor;
    private static final int URL_LOADER = 0;


    public static String selectionType="";
    public String dateField="";
    String TITLE_DAY = "";
    String TITLE_WEEK="";
    String TITLE_MONTH = "";
    String TITLE_YEAR="";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_list_view);


        if(getIntent().getStringExtra("selectiontype")!=null){
            selectionType=getIntent().getStringExtra("selectiontype");

        }

       // initializeData();

       // initRecyclerView();
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }
    private void initializeData(){
        ZodiacSign = new ArrayList<>();

         weatherCursor = this.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",
                new String[] {"Aries"},

                null
        );



    }
    public void loaddata(Cursor cursor){

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("sign"));
                Log.d("sign ", "sign--" + data);
                TITLE_DAY = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_DAY));
                String data2 = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_DAY));
                Log.d("TITLE_DAY ", "TITLE_DAY--- "+TITLE_DAY);
                Log.d("DESCRIPTION_DAY ", "DESCRIPTION_DAY--- "+data2);
                TITLE_WEEK = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_WEEK));
                String data4 = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_WEEK));
                Log.d("TITLE_WEEK ", "TITLE_WEEK--- "+TITLE_WEEK);
                Log.d("DESCRIPTION_WEEK ", "DESCRIPTION_WEEK--- "+data4);
                TITLE_MONTH = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_MONTH));
                String data6 = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_MONTH));
                Log.d("TITLE_MONTH ", "TITLE_MONTH--- "+TITLE_MONTH);
                Log.d("DESCRIPTION_MONTH ", "DESCRIPTION_MONTH--- "+data6);
                TITLE_YEAR = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_YEAR));
                String data8 = cursor.getString(cursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_YEAR));
                Log.d("TITLE_YEAR ", "TITLE_YEAR--- "+TITLE_YEAR);
                Log.d("DESCRIPTION_YEAR ", "DESCRIPTION_YEAR--- "+data8);


            }while(cursor.moveToNext());
        }
        cursor.close();

        if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheday))){
            dateField =TITLE_DAY;

        }
        else if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheweek))){
            dateField = TITLE_WEEK;

        }
        else if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopeforthemonth))){
            dateField = TITLE_MONTH;

        }
        else if(selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheyear))){

            dateField = TITLE_YEAR;
        }
        int chk=  dateField.indexOf("Horoscope");

        dateField=  dateField.substring(chk);


        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.aries), getResources().getString(R.string.ariestimeline), dateField,R.drawable.aries));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.taurus), getResources().getString(R.string.taurustimeline), dateField,R.drawable.taurus));

        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.gemini), getResources().getString(R.string.geminitimeline), dateField,R.drawable.gemini));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.cancer), getResources().getString(R.string.cancertimeline), dateField,R.drawable.cancer));

        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.lion), getResources().getString(R.string.leotimeline), dateField,R.drawable.lion));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.virgo), getResources().getString(R.string.virgotimeline), dateField,R.drawable.virgo));

        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.libra), getResources().getString(R.string.libratimeline), dateField,R.drawable.libra));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.scorpio), getResources().getString(R.string.scorpiotimeline), dateField,R.drawable.scorpio));

        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.sagittarius), getResources().getString(R.string.sagittariustimeline), dateField,R.drawable.sagittarius));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.capricorn), getResources().getString(R.string.capricorntimeline), dateField,R.drawable.capricorn));

        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.aquarius), getResources().getString(R.string.aquariustimeline), dateField,R.drawable.aquarius));
        ZodiacSign.add(new ZodiacSign(getResources().getString(R.string.pisces), getResources().getString(R.string.piscestimeline), dateField,R.drawable.pisces));

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Zodiac_sign);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RVAdapter adapter = new RVAdapter(ZodiacSign,this);
        recyclerView.setAdapter(adapter);




    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ZodiacSign = new ArrayList<>();
        return new CursorLoader(
                this,   // Parent activity context
                DatabaseContract.horoscope_table.CONTENT_URI,        // Table to query
                null,     // Projection to return
                DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",            // No selection clause
                new String[] {"Aries"},            // No selection arguments
                null             // Default sort order
        );
    }
    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        loaddata(cursor);
        initRecyclerView();

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

}
