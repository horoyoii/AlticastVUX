/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package skku.alticastvux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.alticast.mmuxclient.ClientAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import skku.alticastvux.R;
import skku.alticastvux.activity.base.BaseActivity;
import skku.alticastvux.model.VideoInfo;
import skku.alticastvux.util.AudioFromVideo;
import skku.alticastvux.util.AudioUtil;
import skku.alticastvux.util.BookMarkUtil;
import skku.alticastvux.util.Util;
import skku.alticastvux.voiceable.ASREventController;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends BaseActivity {

    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<VideoInfo> videos = Util.getAllVideos();
        ASREventController.getInstance().createASRContext(getApplicationContext());
        checkPermissions();

        BookMarkUtil.DBInit(Util.getAllVideos());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ASREventController.getInstance().destroyASRContext();
    }

    private void init() {
        setContentView(R.layout.activity_main);
    }

    private void checkPermissions() {
        boolean notgranted = true;
        for (String permission : permissions) {
            notgranted = notgranted && ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED;
        }

        if (notgranted) {
            boolean showrationale = true;
            for (String permission : permissions) {
                showrationale = showrationale && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission));
            }
            if (showrationale) {
                Toast.makeText(this, "권한 부족, 설정 확인", 0).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        permissions,
                        100);
            }
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        init();
                    } else {
                        Toast.makeText(this, "권한 부족, 설정 확인", 0).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean receiveCommand(String pattern, String response, ArrayList<ClientAPI.Entity> entities) {
        Toast.makeText(this, response, 0).show();
        Log.e("AlticastVUX", "entites size : "+entities.size());

        for(int i = 0; i < entities.size(); i++) {
            Log.e("AlticastVUX", "entity : "+entities.get(i).getType()+" : "+entities.get(i).getValue().toString());

        }

        return false;
    }
}
