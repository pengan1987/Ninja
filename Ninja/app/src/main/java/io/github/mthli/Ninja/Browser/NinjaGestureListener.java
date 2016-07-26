package io.github.mthli.Ninja.Browser;

import android.view.GestureDetector;
import android.view.MotionEvent;

import io.github.mthli.Ninja.View.NinjaWebView;
import io.github.mthli.Ninja.View.NinjaXWalkView;

public class NinjaGestureListener extends GestureDetector.SimpleOnGestureListener {
    private AlbumController webView;
    private boolean longPress = true;

    public NinjaGestureListener(AlbumController webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (longPress) {
            if (webView instanceof NinjaWebView)
                ((NinjaWebView) webView).onLongPress();
            if (webView instanceof NinjaXWalkView)
                ((NinjaXWalkView) webView).onLongPress();
        }
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        longPress = false;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        longPress = true;
    }
}
