package org.registry.configuration;

import com.intellij.openapi.diagnostic.Logger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.registry.docker.IDockerRegistry;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class BrowsableRegistry implements Comparable<BrowsableRegistry> {
    private static final Logger LOGGER = Logger.getInstance(BrowsableRegistry.class);
    private String url;
    private String name;
    private IDockerRegistry service;

    public BrowsableRegistry(@NotNull String url, @NotNull String name) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            this.url = url;
        } else {
            String tmp = "https://" + url;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(tmp)
                    .build();
            try (Response r = client.newCall(request).execute()) {
                LOGGER.debug(r.message());
            } catch (IOException e) {
                LOGGER.warn("Can't connect to " + tmp + " with https, using http", e);
                tmp = "http://" + url;
            }

            this.url = tmp;
        }
        this.name = name;
        Retrofit r = new Retrofit.Builder().baseUrl(this.url + "/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        this.service = r.create(IDockerRegistry.class);
    }

    public BrowsableRegistry(@NotNull DockerRegistry registry) {
        this(registry.address, registry.name);
    }


    @Override
    public String toString() {
        return this.name + " - " + this.url;
    }

    @Override
    public int compareTo(@NotNull BrowsableRegistry o) {
        int result = this.name.compareTo(o.name);

        if (result != 0) {
            return result;
        }

        return this.url.compareTo(o.url);
    }

    public @NotNull
    IDockerRegistry getService() {
        return service;
    }

    public String getHost() {
        return HttpUrl.get(url).host();
    }
}
