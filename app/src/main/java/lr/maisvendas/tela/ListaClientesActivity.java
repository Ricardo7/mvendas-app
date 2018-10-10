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
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.tela.adaptador.ListaClientesAdapter;

public class ListaClientesActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FloatingActionButton buttonAdd;
    private ListView listViewClientes;
    private ListaClientesAdapter listaClientesAdapter;
    private List<Cliente> listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lista_clientes);
        setDrawerLayoutId(R.id.activity_lista_clientes_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Clientes");

        buttonAdd = (FloatingActionButton) findViewById(R.id.activity_lista_clientes_button_add);
        listViewClientes = (ListView) findViewById(R.id.activity_lista_clientes_list_view);

        buttonAdd.setOnClickListener(this);
        listViewClientes.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this,CadastroClienteActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Cliente cliente = listaClientesAdapter.getItem(i);
        Intent intent = new Intent(this,CadastroClienteActivity.class);
        intent.putExtra(CadastroClienteActivity.PARAM_CLIENTE,cliente);
        startActivity(intent);

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
        listaClientes = clienteDAO.buscaClientes();
        listaClientesAdapter = new ListaClientesAdapter(this,listaClientes);
        listViewClientes.setAdapter(listaClientesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuSearch().setVisible(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        listaClientesAdapter.getFilter().filter(newText);

        return super.onQueryTextChange(newText);

    }
}
