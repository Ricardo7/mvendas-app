package lr.maisvendas.servico;

import java.util.List;

import lr.maisvendas.modelo.Produto;
import lr.maisvendas.modelo.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ProdutoServico {

    @GET("Produto/GetListaProdutosAtualizados") //Busca
    public Call<Response<List<Produto>>> carregarProduto(@Header("Authorization") String token, @Query("dataAt") String dataAt);

    @GET("Produto/GetListaProdutosSugeridos") //Busca
    public Call<Response<List<Produto>>> carregarProdutosSugeridos(@Header("Authorization") String token, @Query("clienteIDWS") String clienteIDWS, @Query("produtoIDWS") String produtoIDWS);

    @PUT("Produto") //Insere (não utilizado no produto)
    public Call<Response<Produto>>AddProduto(@Header("Authorization") String token, @Body Produto produto);

    @POST("Produto/{id}/") //Atualiza (não utilizado no produto)
    public Call<Response<Produto>>editarProduto(@Header("Authorization") String token, @Query("id") Integer id, @Body Produto produto);

    @DELETE("Produto/{id}/") //Deleta (não utilizado no produto)
    public Call<Response<Boolean>>excluirProduto(@Header("Authorization") String token, @Query("id") Integer id);
}
