package lr.maisvendas.comunicacao.estado;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.EstadoServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarEstadoCom extends AsyncTask<String,Void,Response<List<Estado>>> {

        private EstadoServico service;
        private CarregarEstadoTaskCallBack delegate;
        private String TAG = "CarregarEstadoCom";
        private Ferramentas ferramentas;

    public CarregarEstadoCom(CarregarEstadoTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(EstadoServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Estado>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Estado>>> carregarEstadoCall = service.carregarEstado(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarEstadoCall.execute().body();
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
        protected void onPostExecute(Response<List<Estado>> estado) {

            if (estado.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarEstadoSuccess(estado.getData());
            } else {
                delegate.onCarregarEstadoFailure(estado.getMessage());
            }
        }

        public interface CarregarEstadoTaskCallBack {

            public void onCarregarEstadoSuccess(List<Estado> estados);
            public void onCarregarEstadoFailure(String mensagem);
        }
}
