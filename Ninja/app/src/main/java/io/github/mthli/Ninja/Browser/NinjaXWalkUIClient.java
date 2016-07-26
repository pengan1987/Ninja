package io.github.mthli.Ninja.Browser;

import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import io.github.mthli.Ninja.View.NinjaXWalkView;

/**
 * Created by andyzhou on 16-07-24.
 */
public class NinjaXWalkUIClient extends XWalkUIClient {
    private NinjaXWalkView ninjaXWalkView;

    public NinjaXWalkUIClient(XWalkView view) {
        super(view);
        ninjaXWalkView = (NinjaXWalkView) view;
    }

    @Override
    public void onReceivedTitle(XWalkView view, String title) {
        super.onReceivedTitle(view, title);
        ninjaXWalkView.update(title, view.getUrl());
    }

    @Override
    public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        ninjaXWalkView.getBrowserController().openFileChooser(uploadFile);
    }
}
