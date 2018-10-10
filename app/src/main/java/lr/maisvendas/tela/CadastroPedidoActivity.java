package lr.maisvendas.tela;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.adaptador.CadastroPedidoTabAdapter;
import lr.maisvendas.tela.fragmentos.CadastroPedidoFimFragment;
import lr.maisvendas.tela.fragmentos.CadastroPedidoIniFragment;
import lr.maisvendas.tela.fragmentos.CadastroPedidoItemFragment;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;


public class CadastroPedidoActivity extends BaseActivity implements ComunicadorCadastroPedido, View.OnClickListener {

    private static final String TAG = "CadastroPedidoActivity";
    public static final String PARAM_PEDIDO = "PARAM_PEDIDO";

    //Campos de tela
    private Button buttonSalvar;

    //Variáveis
    public Pedido pedido;
    private Ferramentas ferramentas;
    private CadastroPedidoTabAdapter cadastroPedidoTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cadastro_pedido);
        setDrawerLayoutId(R.id.activity_cadastro_pedido_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Pedido");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        buttonSalvar = (Button) findViewById(R.id.activity_cadastro_pedido_button_salvar);

        ferramentas = new Ferramentas();

        loadDataFromActivity();

        buttonSalvar.setOnClickListener(this);
        /*
        // cria uma instância do fragmento
        CadastroPedidoIniFragment cadastroPedidoIniFragment = new CadastroPedidoIniFragment();
        //Objeto que controla o ciclo de vida do fragmento, que controla todos os fragmentos que estão dentro da activity
        //Chava os métodos que estão no fragmento de acordo com o ciclo de vida da Activity
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_cadastro_pedido_container, cadastroPedidoIniFragment);
        transaction.commit();
        */
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        pedido = (Pedido) getIntent().getSerializableExtra(PARAM_PEDIDO);

        //Carrega Tab de detalhes
        Resources resources = getResources();
        cadastroPedidoTabAdapter = new CadastroPedidoTabAdapter(getSupportFragmentManager());
        cadastroPedidoTabAdapter.add(CadastroPedidoIniFragment.newInstance(1), resources.getString(R.string.frag_inicio));
        cadastroPedidoTabAdapter.add(CadastroPedidoItemFragment.newInstance(2), resources.getString(R.string.frag_itens));
        cadastroPedidoTabAdapter.add(CadastroPedidoFimFragment.newInstance(3), resources.getString(R.string.frag_fim));

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_cadastro_pedido_viewPager_pedido);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(cadastroPedidoTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_cadastro_pedido_tab_pedido);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSalvar){
            try {
                salvaDados();
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG,ex.getMessage());
            }
        }
    }

    @Override
    public Pedido getPedido() {
        return pedido;
    }

    @Override
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;

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

    /**
     * Os dados são salvos na tabela, no entanto.
     *
     */
    private void salvaDados() throws Exceptions {
        Boolean pedidoNovo = false;
        //Campos tela
        Spinner spinnerCliente;
        Spinner spinnerTabelaPreco;
        Spinner spinnerCondPgto;
        EditText editObservacao;

        //Istancia o Fragment da tela inicial do pedido, aonde serão buscados os valores dos campos
        CadastroPedidoIniFragment cadastroPedidoIniFragment = (CadastroPedidoIniFragment) cadastroPedidoTabAdapter.getItem(0);

        //Istancia os campos date tela inicial do pedido
        spinnerCliente = (Spinner) cadastroPedidoIniFragment.getView().findViewById(R.id.fragment_cadastro_pedido_ini_spinner_cliente);
        spinnerTabelaPreco = (Spinner) cadastroPedidoIniFragment.getView().findViewById(R.id.fragment_cadastro_pedido_ini_spinner_tabela_preco);
        spinnerCondPgto = (Spinner) cadastroPedidoIniFragment.getView().findViewById(R.id.fragment_cadastro_pedido_fim_spinner_condicao_pgto);
        editObservacao = (EditText) cadastroPedidoIniFragment.getView().findViewById(R.id.fragment_cadastro_pedido_ini_edit_observacao);

        if (pedido == null){
            pedidoNovo = true;
            pedido = new Pedido();
        }

        if (spinnerTabelaPreco.getSelectedItem() == null) {
            throw new Exceptions("Tabela de Preços não selecionada.");
        }else if (spinnerCondPgto.getSelectedItem() == null) {
            throw new Exceptions("Condição de pagamento não selecionada.");
        }else if (spinnerCliente.getSelectedItem() == null) {
            throw new Exceptions("Cliente não selecionado.");
        }

        pedido.setCondicaoPagamento((CondicaoPagamento) spinnerCondPgto.getSelectedItem());
        pedido.setTabelaPreco((TabelaPreco) spinnerTabelaPreco.getSelectedItem());
        pedido.setCliente((Cliente) spinnerCliente.getSelectedItem());
        pedido.setObservacao(editObservacao.getText().toString());
        pedido.setDtAtualizacao(ferramentas.getCurrentDate());

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);

        if (pedidoNovo){
            pedido.setStatus(0);
            pedido.setSituacao(0);
            pedido.setDtCriacao(ferramentas.getCurrentDate());
            pedidoDAO.inserePedido(pedido);
        }else{
            //Se pedido já existe
            pedidoDAO.atualizaPedido(pedido);
        }

        ferramentas.customToast(this,"Pedido salvo com sucesso");

    }

}
