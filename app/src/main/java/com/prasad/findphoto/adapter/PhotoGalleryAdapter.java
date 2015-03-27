package com.prasad.findphoto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.prasad.findphoto.R;
import com.prasad.findphoto.callback.GalleryClickListener;
import com.prasad.findphoto.dao.Photo;
import com.prasad.findphoto.network.ImageDownloader;
import com.prasad.findphoto.utils.ImageStore;

import java.util.List;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder>{

    private List<Photo> items;
    private int itemLayout;
    private  GalleryClickListener listener;

    public PhotoGalleryAdapter(List<Photo> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }

    public void setGalleryClickListener(GalleryClickListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo item = items.get(position);
        String  imgURL = String.format("https://farm%s.staticflickr.com/%s/%s_%s_n.jpg",item.farm,item.server, item.id, item.secret);
        if(ImageStore.getInstance().getBitmapFromDiskCache(imgURL) == null){

            if(item.isDownloadStarted ==0) {
                item.isDownloadStarted = 1;
                holder.image.setTag(imgURL);
                ImageDownloader download = new ImageDownloader();
                download.execute(holder.image);
            }
        }else{
            holder.image.setImageBitmap(ImageStore.getInstance().getBitmapFromDiskCache(imgURL));
        }
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(PhotoGalleryAdapter.this.listener != null){
                Photo item = ((Photo)v.getTag());
                String  imgURL = String.format("https://farm%s.staticflickr.com/%s/%s_%s_n.jpg",item.farm,item.server, item.id, item.secret);
                PhotoGalleryAdapter.this.listener.photoSelected(imgURL, item.title);
            }
        }
    }

    public void update(List<Photo> items) {
        if(items != null) {
            this.items = items;
            notifyDataSetChanged();
        }
    }
}
