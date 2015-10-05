/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.com.app.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import sample.com.horoscope.DatabaseContract;
import sample.com.horoscope.HoroscopeDbHelper;
import sample.com.horoscope.HoroscopeProvider;


/*
    Note: This is not a complete set of tests of the Sunshine ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.

    Students: Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null
        );
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records  not deleted from Weather table during delete", 0, cursor.getCount());


        cursor.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                HoroscopeProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + DatabaseContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DatabaseContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
            Students: Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */
    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(DatabaseContract.horoscope_table.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the WeatherEntry CONTENT_URI should return WeatherEntry.CONTENT_TYPE",
                DatabaseContract.horoscope_table.CONTENT_TYPE, type);


    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testHoroscopeQuery() {
        // insert our test records into the database
        HoroscopeDbHelper dbHelper = new HoroscopeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createHoroscopeValues();
        long locationRowId = TestUtilities.insertHoroscopeValues(mContext);


        db.close();

        // Test the basic content provider query
 /*       Cursor weatherCursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                DatabaseContract.horoscope_table.ZODIACSIGN_COL + "= ?",
                new String[] {"Aries"},

                null
        );
*/
        Cursor weatherCursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,
                null,
                null,

                null
        );

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






        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherQuery", weatherCursor, testValues);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if your location queries are
        performing correctly.
     */

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    public void testUpdateLocation() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createHoroscopeValues();

        Uri locationUri = mContext.getContentResolver().
                insert(DatabaseContract.horoscope_table.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(DatabaseContract.horoscope_table._ID, locationRowId);
        updatedValues.put(DatabaseContract.horoscope_table.TITLE_DAY, "Test");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor locationCursor = mContext.getContentResolver().query(DatabaseContract.horoscope_table.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        locationCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                DatabaseContract.horoscope_table.CONTENT_URI, updatedValues, DatabaseContract.horoscope_table._ID + "= ?",
                new String[] { Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        locationCursor.unregisterContentObserver(tco);
        locationCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null,   // projection
                DatabaseContract.horoscope_table._ID + " = " + locationRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createHoroscopeValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DatabaseContract.horoscope_table.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(DatabaseContract.horoscope_table.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);

        // Fantastic.  Now that we have a location, add some weather!



    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location s.
        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DatabaseContract.horoscope_table.CONTENT_URI, true, locationObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        locationObserver.waitForNotificationOrFail();


        mContext.getContentResolver().unregisterContentObserver(locationObserver);

    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 11;
    static ContentValues[] createBulkInsertWeatherValues(long locationRowId) {
        long currentTestDate = TestUtilities.TEST_DATE;
        long millisecondsInADay = 1000*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate+= millisecondsInADay ) {
            ContentValues HoroscopeValues = new ContentValues();

            HoroscopeValues.put(DatabaseContract.horoscope_table.ZODIACSIGN_COL, "aries"+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DATE_COL, currentTestDate);
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_DAY, 1.1+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_WEEK, 1.1+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_MONTH, 1.2+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.TITLE_YEAR, 1.3+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_DAY, 75+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_WEEK, 65+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_MONTH, "Asteroids"+i);
            HoroscopeValues.put(DatabaseContract.horoscope_table.DESCRIPTION_YEAR, 5.5+i);



            returnContentValues[i] = HoroscopeValues;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createHoroscopeValues();
        Uri locationUri = mContext.getContentResolver().insert(DatabaseContract.horoscope_table.CONTENT_URI, testValues);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
                cursor, testValues);

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues(locationRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DatabaseContract.horoscope_table.CONTENT_URI, true, weatherObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(DatabaseContract.horoscope_table.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                DatabaseContract.horoscope_table.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                //WeatherEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
                null
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
