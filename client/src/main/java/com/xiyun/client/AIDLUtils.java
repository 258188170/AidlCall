package com.xiyun.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xiyun.aidlcall.IRemoteCallback;
import com.xiyun.aidlcall.IRemoteService;

/**
 * Created by WangPeng on 2020-05-01.
 */
public class AIDLUtils {
    private static final String TAG = "AIDLUtils";
    private static Context sContext;
    private IRemoteService mRemoteService;
    private IRemoteCallback mCallback;
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {

        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected: 绑定成功");
            mRemoteService = IRemoteService.Stub.asInterface(iBinder);
            try {
                mRemoteService.asBinder().linkToDeath(deathRecipient, 0);
                mRemoteService.register(sContext.getPackageName(), mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (null != mRemoteService) {
                mRemoteService.asBinder().unlinkToDeath(deathRecipient, 0);
            }
            mRemoteService = null;
            mCallback = null;

        }
    };

    private AIDLUtils() {
    }

    public static AIDLUtils getInstance(Context context) {
        sContext = context.getApplicationContext();
        return Builder.intance;
    }

    public void bindService(Context context, @NonNull IRemoteCallback callback) {
        this.mCallback = callback;
        Intent intent = new Intent();
        intent.setAction("com.xiyun.aidlcall.RemoteService");
        intent.setPackage("com.xiyun.aidlcall");
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public void unBindService(Context context) {
        if (null == mRemoteService) return;
        if (null != mCallback) {
            try {
                mRemoteService.unRegister(context.getPackageName(), mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        context.unbindService(mServiceConnection);
        mRemoteService = null;
        mCallback = null;
    }
    public void send(){
        if (null==mRemoteService)return;
        try {
            mRemoteService.send(sContext.getPackageName(),"test","------>>");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    static class Builder {
        private static AIDLUtils intance = new AIDLUtils();
    }
}
