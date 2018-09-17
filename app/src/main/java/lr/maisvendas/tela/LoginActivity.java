package lr.maisvendas.tela;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lr.maisvendas.R;
import lr.maisvendas.comunicacao.usuario.LogarCom;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.sql.UsuarioDAO;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LogarCom.LogarTaskCallBack {

    private EditText editUsuario;
    private EditText editSenha;
    private Button btnLogar;
    private ProgressDialog progressDialog;
    private Ferramentas ferramentas;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ferramentas = new Ferramentas();

        editUsuario = (EditText) findViewById(R.id.activity_login_edit_nome);
        editSenha = (EditText) findViewById(R.id.activity_login_edit_senha);
        btnLogar = (Button) findViewById(R.id.activity_login_button_login);

        btnLogar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Ferramentas ferramentas = new Ferramentas();
        if (v == btnLogar) {
            //<TESTE>
            //Chama activity principal
            //Intent intent = new Intent(this, TelaInicialActivity.class);
            //--intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
            //startActivity(intent);
            //</TESTE>

            String usuario = editUsuario.getText().toString();
            String senha = editSenha.getText().toString();
            String md5Hash = ferramentas.getMd5Hash(senha);

            ferramentas.customLog(TAG,md5Hash);

            if (usuario.isEmpty() || usuario.equals("") || senha.isEmpty() || senha.equals("")) {
                ferramentas.customToast(this, "Usuário ou senha não informados.");
            } else if (senha.length() < 8) {
                ferramentas.customToast(this, "Senha deve ter no mínimo 8 caracteres.");
            } else {
                VerificaConexao verificaConexao = new VerificaConexao();

                if (verificaConexao.isNetworkAvailable(this)) {
                    progressDialog = ProgressDialog.show(this, "Aguarde", "Efetuando login, por favor aguarde.", true, false);
                    new LogarCom(this).execute(usuario, md5Hash);
                } else {
                    ferramentas.customToast(this, "Não foi possível conectar ao servidor, tente novamente mais tarde.");
                }
            }


        }
    }

    @Override
    public void onLogarSuccess(Usuario usuario) {
        progressDialog.dismiss();

        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);

        //Só pode existir um usuário interno no aplicativo, desta forma busca pelo mesmo método do Token
        Usuario usuarioOld = usuarioDAO.buscaUsuarioLoginToken();

        //Se usuário já existe irá atualizar, senão irá inserir
        if (usuarioOld != null) {
            try {
                usuario.setId(usuarioOld.getId());
                usuarioDAO.atualizaUsuario(usuario);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG,ex.getMessage());
            }
        }else{
            usuarioDAO.insereUsuario(usuario);
        }
        //Chama activity principal
        Intent intent = new Intent(this, TelaInicialActivity.class);
        //intent.putExtra(TelaInicialActivity.PARAM_USUARIO, usuario);
        startActivity(intent);
    }

    @Override
    public void onLogarFailure() {
        progressDialog.dismiss();
        ferramentas.customToast(this, "Não foi possível efetuar o login, verifique os dados informados");
    }
}
