package pl.sebue.magisterka.grs.model.data.dto;

import javax.persistence.*;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendationId")
    private Long recommendationId;
    @Column(name = "userId")
    private Long userId;
    @OneToOne
    @JoinColumn(name = "gameId")
    private Game game;
    @Column(name = "probability")
    private float probability;
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendationType")
    private RecommendationType recommendationType;

    public Recommendation(){
        //for query purpose
    }

    public Recommendation(Long userId, Game game, float probability, RecommendationType recommendationType) {
        this.userId = userId;
        this.game = game;
        this.probability = probability;
        this.recommendationType = recommendationType;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public RecommendationType getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(RecommendationType recommendationType) {
        this.recommendationType = recommendationType;
    }
}
