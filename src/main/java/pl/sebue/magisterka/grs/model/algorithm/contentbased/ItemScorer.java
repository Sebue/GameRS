package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.pref.Preference;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class ItemScorer extends AbstractItemScorer {
    private final UserEventDAO dao;
    private final CBModel model;

    @Inject
    public ItemScorer(UserEventDAO dao, CBModel m) {
        this.dao = dao;
        model = m;
    }

    @Override
    public void score(long user, @Nonnull MutableSparseVector output) {
        // Get the user's profile, which is a vector with their 'like' for each tag
        SparseVector userVector = makeUserVector(user);

        // Loop over each item requested and score it.
        // The *domain* of the output vector is the items that we are to score.
        for (VectorEntry e: output.fast(VectorEntry.State.EITHER)) {
            // Score the item represented by 'e'.
            // Get the item vector for this item
            SparseVector iv = model.getItemVector(e.getKey());
//            // TODO Compute the cosine of this item and the user's profile, store it in the output vector
//            // Cosine b/n iv and userVector
            double numerator = userVector.dot(iv);
            double denominator = userVector.norm() * iv.norm();
            double cosine = numerator / denominator;
            output.set(e.getKey(), cosine);

//            output.set(e.getKey(), userVector.get(e.getKey()));
        }
    }

    private SparseVector makeUserVector(long user) {
        // Get the user's ratings
        List<Rating> userRatings = dao.getEventsForUser(user, Rating.class);
        if (userRatings == null) {
            return SparseVector.empty();
        }

        // Create a new vector over tags to accumulate the user profile
        MutableSparseVector profile = model.newTagVector();
        profile.fill(0);

        // Iterate over the user's ratings to build their profile
//        double ratingSum = 0;
//        int counter = 0;
//        for (Rating rating: userRatings) {
//            Preference preference = rating.getPreference();
//            counter++;
//            ratingSum += preference.getValue();
//        }
//        double avgRating = ratingSum/counter;
        double maxRating = 0.0;
        for(Rating rating : userRatings){
            Preference preference = rating.getPreference();
            if(preference.getValue() > maxRating){
                maxRating = preference.getValue();
            }
        }


        for(Rating rating: userRatings){
            Preference preference = rating.getPreference();
            double ratingValue = preference.getValue();
            double multiplier = ratingValue / maxRating;
//            double multiplier = counter == 1 ? 1.0 : ratingValue - avgRating;

            long itemId = rating.getItemId();
            SparseVector itemVector = this.model.getItemVector(itemId);
            for(VectorEntry v: itemVector.fast()){
                long vKey = v.getKey();
                double vValue = v.getValue();
                double sum = vValue * multiplier + profile.get(vKey);
                profile.set(vKey, sum);
            }

        }

        return profile.freeze();
    }
}
