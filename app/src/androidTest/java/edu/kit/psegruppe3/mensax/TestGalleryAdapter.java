package edu.kit.psegruppe3.mensax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.test.AndroidTestCase;
import android.view.View;

/**
 * Tests the functionality of the {@link GalleryAdapter} class.
 *
 * @author MensaX-Group
 * @version 1.0
 */
public class TestGalleryAdapter extends AndroidTestCase {

    private GalleryAdapter mGalleryAdapter;

    /**
     * This test checks to make sure that all methods of the GalleryAdapter class work correctly.
     */
    protected void testGalleryAdapter() {
        mGalleryAdapter = new GalleryAdapter(getContext());
        int imageCount = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
        for (int i = 0; i < imageCount ; i++) {
            mGalleryAdapter.addBitmap(bitmap);
        }
        assertEquals("Error: Value '" + mGalleryAdapter.getCount() +
                "' did not match the expected value '" +
                imageCount + "'.", imageCount, mGalleryAdapter.getCount());
        assertEquals("Error: Returned object did not match the inserted image", bitmap, mGalleryAdapter.getItem(0));
        assertEquals("Error: Value '" + mGalleryAdapter.getItemId(0) +
                "' did not match the expected value '" +
                0 + "'.", mGalleryAdapter.getItemId(0), 0);
        View view = mGalleryAdapter.getView(0, null, null);
        assertNotNull("Error: Returned view has no image", loadBitmapFromView(view));
        assertEquals("Error: Image of the returned view did not match the expected image", loadBitmapFromView(view), bitmap);


    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
}
