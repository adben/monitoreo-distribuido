/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.developers.msa.goodbye.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Random;

public class ApacheClient extends Thread {

    Random random;

    public ApacheClient() {
        random = new Random();
    }

    @Override
    public void run() {
        // Apache HTTPClient creation
        final String uri = "http://localhost:8080/".concat(obtainSuffix());
        HttpGet httpGet = new HttpGet(uri);
        try (final CloseableHttpClient httpClient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpClient.execute(httpGet)) {
            final String result = EntityUtils.toString(response.getEntity());
            EntityUtils.consume(response.getEntity());
            System.out.printf("#%s - %s - %s - %n", this.getName(), uri, result);
        } catch (Exception e) {
            System.out.printf("#%s - %s - %s - client error", this.getName(), uri);
        }

    }

    private String obtainSuffix() {
        switch (random.nextInt(3)) {
            case 0:
                return "expensive-operation";
            case 1:
                return "nap";
            case 2:
                return "chain";
            default:
                return "hello";
        }


    }

}
