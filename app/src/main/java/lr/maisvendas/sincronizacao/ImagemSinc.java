package lr.maisvendas.sincronizacao;

import java.util.List;

import lr.maisvendas.comunicacao.produto.CarregarImagemCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.ImagemDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class ImagemSinc extends BaseActivity implements CarregarImagemCom.CarregarImagemTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "ImagemSinc";
    private List<Imagem> imagemOld;
    private Notify notify;

    public ImagemSinc(Notify notify) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
    }

    public void sincronizaImagem(){

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincImagens() == null){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincImagens();
        }

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarImagemCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarImagemSuccess(List<Imagem> imagems) {
        trataRegistrosInternos(imagems);
    }

    @Override
    public void onCarregarImagemFailure(String mensagem) {
        ferramentas.customLog(TAG,mensagem);

        notify.setProgress(100,30,false);
    }

    private void trataRegistrosInternos(List<Imagem> imagems){
        ferramentas.customLog(TAG,"Inicio do tratamento de IMAGEM externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        ImagemDAO imagemDAO = ImagemDAO.getInstance(this);
        Imagem imagemTstIdWs;
        /*
        try {
            for (Imagem imagemNew:imagems) {

                //Verifica se já existe internamente
                imagemTstIdWs = imagemDAO.buscaImagemIdWs(imagemNew.getIdWS());

                if (imagemTstIdWs == null){
                    imagemDAO.insereImagem(imagemNew);
                }else{
                    imagemNew.setId(imagemTstIdWs.getId());
                    imagemDAO.atualizaImagem(imagemNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        */
        notify.setProgress(100,50,false);
        ferramentas.customLog(TAG,"Fim do tratamento de IMAGEMS externos");

        atualizaDataSincImagem();
    }

    private void atualizaDataSincImagem(){

        Dispositivo dispositivo = null;
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);
        dispositivo = dispositivoDAO.buscaDispositivo();
        if (dispositivo == null || dispositivo.getId() <= 0) {
            dispositivo = new Dispositivo();
            dispositivo.setDataSincImagens(ferramentas.getCurrentDate());

            dispositivoDAO.insereDispositivo(dispositivo);
        } else {
            dispositivo.setDataSincImagens(ferramentas.getCurrentDate());

            try {
                dispositivoDAO.atualizaDispositivo(dispositivo);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG, ex.getMessage());
            }
        }
    }
}
