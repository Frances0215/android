package com.example.news;

import java.util.ArrayList;

public class NewsType {

    private ArrayList<String> otherType = new ArrayList<>();
    private ArrayList<String> myType = new ArrayList<>();


    public ArrayList<String> getOtherType() {
        return otherType;
    }


    public ArrayList<String> getMyType() {
        return myType;
    }

    public void setOtherType(ArrayList<String> otherType) {
        this.otherType = otherType;
    }


    public void setMyType(ArrayList<String> myType) {
        this.myType = myType;
    }

    public void outOtherType(String[] AllType, ArrayList<String> myType){
        ArrayList<String> myOtherType = new ArrayList<>();
        boolean flag=true;
        int k = 0;
        for(int i=0;i<AllType.length;i++){
            for(int j=0;j<myType.size();j++){
                if(AllType[i].equals(myType.get(j))) {
                    flag =false;
                }
            }
            if(flag){
                myOtherType.add(AllType[i]);
                k++;
            }
            flag = true;
        }
        setOtherType(myOtherType);
    }


}
