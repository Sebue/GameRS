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

    CBModel(List<Long> tagIds, Map<Long,SparseVector> itemVectors) {
        this.tagIds = tagIds;
        this.itemVectors = itemVectors;
    }

    public MutableSparseVector newTagVector() {
        return MutableSparseVector.create(tagIds);
    }

    public SparseVector getItemVector(long item) {
        SparseVector vec = itemVectors.get(item);
        return vec == null ? SparseVector.empty() : vec;
    }
}
