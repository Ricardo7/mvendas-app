package lr.maisvendas.comunicacao.cidade;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.CidadeServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarCidadeCom extends AsyncTask<String,Void,Response<List<Cidade>>> {

        private CidadeServico service;
        private CarregarCidadeTaskCallBack delegate;
        private String TAG = "CarregarCidadeCom";
        private Ferramentas ferramentas;

    public CarregarCidadeCom(CarregarCidadeTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(CidadeServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Cidade>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Cidade>>> carregarCidadeCall = service.carregarCidade(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarCidadeCall.execute().body();
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
        protected void onPostExecute(Response<List<Cidade>> cidade) {

            if (cidade.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarCidadeSuccess(cidade.getData());
            } else {
                delegate.onCarregarCidadeFailure(cidade.getMessage());
            }
        }

        public interface CarregarCidadeTaskCallBack {

            public void onCarregarCidadeSuccess(List<Cidade> cidades);
            public void onCarregarCidadeFailure(String mensagem);
        }
}
