package com.devabit.takestock.rest;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link java.util.concurrent.Callable} so when executed asynchronously can
 * return a value.
 *
 * Created by Victor Artemyev on 22/04/2016.
 */

public class ApiConnection implements Callable<String> {

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    private Request mRequest;

    public static ApiConnection createGET(String url) {
        Request.Builder builder = createRequestBuilder(url);
        builder.get();
        return new ApiConnection(builder.build());
    }

    public static ApiConnection createPOST(String url) {
        Request.Builder builder = createRequestBuilder(url);
//        builder.post();
        return new ApiConnection(builder.build());
    }

    private ApiConnection(Request request) {
        mRequest = request;
    }

    private static Request.Builder createRequestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.
     *
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.
     * @throws IllegalStateException when the call has already been executed.
     */
    public String makeSyncRequest() throws IOException {
     OkHttpClient client = buildClient();
        return client.newCall(mRequest).execute().body().toString();
    }

    private OkHttpClient buildClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        return builder.build();
    }

    @Override public String call() throws Exception {
        return makeSyncRequest();
    }
}
