package ru.startandroid.develop.laba4;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//допоміжний клас, який реалізує інтерфейс TextWatcher для автоматичного блокування клавіші застосування змін
//, якщо хоча б одне поле для введення із 3-ох не буде заповнене
class CustomTextWatcher implements TextWatcher {
    View v;
    EditText[] edList;

    public CustomTextWatcher(EditText[] edList, Button v) {
        this.v = v;
        this.edList = edList;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        for (EditText editText : edList) {
            if (editText.getText().toString().trim().length() <= 0) {
                v.setEnabled(false);
                break;
            }
            else v.setEnabled(true);
        }
    }
}
