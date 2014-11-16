package com.mtu.ito.fotaito.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.mtu.ito.fotaito.data.pojos.Offer;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Database manager to connect to azure back end. Stores received offers/coupons for later use.
 *
 * @author Kyle Oswald
 */
public class AzureDatabaseManager implements DatabaseManager {
    private static final String TAG = AzureDatabaseManager.class.getSimpleName();

    private static AzureDatabaseManager _instance;

    public static AzureDatabaseManager getInstance(final Context context) {
        if (_instance == null) {
            _instance = new AzureDatabaseManager(context);
        } else {
            //_instance.setContext(context); causes a crash on logout for whatever reason
        }

        return _instance;
    }

    // SharedPreference resources for storing login token
    private static final String TOKEN_PREFERENCES = "Azure.TOKEN_PREFERENCES";
    private static final String KEY_ACCESS_TOKEN  = "Azure.KEY_ACCESS_TOKEN";
    private static final String KEY_USER_ID       = "Azure.KEY_USER_ID";

    private static final String VALUE_UNDEFINED = "undefined";

    // Azure url & key to use when making client
    private static final String APP_URL = "https://fotaitoservice.azure-mobile.net/";
    private static final String APP_KEY = "PhuGHIaLjhNSDPniKIdlUssWSdKeyC68";

    private final Object AUTHENTICATION_LOCK = new Object();

    private final MobileServiceClient _client;

    private boolean _isLoggedIn;

    private final AtomicBoolean _isAuthenticating;

    private AzureDatabaseManager(final Context context) {
        try {
            _client = new MobileServiceClient(APP_URL, APP_KEY, context)
                    .withFilter(new RefreshTokenCacheFilter());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        _isLoggedIn = false;
        _isAuthenticating = new AtomicBoolean(false);
    }

    @Override
    public void putOffers(final List<Offer> offerList) {

    }

    @Override
    public List<Offer> getOffers() {
        return null;
    }

    public void login(final LoginCallback callback) {
        authenticate(false, callback);
    }

    @Override
    public boolean login() {
        return false;
    }

    public boolean isLoggedIn() {
        synchronized (AUTHENTICATION_LOCK) {
            return _isLoggedIn;
        }
    }

    /**
     * Logs out the user <b>AND</b> clears the token cache that subsequent
     * logins will be forced to re-authenticate through an identity provider.
     */
    @Override
    public void logout() {
        synchronized (AUTHENTICATION_LOCK) {
            _client.logout();
            _isLoggedIn = false;
            clearTokenCache();
        }
    }

    @Override
    public Context getContext() {
        return _client.getContext();
    }

    /**
     * Sets the context used by the Azure MobileServiceClient.
     *
     * @param context Android application context
     */
    @Override
    public void setContext(final Context context) {
        _client.setContext(context);
    }

    public String getLoggedInUserId() {
        return _client.getCurrentUser().getUserId();
    }

    /**
     * Authenticates with the desired login provider. Also caches the token.
     *
     * If a local token cache is detected, the token cache is used instead of an actual
     * login unless bRefresh is set to true forcing a refresh.
     *
     * @param refreshCache Indicates whether to force a token refresh.
     */
    private void authenticate(final boolean refreshCache, final LoginCallback callback) {
        // If not already authenticating then do so & set _isAuthenticating to true
        if (_isAuthenticating.compareAndSet(false, true)) {
            if (refreshCache || !loadUserTokenCache()) {
                // New login using the provider and update the token cache.
                final ListenableFuture<MobileServiceUser> loginFuture =
                        _client.login(MobileServiceAuthenticationProvider.Google);
                Futures.addCallback(loginFuture, new FutureCallback<MobileServiceUser>() {
                    @Override
                    public void onFailure(final Throwable e) {
                        synchronized (AUTHENTICATION_LOCK) {
                            Log.e(TAG, "Authentication failure", e);

                            _isAuthenticating.set(false);
                            _isLoggedIn = false;
                            AUTHENTICATION_LOCK.notifyAll();
                        }

                        if (callback != null) {
                            callback.onFailure(AzureDatabaseManager.this);
                        }
                    }

                    @Override
                    public void onSuccess(final MobileServiceUser user) {
                        synchronized (AUTHENTICATION_LOCK) {
                            Log.d(TAG, "Logged in user " + user.getUserId());

                            _client.setCurrentUser(user);
                            cacheUserToken(user);
                            _isAuthenticating.set(false);
                            _isLoggedIn = true;
                            AUTHENTICATION_LOCK.notifyAll();
                        }

                        if (callback != null) {
                            callback.onSuccess(AzureDatabaseManager.this);
                        }
                    }
                });
            } else {
                synchronized (AUTHENTICATION_LOCK) {
                    Log.d(TAG, "Logged in user " + _client.getCurrentUser().getUserId()
                            + " with cached token.");
                    _isAuthenticating.set(false);
                    _isLoggedIn = true;
                    AUTHENTICATION_LOCK.notifyAll();
                }

                if (callback != null) {
                    callback.onSuccess(AzureDatabaseManager.this);
                }
            }
        }
    }

    private void clearTokenCache() {
        final SharedPreferences prefs = _client.getContext()
                .getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_ACCESS_TOKEN);
        editor.commit();
    }

    private void cacheUserToken(final MobileServiceUser user) {
        final SharedPreferences prefs = _client.getContext()
                .getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_ACCESS_TOKEN, user.getAuthenticationToken());
        editor.commit();
    }

    private boolean loadUserTokenCache() {
        final SharedPreferences prefs = _client.getContext()
                .getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);

        final String userId = prefs.getString(KEY_USER_ID, VALUE_UNDEFINED);
        if (userId.equals(VALUE_UNDEFINED)) {
            return false;
        }

        final String token = prefs.getString(KEY_ACCESS_TOKEN, VALUE_UNDEFINED);
        if (token.equals(VALUE_UNDEFINED)) {
            return false;
        }

        final MobileServiceUser user = new MobileServiceUser(userId);
        user.setAuthenticationToken(token);
        _client.setCurrentUser(user);

        return true;
    }

    /**
     * Detects if authentication is in progress and waits for it to complete.
     * Returns true if authentication was detected as in progress and completed
     * successfully. False otherwise.
     */
    public boolean detectAndWaitForAuthenticationSuccess() {
        synchronized (AUTHENTICATION_LOCK) {
            boolean ret = false;

            while (_isAuthenticating.get() == true) {
                ret = true;

                try {
                    AUTHENTICATION_LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // propagate interrupt
                }
            }

            return ret && _isLoggedIn;
        }
    }

    /**
     * Waits for authentication to complete then adds or updates the token
     * in the X-ZUMO-AUTH request header.
     *
     * @param request
     *            The request that receives the updated token.
     */
    private void waitAndUpdateRequestToken(final ServiceFilterRequest request) {
        if (detectAndWaitForAuthenticationSuccess()) {
            setTokenHeader(request);
        }
    }

    private void setTokenHeader(final ServiceFilterRequest request) {
        final MobileServiceUser user = _client.getCurrentUser();

        if (user != null) {
            request.removeHeader("X-ZUMO-AUTH");
            request.addHeader("X-ZUMO-AUTH", user.getAuthenticationToken());
        }
    }

    /**
     * The RefreshTokenCacheFilter class filters responses for HTTP status code 401.
     * When 401 is encountered, the filter calls the authenticate method on the
     * UI thread. Out going requests and retries are blocked during authentication.
     * Once authentication is complete, the token cache is updated and
     * any blocked request will receive the X-ZUMO-AUTH header added or updated to
     * that request.
     */
    private class RefreshTokenCacheFilter implements ServiceFilter {
        AtomicBoolean mAtomicAuthenticatingFlag = new AtomicBoolean();

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(final ServiceFilterRequest request,
                final NextServiceFilterCallback nextServiceFilterCallback) {
            // In this example, if authentication is already in progress we block the request
            // until authentication is complete to avoid unnecessary authentications as
            // a result of HTTP status code 401.
            // If authentication was detected, add the token to the request.
            waitAndUpdateRequestToken(request);

            // Send the request down the filter chain
            // retrying up to 5 times on 401 response codes.
            ListenableFuture<ServiceFilterResponse> future = null;
            ServiceFilterResponse response = null;
            int responseCode = 401;
            for (int i = 0; i < 5 && responseCode == 401; i++) {
                future = nextServiceFilterCallback.onNext(request);
                try {
                    response = future.get();
                    responseCode = response.getStatus().getStatusCode();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    if (e.getCause().getClass() == MobileServiceException.class) {
                        final MobileServiceException mEx = (MobileServiceException) e.getCause();

                        responseCode = mEx.getResponse().getStatus().getStatusCode();
                        if (responseCode == 401) {
                            authenticate(true, new LoginCallback() { // Wait for authentication to complete then update the token in the request.
                                @Override
                                public void onSuccess(AzureDatabaseManager sender) {
                                    setTokenHeader(request);
                                }

                                @Override
                                public void onFailure(AzureDatabaseManager sender) {
                                    // TODO idk
                                }
                            });
                        }
                    }
                }
            }

            return future;
        }
    }

    public interface LoginCallback {
        public void onSuccess(AzureDatabaseManager sender);

        public void onFailure(AzureDatabaseManager sender);
    }
}