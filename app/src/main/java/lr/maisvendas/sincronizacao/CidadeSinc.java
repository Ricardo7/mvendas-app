package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.cidade.CarregarCidadeCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.CidadeDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class CidadeSinc extends BaseActivity implements CarregarCidadeCom.CarregarCidadeTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "CidadeSinc";
    private List<Cidade> cidadeOld;
    private Notify notify;
    private Integer peso;

    public CidadeSinc(Notify notify,Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        this.peso = peso;
    }

    public void sincronizaCidade(){

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
            new CarregarCidadeCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarCidadeSuccess(List<Cidade> cidadees) {
        trataRegistrosInternos(cidadees);
    }

    @Override
    public void onCarregarCidadeFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,peso,false);
    }

    private void trataRegistrosInternos(List<Cidade> cidadees){
        ferramentas.customLog(TAG,"Inicio do tratamento de CIDADE externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        CidadeDAO cidadeDAO = CidadeDAO.getInstance(this);
        Cidade cidadeTstIdWs;
        try {
            for (Cidade cidadeNew:cidadees) {

                //Verifica se já existe internamente
                cidadeTstIdWs = cidadeDAO.buscaCidadeIdWs(cidadeNew.getIdWS());

                if (cidadeTstIdWs == null){
                    cidadeDAO.insereCidade(cidadeNew);
                }else{
                    cidadeNew.setId(cidadeTstIdWs.getId());
                    cidadeDAO.atualizaCidade(cidadeNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,peso,false);
        ferramentas.customLog(TAG,"Fim do tratamento de CIDADES externos");
    }
}
