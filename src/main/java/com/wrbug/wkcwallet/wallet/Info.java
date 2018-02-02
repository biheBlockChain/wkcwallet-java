package com.wrbug.wkcwallet.wallet;

import com.wrbug.wkcwallet.entry.TransactionRecordsVo;
import com.wrbug.wkcwallet.entry.WalletBalanceVo;

import java.util.List;
import java.util.Scanner;

/**
 * 钱包信息
 */
public class Info {
    static Scanner scanner = new Scanner(System.in);
    static String address;

    public static void start() {
        System.out.println("输入钱包地址：");
        address = scanner.next();
        getBalance();
        getTransactionRecords();
    }

    /**
     * 获取交易记录
     */
    private static void getTransactionRecords() {
        TransactionRecordsVo transactionRecords = WalletApi.getTransactionRecords(address);
        if (transactionRecords == null) {
            System.out.println("交易记录获取失败");
            return;
        }
        List<TransactionRecordsVo.Record> records = transactionRecords.getResult();
        System.out.println();
        System.out.println("交易信息如下：");
        for (TransactionRecordsVo.Record record : records) {
            System.out.println("---------------------------");
            System.out.println("转账地址：");
            System.out.println(record.getTradeAccount());
            System.out.println("转账数量：");
            System.out.println(record.getShowAmount());
            System.out.println("转账hash：");
            System.out.println(record.getHash());
            System.out.println("---------------------------");
        }
    }

    /**
     * 获取钱包余额
     */
    private static void getBalance() {
        WalletBalanceVo balance = WalletApi.getBalance(address);
        if (balance == null) {
            System.out.println("余额获取失败");
            return;
        }
        System.out.println("当前余额：" + balance.getBalance());
    }
}
