package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;

import lr.maisvendas.R;

public class SincronicacaoManualActivity extends BaseActivity {

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
