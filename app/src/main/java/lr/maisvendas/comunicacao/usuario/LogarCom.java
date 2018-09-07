package lr.maisvendas.comunicacao.usuario;

import android.os.AsyncTask;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.servico.UsuarioServico;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Ronaldo on 16/08/2017.
 */

public class LogarCom extends AsyncTask<String,Void,Response<Usuario>> {

    private UsuarioServico service;
    private LogarTaskCallBack delegate;
    private String TAG = "LogarCom";
    private Ferramentas ferramentas;

    public LogarCom(LogarTaskCallBack delegate) {
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
    protected Response<Usuario> doInBackground(String... loginRequests) {
        if (loginRequests.length == 0) {
            return null;
        }

        // Cria a chamada para o método
        Call<Response<Usuario>> logarAppCall = service.logarApp(loginRequests[0],loginRequests[1]);

        try {
            // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
            return logarAppCall.execute().body();
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
    protected void onPostExecute(Response<Usuario> response) {

        if (response.getStatus().toString().equals("SUCCESS")) {
            delegate.onLogarSuccess(response.getData());
        } else {
            ferramentas.customLog(TAG,response.getMessage());
            delegate.onLogarFailure();
        }
    }

    public interface LogarTaskCallBack {

        public void onLogarSuccess(Usuario usuario);
        public void onLogarFailure();
    }
}
