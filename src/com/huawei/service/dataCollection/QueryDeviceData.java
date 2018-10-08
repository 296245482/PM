package com.huawei.service.dataCollection;

import java.util.HashMap;
import java.util.Map;

import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * Query Device Data :
 * This interface is used by NAs to query information for specify device.
 */
public class QueryDeviceData {

    public static void main(String args[]) throws Exception {

        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication，get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
        String appId = Constant.APPID;

        //please replace the deviceId, when you use the demo.
//        String deviceId = "3d0480b1-a4f4-4ea6-a943-74f7187668a9";
        String deviceId = "5d17af83-e086-477d-9b9a-84b4603455f6";
        String urlQueryDeviceData = Constant.QUERY_DEVICE_DATA + "/" + deviceId;

        Map<String, String> paramQueryDeviceData = new HashMap<>();
        paramQueryDeviceData.put("appId", appId);

        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        
        StreamClosedHttpResponse bodyQueryDeviceData = httpsUtil.doGetWithParasGetStatusLine(urlQueryDeviceData,
                paramQueryDeviceData, header);

        System.out.println("QueryDeviceData, response content:");
        System.out.println(bodyQueryDeviceData.getStatusLine());
        System.out.println(bodyQueryDeviceData.getContent());
        System.out.println();
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

        System.out.println("app auth success,return accessToken:");
        System.out.println(responseLogin.getStatusLine());
        System.out.println(responseLogin.getContent());
        System.out.println();

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
