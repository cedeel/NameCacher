// Imported from Mojang's AccountClient 2014-04-14

package com.cedeel.mojang.api.profiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cedeel.mojang.api.http.BasicHttpClient;
import com.cedeel.mojang.api.http.HttpBody;
import com.cedeel.mojang.api.http.HttpClient;
import com.cedeel.mojang.api.http.HttpHeader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpProfileRepository implements ProfileRepository {

    private static final int MAX_PAGES_TO_CHECK = 100;
    //private static Gson gson = new Gson();
    private static ObjectMapper mapper = new ObjectMapper();
    private HttpClient client;

    public HttpProfileRepository() {
        this(BasicHttpClient.getInstance());
    }

    public HttpProfileRepository(HttpClient client) {
        this.client = client;
    }

    @Override
    public Profile[] findProfilesByCriteria(ProfileCriteria... criteria) {
        try {
            //HttpBody body = new HttpBody(gson.toJson(criteria));
            HttpBody body = new HttpBody(mapper.writeValueAsString(criteria));
            List<HttpHeader> headers = new ArrayList<HttpHeader>();
            headers.add(new HttpHeader("Content-Type", "application/json"));
            List<Profile> profiles = new ArrayList<Profile>();
            for (int i = 1; i <= MAX_PAGES_TO_CHECK; i++) {
                ProfileSearchResult result = post(new URL("https://api.mojang.com/profiles/page/" + i), body, headers);
                if (result.getSize() == 0) {
                    break;
                }
                profiles.addAll(Arrays.asList(result.getProfiles()));
            }
            return profiles.toArray(new Profile[profiles.size()]);
        } catch (Exception e) {
            // TODO: logging and allowing consumer to react?
            return new Profile[0];
        }
    }

    private ProfileSearchResult post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
        String response = client.post(url, body, headers);
        //return gson.fromJson(response, ProfileSearchResult.class);
        return mapper.readValue(response, ProfileSearchResult.class);
    }

}

