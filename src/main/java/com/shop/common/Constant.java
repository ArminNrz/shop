package com.shop.common;

public class Constant {

    /*
    common
     */
    public static final String BASE_URL = "/api/shopping";
    public static final String VERSION = "/v1";
    public static final String EXCEL_FILE_HAS_PROBLEM = "فایل اکسل مشکل دارد";
    public static final String EXCEL_FILE_FORMAT_NOT_CORRECT = "فرمت فایل اکسل نادرست می باشد";
    public static final String FORBIDDEN = "دسترسی به این سرویس با یوزر شما امکان پذیر نمی باشد";
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
    public static final String APP_USER_PHONE_NUMBER_ITERATED = "شماره تلفن توسط کاربر دیگری ثبت شده است";

    /*
    STOCK_MANAGER
     */
    public static final String STOCK_MANAGER_USER_EMPTY = "کاربر نباید خالی باشد";
    public static final String STOCK_MANAGER_TOTAL_NOT_NEGATIVE = "تعداد سهم نباید منفی باشد";
    public static final String STOCK_MANAGER_SAVED_SUCCESS = "سهام کاربر با موفقیت به ثبت رسید";
    public static final String STOCK_MANAGER_USER_EXIST_BEFORE = "اطلاعات سهام این کاربر وجود دارد امکان ایجاد دوباره نیست";
    public static final String STOCK_MANAGER_SALE_DESC = "فروش سهام";
    public static final String STOCK_MANAGER_UPDATE_SALE_STOCK_DESC = "به روز رسانی سهام در حال فروش";
    public static final String STOCK_MANAGER_BUY_STOCK_DESC = "پیشنهاد خرید سهام";
    public static final String STOCK_MANAGER_CANCEL_BUY_DESC = "کنسل شدن پیشنهاد خرید سهام";
    public static final String STOCK_MANAGER_RESET_DESC = "تنظیم اتوماتیک سیستمی وضعیت سهام";
    public static final String STOCK_MANAGER_FINALIZE_SALE = "نهایی سازی فروش سهام";
    public static final String STOCK_MANAGER_FINALIZE_BUY = "نهایی سازی خرید سهام";

    /*
    SALE_STOCK
     */
    public static final String SALE_STOCK_STOCK_COUNT_EMPTY = "تعداد سهم نباید خالی باشد";
    public static final String SALE_STOCK_UNIT_PRICE_EMPTY = "قیمت واحد سهم نباید خالی باشد";
    public static final String SALE_STOCK_NOT_FOUND = "چنین رکوردی برای فروش سهام وجود ندارد";
    public static final String SALE_STOCK_CAN_NOT_UPDATE_UN_OPEN = "رکورد فروش سهام در این وضعیت قابلیت به روز رسانی ندارد";
    public static final String SALE_STOCK_STOCK_COUNT_MORE_THAN_USER_CURRENT_STOCK = "تعداد سهام قرار داده شده برای فروش از تعداد سهام کاربر بیشتر است";
    public static final String SALE_STOCK_NOT_IN_PROPOSE_TO_BUY_STATUS = "امکان ثبت درخواست برای خرید این سهام وجود ندارد";
    public static final String SALE_STOCK_NOT_BELONG_TO_USER = "این درخواست فروش سهام مربوط به کاربر نمی باشد";

    /*
    PROPOSE_BUY_STOCK
     */
    public static final String PROPOSE_BUY_STOCK_COUNT_EMPTY = "تعداد سهم پیشنهادی نباید خالی باشد";
    public static final String PROPOSE_BUY_STOCK_PROPOSE_MORE_THAN_COUNT = "تعداد سهام ویشنهادی برای خرید بیشتر از سهام قرار داده شده برای فروش است";
    public static final String PROPOSE_BUY_STOCK_NOT_FOUND = "چنین پیشنهاد خرید سهامی وجود ندارد";
    public static final String PROPOSE_BUY_STOCK_NOT_IN_OPEN_STATUS = "پیشنهاد خرید سهام در وضعیت باز نیست";
    public static final String PROPOSE_BUY_STOCK_NOT_DELETEABLE = "پیشنهاد خرید سهام در وضعیت قابل حذف شدن نمی باشد";

    /*
    ACCEPTANCE_SALE_STOCK
     */
    public static final String ACCEPTANCE_SALE_STOCK_SELL_TIME_EMPTY = "زمان ثبت قرارداد باید مشخص شود";
    public static final String ACCEPTANCE_SALE_STOCK_LOCATION_EMPTY = "مکان ثبت قرار داد باید مشخص شود";
    public static final String ACCEPTANCE_SALE_STOCK_NOT_EXIST = "چنین قراردادی به ثبت نرسیده است";
    public static final String ACCEPTANCE_SALE_STOCK_NOT_IN_PROPER_STATUS = "این قرارداد در وضعیت قابل انتقال نمی باشد";
}
