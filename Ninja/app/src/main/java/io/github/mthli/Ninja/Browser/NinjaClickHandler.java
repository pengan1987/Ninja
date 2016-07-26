package io.github.mthli.Ninja.Browser;

import android.os.Handler;
import android.os.Message;

import io.github.mthli.Ninja.View.NinjaWebView;
import io.github.mthli.Ninja.View.NinjaXWalkView;

public class NinjaClickHandler extends Handler {
    private AlbumController webView;

    public NinjaClickHandler(AlbumController webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (webView instanceof NinjaWebView)
            ((NinjaWebView) webView).getBrowserController().onLongPress(message.getData().getString("url"));
        if (webView instanceof NinjaXWalkView)
            ((NinjaXWalkView) webView).getBrowserController().onLongPress(message.getData().getString("url"));
    }
}
