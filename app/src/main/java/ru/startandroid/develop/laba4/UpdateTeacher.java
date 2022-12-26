package ru.startandroid.develop.laba4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//актівіті для оновлення даних в базі
public class UpdateTeacher extends AppCompatActivity {

    //для введення імені, посади, та предметів
    EditText name_input,position_input,subjects_input;
    //для збереження користувача, видалення користувача, та очистки полів введення
    Button updateUser,deleteUser, cleanEditBoxes;

    //стрічки, які будуть вставлені в поля для введення при запуску актівіті
     String id,name,position,subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name_input = findViewById(R.id.nameOfUser1);
        position_input = findViewById(R.id.jobPosition1);
        subjects_input = findViewById(R.id.subjects1);
        updateUser =findViewById(R.id.updateUser);
        deleteUser = findViewById(R.id.deleteUser);
        cleanEditBoxes = findViewById(R.id.cleanEditBoxes1);

        updateUser.setEnabled(false);
        EditText[] edList = {name_input, position_input, subjects_input};
        //допоміжний клас для деактивації кнопки збереження користувача, якщо бодай одне поле для введення не заповнене
        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, updateUser);
        for (EditText editText : edList){
            editText.addTextChangedListener(textWatcher);
        }
        //викликаємо метод для обміну даних між адаптером та актівіті
        getAndSetIntentData();


        //оновлюємо дані про користувача натисненням на кнопку
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeachersDatabase myDB = new TeachersDatabase(UpdateTeacher.this);
                name = name_input.getText().toString().trim();
                position = position_input.getText().toString().trim();
                subjects = subjects_input.getText().toString().trim();
                myDB.updateData(id,name,position,subjects);
            }
        });
        //видалення даних користувача
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        cleanEditBoxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_input.setText(null);
                position_input.setText(null);
                subjects_input.setText(null);
            }
        });
    }
    //метод для передачі даних з адаптера до актівіті і встановлення даних в полях для введення
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("position")&& getIntent().hasExtra("subjects")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            position = getIntent().getStringExtra("position");
            subjects = getIntent().getStringExtra("subjects");
            name_input.setText(name);
            position_input.setText(position);
            subjects_input.setText(subjects);
        }
        else{
            Toast.makeText(this,"Немає даних!", Toast.LENGTH_SHORT).show();
        }
    }
    //метод для створення діалогового вікна з підтвердженням видалення даних
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Видалення користувача \""+name+"\"");
        builder.setMessage("Ви справді хочете видалити користувача \""+name+"\" ?");
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TeachersDatabase myDB = new TeachersDatabase(UpdateTeacher.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}