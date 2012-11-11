package com.glue.client.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateStreamSummaryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stream_summary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_stream_summary, menu);
        return true;
    }
}
