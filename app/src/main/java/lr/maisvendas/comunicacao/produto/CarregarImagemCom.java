package lr.maisvendas.comunicacao.produto;

import android.os.AsyncTask;

import java.util.List;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.ImagemServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CarregarImagemCom extends AsyncTask<String,Void,Response<List<Imagem>>> {

        private ImagemServico service;
        private CarregarImagemTaskCallBack delegate;
        private String TAG = "CarregarImagemCom";
        private Ferramentas ferramentas;

    public CarregarImagemCom(CarregarImagemTaskCallBack delegate) {
            EnderecoHost enderecoHost = new EnderecoHost();
            // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(enderecoHost.getHostHTTP())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Solicita para que o Retrofit crie uma classe responsável pelo serviço
            this.service = retrofit.create(ImagemServico.class);

            // Armazena o delegate para chamar informando sucesso ou falha
            this.delegate = delegate;

            ferramentas = new Ferramentas();
        }

        @Override
        protected Response<List<Imagem>> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // Cria a chamada para o método
            Call<Response<List<Imagem>>> carregarImagemCall = service.carregarImagem(params[0],params[1]);

            try {
                // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
                return carregarImagemCall.execute().body();
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
        protected void onPostExecute(Response<List<Imagem>> imagem) {

            if (imagem.getStatus().toString().equals("SUCCESS")) {
                delegate.onCarregarImagemSuccess(imagem.getData());
            } else {
                delegate.onCarregarImagemFailure(imagem.getMessage());
            }
        }

        public interface CarregarImagemTaskCallBack {

            public void onCarregarImagemSuccess(List<Imagem> imagems);
            public void onCarregarImagemFailure(String mensagem);
        }
}
