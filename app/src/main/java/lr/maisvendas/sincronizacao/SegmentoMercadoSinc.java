package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.segmentoMercado.CarregarSegmentoMercadoCom;
import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.sql.SegmentoMercadoDAO;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class SegmentoMercadoSinc extends BaseActivity implements CarregarSegmentoMercadoCom.CarregarSegmentoMercadoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "SegmentoMercadoSinc";
    private List<SegmentoMercado> segmentoMercadoOld;
    private Notify notify;

    public SegmentoMercadoSinc(Notify notify) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
    }

    public void sincronizaSegmentoMercado(){

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
            new CarregarSegmentoMercadoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarSegmentoMercadoSuccess(List<SegmentoMercado> segmentoMercadoes) {
        trataRegistrosInternos(segmentoMercadoes);
    }

    @Override
    public void onCarregarSegmentoMercadoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,40,false);
    }

    private void trataRegistrosInternos(List<SegmentoMercado> segmentoMercadoes){
        ferramentas.customLog(TAG,"Inicio do tratamento de SegmentoMercado externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        SegmentoMercadoDAO segmentoMercadoDAO = SegmentoMercadoDAO.getInstance(this);
        SegmentoMercado segmentoMercadoTstIdWs;
        try {
            for (SegmentoMercado segmentoMercadoNew:segmentoMercadoes) {

                //Verifica se já existe internamente
                segmentoMercadoTstIdWs = segmentoMercadoDAO.buscaSegmentoMercadoIdWs(segmentoMercadoNew.getIdWS());

                if (segmentoMercadoTstIdWs == null){
                    segmentoMercadoDAO.insereSegmentoMercado(segmentoMercadoNew);
                }else{
                    segmentoMercadoNew.setId(segmentoMercadoTstIdWs.getId());
                    segmentoMercadoDAO.atualizaSegmentoMercado(segmentoMercadoNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,40,false);
        ferramentas.customLog(TAG,"Fim do tratamento de SegmentoMercado externos");
    }
}
