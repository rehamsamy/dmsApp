package com.dmsegypt.dms.rest.googleplay;

public final class ApplicationConfig {

    private ApplicationConfig() {
        // no instance
    }

    /**
     * Specify the name of your application. If the application name is
     * {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-Application/1.0".
     */
   public static final String APPLICATION_NAME = "DMS - Egypt";


    /**
     * Specify the package name of the app.
     */
   public static final String PACKAGE_NAME = "com.dmsegypt.dms";

    /**
     * Authentication.
     * <p>
     * Installed application: Leave this string empty and copy or
     * edit resources/client_secrets.json.
     * </p>
     * <p>
     * Service accounts: Enter the service
     * account email and add your key.p12 file to the resources directory.
     * </p>
     */
    public static final String SERVICE_ACCOUNT_EMAIL = "review-dms\n" +
            "review-dms@api-7676257648947777803-566068.iam.gserviceaccount.com";


}