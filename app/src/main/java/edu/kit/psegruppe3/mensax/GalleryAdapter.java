package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekremsenturk on 25.07.15.
 */
public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private int itemBackground;
    private List<Bitmap> bitmaps = new ArrayList<>();

    public GalleryAdapter(Context c) {
        context = c;
        TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
        itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    public void addBitmap(Bitmap bmp) {
        bitmaps.add(bmp);
    }

    // returns the number of images
    public int getCount() {
        if (bitmaps.size() > 0) {
            return bitmaps.size();
        }
        return 1;
    }

    // returns the ID of an item
    public Object getItem(int position) {
        return bitmaps.get(position);
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        if (bitmaps.size() == 0) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
        } else {
            imageView.setImageBitmap((Bitmap) getItem(position));
        }
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundResource(itemBackground);
        return imageView;
    }
}
