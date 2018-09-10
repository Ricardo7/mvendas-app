package lr.maisvendas.comunicacao.cliente;

import android.os.AsyncTask;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.ResponseStatus;
import lr.maisvendas.servico.ClienteServico;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Ferramentas;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CadastrarClienteCom extends AsyncTask<Cliente,Void,Response<Cliente>> {

    private ClienteServico service;
    private CadastrarClienteTaskCallBack delegate;
    private String TAG = "CadastrarClienteTask";
    private Ferramentas ferramentas;

    public CadastrarClienteCom(CadastrarClienteTaskCallBack delegate) {
        EnderecoHost enderecoHost = new EnderecoHost();
        // Configura o retrofit através da baseUrl e do conversor que irá transformar o resultado de JSON para Java
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(enderecoHost.getHostHTTP())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Solicita para que o Retrofit crie uma classe responsável pelo serviço
        this.service = retrofit.create(ClienteServico.class);

        // Armazena o delegate para chamar informando sucesso ou falha
        this.delegate = delegate;

        ferramentas = new Ferramentas();
    }

    @Override
    protected Response<Cliente> doInBackground(Cliente... cliente) {
        //==DEVE ser testado o funcionamento, foi utilizado dessa forma pois não é possível passar o token por parâmetro
        String token = BaseActivity.getUsuario().getToken();

        if (cliente.length == 0) {
            return null;
        }

        // Cria a chamada para o método
        Call<Response<Cliente>> criarClienteCall = service.criarCliente(token,cliente[0]);

        try {
            // Executa a chamada e pega o retorno para devolver para a task no método "onPostExecute"
            return criarClienteCall.execute().body();
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
    protected void onPostExecute(Response<Cliente> cliente) {

        if (cliente.getStatus().toString().equals("SUCCESS")) {
            delegate.onCadastrarClienteSuccess(cliente.getData());
        } else {
            delegate.onCadastrarClienteFailure(cliente.getMessage());
        }
    }

    public interface CadastrarClienteTaskCallBack {

        public void onCadastrarClienteSuccess(Cliente clientes);
        public void onCadastrarClienteFailure(String mensagem);
    }
}
