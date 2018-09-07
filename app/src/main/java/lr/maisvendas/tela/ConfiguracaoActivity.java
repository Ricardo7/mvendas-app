package lr.maisvendas.tela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lr.maisvendas.R;

public class ConfiguracaoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ConfiguracaoActivity";

    private Button buttonSincManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_configuracao);
        setDrawerLayoutId(R.id.activity_configuracao_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Configurações");

        buttonSincManual = (Button) findViewById(R.id.action_configuracao_button_sinc_man);

        buttonSincManual.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == buttonSincManual){
            Intent intent = new Intent(this, SincronicacaoManualActivity.class);
            startActivity(intent);
        }
    }
}
