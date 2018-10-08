package com.huawei.database;

import com.huawei.entity.DeviceNewData;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DatabaseOperation {
    static Connection conn;

    static Statement st;
    /* 获取数据库连接的函数*/
    public static Connection getConnection() {
        //创建用于连接数据库的Connection对象
        Connection con = null;
        try {
            // 加载Mysql数据驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 创建数据连接，打包时注意更换为localhost节点
//            con = DriverManager.getConnection("jdbc:mysql://*********:3306/pm25", "guest", "guest");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pm25", "root", "27271992");

        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }
        //返回所建立的数据库连接
        return con;
    }
    public static void insertDeviceNewData(List<DeviceNewData> deviceNewDataList){
        conn = getConnection();
        for(DeviceNewData item:deviceNewDataList){
            try {
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp time = Timestamp.valueOf(simpleDate.format(item.getTimePoint()));
                String sql = "INSERT INTO data_new_device" +
                        "(shortDeviceId, deviceId, pm25, temp, RH, CO, CO2, NO2, O3, VOC, timePoint)" +
                        "VALUES ('"+item.getShortDeviceId()+"','"+item.getDeviceId()+"','"+item.getPm25()+"','"+item.getTemp()+"'," +
                        "'"+item.getRH()+"','"+item.getCO()+"','"+item.getCO2()+"','"+item.getNO2()+"'," +
                        "'"+item.getO3()+"','"+item.getVOC()+"','"+time+"')";
//                System.out.println("sql="+sql);
                st = (Statement) conn.createStatement();

                int count = st.executeUpdate(sql);

//                System.out.println("inserted " + count + " records");



            } catch (SQLException e) {
                System.out.println("insert failed" + e.getMessage());
            }

        }
    }

    public static void close(){
        try {
            conn.close();
        }catch (Exception e){
            System.out.println("Database close failed");
        }
    }
    // 测试代码
//    public static void main(String[] args){
//        conn = getConnection();
//        try {
//            String sql = "select * from manager";
//            st = (Statement) conn.createStatement();
//
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                System.out.println(rs.getString("name"));
//            }
//            conn.close();   //关闭数据库连接
//
//        } catch (SQLException e) {
//            System.out.println("查询数据失败");
//        }
//    }
}
