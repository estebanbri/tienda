package com.example.tiendabackend.controller.endpoints;

public class ApiConstants {


    private ApiConstants(){}

    public static final String API_BASE = "/api";
    public static final String V1 = API_BASE + "/v1";
    public static final String ID = "/{id}";

    public static final String AUTH_BASE = "/auth";
    public static final String LOGIN = AUTH_BASE + "/login";
    public static final String LOGIN_WITH_BODY_ONLY = AUTH_BASE + "/login-with-body-only";
    public static final String LOGIN_WITH_COOKIE_ONLY = AUTH_BASE + "/login-with-cookie-only";
    public static final String LOGIN_WITH_BODY_AND_COOKIE = AUTH_BASE + "/login-with-body-and-cookie";
    public static final String SIGNUP = AUTH_BASE + "/signup";
    public static final String REFRESH_TOKEN = AUTH_BASE + "/refresh-token";
    public static final String REFRESH_TOKEN_WITH_BODY = AUTH_BASE + "/refresh-token-with-body";
    public static final String REFRESH_TOKEN_WITH_COOKIE = AUTH_BASE + "/refresh-token-with-cookie";
    public static final String ACCOUNT_ACTIVATE_ME = AUTH_BASE + "/activate_account/me";
    public static final String PASSWORD_CHANGE_INTENT = AUTH_BASE + "/password_change/intent";
    public static final String PASSWORD_CHANGE_VALIDATE_TOKEN =AUTH_BASE + "/password_change/validate-token" ;
    public static final String PASSWORD_CHANGE_CONFIRM = AUTH_BASE + "/password_change/confirm";

    public static final String USERS_BASE = "/users";
    public static final String USERS_ME = USERS_BASE + "/me";
    public static final String USER_UPDATE = USERS_BASE + ID;

    public static final String CATALOGOS_BASE = "/catalogo";

    public static final String ACCESS_TOKEN_COOKIE_NAME = "access-token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    public static final String STRIPE_CUSTOMERS_BASE = "/stripe/customers";
    public static final String STRIPE_CUSTOMERS_BY_ID = STRIPE_CUSTOMERS_BASE + ID;
    public static final String STRIPE_CUSTOMER_SEARCH_EMAIL = STRIPE_CUSTOMERS_BASE + "/search/{email}";

    public static final String STRIPE_PAYMENT_INTENTS_BASE = "/stripe/payment_intents";
    public static final String STRIPE_PAYMENT_INTENTS_BY_ID = STRIPE_PAYMENT_INTENTS_BASE +  ID;
    public static final String STRIPE_PAYMENT_INTENTS_CONFIRM = STRIPE_PAYMENT_INTENTS_BASE +  "/confirm" ;
    public static final String STRIPE_PAYMENT_INTENTS_CANCEL = STRIPE_PAYMENT_INTENTS_BASE +  "/cancel" ;

    public static final String VERIFY_CAPTCHA = AUTH_BASE + "/verify-captcha";

    public static final String CONFIG_BASE = "/config";

}
