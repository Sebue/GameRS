package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import org.grouplens.grapht.annotation.DefaultProvider;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@DefaultProvider(CBFModelBuilder.class)
public class CBModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Long> tagIds;
    private final Map<Long, SparseVector> itemVectors;

    /**
     * In a LensKit model designed for a large data set, these would be optimized fastutil maps for
     * efficiency.
     */
    CBModel(List<Long> tagIds, Map<Long,SparseVector> itemVectors) {
        this.tagIds = tagIds;
        this.itemVectors = itemVectors;
    }

    /**
     * Create a new mutable vector over all tag IDs.  The vector is initially empty, and its key
     * domain is the set of all tag IDs.
     *
     * @return A fresh vector over tag IDs.
     */
    public MutableSparseVector newTagVector() {
        return MutableSparseVector.create(tagIds);
    }

    /**
     * Get the tag vector for a particular item.
     *
     * @param item The item.
     * @return The item's tag vector.  If the item is not known to the model, then this vector is
     *         empty.
     */
    public SparseVector getItemVector(long item) {
        SparseVector vec = itemVectors.get(item);
        return vec == null ? SparseVector.empty() : vec;
    }
}
