package skku.alticastvux.util;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import skku.alticastvux.app.SKKUVuxApp;
import skku.alticastvux.model.BookMarkList;
import skku.alticastvux.model.VideoInfo;


/**
 * Created by horoyoii on 2018. 7. 25..
 */

public class BookMarkUtil {
    private final static String DB_NAME ="SKKU.db";
    private final static String DEFAULT_BOOK_MARK = "기본";
    private final static String DEFAULT_BOOK_MARK2 = "미분류";

    private static dbHelper helper;
    private static Gson gson;

    public static void init(Context context){
        helper = new dbHelper(context, DB_NAME, null, 1);
        gson = new Gson();

    }

    public static void DBInit(ArrayList<VideoInfo> list) {

        try{
            String json = SharedPreferencesUtil.getString("BookMarkList", "");
            BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);
            MyObject.isBookMark(DEFAULT_BOOK_MARK);
        }catch (Exception e){
            e.printStackTrace();
            for (VideoInfo item : list) {
                helper.insert(item);
            }
            helper.AddColumn(DEFAULT_BOOK_MARK);
            helper.AddColumn(DEFAULT_BOOK_MARK2);
            BookMarkList initObject = new BookMarkList();
            initObject.AddBookMark(DEFAULT_BOOK_MARK);
            initObject.AddBookMark(DEFAULT_BOOK_MARK2);
            String jsons = gson.toJson(initObject, BookMarkList.class);
            SharedPreferencesUtil.putString("BookMarkList", jsons);

            for(VideoInfo videoInfo : list){
                helper.AddBookMark(DEFAULT_BOOK_MARK, videoInfo);
            }

        }
    }



    // @@북마크를 추가 D
    public static boolean AddBookMark(String cName) {
        if (!helper.AddColumn(cName)) {
            Toast.makeText(SKKUVuxApp.getInstance(), "이미 존재하는 목록이름", 0).show();
            return false;

        }

        String json = SharedPreferencesUtil.getString("BookMarkList", "");
        BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);
        MyObject.AddBookMark(cName);
        json = gson.toJson(MyObject, BookMarkList.class);
        SharedPreferencesUtil.putString("BookMarkList", json);
        return true;
    }

    // @@북마크를 삭제
    public static boolean DeleteBookMark(String cName) {
        String json = SharedPreferencesUtil.getString("BookMarkList", "");
        BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);
        if (MyObject.isBookMark(cName)) {
            // TODO: column delete - https://stackoverflow.com/questions/8045249/how-do-i-delete-column-from-sqlite-table-in-android
            MyObject.DeleteBookMark(cName);
            json = gson.toJson(MyObject, BookMarkList.class);
            SharedPreferencesUtil.putString("BookMarkList", json);
            Toast.makeText(SKKUVuxApp.getInstance(),"북마크가 삭제되었음",0).show();
            return true;
        }else{
            Toast.makeText(SKKUVuxApp.getInstance(),"북마크가 존재하지 않음",0).show();
            return false;
        }

    }



    // 북마크에 @@파일을 추가
    public static boolean AddVideoToBookMark(String cName, VideoInfo videoInfo){
        String json = SharedPreferencesUtil.getString("BookMarkList", "");
        BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);

        if(MyObject.isBookMark(cName)){
            if(helper.AddBookMark(cName, videoInfo)){
                Toast.makeText(SKKUVuxApp.getInstance(), "추가되었음",0).show();
                return true;
            }else{
                Toast.makeText(SKKUVuxApp.getInstance(), "이미 추가되어 있음",0).show();
                return false;
            }

        }else{
            Toast.makeText(SKKUVuxApp.getInstance(), "북마크가 존재하지 않음.",0).show();
            return false;
        }
    }

    // @@북마크에 @@파일을 삭제
    public static boolean DeleteVideoFromBookMark(String cName, VideoInfo videoInfo){
        String json = SharedPreferencesUtil.getString("BookMarkList", "");
        BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);

        if(MyObject.isBookMark(cName)){
            if(helper.delete(cName, videoInfo)){
                Toast.makeText(SKKUVuxApp.getInstance(), "삭제되었음",0).show();
                return true;
            }else{
                Toast.makeText(SKKUVuxApp.getInstance(), "북마크에 해당 목록이 존재하지 않음.",0).show();
                return false;
            }
        }else{
            Toast.makeText(SKKUVuxApp.getInstance(), "북마크가 존재하지 않음.",0).show();
            return false;
        }
    }

    // 해당 북마크의 저장목록들
    public static ArrayList<VideoInfo> getVideosFromBookMark(String cName){
        ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();
        Cursor cur = helper.select(cName);

        while (cur.moveToNext()) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setId(cur.getLong(0));
            videoInfo.setPath(cur.getString(1));
            videoInfo.setTitle(cur.getString(2));
            videoInfo.setSize(cur.getLong(3));
            videoInfo.setDuration(cur.getLong(4));
            videoInfo.setAddedDate(cur.getString(5));
            videoInfo.setWidth(cur.getInt(6));
            videoInfo.setHeight(cur.getInt(7));
            videoInfo.setName(cur.getString(8));
            videoInfoArrayList.add(videoInfo);
        }

        return videoInfoArrayList;
    }

    // 모든 북마크 리스트 확인
    public static ArrayList<String> getAllBookMarkList(){

        String json = SharedPreferencesUtil.getString("BookMarkList", "");
        BookMarkList MyObject = gson.fromJson(json, BookMarkList.class);

        return MyObject.ShowBookMarkList();
    }

/*
        // 기본북마크에 @@파일을 추가
        public static void AddVideoToDefaultBookMark(VideoInfo videoInfo){
            //TODO : TableName이 없다면...
            dbHelper helper = new dbHelper(SKKUTestTT.getInstance(), DB_NAME, null, 1);
            helper.insert(DEFAULT_BOOK_MARK, videoInfo);
        }
*/

}
