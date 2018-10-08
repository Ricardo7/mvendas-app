package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Cliente;

public class ClienteAdap {

    private Context context;

    public void ClienteAdap(Context context){
        this.context = context;
    }

    public Cliente sqlToCliente(Cursor cursor){
        Cliente cliente = new Cliente();

        cliente.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        cliente.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        cliente.setCod(cursor.getString(cursor.getColumnIndex("CODIGO")));
        cliente.setCnpj(cursor.getString(cursor.getColumnIndex("CNPJ")));
        cliente.setRazaoSocial(cursor.getString(cursor.getColumnIndex("RAZAO_SOCIAL")));
        cliente.setNomeFantasia(cursor.getString(cursor.getColumnIndex("NOME_FAN")));
        cliente.setInscricaoEstadual(cursor.getString(cursor.getColumnIndex("INS_EST")));
        cliente.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
        cliente.setFone(cursor.getString(cursor.getColumnIndex("FONE")));
        cliente.setCep(cursor.getInt(cursor.getColumnIndex("CEP")));
        cliente.setBairro(cursor.getString(cursor.getColumnIndex("BAIRRO")));
        cliente.setLogradouro(cursor.getString(cursor.getColumnIndex("LOGRADOURO")));
        cliente.setNumero(cursor.getInt(cursor.getColumnIndex("NUMERO")));
        cliente.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
        cliente.setAtivo(cursor.getInt(cursor.getColumnIndex("ATIVO")));
        cliente.setDtCadastro(cursor.getString(cursor.getColumnIndex("DT_CADASTRO")));
        cliente.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));


        return cliente;
    }

    public ContentValues clienteToContentValue(Cliente cliente){
        ContentValues content = new ContentValues();

        content.put("ID", cliente.getId());
        content.put("ID_WS", cliente.getIdWS());
        content.put("CODIGO", cliente.getCod());
        content.put("CNPJ", cliente.getCnpj());
        content.put("RAZAO_SOCIAL", cliente.getRazaoSocial());
        content.put("NOME_FAN", cliente.getNomeFantasia());
        content.put("INS_EST", cliente.getInscricaoEstadual());
        content.put("EMAIL", cliente.getEmail());
        content.put("FONE", cliente.getFone());
        content.put("CEP", cliente.getCep());
        content.put("CID_ID", cliente.getCidade().getId());
        content.put("BAIRRO", cliente.getBairro());
        content.put("LOGRADOURO", cliente.getLogradouro());
        content.put("NUMERO", cliente.getNumero());
        content.put("STATUS", cliente.getStatus());
        content.put("ATIVO", cliente.getAtivo());
        content.put("DT_CADASTRO", cliente.getDtCadastro());
        content.put("DT_ATUALIZACAO", cliente.getDtAtualizacao());
        //content.put("SEGMER_ID", cliente.getSegmentoMercado().getId());

        return content;
    }
}
