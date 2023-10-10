package com.example.json_firebase_fetch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;
    Button btn;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.edittext);
        tv = findViewById(R.id.json_view);
        btn = findViewById(R.id.json_btn);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remote_method();
            }
        });
    }

    public void remote_method() {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    String json_view = mFirebaseRemoteConfig.getString("json_view");
                    try {
                        JSONArray array = new JSONArray(json_view);
                        int index = Integer.parseInt(et.getText().toString().trim());
                        if (index >= 0 && index < array.length()) {
                            JSONObject obj = array.getJSONObject(index);
                            String quoteText = obj.getString("quoteText");
                            String quoteAuthor = obj.getString("quoteAuthor");
                            tv.setText(String.format("quoteText: %s\nquoteAuthor: %s", quoteText, quoteAuthor));
                        } else {
                            tv.setText("Invalid index.");
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "onComplete: " + e.getMessage());
                    }
                }
            }
        });
    }
}
