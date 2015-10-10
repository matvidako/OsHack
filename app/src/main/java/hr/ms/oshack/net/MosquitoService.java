package hr.ms.oshack.net;

import hr.ms.oshack.model.Bite;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface MosquitoService {

    @POST("/bites")
    void reportBite(@Body Bite bite, Callback<Response> callback);

}
