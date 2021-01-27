/*
 * Copyright 2020 Google Inc.
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
package com.shakuro.twa_demo_app;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class LauncherActivity extends com.google.androidbrowserhelper.trusted.LauncherActivity {
    protected Uri getURLFromIntent() {
        try {
            Uri data = this.getIntent().getData();
            if (data != null) {
                return data;
            }

            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                String path = extras.getString("url");
                if (path != null) {
                    return Uri.parse(path);
                }
            }
            return null;
        } catch (Exception error) {
            return  null;
        }
    }

    @Override
    protected Uri getLaunchingUrl() {
        Uri defaultUrl = super.getLaunchingUrl();
        Uri redirectUrl = this.getURLFromIntent();

        Uri targetUrl = redirectUrl == null ? defaultUrl : redirectUrl;
        Uri.Builder uponTargetUrl = targetUrl.buildUpon();

        String token = getApplicationContext().getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

        // Token may be absent at the moment of the first launch of the application.
        // As soon as the application appears, it will be restarted.
        // Restart with token implemented in FcmService
        if (token != "empty") {
            uponTargetUrl.appendQueryParameter("_notifyToken", token);
        }

        return uponTargetUrl.build();

    }
}
