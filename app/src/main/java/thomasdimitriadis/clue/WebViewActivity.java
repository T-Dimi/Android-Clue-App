package thomasdimitriadis.clue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONObject;

import thomasdimitriadis.clue.R;

public class WebViewActivity extends Activity {
    private WebView browser;
    public String gameHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Log.i("WebViewActivity", "onCreate");

        // Let's see if the Intent has anything extra for us:
        final String name = getIntent().getStringExtra("NAME");
        Log.i("WebActivity", "Name: " + name);

        browser = (WebView)findViewById(R.id.webView);
        browser.setWebViewClient(new WebViewClient());
        browser.setHorizontalScrollBarEnabled(true);
        browser.setVerticalScrollBarEnabled(true);
        browser.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        browser.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                browser.evaluateJavascript("setUser(\""+name+"\")", null);
                browser.evaluateJavascript("setRecord()", null);
            }
        });


        // Make a Java object available in the WebView:
        browser.addJavascriptInterface(new WebAppInterface(this.getApplicationContext()), "Android");

        browser.loadUrl("file:///android_asset/clue.html");
    }

    // Handles appropriate behavior for the "back" button
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Clue Application")
                .setMessage("Are you sure you want to close this application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}

class WebAppInterface extends WebViewActivity{
    private Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void switchActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("history", gameHistory);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void getHis(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            String name = jObj.getString("name");
            int round = jObj.getInt("rounds");
            String room = jObj.getString("selectedRoom");
            String suspect = jObj.getString("selectedSuspect");
            String weapon = jObj.getString("selectedWeapon");

            if (name.equals("Computer")) {
                gameHistory = "In the last game, the " + name + " won after " + round +
                        " round(s) by guessing " + suspect + " in the " + room + " with a " +
                        weapon + ".";
            } else {
                gameHistory = "In the last game, " + name + " won after " + round +
                        " round(s) by guessing " + suspect + " in the " + room + " with a " +
                        weapon + ".";
            }
        } catch (Throwable t) {
            Log.e("App", "Error While Parsing JSON");
        }
    }
}


