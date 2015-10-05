package sample.com.horoscope;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import sample.com.horoscope.service.updateHoroscopeService;
import sample.com.horoscope.service.updateHoroscopeWeekmonthService;

public class SplashScreen extends AppCompatActivity  {
    private  ProgressDialog progressBar;


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 8000;
    private static int NODATA_TIME_OUT = 2000;
    boolean dataavailableindb=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Updating Your Future ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


       if( isNetworkAvailable()) {
           Intent service_startday = new Intent(this, updateHoroscopeService.class);

           service_startday.putExtra("type", "day");
           startService(service_startday);
           Intent service_startmonth = new Intent(this, updateHoroscopeService.class);
           service_startmonth.putExtra("type", "month");
           startService(service_startmonth);


           Intent service_startweek = new Intent(this, updateHoroscopeWeekmonthService.class);
           service_startweek.putExtra("type", "week");
           startService(service_startweek);


           Intent service_startyear = new Intent(this, updateHoroscopeWeekmonthService.class);
           service_startyear.putExtra("type", "year");
           startService(service_startyear);


       }
        else {

           checkdataincursor();

       }


        if(dataavailableindb==true) {
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity


                    progressBar.dismiss();
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);


        }
    }




    public void checkdataincursor() {


        Cursor weatherCursor = getApplicationContext().getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        int datacontent = weatherCursor.getCount();
        Log.d("datacontent---", " " + datacontent);
        if (datacontent <= 0) {

            dataavailableindb=false;
            Log.d("datacontent<=0", " datacontent<=0");

            Context context = this.getApplicationContext();
            CharSequence text = getString(R.string.no_data_available_for_display);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            finish();

        }
        else{
            dataavailableindb=true;
        }
    }
    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }


        if (!(haveConnectedWifi || haveConnectedMobile)) {
            try {
                Context context = this.getApplicationContext();
                CharSequence text = getString(R.string.internet_not_available);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } catch (Exception error) {

                Log.d("Exception  listView ---", error.toString());
            }


        }


        return haveConnectedWifi || haveConnectedMobile;
    }


}
