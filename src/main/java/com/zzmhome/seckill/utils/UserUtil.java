package com.zzmhome.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class UserUtil {

    public static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(15800000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            user.setLoginCount(1);
            user.setLastLoginDate(new Date());
            user.setRegisterDate(new Date());
            users.add(user);
        }
        System.out.println("create user");
//        //插入数据库
//        Connection conn = getConn();
//        String sql = "insert into t_user(login_count,last_login_date,register_date,salt,password,nickname,id) " +
//                "values (?,?,?,?,?,?,?)";
//        PreparedStatement preparedStatement = conn.prepareStatement(sql);
//        for (User user : users){
//            preparedStatement.setInt(1,user.getLoginCount());
//            preparedStatement.setTimestamp(2,new Timestamp(user.getLastLoginDate().getTime()));
//            preparedStatement.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
//            preparedStatement.setString(4,user.getSalt());
//            preparedStatement.setString(5,user.getPassword());
//            preparedStatement.setString(6,user.getNickname());
//            preparedStatement.setLong(7,user.getId());
//            preparedStatement.addBatch();
//        }
//        preparedStatement.executeBatch();
//        preparedStatement.clearParameters();
//        conn.close();
//        System.out.println("insert to db");
        //登录 生成UserTicket
        String urlString = "http://localhost:9090/login/doLogin";
        File file = new File("C:\\Users\\78693\\Desktop\\config.txt");
        if (file.exists()){
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);
        for (User user : users){
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String param = "mobile=" + user.getId() + "&password=123456";
            out.write(param.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len=inputStream.read(buff))>=0){
                bout.write(buff,0,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket :" + user.getId());
            String row = user.getId()+ "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file :" + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception{
        String url = "jdbc:mysql://127.0.0.1:3306/seckill?serverTimezone=GMT%2B8&characterEncoding=utf8&useSSL=false";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }

}
