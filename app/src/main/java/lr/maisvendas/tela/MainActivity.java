package lr.maisvendas.tela;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import lr.maisvendas.R;
import lr.maisvendas.comunicacao.usuario.ValidaLoginCom;
import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.repositorio.sql.UsuarioDAO;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.servico.VerificaServico;
import lr.maisvendas.tmp.PopulaDadosTeste;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;

public class MainActivity extends AppCompatActivity implements ValidaLoginCom.VerificaLoginTaskCallBack {

    public static final String TAG = "MainActivity";
    public static SQLiteDatabase sqlLite;
    private Usuario usuario;
    private Ferramentas ferramentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ferramentas = new Ferramentas();

        sqlLite = new DatabaseHelper(this).getReadableDatabase();

        criarBanco();

        //<USADO SOMENTE PARA TESTES>
        PopulaDadosTeste populaDadosTeste = new PopulaDadosTeste();
        populaDadosTeste.populaPedido(this);
        //</USADO SOMENTE PARA TESTES>

        verificaLogin();
        //Intent intent = new Intent(this, TelaInicialActivity.class);
        //intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
        //startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, TelaInicialActivity.class);
        //intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
        startActivity(intent);
    }

    public static void criarBanco() {

        try {
            DatabaseHelper banco = new DatabaseHelper(null);
            //banco.onUpgrade(sqlLite,12,13);
            // Cria o banco de dados caso não exista
            banco.onCreate(sqlLite);

        } catch (Exception e) {
            Log.v(TAG,"Erro ao criar banco de dados (" + e.getMessage() + ")");
            //arq.WriteFile("Controler->criarBanco: Erro ao criar banco de dados (" + e + ")");
        }
    }

    public void verificaLogin() {
        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);

        usuario = usuarioDAO.buscaUsuarioLoginToken();

        if(usuario == null || usuario.getToken() == null || usuario.getToken().equals("")){
            Intent intent = new Intent(this,LoginActivity.class);
            //Intent intent = new Intent(this, ValidadesActivity.class);
            startActivity(intent);
        }else {
            //Verifica se está conectado a internet
            VerificaConexao verificaConexao = new VerificaConexao();
            //Verifica se o servidor está no ar
            VerificaServico verificaServico = new VerificaServico();
            EnderecoHost enderecoHost = new EnderecoHost();
            try {
                if (verificaConexao.isNetworkAvailable(this) == true && verificaServico.execute(enderecoHost.getHostHTTPRaiz()).get() == true) {

                    new ValidaLoginCom(this).execute(usuario.getToken());

                } else {
                    Intent intent = new Intent(this, TelaInicialActivity.class);
                    //intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
                    startActivity(intent);
                }
            }catch (Exception ex){
                ferramentas.customLog(TAG,ex.getMessage());
            }
        }
    }

    @Override
    public void onVerificaLoginSuccess(Boolean retorno) {

        Intent intent = new Intent(this, TelaInicialActivity.class);
        //intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
        startActivity(intent);
    }

    @Override
    public void onVerificaLoginFailure() {

        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);
        if (usuario != null) {
            usuario.setToken(null);
            try {
                usuarioDAO.atualizaUsuario(usuario);
            }catch (Exceptions ex){
                ferramentas.customLog(TAG,ex.getMessage());
            }

            //loginDAO.trataLogin(login);
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
