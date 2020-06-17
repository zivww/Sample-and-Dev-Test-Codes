package com.galagala.findchinaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.galagala.findchinaapp.R;
import com.galagala.findchinaapp.listener.OnItemClickListener;
import com.galagala.findchinaapp.wrapper.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class ItemlistAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder> {
    private List<AppInfo> appInfos = new ArrayList();
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public OnItemClickListener onItemClickListener;
    private RecycledViewPool viewPool = new RecycledViewPool();

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView appsName;
        ImageView delete;
        ImageView img;
        TextView size;

        public ViewHolder(View view) {
            super(view);
            this.appsName = (TextView) view.findViewById(R.id.appsName);
            this.size = (TextView) view.findViewById(R.id.size);
            this.img = (ImageView) view.findViewById(R.id.img);
            this.delete = (ImageView) view.findViewById(R.id.delete);
        }
    }

    public ItemlistAdapter(Context context2, List<AppInfo> list, OnItemClickListener onItemClickListener2) {
        this.context = context2;
        this.appInfos = list;
        this.onItemClickListener = onItemClickListener2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, position);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final AppInfo item = getItem(i);
        viewHolder.appsName.setText(item.appName);
        viewHolder.size.setText(item.size);
        viewHolder.img.setImageDrawable(item.icon);
        viewHolder.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent;
                ItemlistAdapter.this.onItemClickListener.onItemClick(i);
                if (VERSION.SDK_INT >= 14) {
                    intent = new Intent("android.intent.action.UNINSTALL_PACKAGE");
                } else {
                    intent = new Intent("android.intent.action.DELETE");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.fromParts("package", item.packageName, null));
                if (intent.resolveActivity(ItemlistAdapter.this.context.getPackageManager()) != null) {
                    ItemlistAdapter.this.context.startActivity(intent);
                }
            }
        });
    }

    public AppInfo getItem(int i) {
        return (AppInfo) this.appInfos.get(i);
    }

    public int getItemCount() {
        return this.appInfos.size();
    }
}
