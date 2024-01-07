const API_BASE = '/api';

// TODO refactor asi
// export const BaseEndpoints = {
//     'AUTH_BASE' : `${API_BASE}/v1/auth`,
// }

// export const AuthEndpoints = {
//     'REFRESH_TOKEN_WITH_BODY': `${BaseEndpoints.AUTH_BASE}/refresh-token-with-body`
// }



export const AUTH_BASE = `${API_BASE}/v1/auth`;
export const STRIPE_CUSTOMER_BASE = `${API_BASE}/v1/stripe/customers`;
export const STRIPE_PAYMENT_INTENTS_BASE = `${API_BASE}/v1/stripe/payment_intents`
export const CATALGO_BASE = `${API_BASE}/v1/catalogo`;
export const USER_BASE = `${API_BASE}/v1/users`;
export const CONFIG_BASE = `${API_BASE}/v1/config`;

export const AUTH_REFRESH_TOKEN = `${AUTH_BASE}/refresh-token`;
export const AUTH_REFRESH_TOKEN_WITH_BODY = `${AUTH_BASE}/refresh-token-with-body`;
export const AUTH_REFRESH_TOKEN_WITH_COOKIE = `${AUTH_BASE}/refresh-token-with-cookie`;
export const AUTH_LOGIN = `${AUTH_BASE}/login`;
export const AUTH_LOGIN_WITH_BODY_ONLY = `${AUTH_BASE}/login-with-body-only`;
export const AUTH_LOGIN_WITH_COOKIE_ONLY = `${AUTH_BASE}/login-with-cookie-only`;
export const AUTH_SIGNUP = `${AUTH_BASE}/signup`;
export const AUTH_PASSWORD_CHANGE_INTENT = `${AUTH_BASE}/password_change/intent`;
export const AUTH_PASSWORD_CHANGE_CONFIRM = `${AUTH_BASE}/password_change/confirm`;
export const AUTH_PASSWORD_CHANGE_VALIDATE_TOKEN = `${AUTH_BASE}/password_change/validate-token`;
export const AUTH_LOGOUT = `${AUTH_BASE}/logout`;
export const AUTH_ACTIVATE_ACCOUNT_ME = `${AUTH_BASE}/activate_account/me`;
export const AUTH_VERIFY_CAPTCHA_TOKEN = `${AUTH_BASE}/verify-captcha`;

export const STRIPE_CUSTOMER_SEARCH =  `${STRIPE_CUSTOMER_BASE}/search`;

export const USER_ME = `${USER_BASE}/me`;



