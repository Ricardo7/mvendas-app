package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import lr.maisvendas.R;

public class CadastroPedidoIniActivityBkp extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoIniFragment";
    public static final String PARAM_PEDIDO_INI = "PARAM_PEDIDO_INI";

    //Campos da tela

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_cadastro_pedido_ini);
        setDrawerLayoutId(R.id.activity_cadastro_pedido_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Inicio Pedido");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);


    }

    @Override
    public void onClick(View view) {

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
