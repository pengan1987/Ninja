package io.github.mthli.Ninja.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import io.github.mthli.Ninja.Service.XWalkDetector;

/**
 * Created by andyzhou on 16-07-25.
 */
public class EntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CODE HERE TO CREATE INTENT
        Intent toActivity;
        if (XWalkDetector.detect(EntryActivity.this))
            toActivity = new Intent(EntryActivity.this, XWalkBrowserActivity.class);
        else
            toActivity = new Intent(EntryActivity.this, BrowserActivity.class);
        startActivity(toActivity);
        finish();
    }
}
