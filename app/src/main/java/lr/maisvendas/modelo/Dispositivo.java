package lr.maisvendas.modelo;

public class Dispositivo {

    private Integer id;
    private String dataSincImagens;
    private String dataSincPedidos;
    private String dataSincClientes;
    private String dataSincProdutos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataSincImagens() {
        return dataSincImagens;
    }

    public void setDataSincImagens(String dataSincImagens) {
        this.dataSincImagens = dataSincImagens;
    }

    public String getDataSincPedidos() {
        return dataSincPedidos;
    }

    public void setDataSincPedidos(String dataSincPedidos) {
        this.dataSincPedidos = dataSincPedidos;
    }

    public String getDataSincClientes() {
        return dataSincClientes;
    }

    public void setDataSincClientes(String dataSincClientes) {
        this.dataSincClientes = dataSincClientes;
    }

    public String getDataSincProdutos() {
        return dataSincProdutos;
    }

    public void setDataSincProdutos(String dataSincProdutos) {
        this.dataSincProdutos = dataSincProdutos;
    }
}
