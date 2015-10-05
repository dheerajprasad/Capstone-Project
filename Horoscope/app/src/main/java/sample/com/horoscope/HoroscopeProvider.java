package sample.com.horoscope;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by dheerajprasad on 28/9/2015.
 */
public class HoroscopeProvider extends ContentProvider
{
    public static HoroscopeDbHelper mOpenHelper;
    public static final int DATE = 100;
    public static final int ZODIAC_SIGN = 101;

    private UriMatcher muriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder ScoreQuery =
            new SQLiteQueryBuilder();

/*    private static final String SCORES_BY_LEAGUE = DatabaseContract.scores_table.LEAGUE_COL + " = ?";
    private static final String SCORES_BY_DATE =
            DatabaseContract.scores_table.DATE_COL + " LIKE ?";
    private static final String SCORES_BY_ID =
            DatabaseContract.scores_table.MATCH_ID + " = ?";*/


  public   static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.BASE_CONTENT_URI.toString();

      matcher.addURI(authority, DatabaseContract.PATH_HOROSCOPE, DATE);
      matcher.addURI(authority, DatabaseContract.PATH_HOROSCOPE, ZODIAC_SIGN);

        return matcher;
    }

    private int match_uri(Uri uri)
    {
        String link = uri.toString();
        {
            if(link.contentEquals(DatabaseContract.horoscope_table.CONTENT_URI.toString()))
            {
                return DATE;
            }

        }
        return -1;
    }
    @Override
    public boolean onCreate()
    {
        mOpenHelper = new HoroscopeDbHelper(getContext());
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = match_uri(uri);
        int rowsUpdated;

        switch (match) {
            case DATE:
                   rowsUpdated = db.update(DatabaseContract.horoscope_table.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ZODIAC_SIGN:
                rowsUpdated = db.update(DatabaseContract.horoscope_table.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri)
    {
      //  final int match = muriMatcher.match(uri);
        final int match = match_uri(uri);

        switch (match) {
            case DATE:
                return DatabaseContract.horoscope_table.CONTENT_TYPE;
            case ZODIAC_SIGN:
                return DatabaseContract.horoscope_table.CONTENT_ITEM_TYPE;
             default:
                throw new UnsupportedOperationException("Unknown uri :" + uri );
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor retCursor;
        //Log.v(FetchScoreTask.LOG_TAG,uri.getPathSegments().toString());
        int match = match_uri(uri);
        //Log.v(FetchScoreTask.LOG_TAG,SCORES_BY_LEAGUE);
        //Log.v(FetchScoreTask.LOG_TAG,selectionArgs[0]);
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(match));
      //  content://sample.com.horoscope/horoscope_table
        switch (match)
        {
            case DATE: retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.horoscope_table.TABLE_NAME,
                    projection,selection,selectionArgs,null,null,sortOrder); break;
            case ZODIAC_SIGN: retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.horoscope_table.TABLE_NAME,
                    projection,null,null,null,null,sortOrder); break;
                    default: throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = match_uri(uri);
        Uri returnUri;

        switch (match) {
            case DATE: {
                 long _id = db.insert(DatabaseContract.horoscope_table.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.horoscope_table.buildHoroscopeWithDate(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ZODIAC_SIGN: {
                long _id = db.insert(DatabaseContract.horoscope_table.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.horoscope_table.buildHoroscopeWithDate(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //db.delete(DatabaseContract.SCORES_TABLE,null,null);
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(muriMatcher.match(uri)));
        switch (match_uri(uri))
        {
            case DATE:
                db.beginTransaction();
                int returncount = 0;
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insertWithOnConflict(DatabaseContract.horoscope_table.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1)
                        {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returncount;
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = match_uri(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case DATE:
                rowsDeleted = db.delete(
                        DatabaseContract.horoscope_table.TABLE_NAME, selection, selectionArgs);
                break;
            case ZODIAC_SIGN:
                rowsDeleted = db.delete(
                        DatabaseContract.horoscope_table.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
}
