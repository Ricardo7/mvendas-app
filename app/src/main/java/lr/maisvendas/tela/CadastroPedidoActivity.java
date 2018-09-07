package lr.maisvendas.tela;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.tela.fragmentos.CadastroPedidoIniFragment;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;

public class CadastroPedidoActivity extends BaseActivity implements ComunicadorCadastroPedido {

    private static final String TAG = "CadastroPedidoActivity";
    public static final String PARAM_PEDIDO = "PARAM_PEDIDO";

    public Pedido pedido;

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

        loadDataFromActivity();

        // cria uma instância do fragmento
        CadastroPedidoIniFragment cadastroPedidoIniFragment = new CadastroPedidoIniFragment();
        //Objeto que controla o ciclo de vida do fragmento, que controla todos os fragmentos que estão dentro da activity
        //Chava os métodos que estão no fragmento de acordo com o ciclo de vida da Activity
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_cadastro_pedido_container, cadastroPedidoIniFragment);
        transaction.commit();
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        pedido = (Pedido) getIntent().getSerializableExtra(PARAM_PEDIDO);


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
