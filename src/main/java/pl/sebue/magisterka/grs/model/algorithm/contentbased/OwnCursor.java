package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import org.grouplens.lenskit.cursors.AbstractPollingCursor;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.event.Ratings;

import java.util.List;

public class OwnCursor extends AbstractPollingCursor<Rating> {
    private final List<Rating> ratings;
    private final int ratingSize;
    private int ratingCount;

    public OwnCursor(List<Rating> ratings) {
        this.ratings = ratings;
        this.ratingSize = ratings.size();
        this.ratingCount = 0;
    }

    public Rating poll() {

        if(ratingCount != ratingSize){
            return ratings.get(ratingCount++);
        }

        return null;
    }

    public Rating copy(Rating r) {
        return Ratings.copyBuilder(r).build();
    }
}
