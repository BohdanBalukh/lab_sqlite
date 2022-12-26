package ru.startandroid.develop.laba4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//актівіті для демонстрації відвідуваності та списку доступних викладачів
public class GroupStatisticsActivity extends AppCompatActivity {

    TextView text;
    TextView absentsText,countOfAbsents, monitorAbsents, listNames;
    ImageView imageView;
    TeachersDatabase myDB;
    ArrayList<String> user_name;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        absentsText = findViewById(R.id.absentsText);
        text=findViewById(R.id.text);
        countOfAbsents = findViewById(R.id.countOfAbsents);
        monitorAbsents = findViewById(R.id.monitorAbsents);
        listNames = findViewById(R.id.listNames);
        imageView = findViewById(R.id.smileImage);
        String string = "";

        String str = (String) getIntent().getSerializableExtra("monitorName");
        ArrayList<String> namesOfAbsentStudents = (ArrayList<String>) getIntent().getSerializableExtra("absentStudents");

        if(namesOfAbsentStudents.size()==0) {
            text.setText(R.string.noAbsents);
            imageView.setImageResource(R.drawable.allisok);
        }
        if(namesOfAbsentStudents.size()>0 && namesOfAbsentStudents.size()<=4){
            text.setText(R.string.absents);
            monitorAbsents.setText(R.string.monitorIsNotAbsent);
            imageView.setImageResource(R.drawable.allisok);
        }
        if(namesOfAbsentStudents.size()>4 && namesOfAbsentStudents.size()<10)
        {
            text.setText(R.string.absents);
            imageView.setImageResource(R.drawable.simplyok);
        }
        if(namesOfAbsentStudents.size()>=10 || namesOfAbsentStudents.size()==MainActivity.countOfStudents)
        {
            text.setText(R.string.absents);
            imageView.setImageResource(R.drawable.allisbad);
        }
        for (int i = 0; i < namesOfAbsentStudents.size(); i++) {
           string += (i+1+"."+namesOfAbsentStudents.get(i)+"\n\n");

        }
        absentsText.setText(string);
        countOfAbsents.setText("Кількість відсутніх: "+String.valueOf(namesOfAbsentStudents.size()));
        if(namesOfAbsentStudents.contains(str)){
            monitorAbsents.setText(R.string.monitorIsAbsent);
        }
        else{
            monitorAbsents.setText(R.string.monitorIsNotAbsent);
        }
        myDB = new TeachersDatabase(GroupStatisticsActivity.this);
        user_name = new ArrayList<>();
        storeNameColumnInArray();


    }
    void storeNameColumnInArray(){
        String names = "";
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            listNames.setText("Немає даних");
        }else{
            while(cursor.moveToNext()){
                user_name.add(cursor.getString(1));
            }
            for (int i = 0; i < user_name.size(); i++) {
                names += user_name.get(i)+", ";
            }
            listNames.setText(names.replaceAll(", $",""));
        }
    }

}