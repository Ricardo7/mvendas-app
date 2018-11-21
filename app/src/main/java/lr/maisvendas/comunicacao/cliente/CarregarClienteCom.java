package lr.maisvendas.comunicacao.cliente;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.ClienteServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarClienteCom extends AsyncTask<String,Void,Response<List<Cliente>>> {

        private ClienteServico service;
        private CarregarClienteTaskCallBack delegate;
        private String TAG = "CarregarClienteCom";
        private Ferramentas ferramentas;

    public CarregarClienteCom(CarregarClienteTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(ClienteServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Cliente>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Cliente>>> carregarClienteCall = service.carregarCliente(params[0],params[1],params[2]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarClienteCall.execute().body();
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
        protected void onPostExecute(Response<List<Cliente>> cliente) {

            if (cliente.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarClienteSuccess(cliente.getData());
            } else {
                delegate.onCarregarClienteFailure(cliente.getMessage());
            }
        }

        public interface CarregarClienteTaskCallBack {

            public void onCarregarClienteSuccess(List<Cliente> clientes);
            public void onCarregarClienteFailure(String mensagem);
        }
}
