/*
 * Copyright (C) 2010-2012 Zutubi Pty Ltd
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

package com.zzg.robotium.lib;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.text.TextUtils;
import android.util.Log;

public class ReportTestRunner extends InstrumentationTestRunner {

    private Report rl;
    private String TAG = "ROBOT";

    @Override
    public void onCreate(Bundle arguments) {
        InputDataStore inputDataStore = InputDataStore.getInstance();
        inputDataStore.init(getTargetContext());
        String activity = "";
        int retrytime=0;
        try {
            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            activity = applicationInfo.metaData.getString("activity");
            retrytime = applicationInfo.metaData.getInt("retrytime");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(activity)) {
            Log.e(TAG, "初始化失败，无法获取启动activity");
        } else {
            inputDataStore.setInput_TargetActivity(activity);
        }if (retrytime>1) {
            inputDataStore.setInput_Retrytime(retrytime);
        }

        rl = new Report();
        try {
            if (rl.setup())
                Log.i(TAG, DebugInfoStore.Info_Setup + DebugInfoStore.Info_Pass);
            else {
                Log.e(TAG, DebugInfoStore.Info_Setup + DebugInfoStore.Info_Fail + "reportlib.setup方法失败");
            }
        } catch (Exception e) {
            Log.e(TAG, DebugInfoStore.Info_Setup
                    + DebugInfoStore.Info_Exception + "============" + e.toString());
        }
        super.onCreate(arguments);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        rl.closeLog();
        super.finish(resultCode, results);
    }
}
