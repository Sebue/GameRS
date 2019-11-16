package pl.sebue.magisterka.grs.data.dto;

import javax.persistence.*;

@Entity
@Table(name = "gamestatistic")
public class GameStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameStatisticId")
    private Long gameStatisticId;
    @Column(name = "userId")
    private Long userId;
    @OneToOne
    @JoinColumn(name = "gameId")
    private Game game;
    @Column(name = "playedHours")
    private float playedHours;

    public GameStatistic(){
        //for query purpose
    }

    public GameStatistic(Long userId, Game game, float playedHours) {
        this.userId = userId;
        this.game = game;
        this.playedHours = playedHours;
    }

    public Long getGameStatisticId() {
        return gameStatisticId;
    }

    public void setGameStatisticId(Long gameStatisticId) {
        this.gameStatisticId = gameStatisticId;
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

    public float getPlayedHours() {
        return playedHours;
    }

    public void setPlayedHours(float playedHours) {
        this.playedHours = playedHours;
    }
}
