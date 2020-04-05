package org.registry.docker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDockerRegistry {
    @GET("_catalog")
    Call<Catalog> getCatalog();

    @GET("{image}/tags/list")
    Call<Image> getImage(@Path(value = "image", encoded = true) String image);

    @GET("{image}/manifests/{tag}")
    Call<Tag> getTag(@Path(value = "image", encoded = true) String image, @Path("tag") String tag);
}
