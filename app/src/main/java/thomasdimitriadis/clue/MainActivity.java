package thomasdimitriadis.clue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nameText;
    private TextView historyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "onCreate");

        nameText = (EditText)findViewById(R.id.editText);
        historyText = (TextView) findViewById(R.id.historyText);

        // for launching our WebView
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = nameText.getText().toString().trim();
                if (user != null && !user.equals("")) {
                    Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
                    webIntent.putExtra("NAME", user);

                    startActivity(webIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Put in a name!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String results = extras.getString("history");
            historyText.setText(results);
        }
    }

    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart");
    }

    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity", "onRestart");
    }

    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy");
    }
}
