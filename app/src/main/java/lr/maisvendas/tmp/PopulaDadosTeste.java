package lr.maisvendas.tmp;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Meta;
import lr.maisvendas.modelo.Pais;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.sql.CidadeDAO;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.repositorio.sql.CondicaoPgtoDAO;
import lr.maisvendas.repositorio.sql.EstadoDAO;
import lr.maisvendas.repositorio.sql.ImagemDAO;
import lr.maisvendas.repositorio.sql.ItemPedidoDAO;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.MetaDAO;
import lr.maisvendas.repositorio.sql.PaisDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.repositorio.sql.SegmentoMercadoDAO;
import lr.maisvendas.repositorio.sql.TabelaPrecoDAO;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.ProcessaArquivo;

public class PopulaDadosTeste {

    private Ferramentas ferramentas;

    public PopulaDadosTeste() {
        ferramentas = new Ferramentas();
    }

    //< PEDIDO>
    public Pedido populaPedido(Context context) {
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(context);

        List<Pedido> pedidos = pedidoDAO.buscaPedidos();
        Pedido pedido = null;

        for (Pedido pedido1: pedidos) {
            ferramentas.customLog("RRRRRR",pedido1.getNumero().toString());
        }

        if (pedidos.isEmpty()) {

            pedido = new Pedido();
            pedido.setNumero(1);
            pedido.setCondicaoPagamento(populaCondicaoPgto(context));
            pedido.setTabelaPreco(populaTabelaPreco(context));
            pedido.setCliente(populaClientes(context));
            pedido.setSituacao(0);
            pedido.setStatus(0);
            pedido.setDtCriacao(ferramentas.getCurrentDate());
            pedido.setDtAtualizacao(ferramentas.getCurrentDate());

            pedido = pedidoDAO.inserePedido(pedido);

            pedido.setItensPedido(populaItensPedido(context,pedido.getId()));
        }


        return pedido;
    }

    public List<ItemPedido> populaItensPedido(Context context, Integer pedidoId) {
        List<ItemPedido> itensPedido = new ArrayList<>();
        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(10.0);
        itemPedido.setVlrUnitario(1.23);
        itemPedido.setVlrDesconto(0.0);
        itemPedido.setVlrTotal(12.3);
        itemPedido.setDtCriacao(ferramentas.getCurrentDate());
        itemPedido.setDtAtualizacao(ferramentas.getCurrentDate());

        //Busca produto da tabela
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);
        itemPedido.setProduto(produtoDAO.buscaProdutoId(1));

        itemPedido = itemPedidoDAO.insereItemPedido(itemPedido,pedidoId);

        itensPedido.add(itemPedido);

        return itensPedido;
    }

    public TabelaPreco populaTabelaPreco(Context context) {
        TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

        TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.setCod("1");
        tabelaPreco.setDescricao("Tabela 001");

        tabelaPreco = tabelaPrecoDAO.insereTabelaPreco(tabelaPreco);

        tabelaPreco.setItensTabelaPreco(populaItemTabelaPreco(context,tabelaPreco));

        return tabelaPreco;
    }

    public List<ItemTabelaPreco> populaItemTabelaPreco(Context context,TabelaPreco tabelaPreco) {
        List<ItemTabelaPreco> itensTabelaPreco = new ArrayList<>();
        ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(context);

        ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
        itemTabelaPreco.setVlrUnitario(1.25);
        itemTabelaPreco.setMaxDesc(10.0);
        itemTabelaPreco.setProduto(populaProduto(context));
        //Popula produto

        itemTabelaPreco = itemTabelaPrecoDAO.insereItemTabelaPreco(itemTabelaPreco,tabelaPreco.getId());

        itensTabelaPreco.add(itemTabelaPreco);

        return itensTabelaPreco;
    }

    public Produto populaProduto(Context context) {

        ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);

        Produto produto = new Produto();
        produto.setCod("3");
        produto.setDescricao("Produto 003");
        produto.setObservacao("Teste de observação de produto." +
                               "  Esta é uma nova linha.");

        //Popular imagens
        //Insere no banco
        produto = produtoDAO.insereProduto(produto);

        produto.setImagens(populaImagens(context,produto.getId()));

        return produto;

    }

    public List<Imagem> populaImagens(Context context,Integer produtoId){
        List<Imagem> imagens = new ArrayList<>();
        ImagemDAO imagemDAO = ImagemDAO.getInstance(context);

        ProcessaArquivo processaArquivo = new ProcessaArquivo();
        File caminho = processaArquivo.customDirectory(processaArquivo.homeDirectory().getPath()+"/maisvendas");

        //Imagem 01
        Imagem imagem = new Imagem();
        imagem.setNome("Produto2");
        imagem.setPrincipal(1);
        imagem.setCaminho(caminho.getPath()+"/Produto2.jpg");
        imagem.setTamanho(1000);
        imagem.setDtCriacao(ferramentas.getCurrentDate());
        imagem.setDtAtualizacao(ferramentas.getCurrentDate());

        imagem = imagemDAO.insereImagem(imagem,produtoId);

        imagens.add(imagem);

        //Imagem 02
        imagem = new Imagem();
        imagem.setNome("produto02_02");
        imagem.setPrincipal(1);
        imagem.setCaminho(caminho.getPath()+"/produto02_02.jpg");
        imagem.setTamanho(1000);
        imagem.setDtCriacao(ferramentas.getCurrentDate());
        imagem.setDtAtualizacao(ferramentas.getCurrentDate());

        imagem = imagemDAO.insereImagem(imagem,produtoId);

        imagens.add(imagem);

        //Imagem 03
        imagem = new Imagem();
        imagem.setNome("Produto02_03");
        imagem.setPrincipal(1);
        imagem.setCaminho(caminho.getPath()+"/Produto02_03.png");
        imagem.setTamanho(1000);
        imagem.setDtCriacao(ferramentas.getCurrentDate());
        imagem.setDtAtualizacao(ferramentas.getCurrentDate());

        imagem = imagemDAO.insereImagem(imagem,produtoId);

        imagens.add(imagem);

        //Imagem 04
        imagem = new Imagem();
        imagem.setNome("Produto02_04");
        imagem.setPrincipal(1);
        imagem.setCaminho(caminho.getPath()+"/Produto02_04.png");
        imagem.setTamanho(1000);
        imagem.setDtCriacao(ferramentas.getCurrentDate());
        imagem.setDtAtualizacao(ferramentas.getCurrentDate());

        imagem = imagemDAO.insereImagem(imagem,produtoId);

        imagens.add(imagem);

        return imagens;
    }

    public CondicaoPagamento populaCondicaoPgto(Context context) {
        CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);

        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
        condicaoPagamento.setCod("1");
        condicaoPagamento.setDescricao("A Vista");
        condicaoPagamento.setDescAcr(15.0);

        condicaoPagamento = condicaoPgtoDAO.insereCondicaoPgto(condicaoPagamento);

        return condicaoPagamento;
    }

    //< CLIENTE>
    public Cliente populaClientes(Context context) {

        ClienteDAO clienteDAO = ClienteDAO.getInstance(context);

        Cliente cliente = new Cliente();
        cliente.setCod("1");
        cliente.setRazaoSocial("Supermercado Andreazza");
        cliente.setNomeFantasia("Andreazza");
        cliente.setCnpj("01234567892245");
        cliente.setInscricaoEstadual("00");
        cliente.setBairro("Serrano");
        cliente.setLogradouro("João Inácio Silva");
        cliente.setNumero(123);
        cliente.setStatus(1);
        cliente.setAtivo(1);
        cliente.setDtCadastro(ferramentas.getCurrentDate());
        cliente.setDtAtualizacao(ferramentas.getCurrentDate());
        cliente.setCidade(populaCidade(context));
        cliente.setSegmentoMercado(populaSegmentomercado(context));

        cliente = clienteDAO.insereCliente(cliente);

        return cliente;
    }

    public Cidade populaCidade(Context context) {
        CidadeDAO cidadeDAO = CidadeDAO.getInstance(context);

        Cidade cidade = new Cidade();
        cidade.setSigla("CXS");
        cidade.setDescricao("Caxias do Sul");
        cidade.setEstado(populaEstado(context));

        cidade = cidadeDAO.insereCidade(cidade);

        return cidade;
    }

    public Estado populaEstado(Context context) {

        EstadoDAO estadoDAO = EstadoDAO.getInstance(context);

        Estado estado = new Estado();
        estado.setSigla("RS");
        estado.setDescricao("Rio Grande do Sul");
        estado.setPais(populaPais(context));

        estado = estadoDAO.insereEstado(estado);

        return estado;
    }

    public Pais populaPais(Context context) {

        PaisDAO paisDAO = PaisDAO.getInstance(context);

        Pais pais = new Pais();
        pais.setSigla("BR");
        pais.setDescricao("Brasil");

        //Insere no banco
        pais = paisDAO.inserePais(pais);

        return pais;

    }

    public SegmentoMercado populaSegmentomercado(Context context) {

        SegmentoMercadoDAO segmentoMercadoDAO = SegmentoMercadoDAO.getInstance(context);

        SegmentoMercado segmentoMercado = new SegmentoMercado();
        segmentoMercado.setDescricao("Nordeste");

        //Insere no banco
        segmentoMercado = segmentoMercadoDAO.insereSegmentoMercado(segmentoMercado);

        return segmentoMercado;

    }
    //</ CLIENTE>
    //</ PEDIDO>

    //<META>
    public List<Meta> populaMeta(Context context) {

        MetaDAO metaDAO = MetaDAO.getInstance(context);

        List<Meta> metas = new ArrayList<>();

        //Meta 01
        Meta meta = new Meta();
        meta.setEstimado(1000.00);
        meta.setRealizado(980.00);

        //Insere no banco
        meta = metaDAO.insereMeta(meta);
        metas.add(meta);

        //Meta 02
        meta = new Meta();
        meta.setEstimado(2000.00);
        meta.setRealizado(1355.00);

        //Insere no banco
        meta = metaDAO.insereMeta(meta);
        metas.add(meta);

        //Meta 03
        meta = new Meta();
        meta.setEstimado(1200.00);
        meta.setRealizado(1050.00);

        //Insere no banco
        meta = metaDAO.insereMeta(meta);
        metas.add(meta);

        //Meta 04
        meta = new Meta();
        meta.setEstimado(800.00);
        meta.setRealizado(900.00);

        //Insere no banco
        meta = metaDAO.insereMeta(meta);
        metas.add(meta);

        return metas;

    }
    //</META>


}
