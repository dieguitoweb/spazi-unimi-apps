package it.unimi.unimiplaces.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.unimi.unimiplaces.R;

/**
 * FlorMapView
 */
public class FloorMapView extends RelativeLayout {

    private TextView loadingTextView;
    private WebView webView;
    private final String HTMLfileURL = "file:///android_asset/html/floormap.html";

    public FloorMapView(Context context){
        super(context);
        this.init();
    }

    public FloorMapView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_floor_map,this);

        this.loadingTextView    = (TextView) findViewById(R.id.webview_loading);
        this.webView            = (WebView) findViewById(R.id.webview);

        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().getAllowFileAccessFromFileURLs();
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.webView.getSettings().getAllowContentAccess();
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);

        WebView.setWebContentsDebuggingEnabled(true);

        this.webView.loadUrl(HTMLfileURL);
    }

    private void pageLoadingFinished(){
        this.loadingTextView.setVisibility(GONE);
        this.webView.setVisibility(VISIBLE);
    }

    public void loadFloorMap(String svgURL){
        final String script = String.format("svgFloor.init(\"%s\")",svgURL);
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.evaluateJavascript(script, null);
                pageLoadingFinished();
            }
        });
    }

    public void highlightRoomInMap(String svgURL,String roomId){
        /* extract color from res, remove alpha component and convert in hex rgb form */
        int colorInt = ContextCompat.getColor(getContext(), R.color.colorAccent);
        String color = "#"+Integer.toHexString(colorInt & 0x00ffffff);
        /* JS script */
        final String script = String.format("svgFloor.initAndSelectRoom(\"%s\",\"%s\",\"%s\")",svgURL,roomId,color);
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.evaluateJavascript(script, null);
                pageLoadingFinished();
            }
        });

    }

}
