package pl.edu.uwr.pum.studentcrimeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.edu.uwr.pum.studentcrimeapp.models.Crime;

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentCrimeDB_JAVA.db";

    public static final String TABLE_CRIMES = "crimes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IS_SOLVED= "solved";
    public static final String COLUMN_IMAGE = "image";

     public DBHandler(Context context)
     {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);

     }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_STUDENTS_CRIMES_TABLE = "CREATE TABLE " +
                TABLE_CRIMES +
                "(" +
                COLUMN_ID + " " +
                "INTEGER PRIMARY KEY," +
                COLUMN_TITLE +
                " TEXT," +
                COLUMN_DATE +
                " TEXT," +
                COLUMN_IS_SOLVED +
                " BOOLEAN," +
                COLUMN_IMAGE +
                " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_STUDENTS_CRIMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CRIMES);
        onCreate(sqLiteDatabase);
    }

    public Cursor getStudentCrimes()
    {
        String query = "SELECT * FROM " + TABLE_CRIMES;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addCrime(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, crime.getTitle());
        values.put(COLUMN_DATE, crime.getDate().toString());
        values.put(COLUMN_IS_SOLVED, crime.isSolved());
        values.put(COLUMN_IMAGE, crime.getPicture());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CRIMES, null, values);
        db.close();
    }

    public void deleteCrime(String title)
    {
        String query = "SELECT * FROM " +
                TABLE_CRIMES +
                " WHERE " +
                COLUMN_TITLE +
                "= \"" +
                title +
                "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            int currentID = Integer.parseInt(cursor.getString(0));
            db.delete(TABLE_CRIMES, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(currentID)});
        }
        db.close();
    }

    public void updateCrime(int id, String title, String date, boolean is_solved, String picture)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_IS_SOLVED, is_solved);
        contentValues.put(COLUMN_IMAGE, picture);

        db.update(TABLE_CRIMES, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
    }

    public Cursor searchCrime(String crime_title)
    {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_CRIMES,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DATE, COLUMN_IS_SOLVED},
                COLUMN_TITLE + "=?",
                new String[]{crime_title}, null, null, null, null);
        return cursor;
    }
}
