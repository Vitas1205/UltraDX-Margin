package com.fota.fotamargin.common.util.email;

/**
 * @author jintian on 2018/6/24.
 * @see
 */
public class Constant {
    /**
     * 登录错误次数
     */
    public  static final String LOGIN_ERROR_TIMES= "login_error_times";

    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    /**
     * utf-8编码
     */
    public static final String ENCODING_UTF_8="UTF-8";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 用户 状态
     */
    public enum UserStatus {
        /**
         * 禁用
         */
        DISABLE(0),
        /**
         * 正常
         */
        NORMAL(1);

        private int value;

        UserStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Taxonomy类型
     */
    public enum TaxonomyType {
        /**
         * 分类
         */
        CATEGORY(0),
        /**
         * 专题
         */
        FEATURE(1),
        /**
         * 标签
         */
        TAG(2);

        private int value;

        TaxonomyType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Content状态
     */
    public enum ContentStatus {
        /**
         * 草稿
         */
        DRAFT(0),
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 删除
         */
        DELETE(2);

        private int value;

        ContentStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 需要单独写日志的文件
     */
    public enum SpecifyLogFile{
        /**
         *记录发送优惠码失败的日志文件
         */
        ERROR_SEND_COUPON("notifyUserCouponFailed");

        private String filename;

        SpecifyLogFile(String filename){ this.filename = filename; }

        public String getFilename(){ return filename; }
    }

    /**
     * zone
     */
    public enum Zone{
        /**
         *
         */
        COUPON("coupon");

        private String zone;

        Zone(String zone){ this.zone = zone; }

        public String getZone(){ return zone; }
    }

    /**
     * OptionId
     */
    public enum OptionId{

        /**
         *
         */
        CALL(1,"88"),

        /**
         *
         */
        PUT(0,"89");

        private Integer type;

        private String optionId;

        OptionId(Integer type, String optionId) {
            this.type = type;
            this.optionId = optionId;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getOptionId() {
            return optionId;
        }

        public void setOptionId(String optionId) {
            this.optionId = optionId;
        }
    }

    /**
     * SendStatus
     */
    public enum SendStatus{

        /**
         * 0未发送
         */
        UNSENT(0),

        /**
         * 1发送成功
         */
        SUCCESS(1),

        /**
         * 2发送失败
         */
        FAIL(2);

        private Integer sendStatus;

        SendStatus(Integer sendStatus) {
            this.sendStatus = sendStatus;
        }

        public Integer getSendStatus() {
            return sendStatus;
        }

        public void setSendStatus(Integer sendStatus) {
            this.sendStatus = sendStatus;
        }
    }

    public static final String EMAIL_SYMBOL = "@";
}
