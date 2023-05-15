package com.project.translation.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TranslationUtil {

    public static String getTrans(String origName) {
        String sendGet = null;
        String sl = "zh-CN";
        String tl = "en";
        do {
            try {
                sendGet = httpClientRequest("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + sl + "&tl=" + tl + "&dj=1&dt=t&dt=bd&dt=qc&dt=rm&dt=ex&dt=at&dt=ss&dt=rw&dt=ld&q=" + origName + "&tk=233819.233819");
            } catch (Exception e) {
                System.out.println(origName + "翻译,获取数据失败！" + e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } while (StringUtils.isBlank(sendGet));
        JSONObject jsonObject = JSONObject.parseObject(sendGet.replace("%20", " "));
        JSONArray sentences = JSONObject.parseArray(jsonObject.getString("sentences"));
        JSONObject jsonObject1 = sentences.getJSONObject(0);
        String trans = jsonObject1.getString("trans");
//                    String orig = jsonObject1.getString("orig");
        String orig = origName;
//        System.out.println("原文：" + orig + ",译文：" + trans);
        return trans;
    }


    public static String httpClientRequest(String urlStr) throws IOException {
        StringBuilder response = new StringBuilder();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(urlStr);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            try {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new RuntimeException("Unexpected HTTP response code: " + statusCode);
                }

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    response.append(result);
                }
            } finally {
                httpResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to make HTTP request", e);
        } finally {
            httpClient.close();
        }

        return response.toString();
    }



}
