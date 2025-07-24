package com.example.demo.entity;
    /**
     * 通用返回结果封装类
     * @param <T> 数据类型
     */
    public class Result<T> {
        private int code;       // 状态码
        private String message; // 返回消息
        private T data;         // 返回数据

        /**
         * 成功静态方法
         * @param data 返回数据
         * @return 成功结果
         */
        public static <T> Result<T> success(T data) {
            Result<T> result = new Result<>();
            result.setCode(200);
            result.setMessage("success");
            result.setData(data);
            return result;
        }

        /**
         * 失败静态方法
         * @param code 错误码
         * @param message 错误信息
         * @return 失败结果
         */
        public static <T> Result<T> error(int code, String message) {
            Result<T> result = new Result<>();
            result.setCode(code);
            result.setMessage(message);
            return result;
        }

        // ============= getter 和 setter 方法 =============

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        // ============= 链式调用方法 =============

        public Result<T> code(int code) {
            this.code = code;
            return this;
        }

        public Result<T> message(String message) {
            this.message = message;
            return this;
        }

        public Result<T> data(T data) {
            this.data = data;
            return this;
        }

        // ============= toString 方法 =============

        @Override
        public String toString() {
            return "Result{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
}
