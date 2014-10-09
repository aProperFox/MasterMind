package com.carridegames.mastermind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainMenu extends Activity {

    CheckBox checkBox;

    public static String PREFERENCES = "PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final SharedPreferences preferences = getSharedPreferences("PREFERENCES", 0);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox.setChecked(preferences.getBoolean("allowRepeats", false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    preferences.edit().putBoolean("allowRepeats", true).apply();
                } else {
                    preferences.edit().putBoolean("allowRepeats", false).apply();
                }
            }
        });
    }

    public void playGame(View view) {
        Intent i = new Intent(MainMenu.this, GameScreen.class);
        startActivity(i);
    }

}
