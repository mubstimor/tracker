/*
 * Copyright 2013 The Android Open Source Project
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

package com.ptts.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Field and table name constants for
 * {@link com.ptts.provider.FeedProvider}.
 */
public class FeedContract {
    private FeedContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.ptts";

    /**
     * Base URI. (content://com.ptts)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "entries";

    /**
     * Columns supported by "entries" records.
     */
    public static class Entry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.tracker.entries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.tracker.entry";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "entry";
        /**
         * ROUTE ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_ENTRY_ID = "id";        
        public static final String COLUMN_NAME_NAME = "name";        
        public static final String COLUMN_NAME_START = "start";        
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_STOPS = "stops";
    }
}