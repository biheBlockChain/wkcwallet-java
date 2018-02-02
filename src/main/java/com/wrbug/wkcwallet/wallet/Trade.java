package com.wrbug.wkcwallet.wallet;

import com.wrbug.wkcwallet.entry.KeystoreInfoBean;
import com.wrbug.wkcwallet.entry.TransactionCountVo;
import com.wrbug.wkcwallet.entry.TransactionVo;
import com.wrbug.wkcwallet.util.JsonHelper;
import com.wrbug.wkcwallet.util.TextUtils;
import org.ethereum.core.Transaction;
import org.ethereum.util.Unit;
import org.ethereum.util.Utils;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SignatureException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class Trade {
    static String keystore;
    static String password;
    static String toAddress;
    static double tradeAmount;
    static Scanner scanner = new Scanner(System.in);
    static KeystoreInfoBean keystoreInfo;
    static Wallet wallet;
    static AtomicLong txInd;

    public static void start() {
        System.out.println("输入钱包文件内容：");
        keystore = scanner.next();
        keystoreInfo = JsonHelper.fromJson(keystore, KeystoreInfoBean.class);
        if (keystoreInfo == null) {
            System.out.println("钱包文件不正确");
            return;
        }
        System.out.println("输入钱包密码：");
        password = scanner.next();
        try {
            wallet = CommonWallet.fromV3(keystore, password);
        } catch (GeneralSecurityException e) {
            System.out.println("钱包密码错误");
            return;
        }
        if (wallet == null) {
            System.out.println("钱包密码错误");
            return;
        }
        System.out.println("输入转账地址：");
        toAddress = scanner.next();
        System.out.println("输入转账数量：");
        tradeAmount = scanner.nextDouble();
        step1_getTransactionCount();
        step2_sendRawTransaction();
    }

    /**
     * 第二步，发送交易信息
     */
    private static void step2_sendRawTransaction() {
        BigInteger nonce = BigInteger.valueOf(txInd.getAndIncrement());


        //以下两组数据参考   https://github.com/ImbaQ/MyLinkToken_js/blob/master/js/app.js  57行
        BigInteger gasLimit = new BigInteger("186a0", 16);
        BigInteger gasPrice = new BigInteger("174876e800", 16);

        BigInteger amount = Unit.valueOf(Unit.ether.toString()).toWei(String.valueOf(tradeAmount));

        Transaction tx = Transaction.create(toAddress.replace("0x", ""), amount, nonce, gasPrice, gasLimit, null);
        try {
            tx.sign(wallet);
        } catch (SignatureException e) {
            System.out.println("签名失败");
            System.exit(0);
        }
        byte[] encoded = tx.getEncoded();
        WalletApi.sendRawTransaction("0x" + Hex.toHexString(encoded), new WalletApi.TransactionCallback() {
            @Override
            public void onSuccess(final TransactionVo transactionVo) {
                if (transactionVo == null) {
                    System.out.println("交易失败");
                    return;
                }
                if (TextUtils.isEmpty(transactionVo.getResult())) {
                    System.out.println("交易失败，失败信息信息：" + transactionVo.getError().getMessage());
                    return;
                }
                System.out.println("交易成功, 交易hash：" + transactionVo.getResult());
            }

            @Override
            public void onFailed() {
                System.out.println("交易失败");

            }
        });
    }


    /**
     * 第一步，获取交易次数
     */
    private static void step1_getTransactionCount() {
        TransactionCountVo transactionCount = WalletApi.getTransactionCount(keystoreInfo.getRealAddress());
        BigInteger count = Utils.toBigNumber(transactionCount.getResult());
        txInd = new AtomicLong(count.intValue());
        System.out.println("已交易：" + count + "次");
    }
}
