package com.randyperrone.randyperrone.positivityshare.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = "DatabaseHelper";
    private final String TABLE_NAME = "user";
    private final String USERID_COLUMN_ZERO = "_id";
    private final String USERNAME_COLUMN_ONE = "username";
    private final String AGE_COLUMN_TWO = "age";
    private final String ABOUT_COLUMN_THREE = "about";
    private final String PICURL_COLUMN_FOUR = "picurl";

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String dbTable = "CREATE TABLE " + TABLE_NAME +
                " (" + USERID_COLUMN_ZERO + " text not null, " +
                USERNAME_COLUMN_ONE + " text, " +
                AGE_COLUMN_TWO + " text, " +
                ABOUT_COLUMN_THREE + " text, " +
                PICURL_COLUMN_FOUR + " text);";
        Log.i(TAG, dbTable);
        sqLiteDatabase.execSQL(dbTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertUserData(String userId, String userNameST, String ageST, String aboutST){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USERID_COLUMN_ZERO, userId);
        contentValues.put(USERNAME_COLUMN_ONE, userNameST);
        contentValues.put(AGE_COLUMN_TWO, ageST);
        contentValues.put(ABOUT_COLUMN_THREE, aboutST);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return resultCheck(result);
    }

    public boolean updateUserData(String userId, String userNameST, String ageST, String aboutST){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USERID_COLUMN_ZERO, userId);
        contentValues.put(USERNAME_COLUMN_ONE, userNameST);
        contentValues.put(AGE_COLUMN_TWO, ageST);
        contentValues.put(ABOUT_COLUMN_THREE, aboutST);

        long result = db.update(TABLE_NAME, contentValues, USERID_COLUMN_ZERO + " = ? ", new String[] {userId});
        db.close();
        return resultCheck(result);
    }

    public boolean updatePicUrl(String userId, String picURLST){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PICURL_COLUMN_FOUR, picURLST);

        long result = db.update(TABLE_NAME, contentValues, USERID_COLUMN_ZERO + " = ? ", new String[]{userId});
        db.close();
        return resultCheck(result);
    }

    public Cursor getUser(String userKey){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USERID_COLUMN_ZERO + " = ? ",new String[] {userKey});
        return result;
    }



    private Boolean resultCheck(long result){
        Boolean flag = false;
        if(result < 0){
            flag = false;
        }
        else if(result >= 0){
            flag = true;
        }
        return flag;
    }
}
