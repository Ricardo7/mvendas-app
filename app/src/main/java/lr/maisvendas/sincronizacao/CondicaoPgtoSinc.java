package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.condicaoPgto.CarregarCondicaoPgtoCom;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.sql.CondicaoPgtoDAO;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class CondicaoPgtoSinc extends BaseActivity implements CarregarCondicaoPgtoCom.CarregarCondicaoPgtoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "CondicaoPgtoSinc";
    private List<CondicaoPagamento> condicaoPagamentoOld;
    private Notify notify;

    public CondicaoPgtoSinc(Notify notify) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
    }

    public void sincronizaCondicaoPgto(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincPedidos() == null){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincPedidos();
        }

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarCondicaoPgtoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarCondicaoPgtoSuccess(List<CondicaoPagamento> condicaoPgtoes) {
        trataRegistrosInternos(condicaoPgtoes);
    }

    @Override
    public void onCarregarCondicaoPgtoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,30,false);
    }

    private void trataRegistrosInternos(List<CondicaoPagamento> condicaoPgtoes){
        ferramentas.customLog(TAG,"Inicio do tratamento de CondicaoPagamento externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(this);
        CondicaoPagamento condicaoPagamentoTstIdWs;
        try {
            for (CondicaoPagamento condicaoPagamentoNew :condicaoPgtoes) {

                //Verifica se já existe internamente
                condicaoPagamentoTstIdWs = condicaoPgtoDAO.buscaCondicaoPgtoIdWs(condicaoPagamentoNew.getIdWS());

                if (condicaoPagamentoTstIdWs == null){
                    condicaoPgtoDAO.insereCondicaoPgto(condicaoPagamentoNew);
                }else{
                    condicaoPagamentoNew.setId(condicaoPagamentoTstIdWs.getId());
                    condicaoPgtoDAO.atualizaCondicaoPgto(condicaoPagamentoNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,30,false);
        ferramentas.customLog(TAG,"Fim do tratamento de CondicaoPgtoS externos");
    }
}
