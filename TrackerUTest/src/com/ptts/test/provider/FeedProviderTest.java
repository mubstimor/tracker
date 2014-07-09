package com.ptts.test.provider;

import com.ptts.provider.FeedContract;
import com.ptts.provider.FeedProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

public class FeedProviderTest extends ProviderTestCase2<FeedProvider>  {
    public FeedProviderTest() {
        super(FeedProvider.class, FeedContract.CONTENT_AUTHORITY);
    }

    public void testEntryContentUriIsSane() {
        assertEquals(Uri.parse("content://com.ptts/entries"),
                FeedContract.Entry.CONTENT_URI);
    }

    public void testCreateAndRetrieve() {
        // Create
        ContentValues newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_NAME, "MyTitle");
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://example.com");
        newValues.put(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, "MyEntryID");
        Uri newUri = getMockContentResolver().insert(
                FeedContract.Entry.CONTENT_URI,
                newValues);

        // Retrieve
        String[] projection = {
                FeedContract.Entry.COLUMN_NAME_NAME,      // 0
                FeedContract.Entry.COLUMN_NAME_END,       // 1
                FeedContract.Entry.COLUMN_NAME_ENTRY_ID};  // 2
        Cursor c = getMockContentResolver().query(newUri, projection, null, null, null);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        assertEquals("MyTitle", c.getString(0));
        assertEquals("http://example.com", c.getString(1));
        assertEquals("MyEntryID", c.getString(2));
    }

    public void testCreateAndQuery() {
        // Create
        ContentValues newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_NAME, "Alpha-MyTitle");
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://alpha.example.com");
        newValues.put(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, "Alpha-MyEntryID");
        getMockContentResolver().insert(
                FeedContract.Entry.CONTENT_URI,
                newValues);

        newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_NAME, "Beta-MyTitle");
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://beta.example.com");
        newValues.put(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, "Beta-MyEntryID");
        getMockContentResolver().insert(
                FeedContract.Entry.CONTENT_URI,
                newValues);

        // Retrieve
        String[] projection = {
                FeedContract.Entry.COLUMN_NAME_NAME,      // 0
                FeedContract.Entry.COLUMN_NAME_END,       // 1
                FeedContract.Entry.COLUMN_NAME_ENTRY_ID};  // 2
        String where = FeedContract.Entry.COLUMN_NAME_NAME + " LIKE ?";
        Cursor c = getMockContentResolver().query(FeedContract.Entry.CONTENT_URI, projection,
                where, new String[] {"Alpha%"}, null);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        assertEquals("Alpha-MyTitle", c.getString(0));
        assertEquals("http://alpha.example.com", c.getString(1));
        assertEquals("Alpha-MyEntryID", c.getString(2));
    }

    public void testUpdate() {
        // Create
        ContentValues newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_NAME, "Alpha-MyTitle");
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://alpha.example.com");
        newValues.put(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, "Alpha-MyEntryID");
        Uri alpha = getMockContentResolver().insert(
                FeedContract.Entry.CONTENT_URI,
                newValues);

        newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_NAME, "Beta-MyTitle");
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://beta.example.com");
        newValues.put(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, "Beta-MyEntryID");
        Uri beta = getMockContentResolver().insert(
                FeedContract.Entry.CONTENT_URI,
                newValues);

        // Update
        newValues = new ContentValues();
        newValues.put(FeedContract.Entry.COLUMN_NAME_END, "http://replaced.example.com");
        getMockContentResolver().update(alpha, newValues, null, null);

        // Retrieve
        String[] projection = {
                FeedContract.Entry.COLUMN_NAME_NAME,      // 0
                FeedContract.Entry.COLUMN_NAME_END,       // 1
                FeedContract.Entry.COLUMN_NAME_ENTRY_ID};  // 2
        // Check that alpha was updated
        Cursor c = getMockContentResolver().query(alpha, projection, null, null, null);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        assertEquals("Alpha-MyTitle", c.getString(0));
        assertEquals("http://replaced.example.com", c.getString(1));
        assertEquals("Alpha-MyEntryID", c.getString(2));

        // ...and that beta was not
        c = getMockContentResolver().query(beta, projection, null, null, null);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        assertEquals("Beta-MyTitle", c.getString(0));
        assertEquals("http://beta.example.com", c.getString(1));
        assertEquals("Beta-MyEntryID", c.getString(2));
    }

}
