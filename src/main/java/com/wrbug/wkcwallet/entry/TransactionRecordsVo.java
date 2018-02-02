package com.wrbug.wkcwallet.entry;

import com.google.gson.annotations.SerializedName;
import com.wrbug.wkcwallet.util.AmountConvert;

import java.math.BigDecimal;
import java.util.List;

/**
 * TransactionRecordsVo
 *
 * @author WrBug
 * @since 2018/1/31
 */
public class TransactionRecordsVo {

    private int totalnum;
    private List<Record> result;

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public List<Record> getResult() {
        return result;
    }

    public void setResult(List<Record> result) {
        this.result = result;
    }

    public static class Record {

        /**
         * timestamp : 1516257841
         * type : 0
         * tradeAccount : 0xbdc399ec251b90811e7a6d1605620c588f5bda1b
         * amount : 0xe8506d4ec40a0000
         * cost : 0x2386f26fc10000
         * hash : 0x210ef8824e8c418eb08f8e10f3bb8cc5376a8c803f3481adea1c5358f75b0dcd
         * title :
         * extra :
         * order_id : 201801181444008yetbipojnssf10k
         */

        private String timestamp;
        private int type;
        private String tradeAccount;
        private String amount;
        private String cost;
        private String hash;
        private String title;
        private String extra;
        @SerializedName("order_id")
        private String orderId;


        public String getShowAmount() {
            BigDecimal amount = AmountConvert.fromWei(this.amount, AmountConvert.Unit.ETHER);
            BigDecimal cost = AmountConvert.fromWei(this.cost, AmountConvert.Unit.ETHER);
            amount = amount.add(cost);
            String symb = type == 0 ? "-" : "+";
            return symb + amount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimeMill() {
            long time = Long.parseLong(timestamp);
            return time * 1000;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTradeAccount() {
            return tradeAccount;
        }

        public void setTradeAccount(String tradeAccount) {
            this.tradeAccount = tradeAccount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
