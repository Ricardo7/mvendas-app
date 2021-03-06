package lr.maisvendas.tela;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoAgenda;

public class AgendaActivity extends BaseActivity implements CalendarView.OnDateChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, CarregarAtividadeSugeridaCom.CarregarAtividadeSugeridaTaskCallBack, AdapterView.OnItemLongClickListener {

    private static final String TAG = "AgendaActivity";
    public static final String PARAM_DATA_ATIVIDADE = "PARAM_DATA_ATIVIDADE";
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
    private String dataAtual;

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
        listViewAtividades.setOnItemLongClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Colocado para atualizar a lista ao voltar dos Detalhes, caso tenha mexido em alguma coisa
        if (listaAtividadesAdapter != null) {
            listaAtividadesAdapter.notifyDataSetChanged();
        }
        //loadDataFromActivity();
    }

    //Resultado/retorno de uma Activity seginte
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dataAtividade = null;
        if (data != null) {
            //Pega os parâmetros passados pela intent anterior
            dataAtividade = data.getStringExtra(PARAM_DATA_ATIVIDADE);
        }
        if (dataAtividade != null && dataAtividade.equals("") == false) {
            //Recarrega as atividades para o dia selecionado pelo usuário na atividade
            AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);
            listaAtividades = atividadeDAO.buscaAtividadesDia(dataAtividade);

            try {
                //Seta a data informada pelo usuário no calendário
                calendarView.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(ferramentas.formatDateUser(dataAtividade)).getTime(), true, true);
            } catch (ParseException e) {
                ferramentas.customLog(TAG,e.getMessage());
            }

            //Busca as atividades sugeridas para o dia
            buscaAtividadesSugeridas(dataAtividade);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == buttonAdd) {
            Intent intent = new Intent(this, CadastroAtividadeActivity.class);
            intent.putExtra(CadastroAtividadeActivity.PARAM_DATA_ATUAL,dataAtual);
            // Utilizado o startActivityForResult() para que possa ser retornado as informações de uma activity para outra.
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        month = month + 1;
        dataAtual = year + "-" + String.format("%02d",month) + "-" + String.format("%02d",dayOfMonth);

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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Atividade atividade = listaAtividadesAdapter.getItem(i);

        if(atividade.getIdWS() != null && atividade.getIdWS() != ""){
            ferramentas.customToast(this,"Atividade já sincronizada com o servidor. Não é permitido excluir.");
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
            .setMessage("A atividade será removida." +
                    "\nDeseja continuar?")
            .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(AgendaActivity.this);
                    try {
                        atividadeDAO.deletaAtividade(atividade);
                    } catch (Exceptions ex) {
                        ferramentas.customLog(TAG,ex.getMessage());
                    }
                }
            })
            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int id) {
                    return;
                }
            })
            .show();
        return false;
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {
        dataAtual = ferramentas.getCurrentDate().substring(0, 10);
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
    public void onCarregarAtividadeSugeridaSuccess(List<Atividade> atividades) {
        progressDialog.dismiss();

        trataAtividades(atividades);
    }

    @Override
    public void onCarregarAtividadeSugeridaFailure(String mensagem) {

        progressDialog.dismiss();
        trataAtividades(null);
    }

    public void trataAtividades(List<Atividade> atividadesNew) {
        List<Atividade> atividadesRemov = new ArrayList<>();

        //TESTE
        /*
        Atividade atividadeT = new Atividade();
        atividadeT.setAssunto("Atividade criada pela IA");
        atividadeT.setObservacao("");
        atividadeT.setHoraAtividade("23:15");
        atividadeT.setDataAtividade("2018-11-14");
        atividadeT.setTipo(TipoAgenda.SUGESTAO);
        atividadeT.setDtCadastro(ferramentas.getCurrentDate());
        atividadeT.setDtAtualizacao(ferramentas.getCurrentDate());
        atividadeT.setUsuario(getUsuario());
        ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
        Cliente cliente = clienteDAO.buscaClienteId(4);
        atividadeT.setCliente(cliente);

        if (atividadesNew == null) {
            atividadesNew = new ArrayList<>();
        }

        atividadesNew.add(atividadeT);
*/
        //-/TESTE

        if (atividadesNew != null && listaAtividades != null) {
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
