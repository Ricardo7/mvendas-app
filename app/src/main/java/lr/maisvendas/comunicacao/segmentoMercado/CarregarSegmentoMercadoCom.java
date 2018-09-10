package lr.maisvendas.comunicacao.segmentoMercado;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.SegmentoMercadoServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarSegmentoMercadoCom extends AsyncTask<String,Void,Response<List<SegmentoMercado>>> {

        private SegmentoMercadoServico service;
        private CarregarSegmentoMercadoTaskCallBack delegate;
        private String TAG = "CarregarSegmentoMercadoCom";
        private Ferramentas ferramentas;

    public CarregarSegmentoMercadoCom(CarregarSegmentoMercadoTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(SegmentoMercadoServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<SegmentoMercado>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<SegmentoMercado>>> carregarSegmentoMercadoCall = service.carregarSegmentoMercado(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarSegmentoMercadoCall.execute().body();
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
        protected void onPostExecute(Response<List<SegmentoMercado>> segmentoMercado) {

            if (segmentoMercado.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarSegmentoMercadoSuccess(segmentoMercado.getData());
            } else {
                delegate.onCarregarSegmentoMercadoFailure(segmentoMercado.getMessage());
            }
        }

        public interface CarregarSegmentoMercadoTaskCallBack {

            public void onCarregarSegmentoMercadoSuccess(List<SegmentoMercado> segmentoMercados);
            public void onCarregarSegmentoMercadoFailure(String mensagem);
        }
}
