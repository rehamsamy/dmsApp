/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmsegypt.dms.rest.googleplay;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AndroidPublisherHelper {

    private static final Log log = LogFactory.getLog(AndroidPublisherHelper.class);

    static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    /** Path to the private key file (only used for Service Account auth). */
    private static final String SRC_RESOURCES_KEY_P12 = "src/resources/key.p12";

    /**
     * Path to the client secrets file (only used for Installed Application
     * auth).
     */
    private static final String RESOURCES_CLIENT_SECRETS_JSON = "/resources/client_secrets.json";

    /**
     * Directory to store user credentials (only for Installed Application
     * auth).
     */
    private static final String DATA_STORE_SYSTEM_PROPERTY = "user.home";
    private static final String DATA_STORE_FILE = ".store/android_publisher_api";
    private static final File DATA_STORE_DIR =
            new File(System.getProperty(DATA_STORE_SYSTEM_PROPERTY), DATA_STORE_FILE);

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Installed application user ID. */
    private static final String INST_APP_USER_ID = "user";

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;
    private static File createFileFromInputStream(Context context,InputStream input) {
        File file=null;
        try {
             file = new File(context.getCacheDir(), "cacheFileAppeal.p12");
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
    private static Credential authorizeWithServiceAccount(Context context, String serviceAccountEmail)
            throws GeneralSecurityException, IOException {
        log.info(String.format("Authorizing using Service Account: %s", serviceAccountEmail));

        AssetManager am = context.getAssets();
        InputStream inputStream = am.open("key.p12");
        File file = createFileFromInputStream(context,inputStream);
        // Build service account credential.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(serviceAccountEmail)
                .setServiceAccountScopes(
                        Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
                .setServiceAccountPrivateKeyFromP12File(file)
                .build();
        credential.refreshToken();
        String token=credential.getAccessToken();
        android.util.Log.d("asf", "authorizeWithServiceAccount: "+token);
        return credential;
    }

    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static Credential authorizeWithInstalledApplication() throws IOException {
        log.info("Authorizing using installed application");

        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(
                        AndroidPublisherHelper.class
                                .getResourceAsStream(RESOURCES_CLIENT_SECRETS_JSON)));
        // Ensure file has been filled out.
        checkClientSecretsFile(clientSecrets);

        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT,
                        JSON_FACTORY, clientSecrets,
                        Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
                        .setDataStoreFactory(dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize(INST_APP_USER_ID);
    }

    /**
     * Ensure the client secrets file has been filled out.
     *
     * @param clientSecrets the GoogleClientSecrets containing data from the
     *            file
     */
    private static void checkClientSecretsFile(GoogleClientSecrets clientSecrets) {
        if (clientSecrets.getDetails().getClientId().startsWith("[[INSERT")
                || clientSecrets.getDetails().getClientSecret().startsWith("[[INSERT")) {
            log.error("Enter Client ID and Secret from "
                    + "APIs console into resources/client_secrets.json.");
            System.exit(1);
        }
    }

    /**
     * Performs all necessary setup steps for running requests against the API
     * using the Installed Application auth method.
     *
     * @param applicationName the name of the application: com.example.app
     * @return the {@Link AndroidPublisher} service
     */
    public static AndroidPublisher init(Context context,String applicationName) throws Exception {
        return init(context,applicationName, null);
    }

    /**
     * Performs all necessary setup steps for running requests against the API.
     *
     * @param applicationName the name of the application: com.example.app
     * @param serviceAccountEmail the Service Account Email (empty if using
     *            installed application)
     * @return the {@Link AndroidPublisher} service
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static AndroidPublisher init(Context context,String applicationName,
            @Nullable String serviceAccountEmail) throws IOException, GeneralSecurityException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(applicationName),
                "applicationName cannot be null or empty!");

        // Authorization.
        newTrustedTransport();
        Credential credential;
        if (serviceAccountEmail == null || serviceAccountEmail.isEmpty()) {
            credential = authorizeWithInstalledApplication();
        } else {
            credential = authorizeWithServiceAccount(context,serviceAccountEmail);
        }

        // Set up and return API client.
        return new AndroidPublisher.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName)
                .build();
    }

    private static void newTrustedTransport() throws GeneralSecurityException,
            IOException {
        if (null == HTTP_TRANSPORT) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();;
        }
    }

}