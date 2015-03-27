package com.prasad.findphoto.adapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    public  class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnDragListener(new myDragEventListener());
        }

        @Override
        public void onClick(View v) {
            if(PhotoGalleryAdapter.this.listener != null){
                Photo item = ((Photo)v.getTag());
                String  imgURL = String.format("https://farm%s.staticflickr.com/%s/%s_%s_n.jpg",item.farm,item.server, item.id, item.secret);
                PhotoGalleryAdapter.this.listener.photoSelected(imgURL, item.title);
            }
        }

        public boolean onLongClick(View v) {
            Log.v("onLongClick>>", "Entered");
            ClipData.Item item = new ClipData.Item(((Photo)v.getTag()).id);
            ClipData dragData = new ClipData(((Photo)v.getTag()).id, new String[]{"text/plain"}, item);

            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);
            v.startDrag(dragData,  // the data to be dragged
                    myShadow,  // the drag shadow builder
                    null,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );
            return true;
        }


    }

    public void update(List<Photo> items) {
        if(items != null) {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch){
        // Defines local variables
        int width, height;

        // Sets the width of the shadow to half the width of the original View
        width = getView().getWidth() / 2;

        // Sets the height of the shadow to half the height of the original View
        height = getView().getHeight() / 2;

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow.setBounds(0, 0, width, height);

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height);

        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    @Override
    public void onDrawShadow(Canvas canvas) {

        // Draws the ColorDrawable in the Canvas passed in from the system.
        shadow.draw(canvas);
    }
}


    protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        //v.setBackgroundColor(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.

                    v.setBackgroundColor(Color.GREEN);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    v.setBackgroundColor(Color.BLACK);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    //dragData = item.getText();
                    Log.v("Data>>>>>",item.getText().toString());

                    Log.v("Data<<<<>>>>>",((Photo)v.getTag()).id);
                    PhotoGalleryAdapter.this.listener.insert(item.getText().toString(), ((Photo)v.getTag()).id );
                    // Displays a message containing the dragged data.
                    //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_LONG);

                    // Turns off any color tints
                    v.setBackgroundColor(Color.BLACK);

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    v.setBackgroundColor(Color.BLACK);

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        //Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG);

                    } else {
                        //Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG);

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    };
}
