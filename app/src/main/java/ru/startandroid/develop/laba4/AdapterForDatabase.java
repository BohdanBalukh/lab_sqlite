package ru.startandroid.develop.laba4;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//допоміжний клас для виведення інформації з бази даних на екран
public class AdapterForDatabase extends RecyclerView.Adapter<AdapterForDatabase.MyViewHolder> {
    private Context context;
    Activity activity;
    //списки айді, імені, посади, предметів
    private ArrayList _id,user_name,user_position,user_subjects;

    int position;
    //конструктор в який передаємо списки айді, імен, посад, та предметів
    AdapterForDatabase(Activity activity, Context context, ArrayList _id, ArrayList user_name, ArrayList user_position,
                       ArrayList user_subjects){
        this.activity = activity;
        this.context = context;
        this._id = _id;
        this.user_name = user_name;
        this.user_position = user_position;
        this.user_subjects = user_subjects;
    }
    @NonNull
    //встановлюємо, яка саме розмітка буде застосовуватись нашим адаптером
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //встановлюємо дані на місця передбачені розміткою в my_row.xml
        this.position = position;
        holder._id_txt.setText(String.valueOf(_id.get(position)));
        holder.user_name_txt.setText(String.valueOf(user_name.get(position)));
        holder.user_position_txt.setText(String.valueOf(user_position.get(position)));
        holder.user_subjects_txt.setText(String.valueOf(user_subjects.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            //при натисканні на адаптер, відбувається перехід до актівіті оновлення даних в базі
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateTeacher.class);
                intent.putExtra("id",String.valueOf(_id.get(position)));
                intent.putExtra("name",String.valueOf(user_name.get(position)));
                intent.putExtra("position",String.valueOf(user_position.get(position)));
                intent.putExtra("subjects",String.valueOf(user_subjects.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }


    //для отримання кількості елементів по ідентифікатору
    @Override
    public int getItemCount() {
        return _id.size();
    }

    //внутрішній допоміжний клас
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _id_txt,user_name_txt,user_position_txt,user_subjects_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //ініціалізуємо необхідні елементи
            _id_txt = itemView.findViewById(R.id._id_txt);
            user_name_txt = itemView.findViewById(R.id.user_name_txt);
            user_position_txt = itemView.findViewById(R.id.user_position_txt);
            user_subjects_txt = itemView.findViewById(R.id.user_subjects_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
