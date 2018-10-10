package lr.maisvendas.comunicacao.atividade;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.AtividadeServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarAtividadeSugeridaCom extends AsyncTask<String,Void,Response<List<Atividade>>> {

        private AtividadeServico service;
        private CarregarAtividadeSugeridaTaskCallBack delegate;
        private String TAG = "CarregarAtividadeCom";
        private Ferramentas ferramentas;

    public CarregarAtividadeSugeridaCom(CarregarAtividadeSugeridaTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(AtividadeServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Atividade>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Atividade>>> carregarAtividadeSugeridaCall = service.carregarAtividadesSugeridas(params[0],params[1],params[2]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarAtividadeSugeridaCall.execute().body();
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
        protected void onPostExecute(Response<List<Atividade>> Atividade) {

            if (Atividade.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarAtividadeSugeridaSuccess(Atividade.getData());
            } else {
                delegate.onCarregarAtividadeSugeridaFailure(Atividade.getMessage());
            }
        }

        public interface CarregarAtividadeSugeridaTaskCallBack {

            public void onCarregarAtividadeSugeridaSuccess(List<Atividade> Atividades);
            public void onCarregarAtividadeSugeridaFailure(String mensagem);
        }
}
