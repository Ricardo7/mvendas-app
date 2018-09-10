package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import lr.maisvendas.R;
import lr.maisvendas.sincronizacao.ClienteSinc;
import lr.maisvendas.utilitarios.Notify;

public class SincronicacaoManualActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SincronicacaoManualActivity";
    //Campos da tela
    private Switch switchSincTudo;
    private Switch switchSincImagens;
    private Switch switchSincPedidos;
    private Switch switchSincClientes;
    private Switch switchSincProdutos;
    private Button buttonSinc;

    //Variáveis
    public Notify notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sincronicacao_manual);
        setDrawerLayoutId(R.id.activity_sincronicacao_manual_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Sincronização Manual");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        switchSincTudo = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_tudo);
        switchSincImagens = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_imagens);
        switchSincPedidos = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_pedidos);
        switchSincClientes = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_clientes);
        switchSincProdutos = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_produtos);
        buttonSinc = (Button) findViewById(R.id.activity_sincronicacao_manual_button_sincronizar);

        buttonSinc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSinc){

            notify = new Notify(this);

            if (switchSincTudo.isChecked() || switchSincImagens.isChecked()){

            }

            if (switchSincTudo.isChecked() || switchSincPedidos.isChecked()){

            }

            if (switchSincTudo.isChecked() || switchSincClientes.isChecked()){
                ClienteSinc clienteSinc = new ClienteSinc(notify);
                clienteSinc.sincronizaCliente();
            }

            if (switchSincTudo.isChecked() || switchSincProdutos.isChecked()){

            }
        }
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
