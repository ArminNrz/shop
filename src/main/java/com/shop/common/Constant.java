package com.shop.common;

public class Constant {

    /*
    common
     */
    public static final String BASE_URL = "/api/shopping";
    public static final String VERSION = "/v1";
    public static final String EXCEL_FILE_HAS_PROBLEM = "فایل اکسل مشکل دارد";
    public static final String EXCEL_FILE_FORMAT_NOT_CORRECT = "فرمت فایل اکسل نادرست می باشد";
    public static final String INTERNAL_ERROR = "خطای داخلی";

    /*
    APP_USER
     */
    public static final String APP_USER_FIRST_NAME_EMPTY = "نام کاربر خالی می باشد";
    public static final String APP_USER_LAST_NAME_EMPTY = "نام خانوادگی کاربر خالی می باشد";
    public static final String APP_USER_PHONE_NUMBER_EMPTY = "شماره تلفن کاربر خالی می باشد";
    public static final String APP_USER_PASSWORD_EMPTY = "رمز عبور کاربر خالی می باشد";
    public static final String APP_USER_NOT_FOUND = "کاربری با این مشخصات وجود ندارد";
    public static final String APP_USER_OTP_WRONG = "کد یکبار مصرف اشتباه است";
    public static final String APP_USER_OTP_EMPTY = "کد یکبار مصرف الزامی است";
    public static final String APP_USER_NEW_PASSWORD_EMPTY = "کلمه عبور الزامی می باشد";

    /*
    STOCK_MANAGER
     */
    public static final String STOCK_MANAGER_USER_EMPTY = "کاربر نباید خالی باشد";
    public static final String STOCK_MANAGER_TOTAL_NOT_NEGATIVE = "تعداد سهم نباید منفی باشد";
    public static final String STOCK_MANAGER_SAVED_SUCCESS = "سهام کاربر با موفقیت به ثبت رسید";
    public static final String STOCK_MANAGER_USER_EXIST_BEFORE = "اطلاعات سهام این کاربر وجود دارد امکان ایجاد دوباره نیست";
}
