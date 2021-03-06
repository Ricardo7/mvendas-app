package lr.maisvendas.sincronizacao;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.servico.VerificaServico;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class Sincronizacao extends BaseActivity{

        private Ferramentas ferramentas;
        private static final String TAG = "Sincronizacao";
        private Context context;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        private int notificacaoId = 1;
        public Notify notify;


        public void sincronizaApp(Context context) {
            VerificaConexao verificaConexao = new VerificaConexao();
            VerificaServico verificaServico = new VerificaServico();
            EnderecoHost enderecoHost = new EnderecoHost();
            ferramentas = new Ferramentas();

            if (getUsuario() != null && getUsuario().getToken() != null) {
                try {
                    if (verificaConexao.isNetworkAvailable(context) && verificaServico.execute(enderecoHost.getHostHTTPRaiz()).get()) {
                        Integer peso = 100;
                        Integer modulos = 11;

                        peso = peso / modulos;

                        notify = new Notify(context);
                        notify.iniciaNotificacao("Sincronização", "Sincronização iniciada");

                        ferramentas.customLog(TAG, "Iniciou sincronização");

                        //Cliente
                        //A classe do paisSync, por ser a primeira classe, irá tratar se o usuário está autenticado
                        PaisSinc paisSinc = new PaisSinc(notify,peso);
                        paisSinc.sincronizaPais();

                        EstadoSinc estadoSinc = new EstadoSinc(notify,peso);
                        estadoSinc.sincronizaEstado();

                        CidadeSinc cidadeSinc = new CidadeSinc(notify,peso);
                        cidadeSinc.sincronizaCidade();

                        SegmentoMercadoSinc segmentoMercadoSinc = new SegmentoMercadoSinc(notify,peso);
                        segmentoMercadoSinc.sincronizaSegmentoMercado();

                        ClienteSinc clienteSinc = new ClienteSinc(notify,peso);
                        clienteSinc.sincronizaCliente();

                        //Produto
                        ImagemSinc imagemSinc = new ImagemSinc(notify,peso);
                        imagemSinc.sincronizaImagem();

                        ProdutoSinc produtoSinc = new ProdutoSinc(notify,peso);
                        produtoSinc.sincronizaProduto();

                        //Pedido
                        CondicaoPgtoSinc condicaoPgtoSinc = new CondicaoPgtoSinc(notify,peso);
                        condicaoPgtoSinc.sincronizaCondicaoPgto();

                        TabelaPrecoSinc tabelaPrecoSinc = new TabelaPrecoSinc(notify,peso);
                        tabelaPrecoSinc.sincronizaTabelaPreco();

                        PedidoSinc pedidoSinc = new PedidoSinc(notify,peso);
                        pedidoSinc.sincronizaPedido();

                        //Agenda(Atividades)
                        AtividadeSinc atividadeSinc = new AtividadeSinc(notify,peso);
                        atividadeSinc.sincronizaAtividade();

                        Dispositivo dispositivo = null;
                        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(context);
                        dispositivo = dispositivoDAO.buscaDispositivo();
                        if (dispositivo == null || dispositivo.getId() <= 0) {
                            dispositivo = new Dispositivo();
                            dispositivo.setDataSincImagens(ferramentas.getCurrentDate());
                            dispositivo.setDataSincPedidos(ferramentas.getCurrentDate());
                            dispositivo.setDataSincClientes(ferramentas.getCurrentDate());
                            dispositivo.setDataSincProdutos(ferramentas.getCurrentDate());
                            dispositivo.setDataSincAtividades(ferramentas.getCurrentDate());

                            dispositivoDAO.insereDispositivo(dispositivo);
                        } else {
                            dispositivo.setDataSincImagens(ferramentas.getCurrentDate());
                            dispositivo.setDataSincPedidos(ferramentas.getCurrentDate());
                            dispositivo.setDataSincClientes(ferramentas.getCurrentDate());
                            dispositivo.setDataSincProdutos(ferramentas.getCurrentDate());
                            dispositivo.setDataSincAtividades(ferramentas.getCurrentDate());
                            try {
                                dispositivoDAO.atualizaDispositivo(dispositivo);
                            } catch (Exceptions ex) {
                                ferramentas.customLog(TAG, ex.getMessage());
                            }
                        }

                        ferramentas.customLog(TAG, "Sincronização finalizada");
                    } else {
                        ferramentas.customLog(TAG, "Não foi possível conectar no servidor.");
                    }
                } catch (Exception ex) {
                    ferramentas.customLog(TAG, ex.getMessage());
                }

            }
        }

}
