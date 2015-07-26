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
 * GalleryAdapter class that implements a Gallery view where the picture of the meals are shown.
 * Extends BaseAdapter class
 *
 * @author MensaX-group
 * @version 1.0
 */
public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private int itemBackground;
    private List<Bitmap> bitmaps = new ArrayList<>();

    /**
     * The Gallery adapter.
     * @param c context of the activity
     */
    public GalleryAdapter(Context c) {
        context = c;
        TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
        itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    /**
     * Method to add a picture in form of bitmap to the ArrayList.
     * @param bmp the bitmap object
     */
    public void addBitmap(Bitmap bmp) {
        bitmaps.add(bmp);
    }

    /**
     * Method to get the size of the bitmap object in the ArrayList.
     * @return int size of the bitmap
     */
    public int getCount() {
        if (bitmaps.size() > 0) {
            return bitmaps.size();
        }
        return 1;
    }

    /**
     * Method to get the bitmap in a specific position in the ArrayList.
     * @param position the position where to get the bitmap
     * @return the bitmap object
     */
    public Object getItem(int position) {
        return bitmaps.get(position);
    }

    /**
     * Method to get the id at a position in the ArrayList.
     * Must be implementen from the BaseAdapter class.
     * @param position the position to get
     * @return the position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Method to get the View on an image in the gallery
     * @param position the position on the gallery
     * @param convertView the rootView
     * @param parent the view of the parent
     * @return the view for the image to be shown in the gallery
     */
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
