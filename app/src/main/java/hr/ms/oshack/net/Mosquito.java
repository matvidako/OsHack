package hr.ms.oshack.net;

import java.util.List;

import hr.ms.oshack.model.Bite;
import hr.ms.oshack.model.Bites;
import hr.ms.oshack.model.Cluster;
import hr.ms.oshack.model.Trap;
import hr.ms.oshack.model.Traps;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;

public class Mosquito {

    private static Mosquito INSTANCE = null;
    private MosquitoService mosquitoService;

    public static Mosquito getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Mosquito();
        }
        return INSTANCE;
    }

    private Mosquito() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://moskito-app.herokuapp.com/api")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mosquitoService = adapter.create(MosquitoService.class);
    }

    public void reportBite(Bite bite, Callback<Response> callback) {
        mosquitoService.reportBite(bite, callback);
    }

    public void getBites(Callback<Bites> callback) {
        mosquitoService.getBites(1000, callback);
    }

    public void getClusters(Callback<List<Cluster>> callback) {
        mosquitoService.getClusters(callback);
    }

    public void getTraps(Callback<Traps> callback) {
        mosquitoService.getTraps(1000, callback);
    }

    public void addTrap(Trap trap, Callback<Response> callback) {
        mosquitoService.addTrap(trap, callback);
    }

}
