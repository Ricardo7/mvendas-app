package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ClienteServico {

    @GET("Cliente/GetListaClientesAtualizados") //Busca
    public Call<Response<List<Cliente>>> carregarCliente(@Header("Authorization") String token, @Query("dataAt") String dataAt, @Query("usuarioID") String usuarioIDWS);

    @POST("Cliente/AddCliente") //Insere
    public Call<Response<Cliente>>criarCliente(@Header("Authorization") String token, @Body Cliente cliente);

    @PUT("Cliente/EditaCliente") //Atualiza
    public Call<Response<Cliente>>editarCliente(@Header("Authorization") String token, @Body Cliente cliente);

    @DELETE("Cliente/DeleteCliente") //Deleta
    public Call<Response<Boolean>>excluirCliente(@Header("Authorization") String token, @Query("IDWS") Integer id);
}
