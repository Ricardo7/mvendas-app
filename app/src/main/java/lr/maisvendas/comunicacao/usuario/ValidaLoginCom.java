package lr.maisvendas.comunicacao.usuario;

import android.os.AsyncTask;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.UsuarioServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Ronaldo on 10/08/2017.
 */
public class ValidaLoginCom extends AsyncTask<String,Void,Response<Boolean>>{

    private UsuarioServico service;
    private VerificaLoginTaskCallBack delegate;
    private String TAG = "ValidaLoginCom";
    private Ferramentas ferramentas;

    public ValidaLoginCom(VerificaLoginTaskCallBack delegate) {
        EnderecoHost enderecoHost = new EnderecoHost();
        // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(enderecoHost.getHostHTTP())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Solicita para que o Retrofit crie uma classe responsável pelo serviço
        this.service = retrofit.create(UsuarioServico.class);

        // Armazena o delegate para chamar informando sucesso ou falha
        this.delegate = delegate;

        ferramentas = new Ferramentas();
    }
    @Override
    protected Response<Boolean> doInBackground(String... token) {
        Response<Boolean> response;
        if (token.length == 0) {
            new Exception("Token não populado");
        }

        // Cria a chamada para o método
        Call<Response<Boolean>> verificaLoginCall = service.verificaToken(token[0]);


        try {
            // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"

            response = verificaLoginCall.execute().body();

            if (response == null){
                new Exception("Não foi possível acessar o servidor");
            }

        } catch (Exception e) {
            response = new Response();
            response.setCod(500);
            response.setStatus(ResponseStatus.ERROR);
            response.setMessage(e.getMessage());
            response.setData(null);
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response response) {

        if (response.getStatus().toString().equals("SUCCESS")) {
            delegate.onVerificaLoginSuccess(true);
        } else {
            ferramentas.customLog(TAG,response.getMessage());
            delegate.onVerificaLoginFailure();
        }
    }

    public interface VerificaLoginTaskCallBack {

        public void onVerificaLoginSuccess(Boolean retorno);
        public void onVerificaLoginFailure();
    }
}
