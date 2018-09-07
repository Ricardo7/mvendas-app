package lr.maisvendas.utilitarios;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class ProcessaArquivo {

    private static final String TAG = "ProcessaArquivo";

    /**
     * Busca o caminho para o diretório externo do sistema
     *
     * @return destination
     */
    public File homeDirectory() {
        Ferramentas ferramentas = new Ferramentas();
        File destination = null;
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            String home = sdcard.getPath();
            destination = new File(home);
        }catch (Exception ex){
            ferramentas.customLog(TAG,ex.getMessage());
        }

        return destination;
    }

    /**
     * Busca um diretório específico e se não existir cria o mesmo
     * @param path caminho até o diretório especificado
     * @return pastaDestino
     */
    public File customDirectory(String path){

        Ferramentas ferramentas = new Ferramentas();
        File pastaDestino = null;
        try {
            pastaDestino = new File(path);
            // Vefica se o diretíorio de destino existe
            if (!pastaDestino.exists()) {
                // se não existir, cria o diretório
                pastaDestino.mkdir();

                ferramentas.customLog(TAG,"Mandou criar!");
            }
        }catch (Exception ex){
            ferramentas.customLog(TAG,ex.getMessage());
        }

        return pastaDestino;
    }

    public boolean WriteFile(File pathFile, String fileOutPut, String text) {

        try {
            File file = new File(pathFile, fileOutPut);
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(text.getBytes());
            out.write(";\n".getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception ex) {
            //tools.customLog(TAG,ex.getMessage());
            ex.getStackTrace();
            return false;
        }
    }
}
