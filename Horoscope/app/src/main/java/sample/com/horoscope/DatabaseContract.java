package sample.com.horoscope;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dheerajprasad on 28/9/2015.
 */
public class DatabaseContract
{
    public static final String HOROSCOPE_TABLE = "horoscope_table";

    public static final String CONTENT_AUTHORITY = "sample.com.horoscope";
    public static final String PATH = "horoscope_table";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_HOROSCOPE = "horoscope_table";

    public static final class horoscope_table implements BaseColumns
    {
        public static final String TABLE_NAME = "horoscope_table";
        //Table data
        public static final String ZODIACSIGN_COL = "sign";
        public static final String DATE_COL = "date";
        public static final String TITLE_DAY = "title_day";
        public static final String TITLE_WEEK = "title_week";
        public static final String TITLE_MONTH = "title_month";
        public static final String TITLE_YEAR = "title_year";
        public static final String DESCRIPTION_DAY = "desc_day";
        public static final String DESCRIPTION_WEEK = "desc_week";
        public static final String DESCRIPTION_MONTH = "desc_month";
        public static final String DESCRIPTION_YEAR = "desc_year";

       public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH)
       .build();

        //Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static Uri buildHoroscopeWithDate(long id)
        {
            //return BASE_CONTENT_URI.buildUpon().appendPath("date").build();
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
    //URI data
  /*  public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static final String PATH = "scores";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);*/
}

