package lr.maisvendas.tela.interfaces;

import lr.maisvendas.modelo.Pedido;

/**
 * Interface criada para implementar a comunicação entre a activity de cadastro de pedido e seus fragmentos.
 * Dando assim, uma possíbilidade dos fragmentos se comunicarem com a interface principal.
 */
public interface ComunicadorCadastroPedido {

    public Pedido getPedido();

    public void setPedido(Pedido pedido);
}
