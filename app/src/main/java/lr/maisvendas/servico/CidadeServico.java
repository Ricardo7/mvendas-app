package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CidadeServico {

    @GET("Cidade/GetListaCidades") //Busca
    public Call<Response<List<Cidade>>> carregarCidade(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

    @PUT("Cidade") //Insere (não utilizado no cidade)
    public Call<Response<Cidade>>criarCidade(@Header("X-Auth-Token") String token, @Body Cidade cidade);

    @POST("Cidade/{id}/") //Atualiza (não utilizado no cidade)
    public Call<Response<Cidade>>editarCidade(@Header("X-Auth-Token") String token, @Query("id") Integer id, @Body Cidade cidade);

    @DELETE("Cidade/{id}/") //Deleta (não utilizado no cidade)
    public Call<Response<Boolean>>excluirCidade(@Header("X-Auth-Token") String token, @Query("id") Integer id);
}
