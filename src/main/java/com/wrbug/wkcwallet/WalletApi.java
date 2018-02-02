package com.wrbug.wkcwallet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wrbug.wkcwallet.entry.*;
import com.wrbug.wkcwallet.util.HttpUtil;
import com.wrbug.wkcwallet.util.JsonHelper;
import com.wrbug.wkcwallet.util.TextUtils;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * WalletApi
 *
 * @author WrBug
 * @since 2018/1/31
 */
public class WalletApi {


    /**
     * 获取交易次数
     * @param address
     * @return
     */
    public static TransactionCountVo getTransactionCount(String address) {
        WalletApiRequestVo walletApiRequestVo = new WalletApiRequestVo();
        walletApiRequestVo.setJsonrpc("2.0")
                .setMethod("eth_getTransactionCount")
                .addParam(address)
                .addParam("pending")
                .setId(1);
        try {
            Response response = HttpUtil.postWalletApi(walletApiRequestVo);
            String json = response.body().string();
            TransactionCountVo transactionCountVo = JsonHelper.fromJson(json, TransactionCountVo.class);
            return transactionCountVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送交易
     * @param hash
     * @param callback
     */
    public static void sendRawTransaction(String hash, TransactionCallback callback) {
        WalletTradeApiRequestVo walletApiRequestVo = new WalletTradeApiRequestVo();
        walletApiRequestVo.setJsonrpc("2.0")
                .setMethod("eth_sendRawTransaction")
                .addParam(hash)
                .setId(1);
        Headers headers = new Headers.Builder()
                .add("Nc", "IN")
                .build();
        try {
            Response response = HttpUtil.postWalletApi(walletApiRequestVo, headers);
            String json = response.body().string();
            TransactionVo transactionVo = JsonHelper.fromJson(json, TransactionVo.class);
            callback.onSuccess(transactionVo);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.onFailed();
    }



    public interface TransactionCallback {
        void onSuccess(TransactionVo transactionVo);

        void onFailed();
    }
}
