package lr.maisvendas.tela;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.comunicacao.atividade.CarregarAtividadeSugeridaCom;
import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.repositorio.sql.AtividadeDAO;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.servico.VerificaServico;
import lr.maisvendas.tela.adaptador.ListaAtividadesAdapter;
import lr.maisvendas.tela.interfaces.CadastroAtividadeActivity;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoAgenda;

public class AgendaActivity extends BaseActivity implements CalendarView.OnDateChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, CarregarAtividadeSugeridaCom.CarregarAtividadeSugeridaTaskCallBack {

    private static final String TAG = "AgendaActivity";
    //Campos Tela
    private CalendarView calendarView;
    private FloatingActionButton buttonAdd;
    //private FloatingActionButton buttonSugestoes;

    //Variáveis
    private ListView listViewAtividades;
    private ListaAtividadesAdapter listaAtividadesAdapter;
    private List<Atividade> listaAtividades;
    private Ferramentas ferramentas;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_agenda);
        setDrawerLayoutId(R.id.activity_agenda_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Agenda de Visitas");

        calendarView = (CalendarView) findViewById(R.id.activity_agenda_calendar);
        listViewAtividades = (ListView) findViewById(R.id.activity_agenda_list_view);
        buttonAdd = (FloatingActionButton) findViewById(R.id.activity_agenda_add);
        //buttonSugestoes = (FloatingActionButton) findViewById(R.id.activity_agenda_sugestoes);

        ferramentas = new Ferramentas();

        loadDataFromActivity();

        // quando selecionado alguma data diferente da padrão
        calendarView.setOnDateChangeListener(this);
        buttonAdd.setOnClickListener(this);
        //buttonSugestoes.setOnClickListener(this);
        listViewAtividades.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Colocado para atualizar a lista ao voltar dos Detalhes, caso tenha mexido em alguma coisa no pedido
        if (listaAtividadesAdapter != null) {
            listaAtividadesAdapter.notifyDataSetChanged();
        }
        //loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {

        if (view == buttonAdd) {
            Intent intent = new Intent(this, CadastroAtividadeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String dataAtual = year + "-" + String.format("%02d",month) + "-" + String.format("%02d",dayOfMonth);

        AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);
        listaAtividades = atividadeDAO.buscaAtividadesDia(dataAtual);

        //Busca as atividades sugeridas para o dia
        buscaAtividadesSugeridas(dataAtual);

        //O restante do processo é feito após a busca e validação das atividades sugeridas, no mérodo trataAtividades()

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Atividade atividade = listaAtividadesAdapter.getItem(i);
        Intent intent = new Intent(this, CadastroAtividadeActivity.class);
        intent.putExtra(CadastroAtividadeActivity.PARAM_ATIVIDADE, atividade);
        startActivity(intent);
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {
        String dataAtual = ferramentas.getCurrentDate().substring(0, 10);
        AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);
        listaAtividades = atividadeDAO.buscaAtividadesDia(dataAtual);
        //Busca as atividades sugeridas para o dia
        buscaAtividadesSugeridas(dataAtual);

        //O restante do processo é feito após a busca e validação das atividades sugeridas, no mérodo trataAtividades()

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * Buscar no Web Services as atividades sugeridas pela inteligência artificial para o dia
     *
     * @param dataAtual
     */
    private void buscaAtividadesSugeridas(String dataAtual) {

        VerificaConexao verificaConexao = new VerificaConexao();
        VerificaServico verificaServico = new VerificaServico();
        EnderecoHost enderecoHost = new EnderecoHost();

        if (getUsuario() != null && getUsuario().getToken() != null) {
            try {
                if (verificaConexao.isNetworkAvailable(this) && verificaServico.execute(enderecoHost.getHostHTTPRaiz()).get()) {
                    progressDialog = ProgressDialog.show(this, "Aguarde", "Carregando atividades, por favor aguarde.", true, false);
                    new CarregarAtividadeSugeridaCom(this).execute(getUsuario().getToken(), dataAtual, getUsuario().getIdWS());

                } else {
                    ferramentas.customLog(TAG, "Não foi possível conectar no servidor.");
                    //Chama a função para carregar as atividades que já existem internamente
                    trataAtividades(null);
                }
            } catch (Exception ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }

        }

    }

    @Override
    public void onCarregarAtividadeSugeridaSuccess(List<Atividade> Atividades) {
        progressDialog.dismiss();
        trataAtividades(listaAtividades);
    }

    @Override
    public void onCarregarAtividadeSugeridaFailure(String mensagem) {
        //TESTE
        /*Atividade atividade = new Atividade();
        atividade.setAssunto("Atividade criada pela IA");
        atividade.setObservacao("");
        atividade.setHoraAtividade("22:00");
        atividade.setDataAtividade("2018-11-03");
        atividade.setTipo(TipoAgenda.SUGESTAO);
        atividade.setDtCadastro(ferramentas.getCurrentDate());
        atividade.setDtAtualizacao(ferramentas.getCurrentDate());
        atividade.setUsuario(getUsuario());
        ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
        Cliente cliente = clienteDAO.buscaClienteId(1);
        atividade.setCliente(cliente);
        List<Atividade> atividades = new ArrayList<>();
        atividades.add(atividade);*/
        //-/TESTE
        progressDialog.dismiss();
        trataAtividades(null);
    }

    public void trataAtividades(List<Atividade> atividadesNew) {
        List<Atividade> atividadesRemov = new ArrayList<>();

        if (atividadesNew != null) {
            //Percorre as atividades sugeridas a fim de remover as atividades que já existem na base
            for (Atividade atividadeOld : listaAtividades) {

                for (Atividade atividadeNew : atividadesNew) {
                    if (atividadeOld.getHoraAtividade().equals(atividadeNew.getHoraAtividade())) {
                        atividadesRemov.add(atividadeNew);
                        break; //Aborta o 'for' interno pois já encontrou registro iguais
                    } else if (atividadeOld.getCliente().getCnpj().equals(atividadeNew.getCliente().getCnpj())) {
                        atividadesRemov.add(atividadeNew);
                        break; //Aborta o 'for' interno pois já encontrou registro iguais
                    }
                }
                //Remove os objetos repetidos já encontrados para não percorrer novamenta na próxima interação
                atividadesNew.removeAll(atividadesRemov);
                atividadesRemov.clear();
            }

            //Percorre as atividades sugeridas válidas populando o tipo para as mesmas.
            // Em seguida adiciona na lista de atividades encontradas internamente.
            for (Atividade atividade : atividadesNew) {
                atividade.setTipo(TipoAgenda.SUGESTAO);
                listaAtividades.add(atividade);
            }
        }

        listaAtividadesAdapter = new ListaAtividadesAdapter(this, listaAtividades);
        listViewAtividades.setAdapter(listaAtividadesAdapter);

    }
}
