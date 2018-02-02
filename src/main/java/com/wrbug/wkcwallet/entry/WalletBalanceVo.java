package com.wrbug.wkcwallet.entry;

import com.wrbug.wkcwallet.util.AmountConvert;

import java.math.BigDecimal;

/**
 * WalletBalanceVo
 *
 * @author WrBug
 * @since 2018/1/31
 */
public class WalletBalanceVo {
    /**
     * jsonrpc : 2.0
     * id : 11
     * result : 0x0
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

    public String getBalance() {
        BigDecimal bigDecimal = AmountConvert.fromWei(result, AmountConvert.Unit.ETHER);
        return bigDecimal.toString();
    }
}
