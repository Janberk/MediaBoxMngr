package de.canberkdemirkan.mediaboxmngr.interfaces;

public interface UserAuthenticationConstants {
	
	String SIGNUP_URL = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/sign_in.php";

	int DEFAULT_TIMEOUT = 20 * 1000;

	int RESPONSE_CODE_SUCCESS = 0;
	int RESPONSE_CODE_EMPTY_FIELDS = 1;
	int RESPONSE_CODE_INVALID_DATA = 2;
	int RESPONSE_CODE_INVALID_EMAIL = 3;
	int RESPONSE_CODE_NO_POST = 4;
	
	String KEY_EMAIL = "de.canberkdemirkan.mediaboxmngr.key_email";
	String KEY_EMAIL_STATE = "de.canberkdemirkan.mediaboxmngr.key_email_state";
	String KEY_PASSWORD = "de.canberkdemirkan.mediaboxmngr.key_password";
	String KEY_PASSWORD_STATE = "de.canberkdemirkan.mediaboxmngr.key_password_state";
	String KEY_REMEMBER_EMAIL = "de.canberkdemirkan.mediaboxmngr.key_remember_email";
	String KEY_REMEMBER_PASSWORD = "de.canberkdemirkan.mediaboxmngr.key_remember_password";
	
	String KEY_REQUEST_PARAMS_FIRSTNAME = "firstname";
	String KEY_REQUEST_PARAMS_LASTNAME = "lastname";
	String KEY_REQUEST_PARAMS_EMAIL = "email";
	String KEY_REQUEST_PARAMS_PASSWORD = "password";	

}