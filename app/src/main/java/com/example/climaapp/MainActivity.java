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
import android.widget.TextView;

import com.example.climaapp.interfaces.EditTextWatcherDelegate;
import com.example.climaapp.interfaces.TimeOutDelegate;
import com.example.climaapp.watchers.EditTextWatcher;

public class MainActivity extends AppCompatActivity implements
                                                                View.OnFocusChangeListener,
                                                                View.OnClickListener,
                                                                TimeOutDelegate,
                                                                EditTextWatcherDelegate
{

    ConstraintLayout mainScreen;

    ImageView refreshBtn;
    ImageView searchBtn;
    EditText textField;
    TextView eraseTextFieldBtn;
    EditTextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hideActionBar();
        setContentView(R.layout.activity_main);
        this.catchReferencesFromLayout();
        this.registerForUIEvents();
        this.hideEraseButton(true);

        this.textWatcher.delegate = this;
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
        this.eraseTextFieldBtn = findViewById(R.id.eraseTextBtn);
        this.textWatcher = new EditTextWatcher(this.textField, 1);
    }

    private void registerForUIEvents(){
        this.mainScreen.setOnClickListener(this);
        this.textField.setOnFocusChangeListener(this);
        this.refreshBtn.setOnClickListener(this);
        this.searchBtn.setOnClickListener(this);
        this.eraseTextFieldBtn.setOnClickListener(this);
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
        }else if(v.getId() == R.id.eraseTextBtn){
            this.setAlphaForView(v);
            this.textField.setText("");
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

    private void hideEraseButton(Boolean flag){
        if(flag){
            this.eraseTextFieldBtn.setVisibility(View.INVISIBLE);
        }else{
            this.eraseTextFieldBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void didFinishTimer(Integer viewId) {
        View imageButton = findViewById(viewId);
        imageButton.setAlpha((float) 1.0);
    }

    @Override
    public void eraseButtonShouldAppear(Boolean flag) {
        System.out.println("Esconder ou nao esconder? eis a questão");
        this.hideEraseButton(!flag);
    }
}
