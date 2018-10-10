package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.estado.CarregarEstadoCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.EstadoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class EstadoSinc extends BaseActivity implements CarregarEstadoCom.CarregarEstadoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "EstadoSinc";
    private List<Estado> estadoOld;
    private Notify notify;
    private Integer peso;

    public EstadoSinc(Notify notify,Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        this.peso = peso;
    }

    public void sincronizaEstado(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincClientes() == null){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincClientes();
        }

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarEstadoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarEstadoSuccess(List<Estado> estadoes) {
        trataRegistrosInternos(estadoes);
    }

    @Override
    public void onCarregarEstadoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,peso,false);
    }

    private void trataRegistrosInternos(List<Estado> estadoes){
        ferramentas.customLog(TAG,"Inicio do tratamento de ESTADO externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        EstadoDAO estadoDAO = EstadoDAO.getInstance(this);
        Estado estadoTstIdWs;
        try {
            for (Estado estadoNew:estadoes) {

                //Verifica se já existe internamente
                estadoTstIdWs = estadoDAO.buscaEstadoIdWs(estadoNew.getIdWS());

                if (estadoTstIdWs == null){
                    estadoDAO.insereEstado(estadoNew);
                }else{
                    estadoNew.setId(estadoTstIdWs.getId());
                    estadoDAO.atualizaEstado(estadoNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,peso,false);
        ferramentas.customLog(TAG,"Fim do tratamento de ESTADOS externos");
    }
}
