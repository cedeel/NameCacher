// Imported from Mojang's AccountClient 2014-04-14

package com.cedeel.mojang.api.http;

public class HttpBody {

    private String bodyString;

    public HttpBody(String bodyString) {
        this.bodyString = bodyString;
    }

    public byte[] getBytes() {
        return bodyString != null ? bodyString.getBytes() : new byte[0];
    }

}
