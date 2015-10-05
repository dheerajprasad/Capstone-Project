package sample.com.horoscope;

/**
 * Created by dheerajprasad on 15/8/2015.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */



public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;
    private static HoroscopeDbHelper mOpenHelper;
    Cursor weatherCursor ;
    /*private static final String SCORES_BY_DATE =
            DatabaseContract.scores_table.DATE_COL + " LIKE ?";*/


    Cursor cursor;
    @Override
    public void onDataSetChanged() {

    }

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        final long token = Binder.clearCallingIdentity();
        try {
         weatherCursor =context.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        } finally {
            Binder.restoreCallingIdentity(token);
        }
        populateListItem();
    }

public void populateListItem() {





    if (weatherCursor.moveToFirst()){
        do{
            ListItem listItem = new ListItem();
            listItem.zodiac_name = weatherCursor.getString(weatherCursor.getColumnIndex("sign"));
            Log.d("sign ", "sign--" + listItem.zodiac_name);
            listItem.zodiac_title= weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.TITLE_DAY));
            listItem.zodiac_description = weatherCursor.getString(weatherCursor.getColumnIndex(DatabaseContract.horoscope_table.DESCRIPTION_DAY));

            if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.aries))){
                listItem.zodiac_image=R.drawable.ariessmall;

            }else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.taurus))){

                listItem.zodiac_image=R.drawable.taurussmall;
            }
            else if (listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.gemini))){
                listItem.zodiac_image=R.drawable.gemini;

            }else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.cancer))){
                listItem.zodiac_image=R.drawable.cancersmall;

            }
            else if (listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.lion))){
                listItem.zodiac_image=R.drawable.lionsmall;

            }else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.virgo))){

                listItem.zodiac_image=R.drawable.virgosamll;
            }
            else if (listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.libra))){
                listItem.zodiac_image=R.drawable.librasmall;

            }else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.scorpio))){
                listItem.zodiac_image=R.drawable.scorpiosmall;


            }
            else if (listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.sagittarius))){
                listItem.zodiac_image=R.drawable.sagittariussmall;

            }
            else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.capricorn))){
                listItem.zodiac_image=R.drawable.capricornsmall;


            }
            else if (listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.aquarius))){
                listItem.zodiac_image=R.drawable.aquariussmall;

            }
            else if(listItem.zodiac_name.equalsIgnoreCase(context.getString(R.string.pisces))){
                listItem.zodiac_image=R.drawable.piscessmall;


            }
            listItemList.add(listItem);

        }while(weatherCursor.moveToNext());
    }
    weatherCursor.close();



    }


    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onCreate() {

    }

    /*
        *Similar to getView of Adapter where instead of View
        *we return RemoteViews
        *
        */


    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.horoscope_widget_list_item);
        ListItem listItem = listItemList.get(position);
         //  remoteView.setTextViewText(R.id.zodiac_name_widget,listItem.zodiac_name);
            remoteView.setTextViewText(R.id.zodiac_title_widget, listItem.zodiac_title);
            remoteView.setTextViewText(R.id.zodiac_desc_widget, listItem.zodiac_description);
           remoteView.setImageViewResource(R.id.zodia_img_widget, listItem.zodiac_image);

        return remoteView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}