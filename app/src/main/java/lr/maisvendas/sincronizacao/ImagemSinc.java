package lr.maisvendas.sincronizacao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import lr.maisvendas.utilitarios.ProcessaArquivo;

public class ImagemSinc extends BaseActivity implements CarregarImagemCom.CarregarImagemTaskCallBack {

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "ImagemSinc";
    private List<Imagem> imagemOld;
    private Notify notify;
    private Integer peso;

    public ImagemSinc(Notify notify, Integer peso) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
        this.peso = peso;
    }

    public void sincronizaImagem() {

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincImagens() == null) {
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        } else {
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
        ferramentas.customLog(TAG, mensagem);

        notify.setProgress(100, peso, false);
    }

    private void trataRegistrosInternos(List<Imagem> imagems) {
        ferramentas.customLog(TAG, "Inicio do tratamento de IMAGEM externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        ImagemDAO imagemDAO = ImagemDAO.getInstance(this);
        Imagem imagemTstIdWs;

        try {
            for (Imagem imagemNew : imagems) {

                //Verifica se já existe internamente
                imagemTstIdWs = imagemDAO.buscaImagemIdWs(imagemNew.getIdWS());

                if (imagemTstIdWs == null) {
                    converteImagem(imagemNew);
                    imagemDAO.insereImagem(imagemNew);
                } else {
                    imagemNew.setId(imagemTstIdWs.getId());
                    deleteImage(imagemTstIdWs.getCaminho());
                    converteImagem(imagemNew);
                    imagemDAO.atualizaImagem(imagemNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG, ex.getMessage());
        }

        notify.setProgress(100, peso, false);
        ferramentas.customLog(TAG, "Fim do tratamento de IMAGEMS externos");

        atualizaDataSincImagem();
    }

    private void atualizaDataSincImagem() {

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

    public Imagem converteImagem(Imagem imagem) throws Exceptions{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(imagem.getImagem(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        String caminho = storeImage(decodedImage, imagem.getNome());

        imagem.setCaminho(caminho);
        imagem.setImagem(null);
        return imagem;
    }

    private String storeImage(Bitmap image, String nome) throws Exceptions {
        //Chama a função para montar o arquivo
        File pictureFile = getOutputMediaFile(nome);
        if (pictureFile == null) {
            throw new Exceptions("Erro ao criar o arquivo de mídia, verifique as permissões de armazenamento: ");
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException ex) {
            throw new Exceptions("Arquivo não encontrado:" + ex.getMessage());
        } catch (IOException ex) {
            throw new Exceptions("Erro ao acessar o arquivo: " + ex.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }

    public File getOutputMediaFile(String nome) {
        ProcessaArquivo processaArquivo = new ProcessaArquivo();
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(processaArquivo.homeDirectory()
                + "/maisvendas");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + nome);
        return mediaFile;
    }

    public void deleteImage(String caminho) throws Exceptions{
        File fdelete = new File(caminho);
        if (!fdelete.delete()) {
            throw new Exceptions("Não foi possível remover o arquivo "+caminho);
        }
    }
}

