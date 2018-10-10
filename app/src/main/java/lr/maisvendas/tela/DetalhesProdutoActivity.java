package lr.maisvendas.tela;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.comunicacao.produto.CarregarProdutosSugeridosCom;
import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.repositorio.sql.ItemPedidoDAO;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.servico.VerificaServico;
import lr.maisvendas.tela.adaptador.DetalhesProdutoImgAdapter;
import lr.maisvendas.tela.adaptador.DetalhesProdutoTabAdapter;
import lr.maisvendas.tela.adaptador.ListaProdutosSugestaoAdapter;
import lr.maisvendas.tela.fragmentos.DetalhesProdutoTabObsFragment;
import lr.maisvendas.tela.fragmentos.DetalhesProdutoTabProdutoFragment;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.tela.interfaces.ComunicadorDetalhesProduto;
import lr.maisvendas.tela.interfaces.ProdutoSugeridoClickListener;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.StatusPedido;

public class DetalhesProdutoActivity extends BaseActivity implements ComunicadorDetalhesProduto, ComunicadorCadastroPedido, View.OnClickListener, TextWatcher, ProdutoSugeridoClickListener, CarregarProdutosSugeridosCom.CarregarProdutosSugeridosTaskCallBack {

    private static final String TAG = "DetalhesProdutoActivity";
    public static final String PARAM_PRODUTO = "PARAM_PRODUTO";
    public static final String PARAM_PEDIDO = "PARAM_PEDIDO";
    public static final String PARAM_ITEM_TABELA_PRECO = "PARAM_ITEM_TABELA_PRECO";

    //Campos da tela
    private EditText editPercDesc;
    private EditText editVlrDesc;
    private EditText editQuantidade;
    private EditText editTotal;
    private ImageButton buttonPedido;
    private ViewPager viewPagerImagens;
    private RecyclerView recyclerViewProdutosSugeridos;

    //Variáveis
    private Produto produto;
    private Pedido pedido;
    private ItemTabelaPreco itemTabelaPreco;
    private ItemPedido itemPedido;
    private Ferramentas ferramentas;
    private Boolean editandoAutomarico = false;
    private List<Produto> listaProdutosSugeridos;
    private ListaProdutosSugestaoAdapter listaProdutosSugeridosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detalhes_produto);
        setDrawerLayoutId(R.id.activity_detalhes_produto_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Detalhes pedido");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        //Instancia os campos da tela
        editPercDesc = (EditText) findViewById(R.id.activity_detalhes_produto_edit_per_desc);
        editVlrDesc = (EditText) findViewById(R.id.activity_detalhes_produto_edit_vlr_desc);
        editQuantidade = (EditText) findViewById(R.id.activity_detalhes_produto_edit_quantidade);
        editTotal = (EditText) findViewById(R.id.activity_detalhes_produto_edit_total);
        buttonPedido = (ImageButton) findViewById(R.id.activity_detalhes_produto_button_pedido);
        viewPagerImagens = (ViewPager) findViewById(R.id.activity_detalhes_produto_img_produtos);
        recyclerViewProdutosSugeridos = (RecyclerView) findViewById(R.id.activity_detalhes_produto_sugestao_list_view);

        ferramentas = new Ferramentas();

        loadDataFromActivity();

        //Monta RecyclerView de sugestão de produtos
        recyclerViewProdutosSugeridos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProdutosSugeridos.setLayoutManager(layoutManager);

        //Chama a rotina para inserir tratamento após digitar em um dos campos de valor
        //addTextChangedListener();
        editPercDesc.addTextChangedListener(this);
        editVlrDesc.addTextChangedListener(this);
        editQuantidade.addTextChangedListener(this);

        buttonPedido.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == buttonPedido){

            if (itemPedido != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder
                    .setMessage("O Item será removido do pedido." +
                            "\nDeseja continuar?")
                    .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        //Se o item estiver no pedido irá remover o item do pedido
                        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(DetalhesProdutoActivity.this);
                        itemPedidoDAO.deletaItemPedidoProduto(pedido.getId(), produto.getId());

                        itemPedido = null;

                        buttonPedido.setBackgroundResource(R.mipmap.ic_carrinho_add);
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            return;
                        }
                    })
                    .show();


            }else{

                try {
                    //Chama método para salvar o item no pedido
                    salvaDados();
                } catch (Exceptions ex) {
                    ferramentas.customToast(this,ex.getMessage());
                }
            }
        }

    }


    @Override
    public void onProdutoSugeridoClick(View view, int position) {
        Produto produto = listaProdutosSugeridosAdapter.getItem(position);
        //Verifica se tem um pedido no carrinho, em construção (Status = 0 )
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
        Pedido pedido = pedidoDAO.buscaPedidoStatus(StatusPedido.emConstrucao);

        //Busca o item da tabela de preço do pedido (Só irá ter valor)
        ItemTabelaPreco itemTabelaPreco = null;
        if (pedido != null) {
            ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(this);
            itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedido.getId(), produto.getId());

        }

        Intent intent = new Intent(this, DetalhesProdutoActivity.class);
        intent.putExtra(DetalhesProdutoActivity.PARAM_PRODUTO, produto);
        intent.putExtra(DetalhesProdutoActivity.PARAM_PEDIDO, pedido);
        intent.putExtra(DetalhesProdutoActivity.PARAM_ITEM_TABELA_PRECO, itemTabelaPreco);
        startActivity(intent);
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        produto = (Produto) getIntent().getSerializableExtra(PARAM_PRODUTO);
        pedido = (Pedido) getIntent().getSerializableExtra(PARAM_PEDIDO);

        itemTabelaPreco = (ItemTabelaPreco) getIntent().getSerializableExtra(PARAM_ITEM_TABELA_PRECO);


        if (pedido != null) {
            List<ItemPedido> itensPedido = pedido.getItensPedido();

            /*RONALDO: AVALIAR MELHOR IMPLEMENTAÇÃO, POSSÍVELMENTE BUSCANDO DIRETAMENTE DO BANCO*/
            for (ItemPedido itemPdv : itensPedido) {
                if (itemPdv.getProduto().getId().equals(produto.getId())) {
                    itemPedido = itemPdv;
                }

            }
            if (itemPedido != null) {

                Double percDesc = 100 / itemPedido.getVlrUnitario() * itemPedido.getVlrDesconto();
                editPercDesc.setText(percDesc.toString());
                editVlrDesc.setText(itemPedido.getVlrDesconto().toString());
                editQuantidade.setText(itemPedido.getQuantidade().toString());
                editTotal.setText(itemPedido.getVlrTotal().toString());

                buttonPedido.setBackgroundResource(R.mipmap.ic_carrinho_remov);
            }

        }

        //Carrega imagens
        DetalhesProdutoImgAdapter detalhesProdutoImgAdapter = new DetalhesProdutoImgAdapter(this,produto.getImagens());
        viewPagerImagens.setAdapter(detalhesProdutoImgAdapter);

        //Carrega Tab de detalhes
        Resources resources = getResources();
        DetalhesProdutoTabAdapter detalhesProdutoTabAdapter = new DetalhesProdutoTabAdapter(getSupportFragmentManager());
        detalhesProdutoTabAdapter.add(DetalhesProdutoTabProdutoFragment.newInstance(1), resources.getString(R.string.frag_produto));
        detalhesProdutoTabAdapter.add(DetalhesProdutoTabObsFragment.newInstance(2), resources.getString(R.string.frag_observacoes));

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_detalhes_produto_viewPager_detalhes);
        viewPager.setAdapter(detalhesProdutoTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_detalhes_produto_tab_detalhes);
        tabLayout.setupWithViewPager(viewPager);

        //editPercDesc.setText();
        //Carrega os produtos sugeridos
        buscaProdutosSugeridos();

    }

    private void salvaDados() throws Exceptions{
        if (editQuantidade.getText().toString().equals("0.0") || editQuantidade.getText().toString().equals("")) {
            throw new Exceptions("Quantidade não informada.");
        }else if (editTotal.getText().toString().equals("0.0") || editTotal.getText().toString().equals("")){
            throw new Exceptions("Valor total não informado.");
        }else if((editPercDesc.getText() != null && editPercDesc.getText().toString().equals("") == false) && Double.valueOf(editPercDesc.getText().toString()) > itemTabelaPreco.getMaxDesc()){
            throw new Exceptions("Desconto informado não permitido, o maior desconto permitido para este item é "+itemTabelaPreco.getMaxDesc()+".");
        }

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setVlrUnitario(itemTabelaPreco.getVlrUnitario());
        itemPedido.setQuantidade(Double.valueOf(editQuantidade.getText().toString()));
        itemPedido.setVlrDesconto(Double.valueOf(editVlrDesc.getText().toString())*Double.valueOf(editQuantidade.getText().toString()));
        itemPedido.setVlrTotal(Double.valueOf(editTotal.getText().toString()));
        itemPedido.setProduto(produto);
        itemPedido.setDtCriacao(ferramentas.getCurrentDate());
        itemPedido.setDtAtualizacao(ferramentas.getCurrentDate());

        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(this);
        itemPedidoDAO.insereItemPedido(itemPedido,pedido.getId());

        this.itemPedido = itemPedido;

        buttonPedido.setBackgroundResource(R.mipmap.ic_carrinho_remov);
    }

    @Override
    public Produto getProduto() {
        return produto;
    }

    @Override
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public ItemTabelaPreco getItemTabelaPreco() {
        return itemTabelaPreco;
    }

    @Override
    public void setItemTabelaPreco(ItemTabelaPreco itemTabelaPreco) {
        this.itemTabelaPreco = itemTabelaPreco;
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        BigDecimal bd = null;
        Double percDesc = 0.0;
        Double valorDesc = 0.0;
        Double valorTotal = 0.0;
        Double valorUnitario;
        if (itemTabelaPreco != null) {
            valorUnitario = itemTabelaPreco.getVlrUnitario();
        }else{
            valorUnitario = 0.0;
        }

        if (!editandoAutomarico) {
            if(editPercDesc.hasFocus()){
                editandoAutomarico = true;
                if (editQuantidade.getText().toString().equals("")) {
                    editQuantidade.setText("0.0");
                }
                if (!editPercDesc.getText().toString().equals("")) {
                    //Calcula o valor para o campo de valor a partir do percentual de desconto
                    valorDesc = (valorUnitario / 100.00) * Double.valueOf(editable.toString());
                    bd = new BigDecimal(valorDesc).setScale(2, RoundingMode.HALF_EVEN);
                    editVlrDesc.setText(String.valueOf(bd.doubleValue()));
                    //Calcula o valor para o campo total
                    valorTotal = Double.valueOf(editQuantidade.getText().toString()) * (valorUnitario - valorDesc);
                    bd = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
                    editTotal.setText(String.valueOf(bd.doubleValue()));
                } else {
                    editVlrDesc.setText("0.0");
                }
                editandoAutomarico = false;
            }else if(editVlrDesc.hasFocus()) {
                editandoAutomarico = true;
                if (editQuantidade.getText().toString().equals("")) {
                    editQuantidade.setText("0.0");
                }
                if (!editVlrDesc.getText().toString().equals("")) {
                    //Calcula o valor para o campo de percentual a partir do valor de desconto
                    percDesc = (100.00 / valorUnitario) * Double.valueOf(editable.toString());
                    bd = new BigDecimal(percDesc).setScale(2, RoundingMode.HALF_EVEN);
                    editPercDesc.setText(String.valueOf(bd.doubleValue()));

                    //Calcula o valor para o campo total
                    valorTotal = Double.valueOf(editQuantidade.getText().toString()) * (valorUnitario - Double.valueOf(editable.toString()));
                    bd = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
                    editTotal.setText(String.valueOf(bd.doubleValue()));
                } else {
                    editPercDesc.setText("0.0");
                }
                editandoAutomarico = false;
            }else if(editQuantidade.hasFocus()){

                editandoAutomarico = true;
                if (editVlrDesc.getText().toString().equals("")) {
                    editVlrDesc.setText("0.0");
                }
                if (!editQuantidade.getText().toString().equals("")) {

                    valorDesc = Double.valueOf(editVlrDesc.getText().toString());
                    valorTotal = Double.valueOf(editable.toString()) * (valorUnitario - valorDesc);

                    bd = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
                    editTotal.setText(String.valueOf(bd.doubleValue()));
                }
                editandoAutomarico = false;
            }
        }
    }

    /**
     * Busca no Web Services os produtos sugeridas pela inteligência artificial
     *
     */
    private void buscaProdutosSugeridos() {

        VerificaConexao verificaConexao = new VerificaConexao();
        VerificaServico verificaServico = new VerificaServico();
        EnderecoHost enderecoHost = new EnderecoHost();

        if (getUsuario() != null && getUsuario().getToken() != null) {
            try {
                if (verificaConexao.isNetworkAvailable(this) && verificaServico.execute(enderecoHost.getHostHTTPRaiz()).get()) {
                    new CarregarProdutosSugeridosCom(this).execute(getUsuario().getToken(), produto.getIdWS(),pedido.getIdWS());
                }else{
                    trataProdutos(null);
                }
            }catch (Exception ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }
        }

    }

    @Override
    public void onCarregarProdutosSugeridosSuccess(List<Produto> produtos) {

    }

    @Override
    public void onCarregarProdutosSugeridosFailure(String mensagem) {

        trataProdutos(null);
    }

    public void trataProdutos(List<Produto> produtosNew) {
       List<Produto> produtosRemov = new ArrayList<>();

        ProdutoDAO produtoDAO = ProdutoDAO.getInstance(this);
        if (produtosNew != null){

            //Percorre os produtos sugeridos a fim de remover os produtos que já estão no pedido
            for (ItemPedido itemPedido : pedido.getItensPedido()) {

                for (Produto produdoNew : produtosNew) {
                    if (itemPedido.getProduto().getIdWS().equals(produdoNew.getIdWS())) {
                        produtosRemov.add(produdoNew);
                        break; //Aborta o 'for' interno pois já encontrou registro iguais
                    }
                }
                //Remove os objetos repetidos já encontrados para não percorrer novamenta na próxima interação
                produtosNew.removeAll(produtosRemov);
                produtosRemov.clear();
            }

            //Percorre os produtos sugeridos válidos alterando para o objeto interno
            for (Produto produto : produtosNew) {
                produto = produtoDAO.buscaProdutoIdWs(produto.getIdWS());
                listaProdutosSugeridos.add(produto);
            }
        }else{
            //Se não retornar nada do Web Services irá buscar os produtos internamente fazendo uma pesquisa simples
            listaProdutosSugeridos = produtoDAO.buscaProdutosSugeridos(produto.getId());
        }

        listaProdutosSugeridosAdapter = new ListaProdutosSugestaoAdapter(this,listaProdutosSugeridos);
        recyclerViewProdutosSugeridos.setAdapter(listaProdutosSugeridosAdapter);
        listaProdutosSugeridosAdapter.setClickListener(this);
    }
}
