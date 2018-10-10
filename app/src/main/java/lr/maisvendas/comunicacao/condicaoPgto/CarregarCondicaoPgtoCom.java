package lr.maisvendas.comunicacao.condicaoPgto;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.CondicaoPgtoServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarCondicaoPgtoCom extends AsyncTask<String,Void,Response<List<CondicaoPagamento>>> {

        private CondicaoPgtoServico service;
        private CarregarCondicaoPgtoTaskCallBack delegate;
        private String TAG = "CarregarCondicaoPgtoCom";
        private Ferramentas ferramentas;

    public CarregarCondicaoPgtoCom(CarregarCondicaoPgtoTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(CondicaoPgtoServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<CondicaoPagamento>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            try {
                // Cria a chamada para o método
                Call<Response<List<CondicaoPagamento>>> carregarCondicaoPgtoCall = service.carregarCondicaoPgto(params[0],params[1]);

                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarCondicaoPgtoCall.execute().body();
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
        protected void onPostExecute(Response<List<CondicaoPagamento>> condicoesPgto) {

            if (condicoesPgto.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarCondicaoPgtoSuccess(condicoesPgto.getData());
            } else {
                delegate.onCarregarCondicaoPgtoFailure(condicoesPgto.getMessage());
            }
        }

        public interface CarregarCondicaoPgtoTaskCallBack {

            public void onCarregarCondicaoPgtoSuccess(List<CondicaoPagamento> condicoesPgto);
            public void onCarregarCondicaoPgtoFailure(String mensagem);
        }
}
