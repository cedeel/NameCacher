// Imported from Mojang's AccountClient 2014-04-14

package com.cedeel.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient {
    public String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException;
    public String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException;
}
