package sample.com.horoscope.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import sample.com.horoscope.DatabaseContract;
import sample.com.horoscope.R;

/**
 * Created by dheerajprasad on 28/9/2015.
 */
public class updateHoroscopeService extends IntentService {

    String title_day= "";
    String title_week="";
    String title_month="";
    String title_year="";
    String desc_day="";
    String desc_week="";
    String desc_month="";
    String desc_year="";
    String sign="";
    String type="";
    String date="";
    String value="";


    Context ctx;

  static   Boolean isfirsttimeinserted=false;


    String[] signs = new String[12];

    final static String LOG_TAG ="updateHoroscopeService";

    String text;
    public updateHoroscopeService()
    {
        super("updateHoroscope");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
     //   getData("day","Aries");
        signs[0] = "Aries";
        signs[1] = "Taurus";
        signs[2] = "Gemini";
        signs[3] = "Cancer";
        signs[4] = "Leo";
        signs[5] = "Virgo";
        signs[6] = "Libra";
        signs[7] = "Scorpio";
        signs[8] = "Sagittarius";
        signs[9] = "Capricorn";
        signs[10] = "Aquarius";
        signs[11] = "Pisces";


        String value = intent.getStringExtra("type");

      //  Bundle extras = intent.getExtras();

/*
        if (extras != null) {

            String value = intent.getStringExtra("type");

            String value1 = extras.getString("type");
        }*/


// updating with default value
        title_day=  getString(R.string.pleaserefreshdata);
        title_week=getString(R.string.pleaserefreshdata);
        title_month=getString(R.string.pleaserefreshdata);
        title_year=getString(R.string.pleaserefreshdata);
        desc_day=getString(R.string.pleaserefreshdata);
        desc_week=getString(R.string.pleaserefreshdata);
        desc_month=getString(R.string.pleaserefreshdata);
        desc_year=getString(R.string.pleaserefreshdata);
        sign=getString(R.string.pleaserefreshdata);
        type=getString(R.string.pleaserefreshdata);
        date=getString(R.string.pleaserefreshdata);
// updating with default value
        Log.d("-- value--",value);
        Log.d("isfirsttimeinserted","isfirsttimeinserted--"+isfirsttimeinserted);
        if(!isfirsttimeinserted) {

            Log.d(" inside if inserted","isfirsttimeinserted--"+isfirsttimeinserted);
            insertfirsttimefullset();

        }

        if(value.equalsIgnoreCase("day")){
            for (String s : signs) {
                getData("day", s);
            }
        }
        else if(value.equalsIgnoreCase("week")) {
            for (String s : signs) {
                getData("week", s);
            }
        }
        else if (value.equalsIgnoreCase("month")){
            for (String s : signs) {
                getData("month", s);
            }
        }
        else if(value.equalsIgnoreCase("year")) {
            for (String s : signs) {
                getData("year", s);
            }
        }
        checkdataincursor();
    }



    private void getData(String type, String sign) {
        this.sign=sign;
        this.type=type;
        String BASE_URL ="";
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //Creating fetch URL
     //   final String BASE_URL = "http://www.findyourfate.com/rss/yearly-horoscope.asp?sign=Aries"; //Base URL
        if(type=="day") {
              BASE_URL = "http://www.findyourfate.com/rss/dailyhoroscope-feed.asp"; //Base URL
        }
        else if(type=="week"){
              BASE_URL = "http://www.findyourfate.com/rss/weekly-horoscope.asp";
        }
        else if(type=="month"){
              BASE_URL = "http://www.findyourfate.com/rss/monthly-horoscope.asp";
        }
        else if(type=="year"){
              BASE_URL = "http://www.findyourfate.com/rss/yearly-horoscope.asp"; //Base URL
        }
        final String QUERY_TIME_FRAME = "sign"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";
        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, sign).build();



     //   Uri fetch_build = Uri.parse(BASE_URL).buildUpon().build();
        Log.v(LOG_TAG, fetch_build.toString()); //log spam
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod("GET");
            //  m_connection.addRequestProperty("X-Auth-Token","e136b7858d424b9da07c88f28b61989a");
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            else{


                parse( inputStream);
            }
/*       //     reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JSON_data = buffer.toString();*/
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here" + e.getMessage());
        } finally {
            if (m_connection != null) {
                m_connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
        }
    }



    public void  parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("employee")) {
                            // create a new instance of employee
                           Log.d(LOG_TAG,"employee called");
                            }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        Log.d(LOG_TAG +XmlPullParser.TEXT+"-","text");
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("description")) {
                           // add employee object to list
                            Log.d(LOG_TAG,"description ---" +text);
                            if(type.equalsIgnoreCase("day")){
                                desc_day=text;
                            }
                            else if (type.equalsIgnoreCase("week")){
                                desc_week=text;

                            }
                            else if (type.equalsIgnoreCase("month")){
                                desc_month=text;

                            }
                            else if (type.equalsIgnoreCase("year")){
                                desc_year=text;

                            }

                            } else if (tagname.equalsIgnoreCase("title")) {
                            Log.d(LOG_TAG,"title ---"+text);
                            if(type.equalsIgnoreCase("day")){

                                title_day =text;
                            }
                            else if (type.equalsIgnoreCase("week")){
                                title_week=text;

                            }
                            else if (type.equalsIgnoreCase("month")){
                                title_month=text;

                            }
                            else if (type.equalsIgnoreCase("year")){
                                title_year=text;

                            }
                        }
                        break;

                    default:
                        break;
                    }
                eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            }

        updatedatabase();
        }

public void insertindatabase(){
    //ContentValues to be inserted

    ContentValues HoroscopeValues = new ContentValues();
    HoroscopeValues.put(DatabaseContract.horoscope_table.ZODIACSIGN_COL, sign);
    HoroscopeValues.put(DatabaseContract.horoscope_table.DATE_COL, date);
    HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_DAY, title_day);
    HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_WEEK, title_week);
    HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_MONTH, title_month);
    HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_YEAR, title_year);
    HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_DAY, desc_day);
    HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_WEEK, desc_week);
    HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_MONTH, desc_month);
    HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_YEAR, desc_year);

    Uri locationUri = getApplicationContext().getContentResolver().insert(DatabaseContract.horoscope_table.CONTENT_URI, HoroscopeValues);
    long locationRowId = ContentUris.parseId(locationUri);
    Log.d(" locationRowId",locationRowId+"--");
    if(locationRowId!=-1){

        Log.d("locationRowId ", "!=-1 ");

        Log.d("locationRowId  ","inserted success  ");


        Cursor weatherCursor = getApplicationContext().getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Log.d("locationRowId ", "inserted  success ");

   /* String value1=    weatherCursor.getString(0);
        String value6=    weatherCursor.getString(2);
        String value2=    weatherCursor.getString(3);
        String value3=    weatherCursor.getString(4);
        String value4=    weatherCursor.getString(5);
        String value5=    weatherCursor.getString(6);*/

        if (weatherCursor.moveToFirst()){
            do{
                String data = weatherCursor.getString(weatherCursor.getColumnIndex("sign"));
                Log.d("locationRowId ", "inserted    success ");
                // do what ever you want here
            }while(weatherCursor.moveToNext());
        }
        weatherCursor.close();


    }
}

    public void updatedatabase(){



        ContentValues updatedValues = new ContentValues();
        if(type=="day") {
            updatedValues.put(DatabaseContract.horoscope_table.TITLE_DAY, title_day);
            updatedValues.put(DatabaseContract.horoscope_table.DESCRIPTION_DAY, desc_day);


        }
        if(type=="week") {
            updatedValues.put(DatabaseContract.horoscope_table.TITLE_WEEK, title_week);
            updatedValues.put(DatabaseContract.horoscope_table.DESCRIPTION_WEEK, desc_week);


        }
        if(type=="month") {
            updatedValues.put(DatabaseContract.horoscope_table.TITLE_MONTH, title_month);
            updatedValues.put(DatabaseContract.horoscope_table.DESCRIPTION_MONTH, desc_month);


        }
        if(type=="year") {
            updatedValues.put(DatabaseContract.horoscope_table.TITLE_YEAR, title_year);
            updatedValues.put(DatabaseContract.horoscope_table.DESCRIPTION_YEAR, desc_year);


        }

        int count = getApplicationContext().getContentResolver().update(
                DatabaseContract.horoscope_table.CONTENT_URI, updatedValues, DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",
                new String[]{(sign)});





    }


    public void insertfirsttimefullset(){


        Log.d("insertfirsttimefullset ", "insert start  ");


        Vector<ContentValues> values = new Vector <ContentValues> (signs.length);
        for (String s: signs) {


            ContentValues HoroscopeValues = new ContentValues();
            HoroscopeValues.put(DatabaseContract.horoscope_table.ZODIACSIGN_COL, s);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DATE_COL, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_DAY, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_WEEK, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_MONTH, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_YEAR, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_DAY, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_WEEK, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_MONTH, "");
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_YEAR, "");





            values.add(HoroscopeValues);

        }

        ContentValues[] insert_data = new ContentValues[values.size()];
        values.toArray(insert_data);

        int insertCount = getApplicationContext().getContentResolver().bulkInsert(DatabaseContract.horoscope_table.CONTENT_URI, insert_data);

        if(insertCount!=0 ||insertCount!=-1 ) {
            isfirsttimeinserted = true;
            Log.d("insertfirsttimefullset ", "insert finsish ");

        }
    }


    public void checkdataincursor(){


        Cursor weatherCursor = getApplicationContext().getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,
                null
        );


   /* String value1=    weatherCursor.getString(0);
        String value6=    weatherCursor.getString(2);
        String value2=    weatherCursor.getString(3);
        String value3=    weatherCursor.getString(4);
        String value4=    weatherCursor.getString(5);
        String value5=    weatherCursor.getString(6);*/

        if (weatherCursor.moveToFirst()){
            do{
                String data = weatherCursor.getString(weatherCursor.getColumnIndex("sign"));
                Log.d("sign ", "sign--"+data);
                String data1 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_DAY));
                String data2 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_DAY));
                Log.d("TITLE_DAY ", "TITLE_DAY--- "+data1);
                Log.d("DESCRIPTION_DAY ", "DESCRIPTION_DAY--- "+data2);
                String data3 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_WEEK));
                String data4 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_WEEK));
                Log.d("TITLE_WEEK ", "TITLE_WEEK--- "+data3);
                Log.d("DESCRIPTION_WEEK ", "DESCRIPTION_WEEK--- "+data4);
                String data5 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_MONTH));
                String data6 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_MONTH));
                Log.d("TITLE_MONTH ", "TITLE_MONTH--- "+data5);
                Log.d("DESCRIPTION_MONTH ", "DESCRIPTION_MONTH--- "+data6);
                String data7 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_YEAR));
                String data8 = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_YEAR));
                Log.d("TITLE_YEAR ", "TITLE_YEAR--- "+data7);
                Log.d("DESCRIPTION_YEAR ", "DESCRIPTION_YEAR--- "+data8);


            }while(weatherCursor.moveToNext());
        }
        weatherCursor.close();
    }



}
