package io.v47.tmdb.http.impl;

import io.v47.tmdb.http.HttpResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class HttpResponseImpl<T> implements HttpResponse<T> {
    private final T body;
    private final Map<String, List<String>> headers;
    private final int status;

    public HttpResponseImpl(int status, Map<String, List<String>> headers, T body) {
        this.body = body;
        this.headers = headers;
        this.status = status;
    }

    @Nullable
    @Override
    public T getBody() {
        return this.body;
    }

    @NotNull
    @Override
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    @Override
    public int getStatus() {
        return this.status;
    }
}
