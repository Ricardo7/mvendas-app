package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Pais;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PaisServico {

    @GET("Locais/GetListaPaisesAtualizados") //Busca
    public Call<Response<List<Pais>>> carregarPais(@Header("Authorization") String token, @Query("dataAt") String dataAt);

    @PUT("Pais") //Insere (não utilizado no pais)
    public Call<Response<Pais>>criarPais(@Header("Authorization") String token, @Body Pais pais);

    @POST("Pais/{id}/") //Atualiza (não utilizado no pais)
    public Call<Response<Pais>>editarPais(@Header("Authorization") String token, @Query("id") Integer id, @Body Pais pais);

    @DELETE("Pais/{id}/") //Deleta (não utilizado no pais)
    public Call<Response<Boolean>>excluirPais(@Header("Authorization") String token, @Query("id") Integer id);
}
