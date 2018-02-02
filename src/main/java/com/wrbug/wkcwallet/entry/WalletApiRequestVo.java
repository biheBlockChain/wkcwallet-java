package com.wrbug.wkcwallet.entry;


import java.util.ArrayList;
import java.util.List;

/**
 * WalletApiRequestVo
 *
 * @author WrBug
 * @since 2018/1/31
 */
public class WalletApiRequestVo extends BaseVo {


    /**
     * jsonrpc : 2.0
     * method : eth_getTransactionCount
     * params : ["0xe6637d20b74b7cce17d8eacf8516e85ee646fe74","pending"]
     * id : 1
     */

    private String jsonrpc;
    private String method;
    private int id;
    private List params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public WalletApiRequestVo setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public WalletApiRequestVo setMethod(String method) {
        this.method = method;
        return this;
    }

    public int getId() {
        return id;
    }

    public WalletApiRequestVo setId(int id) {
        this.id = id;
        return this;
    }

    public List getParams() {
        return params;
    }

    public WalletApiRequestVo setParams(List params) {
        this.params = params;
        return this;
    }

    public WalletApiRequestVo addParam(Object param) {
        if (this.params == null) {
            params = new ArrayList<>();
        }
        params.add(param);
        return this;
    }
}
