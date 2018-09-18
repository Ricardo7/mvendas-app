package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ImagemServico {

    @GET("Imagem/GetListaImagens") //Busca
    public Call<Response<List<Imagem>>> carregarImagem(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

    @PUT("Imagem") //Insere (não utilizado no imagem)
    public Call<Response<Imagem>>criarImagem(@Header("X-Auth-Token") String token, @Body Imagem imagem);

    @POST("Imagem/{id}/") //Atualiza (não utilizado no imagem)
    public Call<Response<Imagem>>editarImagem(@Header("X-Auth-Token") String token, @Query("id") Integer id, @Body Imagem imagem);

    @DELETE("Imagem/{id}/") //Deleta (não utilizado no imagem)
    public Call<Response<Boolean>>excluirImagem(@Header("X-Auth-Token") String token, @Query("id") Integer id);
}
