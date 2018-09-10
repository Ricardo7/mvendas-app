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

    @GET("Cliente/GetListaClientes") //Busca
    public Call<Response<List<Cliente>>> carregarCliente(@Header("X-Auth-Token") String token, @Query("dataAt") String dataAt);

    @PUT("Cliente/AddCliente") //Insere
    public Call<Response<Cliente>>criarCliente(@Header("X-Auth-Token") String token, @Body Cliente cliente);

    @POST("Cliente/EditCliente") //Atualiza
    public Call<Response<Cliente>>editarCliente(@Header("X-Auth-Token") String token, @Query("ID_WS") Integer idWS, @Body Cliente cliente);

    @DELETE("Cliente/DeleteCliente") //Deleta
    public Call<Response<Boolean>>excluirCliente(@Header("X-Auth-Token") String token, @Query("ID_WS") Integer id);
}
