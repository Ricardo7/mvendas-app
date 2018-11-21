package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PedidoServico {

    @GET("Pedido/GetListaPedidosAtualizados") //Busca
    public Call<Response<List<Pedido>>> carregarPedido(@Header("Authorization") String token, @Query("dataAt") String dataAt, @Query("usuarioID") String usuarioIDWS);

    @POST("Pedido/AddPedido") //Insere
    public Call<Response<Pedido>>criarPedido(@Header("Authorization") String token, @Body Pedido pedido);

    @PUT("Pedido/EditaPedido") //Atualiza
    public Call<Response<Pedido>>editarPedido(@Header("Authorization") String token, @Query("IDWS") String idWS, @Body Pedido pedido);

    @DELETE("Pedido/DeletePedido") //Deleta
    public Call<Response<Boolean>>excluirPedido(@Header("Authorization") String token, @Query("IDWS") Integer id);
}
