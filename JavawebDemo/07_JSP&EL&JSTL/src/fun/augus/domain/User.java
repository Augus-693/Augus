package fun.augus.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Augus
 * @progect: JavawebDemo
 * @package: com.augus.domain
 * @FileName: User
 * @date 2021年11月13日 21:09
 * @version: 1.0
 */


public class User {

    private String name;
    private int age;
    private Date birthday;

    public User(String name, int age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public User() {
    }

    public String getBitStr(){
        if (birthday != null) {
            //格式化日期对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //返回字符串
            return sdf.format(birthday);
        } else {
            return "";
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
