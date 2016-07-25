package io.github.mthli.Ninja.View;

import android.webkit.WebView;

import io.github.mthli.Ninja.Browser.AlbumController;
import io.github.mthli.Ninja.Browser.BrowserController;


public interface NinjaWebView extends AlbumController {
    void loadUrl(String url);

    void initPreferences();

    void setBrowserController(BrowserController browserController);

    void findNext(boolean forward);

    String getTitle();

    String getUrl();

    boolean isLoadFinish();

    void reload();

    void stopLoading();

    void findAllAsync(String find);

    void destroy();

    int getProgress();

    String getOriginalUrl();

    WebView.HitTestResult getHitTestResult();

    int getMeasuredHeight();

    int getScrollY();

    boolean canGoBack();

    void goBack();

    void onLongPress();

    int getContentHeight();

    BrowserController getBrowserController();


}
