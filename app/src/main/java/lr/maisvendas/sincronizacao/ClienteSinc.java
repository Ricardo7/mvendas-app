    package lr.maisvendas.sincronizacao;

    import java.util.Date;
    import java.util.List;

    import lr.maisvendas.comunicacao.cliente.CadastrarClienteCom;
    import lr.maisvendas.comunicacao.cliente.CarregarClienteCom;
    import lr.maisvendas.comunicacao.cliente.EditarClienteCom;
    import lr.maisvendas.modelo.Cliente;
    import lr.maisvendas.modelo.Dispositivo;
    import lr.maisvendas.repositorio.sql.ClienteDAO;
    import lr.maisvendas.repositorio.sql.DispositivoDAO;
    import lr.maisvendas.tela.BaseActivity;
    import lr.maisvendas.utilitarios.Exceptions;
    import lr.maisvendas.utilitarios.Ferramentas;
    import lr.maisvendas.utilitarios.Notify;

    public class ClienteSinc extends BaseActivity implements CarregarClienteCom.CarregarClienteTaskCallBack,CadastrarClienteCom.CadastrarClienteTaskCallBack,EditarClienteCom.EditarClienteTaskCallBack{

        private Ferramentas ferramentas;
        private Dispositivo dispositivo = null;
        private String dataSincronizacao;
        private static final String TAG = "ClienteSinc";
        private List<Cliente> clientesOld;
        private Notify notify;
        private Integer peso;
        private Integer pesoEnvio;

        public ClienteSinc(Notify notify,Integer peso) {
            this.notify = notify;
            this.ferramentas = new Ferramentas();
            //O peso da sincronização é dividido por 2 pois são dois processos, sendo busca e envio de dados
            this.peso = peso / 2;
        }

        public void sincronizaCliente(){

            //Busca a data da última sincronização
            DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);

            dispositivo = dispositivoDAO.buscaDispositivo();

            if (dispositivo == null || dispositivo.getId() <= 0 || dispositivo.getDataSincClientes() == null){
                //Dispositivo ainda não sincronizado
                dataSincronizacao = "2000-01-01 00:00:00";
            }else{
                dataSincronizacao = dispositivo.getDataSincClientes();
            }

            //Antes de atualizar o banco interno com o retorno do servidor, deve buscar os produtos do banco interno
            ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
            clientesOld = clienteDAO.buscaClientesData(dataSincronizacao);

            if (getUsuario() != null && getUsuario().getToken() != null) {
                new CarregarClienteCom(this).execute(getUsuario().getToken(), dataSincronizacao,getUsuario().getIdWS());
            }

        }

        @Override
        public void onCarregarClienteSuccess(List<Cliente> clientes) {

            trataRegistrosInternos(clientes);
            //Chama a função para buscar  os clientes atualizados internamente e envia ao servidor
            trataRegistrosExternos();
        }

        @Override
        public void onCarregarClienteFailure(String mensagem) {
            ferramentas.customLog(TAG,mensagem);

            notify.setProgress(100,peso,false);
            //Chama a função para buscar  os clientes atualizados internamente e envia ao servidor
            trataRegistrosExternos();
        }

        /**
         * Trata Registros recebidos do servidor, atualizando ou inserindo no banco de dados interno.
         * @param clientes
         */
        private void trataRegistrosInternos(List<Cliente> clientes){
            ferramentas.customLog(TAG,"Inicio do tratamento de CLIENTES externos");
            //Recebe os registros atualizados no servidor e atualiza internamente
            ClienteDAO clientesDAO = ClienteDAO.getInstance(this);
            Cliente clienteTstIds;
            try {
                for (Cliente clienteNew:clientes) {
                    //Verifica se já existe internamente
                    clienteTstIds = clientesDAO.buscaClienteIdWs(clienteNew.getIdWS());

                    if (clienteTstIds == null){
                        //Registro não atualizado com servidor ainda ou não existe
                        Cliente clienteTst = clientesDAO.buscaClienteCnpj(clienteNew.getCnpj());
                        if (clienteTst == null){
                            //Registro não existe
                            clientesDAO.insereCliente(clienteNew);
                        }else{
                            //Registro não atualizado com o servidor ainda, mas já existe internamente
                            Date dtAtMdTst = ferramentas.stringToDate(clienteTst.getDtAtualizacao());
                            Date dtAtMdNew = ferramentas.stringToDate(clienteNew.getDtAtualizacao());

                            //Mantem o registro com data maior (VERIFICAR NECESSIDADE DE TIRAR DA LISTA)
                            if (dtAtMdTst.before(dtAtMdNew)){
                                clienteNew.setId(clienteTst.getId());
                                clientesDAO.atualizaCliente(clienteNew);
                                //clientesDAO.insereCliente(clienteNew);
                            }else{
                                clienteTst.setIdWS(clienteNew.getIdWS());
                                clientesDAO.atualizaCliente(clienteTst);
                            }

                        }
                    }else{
                        //Registro já existe internamente e já foi atualizado com o servidor
                        Cliente clienteTst = clientesDAO.buscaClienteCnpj(clienteNew.getCnpj());
                        if (clienteTst == null){
                            //Registro não existe
                            clienteNew.setId(clienteTstIds.getId());
                            clientesDAO.atualizaCliente(clienteNew);
                        }else if(clienteTst != null && clienteTst.getIdWS() == clienteNew.getIdWS()){
                            clienteNew.setId(clienteTst.getId());
                            clientesDAO.atualizaCliente(clienteNew);
                        }else if(clienteTst != null && clienteTst.getIdWS() != clienteNew.getIdWS()){
                            //clientesDAO.deleteCliente(clienteTst.getId());
                            clienteNew.setId(clienteTst.getId());
                            clientesDAO.atualizaCliente(clienteNew);
                        }
                    }
                }
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG,ex.getMessage());
            }

            notify.setProgress(100,peso,false);
            ferramentas.customLog(TAG,"Fim do tratamento de CLIENTES externos");
        }

        private void trataRegistrosExternos(){
            ferramentas.customLog(TAG,"Inicio do tratamento de CLIENTES internos");
            //O peso da parte de envio será subdividido para cada item que será enviado e,
            // a cada item enviado, será incrementando este peso na barra de progresso(notificação)
            if (clientesOld.size() > 0) {
                pesoEnvio = peso / clientesOld.size();
            }else{

                pesoEnvio = peso;
                notify.setProgress(100,pesoEnvio,false);
            }

            for (Cliente clienteOld:clientesOld) {
                if (clienteOld.getIdWS() != null && clienteOld.getIdWS().equals("") == false ) {
                    //Se tiver IDs já existe no servidor, deverá ser atualizado
                    new EditarClienteCom(this).execute(clienteOld);
                } else{
                    ferramentas.customLog(TAG,"inserindo: "+clienteOld.getId());
                    //Se não tiver IDs não existe no servidor, deverá ser inserido no WS e atualizado internamente com o novo IDs
                    new CadastrarClienteCom(this).execute(clienteOld);

                }
            }

            ferramentas.customLog(TAG,"Fim do tratamento de CLIENTES internos");

            atualizaDataSincCliente();
        }

        @Override
        public void onEditarClienteSuccess(Cliente cliente) {
            notify.setProgress(100,pesoEnvio,false);
            ferramentas.customLog(TAG,"Atualizou CLIENTE id ("+cliente.getIdWS()+")");
        }

        @Override
        public void onEditarClienteFailure(String mensagem) {
            notify.setProgress(100,pesoEnvio,false);
            ferramentas.customLog(TAG,mensagem);
        }

        @Override
        public void onCadastrarClienteSuccess(Cliente cliente) {
            //Atualiza o módulo principalmente para setar o IDs, caso ainda não tenha ocorrido
            ClienteDAO clienteDAO = ClienteDAO.getInstance(this);
            try {
                clienteDAO.atualizaCliente(cliente);
            } catch (Exceptions ex) {
                ferramentas.customLog(TAG,ex.getMessage());
            }
            notify.setProgress(100,pesoEnvio,false);
            ferramentas.customLog(TAG,"Inseriu CLIENTE id ("+cliente.getIdWS()+")");
        }

        @Override
        public void onCadastrarClienteFailure(String mensagem) {
            notify.setProgress(100,pesoEnvio,false);
            ferramentas.customLog(TAG,mensagem);
        }

        private void atualizaDataSincCliente() {

            Dispositivo dispositivo = null;
            DispositivoDAO dispositivoDAO = DispositivoDAO.getInstance(this);
            dispositivo = dispositivoDAO.buscaDispositivo();
            if (dispositivo == null || dispositivo.getId() <= 0) {
                dispositivo = new Dispositivo();
                dispositivo.setDataSincClientes(ferramentas.getCurrentDate());

                dispositivoDAO.insereDispositivo(dispositivo);
            } else {
                dispositivo.setDataSincClientes(ferramentas.getCurrentDate());

                try {
                    dispositivoDAO.atualizaDispositivo(dispositivo);
                } catch (Exceptions ex) {
                    ferramentas.customLog(TAG, ex.getMessage());
                }
            }
        }

    }
