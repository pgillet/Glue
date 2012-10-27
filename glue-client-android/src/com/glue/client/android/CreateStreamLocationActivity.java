package com.glue.client.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateStreamLocationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stream_location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_stream_location, menu);
        return true;
    }
}
