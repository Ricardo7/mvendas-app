package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.tabelaPrecos.CarregarTabelaPrecoCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.TabelaPrecoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class TabelaPrecoSinc extends BaseActivity implements CarregarTabelaPrecoCom.CarregarTabelaPrecoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "TabelaPrecoSinc";
    private List<TabelaPreco> tabelaPrecosOld;
    private Notify notify;
    private Integer peso;

    public TabelaPrecoSinc(Notify notify,Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        this.peso = peso;
    }

    public void sincronizaTabelaPreco(){

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
            new CarregarTabelaPrecoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarTabelaPrecoSuccess(List<TabelaPreco> tabelaPrecos) {

        trataRegistrosInternos(tabelaPrecos);
    }

    @Override
    public void onCarregarTabelaPrecoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
        notify.setProgress(100,peso,false);
    }

    /**
     * Trata Registros recebidos do servidor, atualizando ou inserindo no banco de dados interno.
     * @param tabelaPrecos
     */
    private void trataRegistrosInternos(List<TabelaPreco> tabelaPrecos){
        ferramentas.customLog(TAG,"Inicio do tratamento de TabelaPrecoS externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        TabelaPrecoDAO tabelaPrecosDAO = TabelaPrecoDAO.getInstance(this);
        TabelaPreco tabelaPrecoTst;
        try {
            for (TabelaPreco tabelaPrecoNew:tabelaPrecos) {
                //Verifica se já existe internamente
                tabelaPrecoTst = tabelaPrecosDAO.buscaTabelaPrecoIdWs(tabelaPrecoNew.getIdWS());

                if (tabelaPrecoTst == null){
                    //Registro não existe
                    tabelaPrecoNew = tabelaPrecosDAO.insereTabelaPreco(tabelaPrecoNew);

                }else{
                    //Registro já existe internamente
                    tabelaPrecoNew.setId(tabelaPrecoTst.getId());

                    tabelaPrecosDAO.atualizaTabelaPreco(tabelaPrecoNew);

                }

                trataItemTabelaPreco(tabelaPrecoNew.getItensTabelaPreco(),tabelaPrecoNew.getId());
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,peso,false);
        ferramentas.customLog(TAG,"Fim do tratamento de TabelaPrecoS externos");
    }


    private void trataItemTabelaPreco(List<ItemTabelaPreco> itensTabelaPreco, Integer tabelaPrecoId){

        ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(this);
        ItemTabelaPreco itemTabelaPrecoTst;
        for (ItemTabelaPreco itemTabelaPreco:itensTabelaPreco) {

            itemTabelaPrecoTst = itemTabelaPrecoDAO.buscaItemTabelaPrecoProduto(tabelaPrecoId,itemTabelaPreco.getProduto().getCod());

            if (itemTabelaPrecoTst != null){
                try {
                    itemTabelaPrecoDAO.atualizaItemTabelaPreco(itemTabelaPreco,tabelaPrecoId);
                } catch (Exceptions ex) {
                    ferramentas.customLog(TAG,ex.getMessage());
                }
            }else{
                itemTabelaPrecoDAO.insereItemTabelaPreco(itemTabelaPreco,tabelaPrecoId);
            }
        }

    }

}
