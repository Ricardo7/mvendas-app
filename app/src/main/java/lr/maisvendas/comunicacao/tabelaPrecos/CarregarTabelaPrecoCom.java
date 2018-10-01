package lr.maisvendas.comunicacao.tabelaPrecos;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.servico.TabelaPrecoServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarTabelaPrecoCom extends AsyncTask<String,Void,Response<List<TabelaPreco>>> {

        private TabelaPrecoServico service;
        private CarregarTabelaPrecoTaskCallBack delegate;
        private String TAG = "CarregarTabelaPrecoCom";
        private Ferramentas ferramentas;

    public CarregarTabelaPrecoCom(CarregarTabelaPrecoTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(TabelaPrecoServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<TabelaPreco>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<TabelaPreco>>> carregarTabelaPrecosCall = service.carregarTabelaPreco(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarTabelaPrecosCall.execute().body();
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
        protected void onPostExecute(Response<List<TabelaPreco>> tabelasPreco) {

            if (tabelasPreco.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarTabelaPrecoSuccess(tabelasPreco.getData());
            } else {
                delegate.onCarregarTabelaPrecoFailure(tabelasPreco.getMessage());
            }
        }

        public interface CarregarTabelaPrecoTaskCallBack {

            public void onCarregarTabelaPrecoSuccess(List<TabelaPreco> tabelasPreco);
            public void onCarregarTabelaPrecoFailure(String mensagem);
        }
}
