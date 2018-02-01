package rx.leancloud.core;

import rx.leancloud.internal.AVClassName;
import rx.leancloud.internal.IKVStorage;

@AVClassName("_User")
public class RxAVUser extends RxAVObject {

    public RxAVUser() {
        super("_User");
        RxAVUser.kvStorage =  RxAVCorePlugins.getInstance().getKVStorage();
    }

    static IKVStorage kvStorage;

    static IKVStorage getKVStorage() {
        if (RxAVUser.kvStorage == null) {
            RxAVUser.kvStorage = RxAVCorePlugins.getInstance().getKVStorage();
        }
        return RxAVUser.kvStorage;
    }

    private static final String KEY_SESSION_TOKEN = "sessionToken";
    private static final String KEY_AUTH_DATA = "authData";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE_PHONE_NUMBER = "mobilePhoneNumber";

    public void setUsername(String username) {
        this.set(KEY_USERNAME, username);
    }

    public String getUsername() {
        return this.get(KEY_USERNAME);
    }

    public void setPassword(String password) {
        this.set(KEY_PASSWORD, password);
    }

    public void setEmail(String email) {
        this.set(KEY_EMAIL, email);
    }

    public String getEmail() {
        return this.get(KEY_EMAIL);
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.set(KEY_MOBILE_PHONE_NUMBER, mobilePhoneNumber);
    }

    public String getMobilePhoneNumber() {
        return this.get(KEY_MOBILE_PHONE_NUMBER);
    }

}
