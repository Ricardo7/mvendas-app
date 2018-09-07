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

        cliente.setId(cursor.getInt(0));
        cliente.setCod(cursor.getString(1));
        cliente.setCnpj(cursor.getString(2));
        cliente.setRazaoSocial(cursor.getString(3));
        cliente.setNomeFantasia(cursor.getString(4));
        cliente.setInscricaoEstadual(cursor.getString(5));
        //Cidade
        cliente.setBairro(cursor.getString(7));
        cliente.setLogradouro(cursor.getString(8));
        cliente.setNumero(cursor.getInt(9));
        cliente.setStatus(cursor.getInt(10));
        cliente.setAtivo(cursor.getInt(11));
        //SegmentoMercado
        cliente.setDtCadastro(cursor.getString(13));
        cliente.setDtAtualizacao(cursor.getString(14));

        return cliente;
    }

    public ContentValues clienteToContentValue(Cliente cliente){
        ContentValues content = new ContentValues();

        content.put("ID", cliente.getId());
        content.put("CODIGO", cliente.getCod());
        content.put("CNPJ", cliente.getCnpj());
        content.put("RAZAO_SOCIAL", cliente.getRazaoSocial());
        content.put("NOME_FAN", cliente.getNomeFantasia());
        content.put("INS_EST", cliente.getInscricaoEstadual());
        content.put("CID_ID", cliente.getCidade().getId());
        content.put("BAIRRO", cliente.getBairro());
        content.put("LOGRADOURO", cliente.getLogradouro());
        content.put("NUMERO", cliente.getNumero());
        content.put("STATUS", cliente.getStatus());
        content.put("ATIVO", cliente.getAtivo());
        content.put("SEGMER_ID", cliente.getSegmentoMercado().getId());
        content.put("DT_CADASTRO", cliente.getDtCadastro());
        content.put("DT_ATUALIZACAO", cliente.getDtAtualizacao());
        content.put("SEGMER_ID", cliente.getSegmentoMercado().getId());
        content.put("CID_ID", cliente.getCidade().getId());

        return content;
    }
}
