package sample.com.horoscope;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class HoroscopeDetail extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    String TITLE_DAY = "";
    String TITLE_WEEK="";
    String TITLE_MONTH = "";
    String TITLE_YEAR="";
    String DESCRIPTION_DAY = "";
    String DESCRIPTION_WEEK="";
    String DESCRIPTION_MONTH = "";
    String DESCRIPTION_YEAR="";
    TextView tiltetext;
   String selectionType;
    String displayTitle;
    String displayDetail;
    Intent sharingIntent;


    private static final int URL_LOADER = 0;
    String sign="";
    TextView descriptiontext;
    Cursor weatherCursor;

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
        setContentView(R.layout.activity_horoscope_detail);




        if(getIntent().getStringExtra("sign")!=null){
            sign=getIntent().getStringExtra("sign");

        }
        if(getIntent().getStringExtra("selectiontype")!=null){
            selectionType=getIntent().getStringExtra("selectiontype");

        }

         tiltetext = (TextView)findViewById(R.id.title_datail);
         descriptiontext =(TextView)findViewById(R.id.description_detail);



        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String shareBody = "Here is the share content body";
                 sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tiltetext.getText());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tiltetext.getText()+"\n"+descriptiontext.getText() );
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
            }
        });

        getLoaderManager().initLoader(URL_LOADER, null, this);
     /*   Cursor weatherCursor = this.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",
                new String[] {sign},

                null
        );

*/


    }

    public void loaddata(){

        if (weatherCursor.moveToFirst()){
            do{
                String data = weatherCursor.getString(weatherCursor.getColumnIndex("sign"));
                Log.d("sign ", "sign--" + data);
                TITLE_DAY = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_DAY));
                DESCRIPTION_DAY = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_DAY));
                Log.d("TITLE_DAY ", "TITLE_DAY--- "+TITLE_DAY);
                Log.d("DESCRIPTION_DAY ", "DESCRIPTION_DAY --- "+DESCRIPTION_DAY);
                TITLE_WEEK = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_WEEK));
                DESCRIPTION_WEEK = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_WEEK));
                Log.d("TITLE_WEEK ", "TITLE_WEEK--- "+TITLE_WEEK);
                Log.d("DESCRIPTION_WEEK ", "DESCRIPTION_WEEK--- "+DESCRIPTION_WEEK);
                TITLE_MONTH = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_MONTH));
                DESCRIPTION_MONTH = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_MONTH));
                Log.d("TITLE_MONTH ", "TITLE_MONTH--- "+TITLE_MONTH);
                Log.d("DESCRIPTION_MONTH ", "DESCRIPTION_MONTH--- "+DESCRIPTION_MONTH);
                TITLE_YEAR = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_YEAR));
                DESCRIPTION_YEAR = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_YEAR));
                Log.d("TITLE_YEAR ", "TITLE_YEAR--- "+TITLE_YEAR);
                Log.d("DESCRIPTION_YEAR ", "DESCRIPTION_YEAR--- "+DESCRIPTION_YEAR);


            }while(weatherCursor.moveToNext());
        }
        weatherCursor.close();
        if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheday))){
            tiltetext.setText(TITLE_DAY);
            descriptiontext.setText(DESCRIPTION_DAY);

        }
        else if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheweek))){
            tiltetext.setText(TITLE_WEEK);
            descriptiontext.setText(DESCRIPTION_WEEK);

        }
        else if (selectionType.equalsIgnoreCase(getString(R.string.Horoscopeforthemonth))){
            tiltetext.setText(TITLE_MONTH);
            descriptiontext.setText(DESCRIPTION_MONTH);

        }
        else if(selectionType.equalsIgnoreCase(getString(R.string.Horoscopefortheyear))){

            tiltetext.setText(TITLE_YEAR);
            descriptiontext.setText(DESCRIPTION_YEAR);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,   // Parent activity context
                DatabaseContract.horoscope_table.CONTENT_URI,        // Table to query
                null,     // Projection to return
                DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",            // No selection clause
                new String[] {sign},            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       weatherCursor= cursor;
        loaddata();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
