package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Estado;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface EstadoServico {

    @GET("Locais/GetListaEstados") //Busca
    public Call<Response<List<Estado>>> carregarEstado(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

    @PUT("Estado") //Insere (não utilizado no estado)
    public Call<Response<Estado>>criarEstado(@Header("X-Auth-Token") String token, @Body Estado estado);

    @POST("Estado/{id}/") //Atualiza (não utilizado no estado)
    public Call<Response<Estado>>editarEstado(@Header("X-Auth-Token") String token, @Query("id") Integer id, @Body Estado estado);

    @DELETE("Estado/{id}/") //Deleta (não utilizado no estado)
    public Call<Response<Boolean>>excluirEstado(@Header("X-Auth-Token") String token, @Query("id") Integer id);
}
