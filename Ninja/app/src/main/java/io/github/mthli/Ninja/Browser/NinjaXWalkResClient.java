package io.github.mthli.Ninja.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.MailTo;
import android.net.http.SslError;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.ByteArrayInputStream;

import io.github.mthli.Ninja.R;
import io.github.mthli.Ninja.Unit.BrowserUnit;
import io.github.mthli.Ninja.Unit.IntentUnit;
import io.github.mthli.Ninja.View.NinjaXWalkView;

/**
 * Created by andyzhou on 16-07-24.
 */
public class NinjaXWalkResClient extends XWalkResourceClient {

    private NinjaXWalkView webView;
    int progress;
    private Context context;
    private AdBlock adBlock;

    private boolean white;

    public void updateWhite(boolean white) {
        this.white = white;
    }

    private boolean enable;

    public void enableAdBlock(boolean enable) {
        this.enable = enable;
    }

    public NinjaXWalkResClient(XWalkView view) {
        super(view);
        this.webView = (NinjaXWalkView) view;
        this.adBlock = ((NinjaXWalkView) view).getAdBlock();
        this.white = false;
        this.enable = true;
    }

    @Override
    public void onProgressChanged(XWalkView view, int progressInPercent) {
        super.onProgressChanged(view, progressInPercent);
        this.progress = progressInPercent;
        webView.update(progressInPercent);
        context = webView.getContext();
        webView.getAdBlock();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        super.onLoadStarted(view, url);

        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            //  webView.update(context.getString(R.string.album_untitled), url);
        } else {
            //  webView.update(view.getTitle(), url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        if (url.startsWith(BrowserUnit.URL_SCHEME_MAIL_TO)) {
            Intent intent = IntentUnit.getEmailIntent(MailTo.parse(url));
            context.startActivity(intent);
            view.reload(XWalkView.RELOAD_NORMAL);
            return true;
        } else if (url.startsWith(BrowserUnit.URL_SCHEME_INTENT)) {
            Intent intent;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                context.startActivity(intent);
                return true;
            } catch (Exception e) {
            } // When intent fail will crash
        }

        white = adBlock.isWhite(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        if (enable && !white && adBlock.isAd(request.getUrl().toString())) {

            //TODO: process AD block
        }
        return super.shouldInterceptLoadRequest(view, request);
    }

    @Override
    public void onReceivedSslError(XWalkView view, final ValueCallback<Boolean> callback, SslError error) {

        Context holder = IntentUnit.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_warning);
        builder.setMessage(R.string.dialog_content_ssl_error);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onReceiveValue(true);
            }
        });
        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onReceiveValue(false);
            }
        });

        AlertDialog dialog = builder.create();
        if (error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
            dialog.show();
        } else {
            callback.onReceiveValue(true);
        }

        super.onReceivedSslError(view, callback, error);
    }

    @Override
    public void onReceivedHttpAuthRequest(XWalkView view, final XWalkHttpAuthHandler handler, String host, String realm) {
        Context holder = IntentUnit.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_sign_in);

        LinearLayout signInLayout = (LinearLayout) LayoutInflater.from(holder).inflate(R.layout.dialog_sign_in, null, false);
        final EditText userEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_username);
        final EditText passEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_password);
        passEdit.setTypeface(Typeface.DEFAULT);
        passEdit.setTransformationMethod(new PasswordTransformationMethod());
        builder.setView(signInLayout);

        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = userEdit.getText().toString().trim();
                String pass = passEdit.getText().toString().trim();
                handler.proceed(user, pass);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        builder.create().show();
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        super.onLoadFinished(view, url);
        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            webView.update(context.getString(R.string.album_untitled), url);
        } else {
            webView.update(view.getTitle(), url);
        }

        if (webView.isForeground()) {
            webView.invalidate();
        } else {
            webView.postInvalidate();
        }
    }
}
