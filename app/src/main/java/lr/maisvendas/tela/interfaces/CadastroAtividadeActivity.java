package lr.maisvendas.tela.interfaces;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.repositorio.sql.AtividadeDAO;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.tela.adaptador.ListaClientesSpinnerAdapter;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoAgenda;

public class CadastroAtividadeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CadastroAtividadeActivity";
    public static final String PARAM_ATIVIDADE = "PARAM_ATIVIDADE";

    //Campos da tela
    private EditText editAssunto;
    private Spinner spinnerCliente;
    private EditText editObservacao;
    private EditText editData;
    private EditText editHora;
    private Button buttonSalvar;
    private TextView textSugestao;

    //Variáveis
    private Atividade atividade;
    private ListaClientesSpinnerAdapter listaClientesSpinnerAdapter;
    private List<Cliente> listaClientes;
    private Ferramentas ferramentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cadastro_atividade);
        setDrawerLayoutId(R.id.activity_cadastro_atividade_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Agenda");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_retornar);

        editAssunto = (EditText) findViewById(R.id.activity_cadastro_atividade_assunto);
        spinnerCliente = (Spinner) findViewById(R.id.activity_cadastro_atividade_cliente);
        editObservacao = (EditText) findViewById(R.id.activity_cadastro_atividade_observacao);
        editData = (EditText) findViewById(R.id.activity_cadastro_atividade_data);
        editHora = (EditText) findViewById(R.id.activity_cadastro_atividade_hora);
        textSugestao = (TextView) findViewById(R.id.activity_cadastro_atividade_text_sugestao);
        buttonSalvar = (Button) findViewById(R.id.activity_cadastro_atividade_button_salvar);
        ferramentas = new Ferramentas();

        loadDataFromActivity();

        buttonSalvar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSalvar){
            try {
                salvaDados();
            } catch (Exceptions ex) {
                ferramentas.customToast(this,ex.getMessage());
            }
        }
    }

    private void loadDataFromActivity() {
        atividade = (Atividade) getIntent().getSerializableExtra(PARAM_ATIVIDADE);

        //Popula Spinner de clientes
        Cliente cliente = new Cliente();
        cliente.setCnpj("");
        cliente.setRazaoSocial("Selecione");

        ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
        listaClientes = clienteDAO.buscaClientes();
        listaClientes.add(0,cliente);
        listaClientesSpinnerAdapter = new ListaClientesSpinnerAdapter(this,listaClientes);
        spinnerCliente.setAdapter(listaClientesSpinnerAdapter);

        if (atividade != null){
            editAssunto.setText(atividade.getAssunto());
            editObservacao.setText(atividade.getObservacao());
            editData.setText(ferramentas.formatDateUser(atividade.getDataAtividade()));
            editHora.setText(atividade.getHoraAtividade());
            spinnerCliente.setSelection(listaClientesSpinnerAdapter.getPosition(atividade.getCliente()));
            if(atividade.getTipo().equals(TipoAgenda.SUGESTAO)){
                textSugestao.setText("Esta atividade foi sugerida para este dia, pois é um ótimo momento para visitar o cliente "+atividade.getCliente().getRazaoSocial()+".\nBoas Vendas!");
            }
        }
    }

    private void salvaDados() throws Exceptions {
        if (editAssunto.getText().toString().equals("")) {
            throw new Exceptions("Assunto não informado.");
        } else if (spinnerCliente.getSelectedItem() == null) {
            throw new Exceptions("Cliente não selecionado.");
        } else if (editData.getText().toString().equals("")) {
            throw new Exceptions("Data não informada.");
        } else if (!ferramentas.isDateValid(editData.getText().toString())){
            throw new Exceptions("A Data informada não é válida.");
        } else if (editHora.getText().toString().equals("")){
            throw new Exceptions("Hora não informada.");
        }

        AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);

        Atividade atividadeTst = atividadeDAO.buscaAtividadeDataHora(editData.getText().toString(),editHora.getText().toString());
        if (atividadeTst != null){
            throw  new Exceptions("Já existe uma atividade neste dia e horário");
        }

        if (atividade == null) {
            atividade = new Atividade();
            atividade.setId(null);
            atividade.setDtCadastro(ferramentas.getCurrentDate());
        }

        atividade.setAssunto(editAssunto.getText().toString());
        atividade.setCliente((Cliente) spinnerCliente.getSelectedItem());
        atividade.setObservacao(editObservacao.getText().toString());
        atividade.setDataAtividade(ferramentas.formatDateBD(editData.getText().toString()));
        atividade.setHoraAtividade(editHora.getText().toString());
        atividade.setUsuario(getUsuario());
        atividade.setDtAtualizacao(ferramentas.getCurrentDate());

        atividadeDAO.insereAtividade(atividade);

        if(atividade.getId() == null || atividade.getId() <= 0) {
            //Insere o produto na base de dados
            atividadeDAO.insereAtividade(atividade);
        }else {
            //Atualiza o produto na base de dados
            atividadeDAO.atualizaAtividade(atividade);
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }

}
