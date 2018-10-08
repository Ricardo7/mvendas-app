package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.CondicaoPgto;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CondicaoPgtoServico {

    @GET("CondicaoPgto/GetListaCondicaoPgtosAtualizados") //Busca
    public Call<Response<List<CondicaoPgto>>> carregarCondicaoPgto(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

}
