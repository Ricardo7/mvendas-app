package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import lr.maisvendas.R;
import lr.maisvendas.config.EnderecoHost;
import lr.maisvendas.servico.VerificaConexao;
import lr.maisvendas.servico.VerificaServico;
import lr.maisvendas.sincronizacao.CidadeSinc;
import lr.maisvendas.sincronizacao.ClienteSinc;
import lr.maisvendas.sincronizacao.CondicaoPgtoSinc;
import lr.maisvendas.sincronizacao.EstadoSinc;
import lr.maisvendas.sincronizacao.ImagemSinc;
import lr.maisvendas.sincronizacao.PaisSinc;
import lr.maisvendas.sincronizacao.PedidoSinc;
import lr.maisvendas.sincronizacao.ProdutoSinc;
import lr.maisvendas.sincronizacao.SegmentoMercadoSinc;
import lr.maisvendas.sincronizacao.TabelaPrecoSinc;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class SincronicacaoManualActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SincronicacaoManualActivity";
    //Campos da tela
    private Switch switchSincTudo;
    private Switch switchSincImagens;
    private Switch switchSincPedidos;
    private Switch switchSincClientes;
    private Switch switchSincProdutos;
    private Button buttonSinc;

    //Variáveis
    public Notify notify;
    private Ferramentas ferramentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sincronicacao_manual);
        setDrawerLayoutId(R.id.activity_sincronicacao_manual_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Sincronização Manual");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_retornar);

        switchSincTudo = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_tudo);
        switchSincImagens = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_imagens);
        switchSincPedidos = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_pedidos);
        switchSincClientes = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_clientes);
        switchSincProdutos = (Switch) findViewById(R.id.activity_sincronicacao_manual_switch_produtos);
        buttonSinc = (Button) findViewById(R.id.activity_sincronicacao_manual_button_sincronizar);

        ferramentas = new Ferramentas();

        buttonSinc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSinc){

            VerificaConexao verificaConexao = new VerificaConexao();
            VerificaServico verificaServico = new VerificaServico();
            EnderecoHost enderecoHost = new EnderecoHost();
            try {

                if (verificaConexao.isNetworkAvailable(this) && verificaServico.execute(enderecoHost.getHostHTTPRaiz()).get()) {
                    Integer peso = 100;
                    Integer modulos = 0;
                    notify = new Notify(this);
                    notify.iniciaNotificacao("Sincronização", "Sincronização iniciada");

                    /*Verifica quantos módulos serão atualizados para gerar o percentual de avanço da barra de progresso*/
                    if (switchSincTudo.isChecked() || switchSincImagens.isChecked()) {
<<<<<<< HEAD
                        modulos = modulos + 1;
                    }

                    if (switchSincTudo.isChecked() || switchSincProdutos.isChecked()) {
                        modulos = modulos + 1;
                    }

                    if (switchSincTudo.isChecked() || switchSincClientes.isChecked()) {
                        modulos = modulos + 5;
                    }

                    if (switchSincTudo.isChecked() || switchSincPedidos.isChecked()) {
                        modulos = modulos + 3;
                    }
                    peso = peso / modulos;

                    /*Executa a sincronização de acordo com os módulos a serem atualizados*/
                    if (switchSincTudo.isChecked() || switchSincImagens.isChecked()) {

                        ImagemSinc imagemSinc = new ImagemSinc(notify,peso);
                        imagemSinc.sincronizaImagem();

                    }

                    if (switchSincTudo.isChecked() || switchSincProdutos.isChecked()) {

                        ProdutoSinc produtoSinc = new ProdutoSinc(notify,peso);
=======
                        ImagemSinc imagemSinc = new ImagemSinc(notify);
                        imagemSinc.sincronizaImagem();
                    }

                    if (switchSincTudo.isChecked() || switchSincProdutos.isChecked()) {
                        ProdutoSinc produtoSinc = new ProdutoSinc(notify);
>>>>>>> 9c8cd3eb1ea10273ee031473f87c6ba6dc4609e8
                        produtoSinc.sincronizaProduto();
                    }

                    if (switchSincTudo.isChecked() || switchSincClientes.isChecked()) {
                        peso = 100 / 5;
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
                    }

                    if (switchSincTudo.isChecked() || switchSincPedidos.isChecked()) {
<<<<<<< HEAD

                        CondicaoPgtoSinc condicaoPgtoSinc = new CondicaoPgtoSinc(notify,peso);
                        condicaoPgtoSinc.sincronizaCondicaoPgto();

                        TabelaPrecoSinc tabelaPrecoSinc = new TabelaPrecoSinc(notify,peso);
                        tabelaPrecoSinc.sincronizaTabelaPreco();

                        PedidoSinc pedidoSinc = new PedidoSinc(notify,peso);
=======
                        CondicaoPgtoSinc condicaoPgtoSinc = new CondicaoPgtoSinc(notify);
                        condicaoPgtoSinc.sincronizaCondicaoPgto();

                        TabelaPrecoSinc tabelaPrecoSinc = new TabelaPrecoSinc(notify);
                        tabelaPrecoSinc.sincronizaTabelaPreco();

                        PedidoSinc pedidoSinc = new PedidoSinc(notify);
>>>>>>> 9c8cd3eb1ea10273ee031473f87c6ba6dc4609e8
                        pedidoSinc.sincronizaPedido();
                    }

                    //notify.setProgress(100, 100, false);
                    //notify.finalizaNotificacao("Sincronização concluída");
                }
            }catch (Exception ex){
                ferramentas.customLog(TAG,ex.getMessage());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }

}
