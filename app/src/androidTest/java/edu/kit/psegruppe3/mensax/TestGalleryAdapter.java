package edu.kit.psegruppe3.mensax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

/**
 * Tests the functionality of the {@link GalleryAdapter} class.
 *
 * @author MensaX-Group
 * @version 1.0
 */
public class TestGalleryAdapter extends AndroidTestCase {

    /**
     * This test checks to make sure that all methods of the GalleryAdapter class work correctly.
     */
    public void testGalleryAdapter() {
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext());
        int imageCount = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
        for (int i = 0; i < imageCount ; i++) {
            galleryAdapter.addBitmap(bitmap);
        }
        assertEquals("Error: Value '" + galleryAdapter.getCount() +
                "' did not match the expected value '" +
                imageCount + "'.", imageCount, galleryAdapter.getCount());
        assertEquals("Error: Returned object did not match the inserted image", bitmap, galleryAdapter.getItem(0));
        assertEquals("Error: Value '" + galleryAdapter.getItemId(0) +
                "' did not match the expected value '" +
                0 + "'.", galleryAdapter.getItemId(0), 0);
    }
}
