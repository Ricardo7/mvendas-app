package lr.maisvendas.modelo;

public class Configuracao {

    private Integer id;
    private Boolean downloadImgWifi;
    private Boolean rastrearGps;
    private Boolean sincImg;
    private Boolean sincPedidos;
    private Boolean sincClientes;
    private Boolean sincProdutos;

    public Configuracao () {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDownloadImgWifi() {
        return downloadImgWifi;
    }

    public void setDownloadImgWifi(Boolean downloadImgWifi) {
        this.downloadImgWifi = downloadImgWifi;
    }

    public Boolean getRastrearGps() {
        return rastrearGps;
    }

    public void setRastrearGps(Boolean rastrearGps) {
        this.rastrearGps = rastrearGps;
    }

    public Boolean getSincImg() {
        return sincImg;
    }

    public void setSincImg(Boolean sincImg) {
        this.sincImg = sincImg;
    }

    public Boolean getSincPedidos() {
        return sincPedidos;
    }

    public void setSincPedidos(Boolean sincPedidos) {
        this.sincPedidos = sincPedidos;
    }

    public Boolean getSincClientes() {
        return sincClientes;
    }

    public void setSincClientes(Boolean sincClientes) {
        this.sincClientes = sincClientes;
    }

    public Boolean getSincProdutos() {
        return sincProdutos;
    }

    public void setSincProdutos(Boolean sincProdutos) {
        this.sincProdutos = sincProdutos;
    }
}
