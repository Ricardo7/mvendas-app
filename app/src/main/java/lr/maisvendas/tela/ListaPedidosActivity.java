package lr.maisvendas.tela;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.adaptador.ListaPedidosAdapter;

public class ListaPedidosActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FloatingActionButton buttonAdd;
    private ListView listViewPedidos;
    private ListaPedidosAdapter listaPedidosAdapter;
    private List<Pedido> listaPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lista_pedidos);
        setDrawerLayoutId(R.id.activity_lista_pedidos_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Pedidos");

        buttonAdd = (FloatingActionButton) findViewById(R.id.activity_lista_pedidos_button_add);
        listViewPedidos = (ListView) findViewById(R.id.activity_lista_pedidos_list_view);

        buttonAdd.setOnClickListener(this);
        listViewPedidos.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this,CadastroPedidoActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Pedido pedido = listaPedidosAdapter.getItem(i);
        Intent intent = new Intent(this,CadastroPedidoActivity.class);
        intent.putExtra(CadastroPedidoActivity.PARAM_PEDIDO,pedido);
        startActivity(intent);

        /*
        Pedido pedido = ListaPedidosAdapter.getItem(i);
        Intent intent = new Intent(this,CadastroPedidoIniFragment.class);
        intent.putExtra(CadastroPedidoIniFragment.PARAM_PEDIDO_INI,pedido);
        startActivity(intent);
        */

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
        listaPedidos = pedidoDAO.buscaPedidos();
        listaPedidosAdapter = new ListaPedidosAdapter(this,listaPedidos);
        listViewPedidos.setAdapter(listaPedidosAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuSearch().setVisible(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        listaPedidosAdapter.getFilter().filter(newText);

        return super.onQueryTextChange(newText);

    }
}
