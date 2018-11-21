package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AtividadeServico {

    @GET("Atividade/GetListaAtividadesAtualizados") //Busca
    public Call<Response<List<Atividade>>> carregarAtividade(@Header("Authorization") String token, @Query("dataAt") String dataAt, @Query("usuarioID") String usuarioIDWS);

    @GET("Atividade/GetListaAtividadesSugeridas") //Busca
    public Call<Response<List<Atividade>>> carregarAtividadesSugeridas(@Header("Authorization") String token, @Query("data") String data, @Query("usuarioID") String usuarioIDWS);

    @POST("Atividade/AddAtividade") //Insere
    public Call<Response<Atividade>>criarAtividade(@Header("Authorization") String token, @Body Atividade atividade);

    @PUT("Atividade/EditaAtividade") //Atualiza
    public Call<Response<Atividade>>editarAtividade(@Header("Authorization") String token, @Query("IDWS") String idWS, @Body Atividade atividade);

    @DELETE("Atividade/RemoveAtividade") //Deleta
    public Call<Response<Boolean>>excluirAtividade(@Header("Authorization") String token, @Query("IDWS") Integer id);
}
