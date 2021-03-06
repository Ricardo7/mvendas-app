package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.produto.CarregarProdutoCom;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class ProdutoSinc extends BaseActivity implements CarregarProdutoCom.CarregarProdutoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "ProdutoSinc";
    private List<Produto> produtoOld;
    private Notify notify;
    private Integer peso;

    public ProdutoSinc(Notify notify,Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        this.peso = peso;
    }

    public void sincronizaProduto(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincProdutos() == null){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincProdutos();
        }

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarProdutoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }


    }

    @Override
    public void onCarregarProdutoSuccess(List<Produto> produtos) {
        trataRegistrosInternos(produtos);
    }

    @Override
    public void onCarregarProdutoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,peso,false);
    }

    private void trataRegistrosInternos(List<Produto> produtos){
        ferramentas.customLog(TAG,"Inicio do tratamento de PRODUTO externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance(this);
        Produto produtoTstIdWs;
        try {
            for (Produto produtoNew:produtos) {

                //Verifica se já existe internamente
                produtoTstIdWs = produtoDAO.buscaProdutoIdWs(produtoNew.getIdWS());

                if (produtoTstIdWs == null){
                    produtoDAO.insereProduto(produtoNew);
                }else{
                    produtoNew.setId(produtoTstIdWs.getId());
                    produtoDAO.atualizaProduto(produtoNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,peso,false);
        ferramentas.customLog(TAG,"Fim do tratamento de PRODUTOS externos");

        atualizaDataSincProduto();
    }

    private void atualizaDataSincProduto() {

        Dispositivo dispositivo = null;
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);
        dispositivo = dispositivoDAO.buscaDispositivo();
        if (dispositivo == null || dispositivo.getId() <= 0) {
            dispositivo = new Dispositivo();
            dispositivo.setDataSincProdutos(ferramentas.getCurrentDate());

            dispositivoDAO.insereDispositivo(dispositivo);
        } else {
            dispositivo.setDataSincProdutos(ferramentas.getCurrentDate());

            try {
                dispositivoDAO.atualizaDispositivo(dispositivo);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }
        }
    }
}
