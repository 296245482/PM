package com.huawei.service.dataCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.huawei.database.Constants;
import com.huawei.database.DatabaseOperation;
import com.huawei.entity.DeviceNewData;
import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * Query Device History Data :
 * This interface is used by NAs to query historical device data.
 */
public class QueryDeviceHistoryData {

    public static void main(String args[]) throws Exception {

        Set<String> deviceIds = Constants.devices.keySet();
        for(String deviceIdItem:deviceIds){

            // Two-Way Authentication
            HttpsUtil httpsUtil = new HttpsUtil();
            httpsUtil.initSSLConfigForTwoWay();

            // Authentication，get token
            String accessToken = login(httpsUtil);

            //Please make sure that the following parameter values have been modified in the Constant file.
            String appId = Constant.APPID;
            String urlQueryDeviceHistoryData = Constant.QUERY_DEVICE_HISTORY_DATA;

            String deviceId = deviceIdItem;
            String gatewayId = deviceIdItem;

            Map<String, String> paramQueryDeviceHistoryData = new HashMap<>();
            paramQueryDeviceHistoryData.put("deviceId", deviceId);
            paramQueryDeviceHistoryData.put("gatewayId", gatewayId);

            Map<String, String> header = new HashMap<>();
            header.put(Constant.HEADER_APP_KEY, appId);
            header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

            StreamClosedHttpResponse bodyQueryDeviceHistoryData = httpsUtil.doGetWithParasGetStatusLine(
                    urlQueryDeviceHistoryData, paramQueryDeviceHistoryData, header);

        /*
        // 打印获取到的数据
        System.out.println("QueryDeviceHistoryData, response content:");
        System.out.println(bodyQueryDeviceHistoryData.getContent());
        */

            // 新增解析模块
            String response = bodyQueryDeviceHistoryData.getContent();
            Map<String, String> data = JSON.parseObject(response, Map.class);

            List<Map> deviceDataList = JSON.parseArray(String.valueOf(data.get("deviceDataHistoryDTOs")), Map.class);

            // 打印一条数据测试用
//        System.out.println(String.valueOf(deviceDataList.get(0)));

            List<DeviceNewData> dataToStore = new ArrayList<>();

            // 获取现在时间
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYMMddHHmm");
            int nowTimeR = Integer.parseInt(dateFormat.format(date));
            // 处理时间格式
            DateFormat fmt =new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

//        System.out.println(deviceDataList.get(0).get("timestamp"));
//        System.out.println(dateFormat.format(fmt.parse(String.valueOf(deviceDataList.get(0).get("timestamp")))));

            fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            for(Map item:deviceDataList){
                int dataTimeR = Integer.parseInt(dateFormat.format(fmt.parse(String.valueOf(item.get("timestamp")))));
                // 只取最近一小时的数据
                if(nowTimeR - dataTimeR > 0 && nowTimeR - dataTimeR < 60) {
                    DeviceNewData addItem = new DeviceNewData();
                    addItem.setTimePoint(fmt.parse(String.valueOf(item.get("timestamp"))));
                    addItem.setDeviceId(String.valueOf(item.get("deviceId")));
                    Map<String, Double> dataItem = JSON.parseObject(String.valueOf(item.get("data")), Map.class);
                    addItem.setTemp(Double.valueOf(String.valueOf(dataItem.get("TEMP"))));
                    addItem.setPm25(Double.valueOf(String.valueOf(dataItem.get("PM25"))));
                    addItem.setRH(Double.valueOf(String.valueOf(dataItem.get("RH"))));
                    addItem.setShortDeviceId(Constants.devices.get(deviceIdItem));
                    dataToStore.add(addItem);
                }
            }
//            System.out.println(dataToStore.size());
            // 存入数据库，这里以第1个数据测试为例
            if(dataToStore.size() > 0) {
                DatabaseOperation.insertDeviceNewData(dataToStore);
            }
        }
        // 关闭数据库连接
        DatabaseOperation.close();
    }

    /**
     * Authentication，get token
     * */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> paramLogin = new HashMap<>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

//        System.out.println("app auth success,return accessToken:");
//        System.out.println(responseLogin.getContent());

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
