package com.xiyun.aidlcall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by WangPeng on 2020-05-01.
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    private Map<String, IRemoteCallback> mMap;

    IRemoteService.Stub mStub = new IRemoteService.Stub() {
        @Override
        public void register(String pkgName, IRemoteCallback callback) throws RemoteException {
            if (null == callback) return;
            callback.asBinder().linkToDeath(new PkgDeathRecipient(pkgName) {
                @Override
                public void binderDied() {
                    super.binderDied();
                    // 设置死亡代理，进程意外挂掉等移除callback
                    IRemoteCallback iRemoteCallback = mMap.get(getPkgName());
                    if (iRemoteCallback != null) {
                        iRemoteCallback.asBinder().unlinkToDeath(this, 0);
                    }

                    mMap.remove(getPkgName());
                }
            }, 0);


            mMap.put(pkgName, callback);
        }

        @Override
        public void unRegister(String pkgName, IRemoteCallback callback) throws RemoteException {
            Log.i(TAG, "unRegister: ");
            if (null == callback) return;
            mMap.remove(pkgName);
        }

        @Override
        public void send(String packageName, String func, String params) throws RemoteException {
            if (TextUtils.isEmpty(func)) {
                return;
            }
            IRemoteCallback iRemoteCallback = mMap.get(packageName);
            if (null != iRemoteCallback) {
                iRemoteCallback.onSuccess(func, "收到请求啦，给你返回来的数据请接收");
            }


        }
    };


    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMap = new HashMap<>();
        push();
    }

    private void push() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Set<Map.Entry<String, IRemoteCallback>> entries = mMap.entrySet();
                for (Map.Entry<String, IRemoteCallback> entry : entries) {
                    try {
                        entry.getValue().onSuccess("push", "接口主动推送的数据" + System.currentTimeMillis());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
