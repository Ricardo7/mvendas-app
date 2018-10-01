package lr.maisvendas.sincronizacao;

import android.content.Intent;

import java.util.List;

import lr.maisvendas.comunicacao.pais.CarregarPaisCom;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.modelo.Pais;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.PaisDAO;
import lr.maisvendas.repositorio.sql.UsuarioDAO;
import lr.maisvendas.tela.BaseActivity;
import lr.maisvendas.tela.LoginActivity;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.Notify;

public class PaisSinc extends BaseActivity implements CarregarPaisCom.CarregarPaisTaskCallBack{

    private Ferramentas ferramentas;
    private Dispositivo dispositivo = null;
    private String dataSincronizacao;
    private static final String TAG = "PaisSinc";
    private List<Pais> paisOld;
    private Notify notify;

    public PaisSinc(Notify notify) {
        this.notify = notify;
        this.ferramentas = new Ferramentas();
    }

    public void sincronizaPais(){
        notify.setContentText("Sincronizando Dados");

        //Busca a data da última sincronização
        DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

        dispositivo = dispositivoDAO.buscaDispositivo();

        if (dispositivo == null || dispositivo.getId() <= 0){
            //Dispositivo ainda não sincronizado
            dataSincronizacao = "2000-01-01 00:00:00";
        }else{
            dataSincronizacao = dispositivo.getDataSincClientes();
        }

        if (getUsuario() != null && getUsuario().getToken() != null) {
            new CarregarPaisCom(this).execute(getUsuario().getToken(), dataSincronizacao);
        }

    }

    @Override
    public void onCarregarPaisSuccess(List<Pais> paises) {
        trataRegistrosInternos(paises);
    }

    @Override
    public void onCarregarPaisFailure(String mensagem, Integer codigo) {
        try {

            if (codigo == 401) {
                //Caso o usuário não esteja autenticado executa o procedimento abaixo
                //Apaga o registro do usuário da base de dados
                UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(this);
                usuarioDAO.deletaUsuario(getUsuario());

                //Chama a activity de login
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }catch (Exceptions ex){
            ferramentas.customLog(TAG,ex.getMessage());
        }

        notify.setProgress(100,15,false);
    }

    private void trataRegistrosInternos(List<Pais> paises){
        ferramentas.customLog(TAG,"Inicio do tratamento de PAIS externos");
        //Recebe os registros atualizados no servidor e atualiza internamente
        PaisDAO paisDAO = PaisDAO.getInstance(this);
        Pais paisTstIdWs;
        try {
            for (Pais paisNew:paises) {

                //Verifica se já existe internamente
                paisTstIdWs = paisDAO.buscaPaisIdWs(paisNew.getIdWS());

                if (paisTstIdWs == null){
                    paisDAO.inserePais(paisNew);
                }else{
                    paisNew.setId(paisTstIdWs.getId());
                    paisDAO.atualizaPais(paisNew);
                }
            }
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }
        notify.setProgress(100,15,false);
        ferramentas.customLog(TAG,"Fim do tratamento de PAÍSES externos");
    }
}
