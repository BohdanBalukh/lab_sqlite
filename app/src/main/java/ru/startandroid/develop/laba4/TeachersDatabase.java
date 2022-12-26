package ru.startandroid.develop.laba4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

//клас для представлення бази даних (наслідує базовий SQLiteOpenHelper)
class TeachersDatabase extends SQLiteOpenHelper {

    //створення усіх необхідних полів
    private Context context;
    private static final String DATABASE_NAME = "Teachers.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_teachers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "user_name";
    private static final String COLUMN_POSITION = "user_position";
    private static final String COLUMN_SUBJECTS = "user_subjects";

    TeachersDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //створення та ініціалізація таблиці бази даних
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_POSITION + " TEXT, " +
                COLUMN_SUBJECTS + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //метод додавання вчителів до бази даних
    void addTeacher(String name, String position, String subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_POSITION, position);
        cv.put(COLUMN_SUBJECTS, subject);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Помилка", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Додано успішно!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    //метод для читання усієї інформації з бази даних
    Cursor readAllData(){
        String query = "SELECT * FROM " +TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //метод для оновлення даних
    void updateData(String row_id,String name, String position, String subjects){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_POSITION,position);
        cv.put(COLUMN_SUBJECTS,subjects);

        long result = db.update(TABLE_NAME, cv,"_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Помилка при оновленні", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Дані оновлено успішно!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    //метод для видалення однієї стрічки з таблиці
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        if(result == -1){
            Toast.makeText(context, "Помилка при видаленні", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Дані видалено успішно!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    //метод для видалення усієї таблиці
    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        Toast.makeText(context, "Дані видалено успішно!", Toast.LENGTH_SHORT).show();
        db.close();
    }
}
