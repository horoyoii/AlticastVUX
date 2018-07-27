package skku.alticastvux.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import skku.alticastvux.model.VideoInfo;

public class dbHelper extends SQLiteOpenHelper {
    final static String TAG = "VideoManger";
    final static String TABLE_NAME = "ALLVIDEO";
    Context context;


    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "_id INTEGER, " +
                "path TEXT, " +
                "title TEXT, " +
                "size INTEGER, " +
                "duration INTEGER, " +
                "addedDate TEXT, " +
                "width INTEGER, " +
                "height INTEGER, " +
                "name TEXT)";
        Log.d(TAG,"onCreate _ BaseTable ");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    // 레코드 추가
    public void insert(VideoInfo video){
        SQLiteDatabase db = getWritableDatabase();
        String sql ="INSERT INTO "+TABLE_NAME +"(_id, path, title, size, duration, addedDate, width, height, name) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {video.getId(), video.getPath(), video.getTitle(), video.getSize(), video.getDuration(), video.getAddedDate(), video.getWidth(), video.getHeight(), video.getName()};
        db.execSQL(sql, params);
        db.close();

    }

    public boolean AddColumn(String cName){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + cName + " INTEGER DEFAULT 0";
            db.execSQL(sql);
        }catch(SQLiteException e){
            e.printStackTrace();
            return false;
        }
        db.close();
        Log.d(TAG, "열추가");
        return true;
    }

    public void AddBookMark(String cName, VideoInfo videoInfo){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE "+TABLE_NAME+" SET "+cName+"=1 WHERE _id='"+videoInfo.getId()+"'";
        Log.d(TAG, "북마크 추가");
        db.execSQL(sql);
        db.close();


    }

    // 테이블 drop
    public void TableDROP(String TableName){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS "+TableName;
        db.execSQL(sql);
    }


    // @@북마크에 ## 삭제
    public void delete(String cName, VideoInfo videoInfo) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d(TAG,"북마크에서 ##목록 삭제");
        String sql = "UPDATE "+TABLE_NAME+" SET "+cName+"=0 WHERE _id='"+videoInfo.getId()+"'";
        db.execSQL(sql);
        db.close();
    }


    public Cursor select(String cName){
        Log.d(TAG,cName+"select호출");
        SQLiteDatabase db = getReadableDatabase();
        Log.d(TAG,db.toString());

        String sql = "select * from "+TABLE_NAME+" WHERE "+cName+"=1";
        return db.rawQuery(sql, null);

    }

}
