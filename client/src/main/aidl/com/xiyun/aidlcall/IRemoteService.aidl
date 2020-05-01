// IRemoteService.aidl
package com.xiyun.aidlcall;

// Declare any non-default types here with import statements
import com.xiyun.aidlcall.IRemoteCallback;
interface IRemoteService {

   void register(in String pkgName,in IRemoteCallback callback);
   void unRegister(in String pkgName,in IRemoteCallback callback);
   void send (in String packageName,in String func,in String params);
}
