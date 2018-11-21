package lr.maisvendas.comunicacao.pedido;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.PedidoServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarPedidoCom extends AsyncTask<String,Void,Response<List<Pedido>>> {

        private PedidoServico service;
        private CarregarPedidoTaskCallBack delegate;
        private String TAG = "CarregarPedidoCom";
        private Ferramentas ferramentas;

    public CarregarPedidoCom(CarregarPedidoTaskCallBack delegate) {
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
        protected Response<List<Pedido>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Pedido>>> carregarPedidoCall = service.carregarPedido(params[0],params[1],params[2]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarPedidoCall.execute().body();
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
        protected void onPostExecute(Response<List<Pedido>> pedido) {

            if (pedido.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarPedidoSuccess(pedido.getData());
            } else {
                delegate.onCarregarPedidoFailure(pedido.getMessage());
            }
        }

        public interface CarregarPedidoTaskCallBack {

            public void onCarregarPedidoSuccess(List<Pedido> pedidos);
            public void onCarregarPedidoFailure(String mensagem);
        }
}
