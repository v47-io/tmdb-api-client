package io.v47.tmdb.http.utils;

public final class ExceptionUtil {
    private ExceptionUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }
}
