package com.xiyun.client;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xiyun.aidlcall.IRemoteCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button mBindBtn;
    private Button mUnbindBtn;
    private Button mRequestBtn;
    private TextView mResultTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBindBtn = findViewById(R.id.bindBtn);
        mBindBtn.setOnClickListener(this);
        mUnbindBtn = findViewById(R.id.unbindBtn);
        mUnbindBtn.setOnClickListener(this);
        mRequestBtn = findViewById(R.id.requestBtn);
        mRequestBtn.setOnClickListener(this);
        mResultTv = findViewById(R.id.resultTv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bindBtn:
                Log.i(TAG, "onClick: ");
                AIDLUtils.getInstance(this).bindService(this, new IRemoteCallback.Stub() {
                    @Override
                    public void onSuccess(final String func,final String params) throws RemoteException {
                        Log.i(TAG, "onSuccess: "+func + params);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResultTv.append("func：" + func + "\n");
                                mResultTv.append("params：" + params + "\n");
                            }
                        });
                    }

                    @Override
                    public void onError(String func, int errorCode) throws RemoteException {
                        Log.i(TAG, "onError: ");
                    }
                });

                mResultTv.append("绑定服务");
                mResultTv.append("\n");
                break;
            case R.id.unbindBtn:
                Log.i(TAG, "onClick: ");
                AIDLUtils.getInstance(this).unBindService(MainActivity.this);
                mResultTv.append("取消绑定");
                mResultTv.append("\n");
                break;
            case R.id.requestBtn:
                Log.i(TAG, "onClick: ");
                AIDLUtils.getInstance(this).send();
                break;
        }
    }
}
