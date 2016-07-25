package io.github.mthli.Ninja.Browser;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

/**
 * Created by andyzhou on 16-07-24.
 */
public class NinjaXWalkResClient extends XWalkResourceClient {
    int progress = 0;

    public NinjaXWalkResClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onProgressChanged(XWalkView view, int progressInPercent) {
        this.progress = progressInPercent;
        super.onProgressChanged(view, progressInPercent);
    }

    public int getProgress() {
        return progress;
    }
}
