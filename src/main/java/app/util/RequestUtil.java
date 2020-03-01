package app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.Request;

import java.io.IOException;
import java.io.StringWriter;

/**
 */
public class RequestUtil {

    public static long getId(Request request) {
        return Long.parseLong(request.params("id"));
    }

    public static double getAmount(Request request) {
        return Double.parseDouble(request.params("amount"));
    }

    public static long getFrom(Request request) {
        return Long.parseLong(request.params("from"));
    }

    public static long getTo(Request request) {
        return Long.parseLong(request.params("to"));
    }

    public static String toJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}
