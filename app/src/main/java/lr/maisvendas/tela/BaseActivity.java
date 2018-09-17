package lr.maisvendas.tela;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.sql.UsuarioDAO;
//import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private static final String TAG = StdKeySerializers.Default.class.getName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int drawerLayoutId;
    private int toolbarId;
    private int icMenuId;
    private int navigationViewId;
    private static Usuario usuario;
    //private Tools tools;
    private int itemId;
    private MenuItem menuSearch;
    public static ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(icMenuId);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //MapFragment listFragment = new MapFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.replace(R.id., listFragment);
        transaction.commit();

        drawerLayout = (DrawerLayout) findViewById(drawerLayoutId);
        navigationView = (NavigationView) findViewById(navigationViewId);
        navigationView.setNavigationItemSelectedListener(this);

        //tools = new Tools();

    }

    @Override
    protected void onResume() {
        super.onResume();
        validaMenus();
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public void setToolbarId(int toolbarId) {
        this.toolbarId = toolbarId;
    }

    public void setIcMenuId(int icMenuId) {
        this.icMenuId = icMenuId;
    }

    public void setNavigationViewId(int navigationViewId) {
        this.navigationViewId = navigationViewId;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public MenuItem getMenuSearch() {
        return menuSearch;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_principal_pedidos:
                 intent = new Intent(this,ListaPedidosActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_principal_catalogo:
                intent = new Intent(this,ListaProdutosActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_principal_clientes:
                intent = new Intent(this, ListaClientesActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_principal_agenda:
                /*intent = new Intent(this,ListaClientesActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.menu_principal_configuracoes:
                intent = new Intent(this,ConfiguracaoActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_principal_sair:
                usuario.setToken(null);
                UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);

                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opcoes, menu);

        //Pega o Componente.
        SearchView mSearchView = (SearchView) menu.findItem(R.id.menu_opcoes_search).getActionView();
        //Define um texto de ajuda:
        mSearchView.setQueryHint("Pesquisar");

        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(this);

        menuSearch = menu.findItem(R.id.menu_opcoes_search);
        menuSearch.setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    public void validaMenus() {
        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);
        usuario = usuarioDAO.buscaUsuarioLoginToken();

        NavigationView navigation = (NavigationView) findViewById(navigationViewId);
        Menu menu = navigation.getMenu();
        MenuItem menuItem;

        menuItem = menu.findItem(R.id.menu_principal_pedidos);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.menu_principal_catalogo);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.menu_principal_clientes);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.menu_principal_agenda);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.menu_principal_configuracoes);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.menu_principal_sair);
        menuItem.setVisible(true);
    }
}
