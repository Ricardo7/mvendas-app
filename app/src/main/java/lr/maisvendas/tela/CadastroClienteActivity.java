package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.modelo.Pais;
import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.repositorio.sql.CidadeDAO;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.repositorio.sql.EstadoDAO;
import lr.maisvendas.repositorio.sql.PaisDAO;
import lr.maisvendas.repositorio.sql.SegmentoMercadoDAO;
import lr.maisvendas.tela.adaptador.ListaCidadesSpinnerAdapter;
import lr.maisvendas.tela.adaptador.ListaEstadosSpinnerAdapter;
import lr.maisvendas.tela.adaptador.ListaSegmentoMercadoSpinnerAdapter;
import lr.maisvendas.utilitarios.EditTextMask;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoMask;
import lr.maisvendas.utilitarios.TipoPessoa;
import lr.maisvendas.utilitarios.ValidaCampos;

public class CadastroClienteActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "CadastroClienteActivity";
    public static final String PARAM_CLIENTE = "PARAM_CLIENTE";

    //Campos da tela
    private Spinner spinnerSegmentoMercado;
    private CheckBox checkAtivo;
    private EditText editRazSocial;
    private EditText editNomeFant;
    private EditText editCnpj;
    private EditText editInsEst;
    private EditText editEmail;
    private EditText editFone;
    private EditText editCep;
    private Spinner spinnerEstado;
    private Spinner spinnerCidade;
    private EditText editBairro;
    private EditText editLogradouro;
    private EditText editNumero;
    private Button buttonCadastrar;

    //Variáveis
    private List<SegmentoMercado> segmentosMercado;
    private List<Estado> listaEstados;
    private List<Cidade> listaCidades;
    private Cliente cliente;
    private Ferramentas ferramentas;
    private ListaSegmentoMercadoSpinnerAdapter listaSegmentoMercadoSpinnerAdapter;
    private ListaEstadosSpinnerAdapter listaEstadosSpinnerAdapter;
    private ListaCidadesSpinnerAdapter listaCidadesSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cadastro_cliente);
        setDrawerLayoutId(R.id.activity_cadastro_cliente_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        spinnerSegmentoMercado = (Spinner) findViewById(R.id.activity_cadastro_cliente_spinner_segmento_mercado);
        checkAtivo = (CheckBox) findViewById(R.id.activity_cadastro_cliente_check_ativo);
        editRazSocial = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_raz_social);
        editNomeFant = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_nome_fant);
        editCnpj = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_cnpj);
        editInsEst = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_insc_est);
        editEmail = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_email);
        editFone = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_fone);
        editCep = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_cep);
        spinnerEstado = (Spinner) findViewById(R.id.activity_cadastro_cliente_spinner_estado3);
        spinnerCidade = (Spinner) findViewById(R.id.activity_cadastro_cliente_spinner_cidade);
        editBairro = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_bairro);
        editLogradouro = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_logradouro);
        editNumero = (EditText) findViewById(R.id.activity_cadastro_cliente_edit_numero);
        buttonCadastrar = (Button) findViewById(R.id.activity_cadastro_cliente_button_salvar);

        //Seta o gerador de máscaras
        editCnpj.addTextChangedListener(EditTextMask.insert(editCnpj, TipoMask.CNPJ));
        editFone.addTextChangedListener(EditTextMask.insert(editFone, TipoMask.FONE));
        editCep.addTextChangedListener(EditTextMask.insert(editCep, TipoMask.CEP));

        ferramentas = new Ferramentas();
        listaEstados = new ArrayList<>();

        loadDataFromActivity();

        buttonCadastrar.setOnClickListener(this);
        spinnerEstado.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCadastrar){
            try {
                salvaDados();
            } catch (Exceptions ex) {
                ferramentas.customToast(this,ex.getMessage());
            }
        }
    }

    //Metodo para carregar informação ao abriar a Activity.
    public void loadDataFromActivity() {
        cliente = (Cliente) getIntent().getSerializableExtra(PARAM_CLIENTE);
        //Objeto para setar o primeiro item do spinner com default
        SegmentoMercado segmentoMercado = new SegmentoMercado();
        segmentoMercado.setId(0);
        segmentoMercado.setDescricao("Selecione");

        SegmentoMercadoDAO segmentoMercadoDAO = SegmentoMercadoDAO.getInstance(this);
        segmentosMercado = segmentoMercadoDAO.buscaSegmentosMercado();
        segmentosMercado.add(0,segmentoMercado);
        if (segmentosMercado != null){
            listaSegmentoMercadoSpinnerAdapter = new ListaSegmentoMercadoSpinnerAdapter(this,segmentosMercado);
            spinnerSegmentoMercado.setAdapter(listaSegmentoMercadoSpinnerAdapter);
        }

        //Objeto para setar o primeiro item do spinner com default
        Estado estado = new Estado();
        estado.setId(0);
        estado.setSigla("");
        estado.setDescricao("Selecione");

        //Inicialmente o pais será setado como Default Brasil
        PaisDAO paisDAO = PaisDAO.getInstance(this);
        Pais pais = paisDAO.buscaPaisSigla("BR");

        if (pais != null) {
            //Popula o spinner de estados
            EstadoDAO estadoDAO = EstadoDAO.getInstance(this);
            listaEstados = estadoDAO.buscaEstadoPais(pais.getId());

        }

        listaEstados.add(0,estado);
        if (listaEstados.isEmpty() == false) {
            listaEstadosSpinnerAdapter = new ListaEstadosSpinnerAdapter(this, listaEstados);
            spinnerEstado.setAdapter(listaEstadosSpinnerAdapter);

        }

        if (cliente != null){

            spinnerSegmentoMercado.setSelection(listaSegmentoMercadoSpinnerAdapter.getPosition(cliente.getSegmentoMercado()));
            editRazSocial.setText(cliente.getRazaoSocial());
            editNomeFant.setText(cliente.getNomeFantasia());
            editCnpj.setText(ferramentas.formatCnpjCpf(cliente.getCnpj(), TipoPessoa.juridica));
            editInsEst.setText(cliente.getInscricaoEstadual());
            editEmail.setText(cliente.getEmail());
            editFone.setText(cliente.getFone());
            editCep.setText(cliente.getCep().toString());

            //Busca o estado a partir da Cidade
            estado = cliente.getCidade().getEstado();
            //Objeto para setar o primeiro item do spinner com default
            Cidade cidade = new Cidade();
            cidade.setId(0);
            cidade.setSigla("");
            cidade.setDescricao("Selecione");

            //Popula o spinner de cidades de acordo com estado selecionado
            CidadeDAO cidadeDAO = CidadeDAO.getInstance(this);
            listaCidades = cidadeDAO.buscaCidadeEstado(estado.getId());
            listaCidades.add(0,cidade);
            if (listaCidades != null) {
                listaCidadesSpinnerAdapter = new ListaCidadesSpinnerAdapter(this, listaCidades);
                spinnerCidade.setAdapter(listaCidadesSpinnerAdapter);
            }

            spinnerEstado.setSelection(listaEstadosSpinnerAdapter.getPosition(cliente.getCidade().getEstado()));
            spinnerCidade.setSelection(listaCidadesSpinnerAdapter.getPosition(cliente.getCidade()));
            editBairro.setText(cliente.getBairro());
            editLogradouro.setText(cliente.getLogradouro());
            editNumero.setText(cliente.getNumero().toString());

            if (cliente.getStatus() == 1) {
                checkAtivo.setChecked(true);
            } else {
                checkAtivo.setChecked(false);
            }
        }else {
            checkAtivo.setChecked(true);
        }

    }

    public void salvaDados() throws Exceptions {
        if (spinnerSegmentoMercado.getSelectedItem() == null || ((SegmentoMercado)spinnerSegmentoMercado.getSelectedItem()).getId() == 0) {
            throw new Exceptions("Segmento de mercado não selecionado.");
        } else if (editRazSocial.getText().toString().equals("")) {
            throw new Exceptions("Razão Social não informada.");
        } else if (editCnpj.getText().toString().equals("")){
            throw new Exceptions("CNPJ não informada.");
        } else if (editEmail.getText().toString().equals("")){
            throw new Exceptions("Email não informada.");
        } else if (editFone.getText().toString().equals("")){
            throw new Exceptions("Fone não informada.");
        } else if (editCep.getText().toString().equals("")){
            throw new Exceptions("CEP não informada.");
        } else if (spinnerEstado.getSelectedItem() == null || ((Estado) spinnerEstado.getSelectedItem()).getId() == 0){
            throw new Exceptions("Estado não selecionado.");
        } else if (spinnerCidade.getSelectedItem() == null || ((Cidade) spinnerCidade.getSelectedItem()).getId() == 0){
            throw new Exceptions("Cidade não selecionada.");
        } else if (editBairro.getText().toString().equals("")) {
            throw new Exceptions("Bairro não informada.");
        } else if (editLogradouro.getText().toString().equals("")) {
            throw new Exceptions("Logradouro não informada.");
        } else if (editNumero.getText().toString().equals("")) {
            throw new Exceptions("Número não informada.");
        }

        ValidaCampos validaCampos = new ValidaCampos();
        if (validaCampos.validaCNPJ(editCnpj.getText().toString()) == false){
            throw new Exceptions("O CNPJ informado não é válido.");
        }else if (validaCampos.validaCep(editCep.getText().toString()) == false){
            throw new Exceptions("O CEP informado não é válido.");
        }else if (validaCampos.validaEmail(editEmail.getText().toString()) == false){
            throw new Exceptions("O EMAIL informado não é válido.");
        }

        ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
        //Verifica se o cliente já existe
        Cliente clienteTeste = clienteDAO.buscaClienteCnpj(editCnpj.getText().toString());

        if(clienteTeste != null && clienteTeste.getId().equals(cliente.getId()) == false){
            throw new Exceptions("Cliente já cadastrado com este CNPJ.");
        }

        if (cliente == null) {
            cliente = new Cliente();
            cliente.setDtCadastro(ferramentas.getCurrentDate());
        }

        if (checkAtivo.isChecked()) {
            cliente.setStatus(1);
        } else {
            cliente.setStatus(0);
        }
        cliente.setSegmentoMercado((SegmentoMercado) spinnerSegmentoMercado.getSelectedItem());
        cliente.setRazaoSocial(editRazSocial.getText().toString());
        cliente.setNomeFantasia(editNomeFant.getText().toString());
        cliente.setCnpj(editCnpj.getText().toString().replaceAll("[^0-9]*",""));
        cliente.setInscricaoEstadual(editInsEst.getText().toString());
        cliente.setEmail(editEmail.getText().toString());
        cliente.setFone(editFone.getText().toString().replaceAll("[^0-9]*",""));
        cliente.setCep(Integer.valueOf(editCep.getText().toString().replaceAll("[^0-9]*","")));
        cliente.setCidade((Cidade) spinnerCidade.getSelectedItem());
        cliente.setBairro(editBairro.getText().toString());
        cliente.setLogradouro(editLogradouro.getText().toString());
        cliente.setNumero(Integer.valueOf(editNumero.getText().toString()));
        cliente.setDtAtualizacao(ferramentas.getCurrentDate());

        if(cliente.getId() == null || cliente.getId() <= 0) {
            //Insere o produto na base de dados
            clienteDAO.insereCliente(cliente);
        }else {
            //Atualiza o produto na base de dados
            clienteDAO.atualizaCliente(cliente);
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == spinnerEstado){
            Estado estado = (Estado) spinnerEstado.getSelectedItem();
            if (estado.getId() != 0){
                //Objeto para setar o primeiro item do spinner com default
                Cidade cidade = new Cidade();
                cidade.setId(0);
                //produto.setCodInterno(0);
                cidade.setSigla("");
                cidade.setDescricao("Selecione");

                //Popula o spinner de cidades de acordo com estado selecionado
                CidadeDAO cidadeDAO = CidadeDAO.getInstance(this);
                listaCidades = cidadeDAO.buscaCidadeEstado(estado.getId());
                listaCidades.add(0,cidade);
                if (listaCidades != null) {
                    listaCidadesSpinnerAdapter = new ListaCidadesSpinnerAdapter(this, listaCidades);
                    spinnerCidade.setAdapter(listaCidadesSpinnerAdapter);
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
