package sample.com.horoscope;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import sample.com.horoscope.service.updateHoroscopeService;
import sample.com.horoscope.service.updateHoroscopeWeekmonthService;

/**
 * Created by dheerajprasad on 14/8/2015.
 */
public class HoroscopeWidgetProvider extends AppWidgetProvider {

    RemoteViews remoteViews;
    AppWidgetManager manager;
    ComponentName thisWidget;
    Context context;


    public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context=context;

         manager = AppWidgetManager.getInstance(context);
         thisWidget = new ComponentName(context, HoroscopeWidgetProvider.class);
        Log.d("FootballWidgetProvider", "FootballWidgetProvider calling");

        final int N = appWidgetIds.length;
		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
         remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_horoscope_small);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);



        if( isNetworkAvailable()) {
            Intent service_startday = new Intent(context, updateHoroscopeService.class);

            service_startday.putExtra("type", "day");
            context.startService(service_startday);
            Intent service_startmonth = new Intent(context, updateHoroscopeService.class);
            service_startmonth.putExtra("type", "month");
            context.startService(service_startmonth);


            Intent service_startweek = new Intent(context, updateHoroscopeWeekmonthService.class);
            service_startweek.putExtra("type", "week");
            context.startService(service_startweek);


            Intent service_startyear = new Intent(context, updateHoroscopeWeekmonthService.class);
            service_startyear.putExtra("type", "year");
            context.startService(service_startyear);
        }


        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.horo_widget_list,
                svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.horo_widget_list, R.id.No_data_available_widget);

        String dayname= "Horoscope for Today";

        Log.d("---dayname-- main", dayname);
        remoteViews.setTextViewText(R.id.title_displayed_widget, dayname);


        manager.updateAppWidget(thisWidget, remoteViews);

        Intent intent = new Intent(context, SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.launchapp, pendingIntent);
/*// Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_horoscope_small);
        views.setOnClickPendingIntent(R.id.launchapp, pendingIntent);*/
        return remoteViews;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.zodia_img_widget, description);
    }


    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
             //   Context context = context;
                CharSequence text = context.getString(R.string.internet_not_available);
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
