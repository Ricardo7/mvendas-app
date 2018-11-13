package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.atividade.CadastrarAtividadeCom;
import lr.maisvendas.comunicacao.atividade.CarregarAtividadeCom;
import lr.maisvendas.comunicacao.atividade.EditarAtividadeCom;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.sql.AtividadeDAO;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class AtividadeSinc extends BaseActivity implements CarregarAtividadeCom.CarregarAtividadeTaskCallBack,CadastrarAtividadeCom.CadastrarAtividadeTaskCallBack,EditarAtividadeCom.EditarAtividadeTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "AtividadeSinc";
    private List<Atividade> atividadesOld;
    private Notify notify;
    private Integer peso;
    private Integer pesoEnvio;

    public AtividadeSinc(Notify notify, Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        //O peso da sincronização é dividido por 2 pois são dois processos, sendo busca e envio de dados
        this.peso = peso / 2;
    }

    public void sincronizaAtividade(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincAtividades() == null){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincAtividades();
        }

        ferramentas.customLog("RRRRR",dataSincronizacao);
        //Antes de atualizar o banco interno com o retorno do servidor, deve buscar os produtos do banco interno
        AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);
        atividadesOld = atividadeDAO.buscaAtividadesData(dataSincronizacao);

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarAtividadeCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarAtividadeSuccess(List<Atividade> Atividades) {

        trataRegistrosInternos(Atividades);
        //Chama a função para buscar  os Atividades atualizados internamente e envia ao servidor
        trataRegistrosExternos();
    }

    @Override
    public void onCarregarAtividadeFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,peso,false);
        //Chama a função para buscar  os Atividades atualizados internamente e envia ao servidor
        trataRegistrosExternos();
    }

    /**
     * Trata Registros recebidos do servidor, atualizando ou inserindo no banco de dados interno.
     * @param Atividades
     */
    private void trataRegistrosInternos(List<Atividade> Atividades){
        ferramentas.customLog(TAG,"Inicio do tratamento de AtividadeS externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        AtividadeDAO atividadesDAO = AtividadeDAO.getInstance(this);
        Atividade atividadeTst;
        try {
            for (Atividade atividadeNew:Atividades) {
                //Verifica se já existe internamente
                atividadeTst = atividadesDAO.buscaAtividadeIdWs(atividadeNew.getIdWS());

                if (atividadeTst == null){
                    //Registro não existe
                    atividadeNew = atividadesDAO.insereAtividade(atividadeNew);

                }else{
                    //Registro já existe internamente
                    atividadeNew.setId(atividadeTst.getId());

                    atividadesDAO.atualizaAtividade(atividadeNew);

                }

            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,peso,false);
        ferramentas.customLog(TAG,"Fim do tratamento de AtividadeS externos");
    }

    /**
     * Trata Registros que foram criados ou atualizados no aplicativo, serão enviados ao servidor.
     *
     */
    private void trataRegistrosExternos(){
        ferramentas.customLog(TAG,"Inicio do tratamento de AtividadeS internos");

        //O peso da parte de envio será subdividido para cada item que será enviado e,
        // a cada item enviado, será incrementando este peso na barra de progresso(notificação)
        if (atividadesOld.size() > 0) {
            pesoEnvio = peso / atividadesOld.size();
        }else{

            pesoEnvio = peso;
            notify.setProgress(100,pesoEnvio,false);
        }

        for (Atividade atividadeOld:atividadesOld) {

            if (atividadeOld.getIdWS() != null && atividadeOld.getIdWS().equals("") == false ) {
                //Se tiver IDs já existe no servidor, deverá ser atualizado
                new EditarAtividadeCom(this).execute(atividadeOld);
            } else{

                //Se não tiver IDs não existe no servidor, deverá ser inserido no WS e atualizado internamente com o novo IDs
                new CadastrarAtividadeCom(this).execute(atividadeOld);

            }
        }

        ferramentas.customLog(TAG,"Fim do tratamento de AtividadeS internos");

        atualizaDataSincAtividade();
    }

    @Override
    public void onEditarAtividadeSuccess(Atividade atividade) {
        ferramentas.customLog(TAG,"Atualizou Atividade id ("+atividade.getIdWS()+")");
        notify.setProgress(100,pesoEnvio,false);
    }

    @Override
    public void onEditarAtividadeFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
        notify.setProgress(100,pesoEnvio,false);
    }

    @Override
    public void onCadastrarAtividadeSuccess(Atividade atividade) {
        //Atualiza o módulo principalmente para setar o IDWS, caso ainda não tenha ocorrido
        AtividadeDAO atividadeDAO = AtividadeDAO.getInstance(this);
        try {
            atividadeDAO.atualizaAtividade(atividade);
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,pesoEnvio,false);
        ferramentas.customLog(TAG,"Inseriu Atividade id ("+atividade.getIdWS()+")");
    }

    @Override
    public void onCadastrarAtividadeFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
        notify.setProgress(100,pesoEnvio,false);
    }

    private void atualizaDataSincAtividade() {

        Dispositivo dispositivo = null;
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);
        dispositivo = dispositivoDAO.buscaDispositivo();
        if (dispositivo == null || dispositivo.getId() <= 0) {
            dispositivo = new Dispositivo();
            dispositivo.setDataSincAtividades(ferramentas.getCurrentDate());

            dispositivoDAO.insereDispositivo(dispositivo);
        } else {
            dispositivo.setDataSincAtividades(ferramentas.getCurrentDate());

            try {
                dispositivoDAO.atualizaDispositivo(dispositivo);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }
        }
        notify.setProgress(100,pesoEnvio,false);
    }


}
