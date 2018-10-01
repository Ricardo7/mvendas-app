package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.pedido.CadastrarPedidoCom;
import lr.maisvendas.comunicacao.pedido.CarregarPedidoCom;
import lr.maisvendas.comunicacao.pedido.EditarPedidoCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.ItemPedidoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class PedidoSinc extends BaseActivity implements CarregarPedidoCom.CarregarPedidoTaskCallBack,CadastrarPedidoCom.CadastrarPedidoTaskCallBack,EditarPedidoCom.EditarPedidoTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "PedidoSinc";
    private List<Pedido> pedidosOld;
    private Notify notify;

    public PedidoSinc(Notify notify) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
    }

    public void sincronizaPedido(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincPedidos();
        }

        //Antes de atualizar o banco interno com o retorno do servidor, deve buscar os produtos do banco interno
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
        pedidosOld = pedidoDAO.buscaPedidosData(dataSincronizacao);

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarPedidoCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarPedidoSuccess(List<Pedido> pedidos) {

        trataRegistrosInternos(pedidos);
        //Chama a função para buscar  os pedidos atualizados internamente e envia ao servidor
        trataRegistrosExternos();
    }

    @Override
    public void onCarregarPedidoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
        //Chama a função para buscar  os pedidos atualizados internamente e envia ao servidor
        trataRegistrosExternos();
    }

    /**
     * Trata Registros recebidos do servidor, atualizando ou inserindo no banco de dados interno.
     * @param pedidos
     */
    private void trataRegistrosInternos(List<Pedido> pedidos){
        ferramentas.customLog(TAG,"Inicio do tratamento de PEDIDOS externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        PedidoDAO pedidosDAO = PedidoDAO.getInstance(this);
        Pedido pedidoTst;
        try {
            for (Pedido pedidoNew:pedidos) {
                //Verifica se já existe internamente
                pedidoTst = pedidosDAO.buscaPedidoIdWs(pedidoNew.getIdWS());

                if (pedidoTst == null){
                    //Registro não existe
                    pedidoNew = pedidosDAO.inserePedido(pedidoNew);

                }else{
                    //Registro já existe internamente
                    pedidoNew.setId(pedidoTst.getId());

                    pedidosDAO.atualizaPedido(pedidoNew);

                }

                trataItemPedido(pedidoNew.getItensPedido(),pedidoNew.getId());
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        ferramentas.customLog(TAG,"Fim do tratamento de PEDIDOS externos");
    }

    /**
     * Trata Registros que foram criados ou atualizados no aplicativo, serão enviados ao servidor.
     *
     */
    private void trataRegistrosExternos(){
        ferramentas.customLog(TAG,"Inicio do tratamento de PEDIDOS internos");
        for (Pedido pedidoOld:pedidosOld) {
            if (pedidoOld.getIdWS() != null && pedidoOld.getIdWS().equals("") == false ) {
                //Se tiver IDs já existe no servidor, deverá ser atualizado
                new EditarPedidoCom(this).execute(pedidoOld);
            } else{
                //Se não tiver IDs não existe no servidor, deverá ser inserido no WS e atualizado internamente com o novo IDs
                new CadastrarPedidoCom(this).execute(pedidoOld);

            }
        }

        notify.setProgress(100,100,false);
        ferramentas.customLog(TAG,"Fim do tratamento de PEDIDOS internos");

        atualizaDataSincPedido();
    }

    @Override
    public void onEditarPedidoSuccess(Pedido pedido) {
        ferramentas.customLog(TAG,"Atualizou PEDIDO id ("+pedido.getIdWS()+")");
    }

    @Override
    public void onEditarPedidoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
    }

    @Override
    public void onCadastrarPedidoSuccess(Pedido pedido) {
        //Atualiza o módulo principalmente para setar o IDs, caso ainda não tenha ocorrido
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
        try {
            pedidoDAO.atualizaPedido(pedido);
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        ferramentas.customLog(TAG,"Inseriu PEDIDO id ("+pedido.getIdWS()+")");
    }

    @Override
    public void onCadastrarPedidoFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);
    }

    private void atualizaDataSincPedido() {

        Dispositivo dispositivo = null;
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);
        dispositivo = dispositivoDAO.buscaDispositivo();
        if (dispositivo == null || dispositivo.getId() <= 0) {
            dispositivo = new Dispositivo();
            dispositivo.setDataSincPedidos(ferramentas.getCurrentDate());

            dispositivoDAO.insereDispositivo(dispositivo);
        } else {
            dispositivo.setDataSincPedidos(ferramentas.getCurrentDate());

            try {
                dispositivoDAO.atualizaDispositivo(dispositivo);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }
        }
    }

    private void trataItemPedido(List<ItemPedido> itensPedido, Integer pedidoId){

        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(this);
        ItemPedido itemPedidoTst = null;
        for (ItemPedido itemPedido:itensPedido) {

            itemPedidoTst = itemPedidoDAO.buscaItemPedidoProduto(pedidoId,itemPedido.getProduto().getCod());

            if (itemPedidoTst != null){
                try {
                    itemPedidoDAO.atualizaItemPedido(itemPedido,pedidoId);
                } catch (Exceptions ex) {
                    ferramentas.customLog(TAG,ex.getMessage());
                }
            }else{
                itemPedidoDAO.insereItemPedido(itemPedido,pedidoId);
            }
        }

    }

}
