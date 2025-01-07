package image;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A package-private class of the package image.
 * @author Amit Halbreich
 */
class SubImageIterable<T> implements Iterable<T> {
    private final Image img;
    private final int subImageSize;

    /**
     * Class of SubImageIterable
     * @param img
     * @param subImageSize
     */
    public SubImageIterable(
            Image img,int subImageSize) {
        this.img = img;
        this.subImageSize = subImageSize;
    }

    /**
     * Iterator for SubImages.
     * @return next SubImage - a square small piece of the original image mapped by subimage col and row indices that
     * implies on the location in the original image.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int subimageRowIdx = 0, subimageColIdx = 0;

            @Override
            public boolean hasNext() { return subimageRowIdx < img.getHeight(); }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                var next = new SubImage(subImageSize,img, subimageRowIdx, subimageColIdx);
                subimageColIdx += subImageSize;
                if (subimageColIdx >= img.getWidth()) {
                    subimageColIdx = 0;
                    subimageRowIdx += subImageSize;
                }
                return (T) next;
            }
        };
    }
}

