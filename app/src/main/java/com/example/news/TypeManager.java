package com.example.news;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TypeManager {

    private static final String TAG = "TypeManager";
    private static final String TABLE_NAME = "Type";
    private static final String TYPE_NAME = "name";
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "Data3.dp";
    private Context mContext = null;

    private MyDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public TypeManager(Context context) {
        mContext = context;
        Log.i(TAG,"TypeManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new MyDataBaseHelper(mContext,DB_NAME,null,DB_VERSION);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    //添加新的类型
    public boolean insertType(String newType) {
        ContentValues values = new ContentValues();
        values.put("name",newType);
        return mSQLiteDatabase.insert(TABLE_NAME,TYPE_NAME,values)>0;
    }

    //删除一个类型
    public boolean deleteMyType(String typeName){
        //SQLite中字符串必须用单引号
        return mSQLiteDatabase.delete(TABLE_NAME,TYPE_NAME+"='"+typeName + "'",null)>0;
    }

    //得到所有的我的频道
    public ArrayList<String> getAllMyType(){
        Cursor mCursor = mSQLiteDatabase.query(TABLE_NAME,null,null,null,null,null,null);
        ArrayList<String> myType = new ArrayList<>();
        while (mCursor.moveToNext()){
            @SuppressLint("Range") String type = mCursor.getString(mCursor.getColumnIndex(TYPE_NAME));
            myType.add(type);
        }
        return myType;
    }

    //删除所有的类型
    public int delectAllType(){
        int i = mSQLiteDatabase.delete(TABLE_NAME,null,null);
        return i;
    }


}
