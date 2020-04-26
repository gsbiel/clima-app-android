package com.example.climaapp.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextWatcher implements TextWatcher {

    private EditText textField;
    private int maxLines;
    private String lastValue;

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
