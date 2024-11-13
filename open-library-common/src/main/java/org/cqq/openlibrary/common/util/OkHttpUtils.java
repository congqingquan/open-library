package org.cqq.openlibrary.common.util;

import io.vavr.Tuple3;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.cqq.openlibrary.common.exception.NetworkException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp utils
 *
 * @author Qingquan
 */
@Slf4j
public class OkHttpUtils {
    
    private OkHttpUtils() {
    }
    
    private static OkHttpClient client;
    
    static {
        // default config
        client = new OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(10L, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(20, 5L, TimeUnit.MINUTES))
                .build();
    }
    
    public static void setClient(OkHttpClient client) {
        OkHttpUtils.client = client;
    }
    
    public static OkHttpClient getClient() {
        return OkHttpUtils.client;
    }
    
    // ====================================================== COMMON ======================================================
    
    private static final MediaType APPLICATION_JSON_UTF8_VALUE = MediaType.parse("application/json; charset=utf-8");
    
    private static final MediaType APPLICATION_XML_UTF8_VALUE = MediaType.parse("application/xml; charset=utf-8");
    
    private static final MediaType APPLICATION_FORM_URLENCODED_UTF8_VALUE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    
    private static final MediaType MULTIPART_FORM_DATA_UTF8_VALUE = MediaType.parse("multipart/form-data; charset=utf-8");
    
    public static Response execute(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (Exception exception) {
            throw new NetworkException("Okhttp call failed", exception);
        }
    }
    
    // ====================================================== GET ======================================================
    
    public static Response get(String url) {
        return get(url, null, null);
    }
    
    public static Response get(String url, Map<String, String> params) {
        return get(url, null, params);
    }
    
    public static Response get(String url, Map<String, String> headers, Map<String, String> params) {
        return execute(
                new Request.Builder()
                        .url(url + NetUtils.toGetRequestParamsUri(params))
                        .headers(Headers.of(Optional.ofNullable(headers).orElse(new HashMap<>())))
                        .build()
        );
    }
    
    // ====================================================== POST ======================================================
    
    public static Response postJSON(String url, Map<String, String> headers, Object data) {
        return post(url, headers, RequestBody.create(JSONUtils.toJSONString(data), APPLICATION_JSON_UTF8_VALUE));
    }
    
    public static Response postForm(String url, Map<String, String> headers, Map<String, String> params) {
        return post(
                url,
                headers,
                Optional.ofNullable(params)
                        .map(ps -> {
                            FormBody.Builder formBodyBuilder = new FormBody.Builder();
                            ps.forEach(formBodyBuilder::add);
                            return formBodyBuilder.build();
                        })
                        .orElse(new FormBody.Builder().build())
        );
    }
    
    /**
     * <p>
     *      Example:
     *          File file = new File("/path/file.jpg");
     *          new MultipartBody.Builder().addFormDataPart("uploadFile", file.getName(), RequestBody.create(file, MediaType.parse("image/png")))
     * </p>
     *
     * @param files [formFieldName, filename, RequestBody.create(new File("/path/file.jpg"), MediaType.parse("image/png"))]
     */
    public static Response postMultipartForm(String url,
                                             Map<String, String> headers,
                                             Map<String, String> params,
                                             List<Tuple3<String, String, RequestBody>> files) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        // multipart/form-data
        multipartBodyBuilder.setType(MultipartBody.FORM);
        // 文字段
        Optional.ofNullable(params).ifPresent(ps -> ps.forEach(multipartBodyBuilder::addFormDataPart));
        // 文件段
        Optional.ofNullable(files).ifPresent(fts -> {
            if (CollectionUtils.isEmpty(fts)) {
                return;
            }
            for (Tuple3<String, String, RequestBody> ft : fts) {
                multipartBodyBuilder.addFormDataPart(ft._1, ft._2, ft._3);
            }
        });
        
        return post(url, headers, multipartBodyBuilder.build());
    }
    
    public static Response post(String url, Map<String, String> headers, RequestBody requestBody) {
        return execute(
                new Request.Builder()
                        .url(url)
                        .headers(Headers.of(Optional.ofNullable(headers).orElse(new HashMap<>())))
                        .post(requestBody)
                        .build()
        );
    }
}
