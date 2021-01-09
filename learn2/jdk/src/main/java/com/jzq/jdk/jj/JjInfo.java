package com.jzq.jdk.jj;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JjInfo {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet method = new HttpGet("https://api.doctorxiong.club/v1/fund/all");
        method.setConfig(
                RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .build()
        );
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.execute(method).getEntity().getContent(), StandardCharsets.UTF_8));
        System.out.println(reader.readLine());
    }

}
