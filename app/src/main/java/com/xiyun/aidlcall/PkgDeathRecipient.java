package com.xiyun.aidlcall;

import android.os.IBinder;

/**
 * Created by WangPeng on 2020-05-01.
 */
public class PkgDeathRecipient implements IBinder.DeathRecipient {

    private String pkgName;

    public PkgDeathRecipient(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgName() {
        return pkgName;
    }


    @Override
    public void binderDied() {

    }
}
