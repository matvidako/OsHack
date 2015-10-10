package hr.ms.oshack.net;

import java.util.List;

import hr.ms.oshack.model.Bite;
import hr.ms.oshack.model.Bites;
import hr.ms.oshack.model.Cluster;
import hr.ms.oshack.model.Traps;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MosquitoService {

    @POST("/bites")
    void reportBite(@Body Bite bite, Callback<Response> callback);

    @GET("/bites")
    void getBites(@Query("limit") int limit, Callback<Bites> callback);

    @GET("/clusters")
    void getClusters(Callback<List<Cluster>> callback);

    @GET("/traps")
    void getTraps(@Query("limit") int limit, Callback<Traps> callback);
}
