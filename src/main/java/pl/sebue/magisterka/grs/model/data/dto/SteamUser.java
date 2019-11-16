package pl.sebue.magisterka.grs.model.data.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class SteamUser {
    private Long userId;
    private List<GameStatistic> gameStatisticList;

    public SteamUser(Long userId) {
        this.userId = userId;
        this.gameStatisticList = Lists.newArrayList();
    }
}
