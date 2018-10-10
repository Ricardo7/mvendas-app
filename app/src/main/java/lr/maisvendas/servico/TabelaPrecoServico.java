package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.TabelaPreco;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TabelaPrecoServico {

    @GET("TabelaPrecos/GetListaTabelaPrecosAtualizados") //Busca
    public Call<Response<List<TabelaPreco>>> carregarTabelaPreco(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

}
