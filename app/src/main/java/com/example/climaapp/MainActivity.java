package com.example.climaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.climaapp.interfaces.TimeOutDelegate;
import com.example.climaapp.watchers.EditTextWatcher;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, TimeOutDelegate {

    ConstraintLayout mainScreen;

    ImageView refreshBtn;
    ImageView searchBtn;
    EditText textField;
    EditTextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hideActionBar();
        setContentView(R.layout.activity_main);
        this.catchReferencesFromLayout();
        this.registerForUIEvents();
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void catchReferencesFromLayout(){
        this.mainScreen = findViewById(R.id.mainScreen);
        this.refreshBtn = findViewById(R.id.refreshBtn);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.textField = findViewById(R.id.searchTextField);
        this.textWatcher = new EditTextWatcher(this.textField, 1);
    }

    private void registerForUIEvents(){
        this.mainScreen.setOnClickListener(this);
        this.textField.setOnFocusChangeListener(this);
        this.refreshBtn.setOnClickListener(this);
        this.searchBtn.setOnClickListener(this);
        this.textField.addTextChangedListener(this.textWatcher);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.searchTextField){
            if(hasFocus){
                System.out.println("Search Text Field has focus!");
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,255)));
            }else{
                System.out.println("Search Text Field has not focus anymore!");
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            }
        }
    }

    @Override
    public void onClick(View v) {
        this.hideSoftKeyBoard();
        if(v.getId() == R.id.searchBtn){
            this.setAlphaForView(v);
        }else if(v.getId() == R.id.refreshBtn){
            this.setAlphaForView(v);
        }else if(v.getId() == R.id.mainScreen){
            this.hideSoftKeyBoard();
        }
    }

    private void setAlphaForView(View v){
        v.setAlpha((float) 0.2);
        TimeOut timer = new TimeOut(200,this);
        int viewId = v.getId();
        timer.execute(viewId);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void didFinishTimer(Integer viewId) {
        ImageView imageButton = findViewById(viewId);
        imageButton.setAlpha((float) 1.0);
    }
}
