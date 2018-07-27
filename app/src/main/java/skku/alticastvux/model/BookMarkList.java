package skku.alticastvux.model;

import java.util.ArrayList;

public class BookMarkList {
    ArrayList<String> bmList;

    public BookMarkList() {
        this.bmList = new ArrayList<String>();
    }

    public void AddBookMark(String cName){
        bmList.add(cName);
    }
    public void DeleteBookMark(String cName){
        bmList.remove(cName);
    }
    public ArrayList<String> ShowBookMarkList(){
        return bmList;
    }
    public int BookMkarListSize(){
        return bmList.size();
    }
    public boolean isBookMark(String cName){
        return bmList.contains(cName);
    }
}
