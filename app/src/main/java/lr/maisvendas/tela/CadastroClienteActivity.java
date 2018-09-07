package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import lr.maisvendas.R;

public class CadastroClienteActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CadastroClienteActivity";
    public static final String PARAM_CLIENTE = "PARAM_CLIENTE";

    //Campos da tela
    private Button buttonCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cadastro_cliente);
        setDrawerLayoutId(R.id.activity_cadastro_cliente_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        buttonCadastrar = (Button) findViewById(R.id.activity_cadastro_cliente_button_salvar);

        buttonCadastrar.setOnClickListener(this);
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
