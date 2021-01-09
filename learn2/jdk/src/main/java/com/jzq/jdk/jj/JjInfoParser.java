package com.jzq.jdk.jj;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class JjInfoParser {
    private static final Logger logger = LoggerFactory.getLogger(JjInfoParser.class);


    public static void main(String[] args) {
        String res = getAllJj();
//        res = res.substring("var r = ".length() + 1, res.length() - 2);
        System.out.println(res);

        JSONArray array = JSONArray.parseArray(res);
        System.out.println(array.size());


    }

    private static String getAllJj() {
        String value = null;
        try {
            value = doGet("http://fund.eastmoney.com/js/fundcode_search.js", "utf-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }


    public static String doGet(String url, String charset) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpClient client = HttpClients.createDefault();
        get.setConfig(
                RequestConfig.custom()
                        .setProxy(new HttpHost("218.7.171.91", 3128))
                        .setConnectTimeout(10000)
                        .build()
        );
        HttpEntity entity = client.execute(get).getEntity();
        BufferedInputStream stream = new BufferedInputStream(entity.getContent());
        List<Byte> byteLinkedList = new LinkedList<>();

        while (true) {
            int available = stream.available();
            if (available == 0) {
                break;
            } else {
                byte[] bytes = new byte[available];
                stream.read(bytes);
                for (byte b : bytes) {
                    byteLinkedList.add(b);
                }
            }
        }

        byte[] bs = new byte[byteLinkedList.size()];
        int index = 0;
        for (byte b : byteLinkedList) {
            bs[index++] = b;
        }
        String str = new String(bs, charset);
        System.out.println(str);
        return str;
    }
}
