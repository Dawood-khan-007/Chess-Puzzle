package boardgame.util;

import javafx.scene.image.Image;

/**
 * An interface representing a storage mechanism for retrieving images based on a given key.
 *
 * @param <T> the type of the key used to retrieve images
 */
public interface ImageStorage<T> {

        /**
         * Retrieves the image associated with the specified key.
         *
         * @param key the key used to retrieve the image
         * @return the image associated with the key, or {@code null} if no image is found
         */
    Image get(T key);

}