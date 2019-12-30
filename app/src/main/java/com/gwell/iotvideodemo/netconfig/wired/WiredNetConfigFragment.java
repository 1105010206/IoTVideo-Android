package com.gwell.iotvideodemo.netconfig.wired;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.gwell.iotvideo.netconfig.DeviceInfo;
import com.gwell.iotvideo.netconfig.NetConfig;
import com.gwell.iotvideo.netconfig.NetConfigInfo;
import com.gwell.iotvideodemo.R;
import com.gwell.iotvideodemo.base.BaseFragment;
import com.gwell.iotvideodemo.netconfig.NetConfigActivity;
import com.gwell.iotvideodemo.netconfig.NetConfigViewModel;
import com.gwell.iotvideodemo.netconfig.NetConfigViewModelFactory;
import com.gwell.iotvideodemo.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WiredNetConfigFragment extends BaseFragment {

    private LinearLayout mRootView;
    private TextView mTvNetConfigInfo;
    private RecyclerView mRVDeviceList;
    private NetConfigViewModel mNetConfigInfoViewModel;
    private RecyclerView.Adapter<DeviceItemHolder> mAdapter;
    private List<DeviceInfo> mDeviceInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wired_net_config, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDeviceInfoList = new ArrayList<>();
        mRootView = view.findViewById(R.id.root_view);
        mTvNetConfigInfo = view.findViewById(R.id.net_config_info);
        mRVDeviceList = view.findViewById(R.id.device_list);
        mRVDeviceList.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL));
        mRVDeviceList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mAdapter = new RecyclerView.Adapter<DeviceItemHolder>() {
            @NonNull
            @Override
            public DeviceItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_scan_device, parent, false);
                DeviceItemHolder holder = new DeviceItemHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull DeviceItemHolder holder, int position) {
                final DeviceInfo deviceInfo = mDeviceInfoList.get(position);
                holder.tvDeviceName.setText(String.valueOf(deviceInfo.deviceID));
                holder.llDeviceInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bindDevice(String.valueOf(deviceInfo.deviceID));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mDeviceInfoList.size();
            }
        };
        mRVDeviceList.setAdapter(mAdapter);
        view.findViewById(R.id.find_devices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findDevices();
            }
        });
        mNetConfigInfoViewModel = ViewModelProviders.of(getActivity(), new NetConfigViewModelFactory())
                .get(NetConfigViewModel.class);
        NetConfigInfo netConfigInfo = mNetConfigInfoViewModel.getNetConfigInfo();
        mTvNetConfigInfo.setText(netConfigInfo.toString());
    }

    private void findDevices() {
        DeviceInfo[] deviceInfos = NetConfig.newWiredNetConfig().getDeviceList();
        if (deviceInfos != null && deviceInfos.length > 0) {
            mDeviceInfoList.clear();
            mDeviceInfoList.addAll(Arrays.asList(deviceInfos));
            mAdapter.notifyDataSetChanged();
            Snackbar.make(mRootView, "device count = " + mDeviceInfoList.size(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mRootView, "no device", Snackbar.LENGTH_LONG).show();
        }
    }

    class DeviceItemHolder extends RecyclerView.ViewHolder {
        LinearLayout llDeviceInfo;
        TextView tvDeviceName;

        DeviceItemHolder(View view) {
            super(view);
            llDeviceInfo = view.findViewById(R.id.ll_device_info);
            tvDeviceName = view.findViewById(R.id.device_name);
        }
    }

    private void bindDevice(String did) {
        if (getActivity() instanceof NetConfigActivity) {
            ((NetConfigActivity) getActivity()).bindDevice(did);
        }
    }
}
