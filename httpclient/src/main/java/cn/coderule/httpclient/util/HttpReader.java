package cn.coderule.httpclient.util;

import cn.coderule.common.util.io.FileUtil;
import lombok.Getter;
import lombok.NonNull;
import cn.coderule.common.lang.exception.lang.IllegalArgumentException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class HttpReader {
    private final String path;
    private final HttpRequestMap requestMap;
    private String namespace;
    private Integer requestCounter = 1;


    public static HttpRequestMap read(String path) {
        return new HttpReader(path).readAll();
    }

    public HttpReader(String path) {
        if (StringUtil.isBlank(path)
                || StringUtil.endsWith(path, ".http")) {
            throw new IllegalArgumentException("invalid http request file: " + path);
        }

        this.path = path;
        parseNamespace();

        requestMap = new HttpRequestMap();

        assert namespace != null;
        requestMap.namespace(namespace);
    }

    public HttpRequestMap readAll() {
        List<String> lines = parseFile();
        if (CollectionUtil.isEmpty(lines)) {
            return requestMap;
        }

        parseHttpRequestList(lines);
        return requestMap;
    }

    private void parseNamespace() {
        String tmp = StringUtil.substringTo(path, ".");
        namespace = StringUtil.substringFrom(tmp, "/");
    }

    private void parseHttpRequestList(@NonNull List<String> lines) {
        HttpRequestParser parser = null;
        for (String line : lines) {
            if (isRequestStart(line)) {
                if (parser != null) {
                    saveHttpRequest(parser);
                }

                parser = new HttpRequestParser();
                parser.parseNameLine(line);
                continue;
            }


            if (parser == null) {
                continue;
            }

            parser.parseLine(line);
        }
    }

    private void saveHttpRequest(HttpRequestParser parser) {
        requestMap.put(getRequestName(parser.getName()), parser.getRequest());
    }

    private String getRequestName(String name) {
        if (StringUtil.notBlank(name)) {
            return name;
        }

        String result = StringUtil.join("req-", requestCounter);
        requestCounter++;
        return result;
    }

    private boolean isRequestStart(String line) {
        return StringUtil.startsWith(line, "###");
    }

    private List<String> parseFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(FileUtil.getResourceFileReader(path))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }

        return lines;
    }

}
