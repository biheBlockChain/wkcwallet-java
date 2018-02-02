package org.ethereum.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 以太坊金额单位
 * 
 * @author tongsh
 *
 */
public enum Unit {
    noether(0), wei(0), kwei(3), Kwei(3), babbage(3), femtoether(3), mwei(6), Mwei(6), lovelace(
	    6), picoether(6), gwei(9), Gwei(9), shannon(9), nanoether(9), nano(
	    9), szabo(12), microether(12), micro(12), finney(15), milliether(
	    15), milli(15), ether(18), kether(21), grand(
	    21), mether(24), gether(27), tether(
	    30);
    private int scale;

    Unit(int scale) {
	this.scale = scale;
    }

    /**
     * 金额单位转换，从其他单位转为Wei
     * 
     * @param amount 
     *            金额
     * @return 以Wei为单位的金额
     */
    public BigInteger toWei(BigDecimal amount) {
	return amount.movePointRight(this.scale).toBigInteger();
    }


    /**
     * 金额单位转换，从其他单位转为Wei
     * 
     * @param amount
     *            金额，字符串；如果以0x开头则为十六进制
     * @return 以Wei为单位的金额
     */
    public BigInteger toWei(String amount) {
	if(amount == null || amount.length()==0) {
	    return BigInteger.ZERO;
	}
	if(amount.indexOf('.')>0) {
	    //If it's a double value
	    return toWei(new BigDecimal(amount));
	}else {
	    //Maybe HEX or BigInteger
		BigInteger val = Utils.toBigNumber(amount);
		return new BigDecimal(val, 0 - this.scale).toBigInteger();
	}

    }

    /**
     * 从Wei转换为指定单位
     * 
     * @param amount
     *            金额
     * @return
     */
    public BigDecimal fromWei(BigInteger amount) {
	return new BigDecimal(amount, this.scale);
    }

    /**
     * 从Wei转换为指定单位
     * 
     * @param amount
     *            金额
     * @return
     */
    public BigDecimal fromWei(String amount) {
	BigInteger value = Utils.toBigNumber(amount);
	return fromWei(value);
    }

    /**
     * 从Wei转换为指定单位
     * 
     * @param amount
     *            金额
     * @return
     */
    public BigDecimal fromWei(long amount) {
	BigInteger value = BigInteger.valueOf(amount);
	return fromWei(value);
    }

}
