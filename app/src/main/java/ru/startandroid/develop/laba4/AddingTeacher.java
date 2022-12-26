package ru.startandroid.develop.laba4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//актівіті для додавання нових викладачів до бази
public class AddingTeacher extends AppCompatActivity {

    //для введення імені, посади, та предметів
    EditText nameOfUser,jobPosition,subjects;
    //для збереження користувача, та очистки полів введення
    Button saveUser, cleanEditBoxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_data);
        nameOfUser =  findViewById(R.id.nameOfUser);
        jobPosition = findViewById(R.id.jobPosition);
        subjects = findViewById(R.id.subjects);
        saveUser = findViewById(R.id.saveUser);
        cleanEditBoxes = findViewById(R.id.cleanEditBoxes);

        saveUser.setEnabled(false);
        EditText[] edList = {nameOfUser, jobPosition, subjects};
        //допоміжний клас для деактивації кнопки збереження користувача, якщо бодай одне поле для введення не заповнене
        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, saveUser);
        for (EditText editText : edList){
            editText.addTextChangedListener(textWatcher);
        }
        //додаємо користувачів до бази даних натисненням на кнопку
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeachersDatabase myDB = new TeachersDatabase(AddingTeacher.this);
                myDB.addTeacher(nameOfUser.getText().toString().trim(),
                        jobPosition.getText().toString().trim(),
                        subjects.getText().toString().trim());
            }
        });
        //очищаємо поля для введення
        cleanEditBoxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOfUser.setText(null);
                jobPosition.setText(null);
                subjects.setText(null);
            }
        });
    }
}

