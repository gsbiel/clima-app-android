package com.example.climaapp.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.climaapp.interfaces.EditTextWatcherDelegate;

public class EditTextWatcher implements TextWatcher {

    private EditText textField;
    private int maxLines;
    private String lastValue;

    public EditTextWatcherDelegate delegate;

    public EditTextWatcher(EditText editText, int maxLines){
        this.textField = editText;
        this.maxLines = maxLines;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.lastValue = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(this.textField.length() > 0){
            this.delegate.eraseButtonShouldAppear(true);
        }else{
            this.delegate.eraseButtonShouldAppear(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (this.textField.getLineCount() > maxLines) {
            int selectionStart = this.textField.getSelectionStart() - 1;
            this.textField.setText(lastValue);
            if (selectionStart >= this.textField.length()) {
                selectionStart = this.textField.length();
            }
            this.textField.setSelection(selectionStart);
        }
    }
}
