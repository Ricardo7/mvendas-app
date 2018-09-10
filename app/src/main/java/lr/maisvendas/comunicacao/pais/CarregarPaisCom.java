package lr.maisvendas.comunicacao.pais;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Pais;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.PaisServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarPaisCom extends AsyncTask<String,Void,Response<List<Pais>>> {

        private PaisServico service;
        private CarregarPaisTaskCallBack delegate;
        private String TAG = "CarregarPaisCom";
        private Ferramentas ferramentas;

    public CarregarPaisCom(CarregarPaisTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(PaisServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Pais>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Pais>>> carregarPaisCall = service.carregarPais(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarPaisCall.execute().body();
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
        protected void onPostExecute(Response<List<Pais>> pais) {

            if (pais.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarPaisSuccess(pais.getData());
            } else {
                delegate.onCarregarPaisFailure(pais.getMessage(),pais.getCod());
            }
        }

        public interface CarregarPaisTaskCallBack {

            public void onCarregarPaisSuccess(List<Pais> paises);
            public void onCarregarPaisFailure(String mensagem, Integer codigo);
        }
}
