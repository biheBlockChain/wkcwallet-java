package com.wrbug.wkcwallet.wallet;

import com.wrbug.wkcwallet.entry.KeystoreInfoBean;
import com.wrbug.wkcwallet.util.FileUtils;
import com.wrbug.wkcwallet.util.JsonHelper;
import org.ethereum.util.FileUtil;
import org.ethereum.wallet.CommonWallet;

import java.io.File;
import java.util.Scanner;

/**
 * 钱包生成
 */
public class Create {
    static Scanner scanner = new Scanner(System.in);
    static String password;

    public static void start() {
        System.out.println("输入钱包密码（不少于8位）");
        password = scanner.next();
        if (password.length() < 8) {
            System.out.println("钱包密码不少于8位");
            return;
        }
        String s = CommonWallet.generate().toV3(password);
        KeystoreInfoBean keystoreInfoBean = JsonHelper.fromJson(s, KeystoreInfoBean.class);
        if (keystoreInfoBean == null) {
            System.out.println("钱包生成失败");
            return;
        }
        System.out.println("钱包已生成：");
        System.out.println(s);
        File file = new File(keystoreInfoBean.getAddress());
        FileUtils.whiteFile(file, s);
        System.out.println("储存路径：" + file.getAbsolutePath());
    }
}
