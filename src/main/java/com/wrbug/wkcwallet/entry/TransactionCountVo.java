package com.wrbug.wkcwallet.entry;

/**
 * TransactionCountVo
 *
 * @author WrBug
 * @since 2018/2/1
 */
public class TransactionCountVo {

    /**
     * jsonrpc : 2.0
     * id : 1
     * result : 0x9
     */

    private String jsonrpc;
    private int id;
    private String result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
