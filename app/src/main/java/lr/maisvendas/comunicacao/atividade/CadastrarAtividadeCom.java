package lr.maisvendas.comunicacao.atividade;

import android.os.AsyncTask;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.AtividadeServico;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CadastrarAtividadeCom extends AsyncTask<Atividade,Void,Response<Atividade>> {

    private AtividadeServico service;
    private CadastrarAtividadeTaskCallBack delegate;
    private String TAG = "CadastrarAtividadeTask";
    private Ferramentas ferramentas;

    public CadastrarAtividadeCom(CadastrarAtividadeTaskCallBack delegate) {
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
    protected Response<Atividade> doInBackground(Atividade... Atividade) {
        //==DEVE ser testado o funcionamento, foi utilizado dessa forma pois não é possível passar o token por parâmetro
        String token = BaseActivity.getUsuario().getToken();

        if (Atividade.length == 0) {
            return null;
        }

        // Cria a chamada para o método
        Call<Response<Atividade>> criarAtividadeCall = service.criarAtividade(token,Atividade[0]);

        try {
            // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
            return criarAtividadeCall.execute().body();
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
    protected void onPostExecute(Response<Atividade> Atividade) {

        if (Atividade.getStatus().toString().equals("SUCCESS")) {
            delegate.onCadastrarAtividadeSuccess(Atividade.getData());
        } else {
            delegate.onCadastrarAtividadeFailure(Atividade.getMessage());
        }
    }

    public interface CadastrarAtividadeTaskCallBack {

        public void onCadastrarAtividadeSuccess(Atividade Atividades);
        public void onCadastrarAtividadeFailure(String mensagem);
    }
}
