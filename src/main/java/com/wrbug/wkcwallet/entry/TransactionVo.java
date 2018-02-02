package com.wrbug.wkcwallet.entry;

/**
 * TransactionVo
 *
 * @author WrBug
 * @since 2018/2/1
 */
public class TransactionVo {


    /**
     * error : {"message":"user ip is not allowed","code":-32000}
     * jsonrpc : 2.0
     * id : 1
     */

    private ErrorBean error;
    private String jsonrpc;
    private int id;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

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

    public static class ErrorBean {
        /**
         * message : user ip is not allowed
         * code : -32000
         */

        private String message;
        private int code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
