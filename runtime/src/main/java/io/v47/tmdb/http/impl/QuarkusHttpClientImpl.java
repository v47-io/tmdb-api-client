/*
 * The Clear BSD License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.v47.tmdb.http.HttpClient;
import io.v47.tmdb.http.HttpRequest;
import io.v47.tmdb.http.HttpResponse;
import io.v47.tmdb.http.TypeInfo;
import io.v47.tmdb.http.api.ErrorResponse;
import io.v47.tmdb.http.api.ErrorResponseKt;
import io.v47.tmdb.http.api.RawErrorResponse;
import io.v47.tmdb.http.utils.ExceptionUtil;
import io.v47.tmdb.http.utils.MultiMapUtil;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuarkusHttpClientImpl implements HttpClient {
    private static final Pattern uriVariablePattern = Pattern.compile("\\{(\\w+)}", Pattern.CASE_INSENSITIVE);
    private static final Pattern imageErrorPattern = Pattern.compile("<h\\d>(.+?)</h\\d>", Pattern.CASE_INSENSITIVE);

    private final String baseUrl;
    private final WebClient client;
    private final ObjectMapper objectMapper;

    public QuarkusHttpClientImpl(String baseUrl, WebClient client, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @NotNull
    @Override
    public Flow.Publisher<HttpResponse<?>> execute(@NotNull HttpRequest request, @NotNull TypeInfo responseType) {
        Boolean jsonBody = isNotByteArray(responseType);

        io.vertx.mutiny.ext.web.client.HttpRequest<Buffer> vxRequest = createHttpRequest(request, jsonBody);
        vxRequest.as(BodyCodec.buffer());

        Buffer body = null;
        if (request.getBody() != null) {
            if (request.getBody() instanceof byte[])
                body = Buffer.buffer((byte[]) request.getBody());
            else {
                try {
                    body = Buffer.buffer(this.objectMapper.writeValueAsBytes(request.getBody()));
                } catch (JsonProcessingException e) {
                    ExceptionUtil.sneakyThrow(e);
                }
            }
        }

        Uni<io.vertx.mutiny.ext.web.client.HttpResponse<Buffer>> vxResponse = body != null ? vxRequest.sendBuffer(body) : vxRequest.send();

        return Multi.createFrom().uni(vxResponse).map(bufferHttpResponse -> {
            if (bufferHttpResponse.statusCode() == 200)
                return mapHttpResponse(bufferHttpResponse, jsonBody ? responseType : null);
            else
                return new DefaultHttpResponse<>(bufferHttpResponse.statusCode(),
                                                 MultiMapUtil.convertToMap(bufferHttpResponse.headers()),
                                                 createErrorResponse(bufferHttpResponse));
        });
    }

    private Boolean isNotByteArray(TypeInfo typeInfo) {
        if (typeInfo instanceof TypeInfo.Simple) {
            return ((TypeInfo.Simple) typeInfo).getType() != byte[].class;
        } else {
            return true;
        }
    }

    private io.vertx.mutiny.ext.web.client.HttpRequest<Buffer> createHttpRequest(HttpRequest request, Boolean json) {
        HttpMethod httpMethod = extractHttpMethod(request);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setMethod(httpMethod);
        requestOptions.setAbsoluteURI(createUri(request));
        requestOptions.addHeader("Accept", json ? "application/json" : "*/*");
        requestOptions.addHeader("Content-Type",
                                 request.getBody() != null && request.getBody() instanceof byte[] ? "application/octet-stream" : "application/json");

        return this.client.request(httpMethod, requestOptions);
    }

    private static HttpMethod extractHttpMethod(HttpRequest request) {
        HttpMethod method = null;
        switch (request.getMethod()) {
            case Get:
                method = HttpMethod.GET;
                break;
            case Post:
                method = HttpMethod.POST;
                break;
            case Put:
                method = HttpMethod.PUT;
                break;
            case Delete:
                method = HttpMethod.DELETE;
                break;
        }

        return method;
    }

    private URL createUri(HttpRequest request) {
        StringBuilder uriSB = new StringBuilder(this.baseUrl);

        if (request.getUrl().startsWith("/"))
            uriSB.append('/');

        uriSB.append(uriVariablePattern.matcher(request.getUrl()).replaceAll(mr -> {
            String name = mr.group(1);
            Object value = request.getUriVariables().get(name);
            if (value == null)
                throw new IllegalArgumentException("No value specified for URI variable " + name);

            return value.toString();
        }));

        if (!request.getQuery().isEmpty()) {
            uriSB.append('?');

            boolean first = true;
            for (Map.Entry<String, List<Object>> entry : request.getQuery().entrySet()) {
                String name = entry.getKey();
                List<Object> values = entry.getValue();

                if (first)
                    first = false;
                else
                    uriSB.append('&');

                uriSB.append(URLEncoder.encode(name, StandardCharsets.UTF_8));
                uriSB.append('=');

                boolean valueFirst = true;
                for (Object value : values) {
                    if (valueFirst)
                        valueFirst = false;
                    else
                        uriSB.append(',');

                    uriSB.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
                }
            }
        }

        URL result = null;
        try {
            result = new URL(uriSB.toString());
        } catch (MalformedURLException e) {
            ExceptionUtil.sneakyThrow(e);
        }

        return result;
    }

    @NotNull
    private HttpResponse<?> mapHttpResponse(io.vertx.mutiny.ext.web.client.HttpResponse<Buffer> vxResponse,
                                            @Nullable TypeInfo typeInfo) {
        try {
            return new DefaultHttpResponse<>(vxResponse.statusCode(),
                                             MultiMapUtil.convertToMap(vxResponse.headers()),
                                             readResponseBody(vxResponse, typeInfo));
        } catch (IOException e) {
            ExceptionUtil.sneakyThrow(e);
        }

        return null; // Unreachable
    }

    private Object readResponseBody(io.vertx.mutiny.ext.web.client.HttpResponse<Buffer> vxResponse,
                                    @Nullable TypeInfo typeInfo) throws IOException {
        if (typeInfo != null)
            return this.objectMapper.readValue(vxResponse.body().getBytes(), new TmdbTypeReference<>(typeInfo));
        else
            return vxResponse.body().getBytes();
    }

    private ErrorResponse createErrorResponse(io.vertx.mutiny.ext.web.client.HttpResponse<Buffer> vxResponse) {
        try {
            return ErrorResponseKt.toErrorResponse(this.objectMapper.readValue(vxResponse.body().getBytes(),
                                                                               RawErrorResponse.class));
        } catch (IOException e) {
            String str = vxResponse.bodyAsString();
            Matcher imageErrorMatcher = imageErrorPattern.matcher(str);

            String msg = str;
            if (imageErrorMatcher.find()) {
                msg = imageErrorMatcher.group(1);
            }

            return new ErrorResponse(msg, vxResponse.statusCode());
        }
    }

    @Override
    public void close() {
        this.client.close();
    }
}
