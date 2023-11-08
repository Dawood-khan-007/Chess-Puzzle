package boardgame.util;

import javafx.scene.image.Image;

/**
 * An implementation of the {@link ImageStorage} interface that stores images in an array based on ordinal indices.
 * The ordinal indices are used as keys to retrieve the corresponding images.
 */

public class OrdinalImageStorage implements ImageStorage<Integer> {

    private final Image[] images;

    /**
     * Constructs an {@code OrdinalImageStorage} object with the specified path and filenames.
     *
     * @param path      the path to the directory containing the image files
     * @param filenames the filenames of the image files
     */

    public OrdinalImageStorage(String path, String... filenames) {
        images = new Image[filenames.length];
        for (var i = 0; i < filenames.length; i++) {
            var url = String.format("%s/%s", path, filenames[i]);
            try {
                images[i] = new Image(url);
            } catch (Exception e) {

                System.err.println("Failed to load image: " + filenames[i]);
            }
        }
    }

    /**
     * Retrieves the image associated with the specified ordinal index key.
     *
     * @param key the ordinal index used to retrieve the image
     * @return the image associated with the ordinal index key, or {@code null} if no image is found
     */
    @Override
    public Image get(Integer key) {
        if (key >= 0 && key < images.length) {
            return images[key];
        } else {
            System.err.println("Invalid image index: " + key);
            return null;
        }
    }
}