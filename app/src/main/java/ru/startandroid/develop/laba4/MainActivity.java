package ru.startandroid.develop.laba4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

//головне актівіті
public class MainActivity extends AppCompatActivity {

    //Створюємо список для збереження чекбоксів (що служать для позначення відсутніх студентів)
    private ArrayList<CheckBox> boxesOfAttending;
    //Створюємо список імен студентів
    private ArrayList<String> listOfStudentNames;

    //Кількість студентів та кількість чекбоксів
    public static final int countOfCheckBoxes=18;
    public static final int countOfStudents=18;

    //Об'єкт для ініціалізації бази даних
    TeachersDatabase myDB;
    //Списки для завантаження даних з бази
    ArrayList<String> _id,user_name,user_position,user_subjects;
    //Адаптер для відображення зчитаних даних на екрані
    AdapterForDatabase customAdapter;
    //Для відображення даних
    RecyclerView recyclerView;

    //Картинка для ілюстрації пустої бази даних
    ImageView emptyImage;
    //Текст для ілюстрації пустої бази даних
    TextView emptyText;

    //Для вибору одночасно усіх чекбоксів
    ToggleButton checkAllButton;
    //Для введення імені старости
    EditText editMonitor;
    //Кнопки для видалення усіх даних з бази, та збереження старости
    Button deleteAllUsers, saveMonitor;
    //Кнопка переходу до GroupStatisticsActivity
    ImageButton nextStepButton;

    //Для розміщення елементів всередині двох CardView
    LinearLayout layoutToExpand, layoutIntoCardView, layoutToExpand1,layoutIntoCardView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editMonitor = findViewById(R.id.editMonitorId);
        saveMonitor = findViewById(R.id.saveMonitor);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editMonitor.setText(prefs.getString("autoSave", null));

        //зберігаємо ім'я старости по ключу autoSave в пам'ять телефона
        saveMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putString("autoSave", editMonitor.getText().toString()).commit();
                recreate();
                Toast.makeText(MainActivity.this,"Старосту збережено!", Toast.LENGTH_SHORT).show();
            }
        });
        saveMonitor.setEnabled(false);
        editMonitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String str =  editMonitor.getText().toString();
                if(listOfStudentNames.contains(str)) saveMonitor.setEnabled(true);
                if(!listOfStudentNames.contains(str)) saveMonitor.setEnabled(false);
            }
        });

        //ініціалізуємо усі необхідні елементи
        nextStepButton = findViewById(R.id.nextStep);
        deleteAllUsers = findViewById(R.id.deleteAllUsers);
        emptyImage = findViewById(R.id.empty);
        emptyText=findViewById(R.id.emptyText);

        layoutToExpand = findViewById(R.id.layoutToExpand);
        layoutIntoCardView = findViewById(R.id.layoutIntoCardview);
        layoutToExpand1 = findViewById(R.id.layoutToExpand1);
        layoutIntoCardView1 = findViewById(R.id.layoutIntoCardview1);

        boxesOfAttending = new ArrayList<>();

        String[] nameString = new String[countOfStudents];
        nameString = getResources().getStringArray(R.array.nameOfStudents);

        listOfStudentNames = new ArrayList( Arrays.asList(nameString));

        //для отримання ідентифікаторів 18-ти доступних чекбоксів
        for (int i = 0; i < countOfCheckBoxes; i++) {
            int checkBoxesId = getResources().getIdentifier("checkbox"+i,"id",getPackageName());
            boxesOfAttending.add(i,findViewById(checkBoxesId));
            boxesOfAttending.get(i).setText(listOfStudentNames.get(i));
        }
        //для позначення усіх відсутніх/присутніх студентів одночасно
        checkAllButton = findViewById(R.id.checkAllButton);
        boolean isChecked = checkAllButton.isChecked();
        updateVisibility(isChecked);
        checkAllButton.setOnClickListener(view->updateVisibility(checkAllButton.isChecked()));

        //якщо введене ім'я старости присутнє в доступному списку імен студентів, то маркуємо цього студента
        String str = editMonitor.getText().toString();
        for (int i =0; i<countOfStudents;i++) {
            int index = listOfStudentNames.indexOf(str);
            if(listOfStudentNames.contains(str)){
                boxesOfAttending.get(index).setText(Html.fromHtml(listOfStudentNames.get(index)+getString(R.string.monitor)));
                nextStepButton.setEnabled(true);
                nextStepButton.setVisibility(View.VISIBLE);
            }
            if(!listOfStudentNames.contains(str)){
                boxesOfAttending.get(i).setText(listOfStudentNames.get(i));
                nextStepButton.setEnabled(false);
                nextStepButton.setVisibility(View.GONE);
            }

        }

        //ініціалізація бази даних
        myDB = new TeachersDatabase(MainActivity.this);
        //ініціалізація списків, в які будуть завантажені дані з бази даних
        _id = new ArrayList<>();
        user_name = new ArrayList<>();
        user_position = new ArrayList<>();
        user_subjects = new ArrayList<>();
        //виклик методу, для завантаження даних у списки
        storeDataInArrays();

        //ініціалізація адаптера
        customAdapter = new AdapterForDatabase(MainActivity.this,this,_id,user_name,user_position,user_subjects);
        //встановлюємо адаптер
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    //для створення меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addTeacher:
                Intent intentTeacher = new Intent(this, AddingTeacher.class);
                startActivity(intentTeacher);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //для оновлення вмісту екрану
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            recreate();
        }
    }
     //метод для завантаження даних з бази в списки
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            deleteAllUsers.setEnabled(false);
        }else{
            while(cursor.moveToNext()){
                _id.add(cursor.getString(0));
                user_name.add(cursor.getString(1));
                user_position.add(cursor.getString(2));
                user_subjects.add(cursor.getString(3));
            }
            deleteAllUsers.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        }
    }

    //метод для переходу до GroupStatisticsActivity і перенесення туди усіх необхідних даних
    public void startNewActivity(View view){
        String str = editMonitor.getText().toString();
        ArrayList<String> namesOfAbsentStudents = new ArrayList<>();
        Intent intent = new Intent(this, GroupStatisticsActivity.class);
        for (int i = 0; i < countOfCheckBoxes; i++) {
            if(boxesOfAttending.get(i).isChecked()){
                namesOfAbsentStudents.add(listOfStudentNames.get(i));
            }
        }
        intent.putExtra("monitorName",(Serializable) str);
        intent.putExtra("absentStudents",(Serializable) namesOfAbsentStudents);
        startActivity(intent);
    }
    //метод для розгортання CardView з іменами студентів
    public void expand(View view){
        CardView card = (CardView) view;
        ImageView arrow = (ImageView) card.getChildAt(1);
        int v =(layoutToExpand.getVisibility() == View.GONE)? View.VISIBLE:View.GONE;
        TransitionManager.beginDelayedTransition(layoutIntoCardView, new AutoTransition());
        arrow.animate().rotation((v==View.GONE)?0:180).start();
        layoutToExpand.setVisibility(v);
    }
    //метод для розгортання CardView з даними викладачів
    public void expand1(View view){
        CardView card = (CardView) view;
        ImageView arrow = (ImageView) card.getChildAt(1);
        int v =(layoutToExpand1.getVisibility() == View.GONE)? View.VISIBLE:View.GONE;
        TransitionManager.beginDelayedTransition(layoutIntoCardView1, new AutoTransition());
        arrow.animate().rotation((v==View.GONE)?0:180).start();
        layoutToExpand1.setVisibility(v);
    }

    //метод для кнопки видалення усіх користувачів з бази
    public void deleteAllUsers(View view){
        int v =(layoutToExpand1.getVisibility() == View.VISIBLE)? View.VISIBLE:View.GONE;
        TransitionManager.beginDelayedTransition(layoutIntoCardView1, new AutoTransition());
        confirmDialog();
        layoutToExpand1.setVisibility(v);
    }

    //метод для створення діалогового вікна з підтвердженням видалення даних
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Видалення УСІХ даних");
        builder.setMessage("Ви справді хочете видалити дані УСІХ користувачів?");
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TeachersDatabase myDB = new TeachersDatabase(MainActivity.this);
                myDB.deleteAllData();
                recreate();
            }
        });
        builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    //допоміжний метод для кнопки вибору усіх відсутніх/усіх присутніх студентів
    private void updateVisibility(boolean isChecked) {
        if (isChecked) {
            for (CheckBox checkBox : boxesOfAttending) {
                checkBox.setChecked(false);
            }

        } else {
            for (CheckBox checkBox : boxesOfAttending) {
                checkBox.setChecked(true);
            }
        }
    }
}