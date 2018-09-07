package lr.maisvendas.tela;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.tela.adaptador.DetalhesProdutoImgAdapter;
import lr.maisvendas.tela.adaptador.DetalhesProdutoTabAdapter;
import lr.maisvendas.tela.fragmentos.DetalhesProdutoTabObsFragment;
import lr.maisvendas.tela.fragmentos.DetalhesProdutoTabProdutoFragment;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.tela.interfaces.ComunicadorDetalhesProduto;

public class DetalhesProdutoActivity extends BaseActivity implements ComunicadorDetalhesProduto, ComunicadorCadastroPedido{

    private static final String TAG = "DetalhesProdutoActivity";
    public static final String PARAM_PRODUTO = "PARAM_PRODUTO";
    public static final String PARAM_PEDIDO = "PARAM_PEDIDO";
    public static final String PARAM_ITEM_TABELA_PRECO = "PARAM_ITEM_TABELA_PRECO";

    //Campos da tela
    private EditText editPercDesc;
    private EditText editVlrDesc;
    private EditText editQuantidade;
    private EditText editTotal;
    private ImageButton buttonProduto;
    private ViewPager viewPagerImagens;

    //Variáveis
    private Produto produto;
    private Pedido pedido;
    private ItemTabelaPreco itemTabelaPreco;

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
        buttonProduto = (ImageButton) findViewById(R.id.activity_detalhes_produto_button_pedido);
        viewPagerImagens = (ViewPager) findViewById(R.id.activity_detalhes_produto_img_produtos);


        loadDataFromActivity();
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        produto = (Produto) getIntent().getSerializableExtra(PARAM_PRODUTO);
        pedido = (Pedido) getIntent().getSerializableExtra(PARAM_PEDIDO);
        itemTabelaPreco = (ItemTabelaPreco) getIntent().getSerializableExtra(PARAM_ITEM_TABELA_PRECO);


        if (pedido != null) {
            List<ItemPedido> itensPedido = pedido.getItensPedido();
            ItemPedido itemPedido = null;
            for (ItemPedido itemPdv : itensPedido) {

                if (itemPdv.getProduto().getId() == produto.getId()) {
                    itemPedido = itemPdv;
                }

            }
            if (itemPedido != null) {
                Double percDesc = 100 / itemPedido.getVlrUnitario() * itemPedido.getVlrDesconto();
                editPercDesc.setText(percDesc.toString());
                editVlrDesc.setText(itemPedido.getVlrDesconto().toString());
                editQuantidade.setText(itemPedido.getQuantidade().toString());
                editTotal.setText(itemPedido.getVlrTotal().toString());
            }

            buttonProduto.setBackgroundResource(R.mipmap.ic_carrinho_remov);
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
}
