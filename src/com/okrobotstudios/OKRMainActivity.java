package com.okrobotstudios;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class OKRMainActivity extends Activity {
    private final String START_LOC_KEY = "startLoc";
    private final String END_LOC_KEY = "endLoc";
    private final String CUR_LOC_KEY = "currentLoc";
    private final String SHARED_PREFS_KEY = "OKRProgressCalc";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupRow(START_LOC_KEY, R.id.startLocText);
        setupRow(END_LOC_KEY, R.id.endLocText);
        setupRow(CUR_LOC_KEY, R.id.currentLocText);
    }

    private void setupRow(String key, int viewId) {
        EditText txt = (EditText)findViewById(viewId);
        txt.addTextChangedListener(getTextWatcher(key));
        txt.setText(String.valueOf(getPref(key)));
    }

    private TextWatcher getTextWatcher(final String key) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_KEY, MODE_WORLD_READABLE).edit();
                try {
                    editor.putInt(key, Integer.parseInt(editable.toString()));
                    editor.commit();
                    update();
                } catch (Exception e) {
                    // meh.
                }
            }
        };
    }

    private int getPref(String key) {
        return getSharedPreferences(SHARED_PREFS_KEY, MODE_WORLD_READABLE).getInt(key, 0);
    }

    public void update() {
        int startLoc = getPref(START_LOC_KEY);
        int endLoc = getPref(END_LOC_KEY);
        int curLoc = getPref(CUR_LOC_KEY);

        int bookLocs = endLoc - startLoc;
        int progressLocs = curLoc - startLoc;
        int progress = 0;
        if (bookLocs > 0) {
            progress = 100 * progressLocs / bookLocs;
        }

        TextView progressText = (TextView)findViewById(R.id.progressText);
        progressText.setText(String.valueOf(progress) + "%");
    }
}
