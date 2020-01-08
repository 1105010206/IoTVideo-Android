package com.gwell.iotvideodemo.netconfig;

import com.gwell.iotvideo.IoTVideoSdk;
import com.gwell.iotvideo.accountmgr.AccountMgr;
import com.gwell.iotvideo.messagemgr.IResultListener;
import com.gwell.iotvideo.netconfig.DeviceInfo;
import com.gwell.iotvideo.utils.LogUtils;
import com.gwell.iotvideodemo.base.HttpRequestState;
import com.gwell.iotvideodemo.base.SimpleSubscriberListener;

import androidx.lifecycle.MutableLiveData;

class NetConfigHelper {
    private static final String TAG = "NetConfigHelper";

    private NetConfigViewModel mNetConfigViewModel;

    NetConfigHelper(NetConfigViewModel model) {
        mNetConfigViewModel = model;
    }

    void bindDevice(String did, MutableLiveData<HttpRequestState> httpRequestStateMutableLiveData) {
        AccountMgr.getInstance().deviceBind(did, new SimpleSubscriberListener(httpRequestStateMutableLiveData));
    }

    void findDevices() {
        DeviceInfo[] deviceInfos = IoTVideoSdk.getNetConfig().newWiredNetConfig().getDeviceList();
        mNetConfigViewModel.getLanDeviceData().setValue(deviceInfos);
        if(deviceInfos != null){
            for (DeviceInfo deviceInfo : deviceInfos) {
                LogUtils.d(TAG, "findDevices " + deviceInfo);
            }
        }
    }

    void getNetConfigToken(IResultListener listener) {
        IoTVideoSdk.getNetConfig().getNetConfigToken(listener);
    }
}
