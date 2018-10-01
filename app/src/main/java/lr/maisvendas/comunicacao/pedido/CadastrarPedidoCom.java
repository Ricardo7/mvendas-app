package lr.maisvendas.comunicacao.pedido;

import android.os.AsyncTask;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.PedidoServico;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CadastrarPedidoCom extends AsyncTask<Pedido,Void,Response<Pedido>> {

    private PedidoServico service;
    private CadastrarPedidoTaskCallBack delegate;
    private String TAG = "CadastrarPedidoTask";
    private Ferramentas ferramentas;

    public CadastrarPedidoCom(CadastrarPedidoTaskCallBack delegate) {
        EnderecoHost enderecoHost = new EnderecoHost();
        // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(enderecoHost.getHostHTTP())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Solicita para que o Retrofit crie uma classe responsável pelo serviço
        this.service = retrofit.create(PedidoServico.class);

        // Armazena o delegate para chamar informando sucesso ou falha
        this.delegate = delegate;

        ferramentas = new Ferramentas();
    }

    @Override
    protected Response<Pedido> doInBackground(Pedido... pedido) {
        //==DEVE ser testado o funcionamento, foi utilizado dessa forma pois não é possível passar o token por parâmetro
        String token = BaseActivity.getUsuario().getToken();

        if (pedido.length == 0) {
            return null;
        }

        // Cria a chamada para o método
        Call<Response<Pedido>> criarPedidoCall = service.criarPedido(token,pedido[0]);

        try {
            // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
            return criarPedidoCall.execute().body();
        } catch (Exception e) {
            Response response = new Response();
            response.setCod(500);
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage(e.getMessage());
            response.setData(null);
            return response;
        }
    }

    @Override
    protected void onPostExecute(Response<Pedido> pedido) {

        if (pedido.getStatus().toString().equals("SUCCESS")) {
            delegate.onCadastrarPedidoSuccess(pedido.getData());
        } else {
            delegate.onCadastrarPedidoFailure(pedido.getMessage());
        }
    }

    public interface CadastrarPedidoTaskCallBack {

        public void onCadastrarPedidoSuccess(Pedido pedidos);
        public void onCadastrarPedidoFailure(String mensagem);
    }
}
