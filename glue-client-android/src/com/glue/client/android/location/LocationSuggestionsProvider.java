package com.glue.client.android.location;

import android.content.SearchRecentSuggestionsProvider;

public class LocationSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.glue.client.android.location.LocationSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public LocationSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}