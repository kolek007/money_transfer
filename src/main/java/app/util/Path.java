package app.util;

import lombok.Getter;

/**
 * REST service paths structure
 */
public class Path {

    public static class Web {
        @Getter public static final String NEW = "/new";
        @Getter public static final String GET = "/get";
        @Getter public static final String GET_ID = "/get/:id";
        @Getter public static final String UPDATE = "/update";
        @Getter public static final String DELETE = "/delete/:id";
        @Getter public static final String TRANSFER = "/transfer/:from/:to/:amount";
    }
}
