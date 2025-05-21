package cn.coderule.httpclient.vendor.okhttp;

import cn.coderule.httpclient.enums.ContentTypeEnum;
import cn.coderule.httpclient.enums.HttpMethodEnum;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.common.util.lang.bean.BeanUtil;
import cn.coderule.common.util.lang.string.JSONUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.httpclient.HttpRequestBuilder;

import java.util.Map;

public class OkHttpBuilder {
    private static final OkHttpBuilder INSTANCE = new OkHttpBuilder();

    public static OkHttpBuilder getInstance() {
        return INSTANCE;
    }

    public Request build(HttpRequestBuilder request) {
        Request.Builder builder = new Request.Builder()
                .url(request.getUrl());

        buildHeader(builder, request);
        buildBody(builder, request);

        return builder.build();
    }

    private void buildHeader(Request.Builder builder, HttpRequestBuilder request) {
        if (request.getContentType() == null) {
            request.contentType(ContentTypeEnum.HTML);
        }

        request.header("Content-Type", request.getContentType().getHeader());
        for (Map.Entry<String, String> entry : request.getHeader().entrySet()) {
            if (StringUtil.isBlank(entry.getKey())) {
                continue;
            }

            String value = null != entry.getValue()
                    ? entry.getValue()
                    : "";
            builder.header(entry.getKey(), value);
        }
    }

    private void buildBody(Request.Builder builder, HttpRequestBuilder request) {
        if (request.getContentType() == null) {
            request.contentType(ContentTypeEnum.HTML);
        }

        String body;

        switch (request.getContentType()) {
            case JSON:
                if (null != request.getMapBody()) {
                    body = JSONUtil.toJSONString(request.getMapBody());
                } else if (null != request.getObjectBody()) {
                    if (request.getObjectBody() instanceof String) {
                        body = (String) request.getObjectBody();
                    } else {
                        body = JSONUtil.toJSONString(request.getObjectBody());
                    }

                } else if (StringUtil.notBlank(request.getStringBody())) {
                    body = request.getStringBody();
                } else {
                    body = "{}";
                }
                break;
            case HTML:
            case JOSE:
                buildJoseBody(builder, request.getMethod(), request);
                return;
            case TEXT:
            case XML:
                body = StringUtil.isBlank(request.getStringBody()) ? request.getStringBody() : "";
                break;
            case FORM:
                buildFormBody(builder, request.getMethod(), request);
                return;
            case FILE:
            case PDF:
            case MSWORD:
                buildFileBody();
                return;
            default:
                throw new IllegalArgumentException("invalid contentType for HttpClient: " + request.getContentType().getName());
        }

        if (HttpMethodEnum.GET.equals(request.getMethod())) {
            return;
        }

        if (StringUtil.isBlank(body)) {
            body = "";
        }

        MediaType mediaType = null != request.getContentType() && StringUtil.notBlank(body)
                ? MediaType.parse(request.getContentType().getHeader())
                : null;
        RequestBody requestBody = RequestBody.create(body, mediaType);
        switch (request.getMethod()) {
            case POST:
                builder.post(requestBody);
                break;
            case PUT:
                builder.put(requestBody);
                break;
            case DELETE:
                builder.delete(requestBody);
                break;
            case PATCH:
                builder.patch(requestBody);
                break;
        }
    }

    private void buildJoseBody(Request.Builder builder, HttpMethodEnum method, HttpRequestBuilder request) {
        JoseBody joseBody = JoseBody.create(request.getStringBody());

        switch (method) {
            case POST:
                builder.post(joseBody);
                break;
            case PUT:
                builder.put(joseBody);
                break;
            case DELETE:
                builder.delete(joseBody);
                break;
            case PATCH:
                builder.patch(joseBody);
                break;
        }
    }

    private void buildFormBody(Request.Builder builder, HttpMethodEnum method, HttpRequestBuilder request) {
        Map<String, Object> map;
        if (MapUtil.notEmpty(request.getFormBody())) {
            map = request.getFormBody();
        } else if (MapUtil.notEmpty(request.getMapBody())) {
            map = request.getMapBody();
        } else {
            map = MapUtil.empty();
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (StringUtil.isBlank(entry.getKey())) {
                continue;
            }

            String value = BeanUtil.toString(entry.getValue());
            formBuilder.add(entry.getKey(), value);
        }

        FormBody formBody = formBuilder.build();
        switch (method) {
            case POST:
                builder.post(formBody);
                break;
            case PUT:
                builder.put(formBody);
                break;
            case DELETE:
                builder.delete(formBody);
                break;
            case PATCH:
                builder.patch(formBody);
                break;
        }
    }

    private void buildFileBody() {

    }


}
