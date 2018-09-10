package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SegmentoMercadoServico {

    @GET("SegmentoMercado/GetListaSegmentoMercados") //Busca
    public Call<Response<List<SegmentoMercado>>> carregarSegmentoMercado(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

    @PUT("SegmentoMercado") //Insere (não utilizado no segmentoMercado)
    public Call<Response<SegmentoMercado>>criarSegmentoMercado(@Header("X-Auth-Token") String token, @Body SegmentoMercado segmentoMercado);

    @POST("SegmentoMercado/{id}/") //Atualiza (não utilizado no segmentoMercado)
    public Call<Response<SegmentoMercado>>editarSegmentoMercado(@Header("X-Auth-Token") String token, @Query("id") Integer id, @Body SegmentoMercado segmentoMercado);

    @DELETE("SegmentoMercado/{id}/") //Deleta (não utilizado no segmentoMercado)
    public Call<Response<Boolean>>excluirSegmentoMercado(@Header("X-Auth-Token") String token, @Query("id") Integer id);
}
