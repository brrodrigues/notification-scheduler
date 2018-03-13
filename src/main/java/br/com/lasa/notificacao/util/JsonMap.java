package br.com.lasa.notificacao.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;

public class JsonMap {

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonMap.class);

    public static <T> Object searchFromXPath(Object o, String xpath)  {

        ObjectMapper mapper = new ObjectMapper();
        Writer writer = new StringWriter();
        try {
            mapper.writeValue(writer,o);
            DocumentContext parse = JsonPath.parse(writer.toString());
            Object read = parse.read(xpath);
            return (T) read;
        } catch (PathNotFoundException e) {
            LOGGER.error(null, e);
            return "";
        } catch (java.io.IOException e) {
            LOGGER.error(null, e);
            throw new IllegalArgumentException("Nao foi possivel conver");
        }
    }

    public static <T> Object searchFromXPath(String jsonValue, String xpath) throws java.io.IOException {

        try {
            DocumentContext parse = JsonPath.parse(jsonValue);
            Object read = parse.read(xpath);
            return (T) read;
        } catch (PathNotFoundException e) {
            LOGGER.error(null, e);
            return "";
        }
    }

    public static String toJsonString(Object o)  {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(o);
            return s;
        } catch (PathNotFoundException e) {
            LOGGER.error(null, e);
            return "";
        } catch (java.io.IOException e) {
            LOGGER.error(null, e);
            throw new IllegalArgumentException("Nao foi possivel conver");
        }
    }


}
