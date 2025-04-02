package com.wolf.common.util.lang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2021/4/2 12:37 下午
 **/
@Slf4j
public class XMLUtil {

    //ObjectMapper是线程安全的，全局共享一个静态变量即可，不需要每次解析都build一个新的——浪费内存
    private static ObjectMapper xmlMapper = XmlMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true)
            .build();

    /**
     * Parse XML String to typed object
     * this method Only for small xml size, if up then 1MB please use inputStream
     *
     * @param xmlContent xml string content
     * @param valueType  object class type
     * @param <T>
     * @return parsed Object or null (if parse error)
     * @author YIK
     * @since 2022/1/18/11:20
     */
    public static <T> T toObject(String xmlContent, Class<T> valueType) {
        try {
            return xmlMapper.readValue(xmlContent, valueType);
        } catch (JsonProcessingException e) {
            log.error("XML parseToObject  occur error, errorMessage: " + e.getMessage() + ", objectType: " + valueType + " , xmlContent: " + xmlContent, e);
        }
        return null;
    }

    public static String toXmlString(Object object) {
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("XML toXmlString  occur error, errorMessage: " + e.getMessage() + ", object: " + object, e);
        }
        return null;
    }

    public static byte[] toXmlBytes(Object object) {
        try {
            return xmlMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("XML toXmlBytes  occur error, errorMessage: " + e.getMessage() + ", object: " + object, e);
        }
        return null;
    }
}
