// IRemoteCallback.aidl
package com.xiyun.aidlcall;

// Declare any non-default types here with import statements

interface IRemoteCallback {
    void onSuccess(String func,String params);
    void onError(String func,int errorCode);
}
