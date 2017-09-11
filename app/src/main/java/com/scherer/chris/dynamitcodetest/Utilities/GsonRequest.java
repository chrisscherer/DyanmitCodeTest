package com.scherer.chris.dynamitcodetest.Utilities;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Modified Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 *
 * Code adapted from https://developer.android.com/training/volley/request-custom.html
 *
 * Volley adapter that has been extended to allow for other METHODS besides GET
 * @param <U> The type of the body object being sent, can be null if no body needs posted
 * @param <T> The type of the object you're expecting in response
 */
public class GsonRequest<U, T> extends Request<T> {
    /**
     * Gson parser from Google
     */
    private final Gson gson = new Gson();
    /**
     * Class reference for the return type class
     */
    private final Class<T> responseClass;
    /**
     * A map of headers that will be parsed during request
     */
    private final Map<String, String> headers;
    /**
     * Listener object listening for request return
     */
    private final Listener<T> listener;
    /**
     * Object that holds relevant body information
     */
    private final U bodyClass;

    private String encoding = "application/json";


    /**
     * Make a REST request and return a JSON parsed result
     * 
     * @param requestType The METHOD used for your request e.g. Method.DELETE, Method.Get
     * @param url The URL of the request endpoint
     * @param bodyClass Object of type U that will be sent as the request body, can be null
     * @param responseClass Class reference used to parse the JSON response
     * @param headers Map that contains any headers that need to be sent in request
     * @param listener Listener for request response
     * @param errorListener Listener for request error
     */
    public GsonRequest(int requestType, String url, U bodyClass, Class<T> responseClass, Map<String, String> headers,
                       Listener<T> listener, ErrorListener errorListener) {
        super(requestType, url, errorListener);
        this.responseClass = responseClass;
        this.headers = headers;
        this.listener = listener;
        this.bodyClass = bodyClass;
    }

    public GsonRequest(int requestType, String url, U bodyClass, Class<T> responseClass, Map<String, String> headers, String encoding,
                       Listener<T> listener, ErrorListener errorListener) {
        super(requestType, url, errorListener);
        this.responseClass = responseClass;
        this.headers = headers;
        this.listener = listener;
        this.bodyClass = bodyClass;
        this.encoding = encoding;
    }

    /**
     * Override of the getHeaders funtion on the base Request
     * If headers are not provided, the base Request will return an empty map
     * @return A map containing the headers for the request
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    /**
     * Uses Gson to parse our request body into json, and then into a byte array for sending
     * @return
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        return gson.toJson(bodyClass).getBytes();
    }

    @Override
    public String getBodyContentType(){
        return encoding;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}