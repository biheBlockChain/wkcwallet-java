package com.wrbug.wkcwallet;


import com.wrbug.wkcwallet.wallet.Create;
import com.wrbug.wkcwallet.wallet.Info;
import com.wrbug.wkcwallet.wallet.Trade;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("简易钱包交易系统\n喜欢请请打赏支持：0x9a3f8bc4bc644e7626292c74cbe17d9ebaaace75");
        System.out.println("1. 账户转账\n2. 生成钱包\n3. 钱包信息 \n请输入序号");
        int a = scanner.nextInt();
        if (a == 1) {
            Trade.start();
            return;
        }
        if (a == 2) {
            Create.start();
            return;
        }

        if (a==3) {
            Info.start();
        }
    }


}
