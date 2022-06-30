package com.example.news;

public class UserData {
    private String ID;
    private String name;
    private String password;
    private String sex;
    private String birthday;
    private String phone;
    private int age;
    private String userSystem;
    private String area;
    private int environment;
    private int equipment;
    private String type;

    public UserData(String name,String password,String sex,String phone){

        this.name = name;
        this.password = password;
        this.sex = sex;
        this.phone = phone;

    }

    public UserData(String phone,String password){
        this.phone = phone;
        this.password = password;
    }

    public UserData(String ID,String name,String password,String sex,String birthday,String phone,int age,String userSystem,String area,int environment,int equipment,String type){
        this.phone = phone;
        this.sex = sex;
        this.age = age;
        this.name = name;
        this.password = password;
        this.area = area;
        this.birthday = birthday;
        this.environment = environment;
        this.equipment = equipment;
        this.ID = ID;
        this.userSystem = userSystem;
        this.type = type;
    }
    public int getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public int getEnvironment() {
        return environment;
    }

    public int getEquipment() {
        return equipment;
    }

    public String getArea() {
        return area;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

    public String getUserSystem() {
        return userSystem;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }

    public void setEquipment(int equipment) {
        this.equipment = equipment;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUserSystem(String userSystem) {
        this.userSystem = userSystem;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void clear(){
        this.setSex(null);
        this.setPassword(null);
        this.setID(null);
        this.setEquipment(0);
        this.setEnvironment(0);
        this.setArea(null);
        this.setBirthday(null);
        this.setAge(0);
        this.setPhone(null);
        this.setName(null);
        this.setUserSystem(null);
        this.setType(null);
    }
}
