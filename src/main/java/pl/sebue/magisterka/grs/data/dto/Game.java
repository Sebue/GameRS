package pl.sebue.magisterka.grs.data.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "game")
public class Game implements Serializable {
    private static final Logger logger = Logger.getLogger(Game.class.getSimpleName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameId")
    private Long gameId;
    @Column(name = "name")
    private String name;
    @Column(name = "releaseYear")
    private int releaseYear;
    @Column(name = "requiredAge")
    private int requiredAge;
    @Column(name = "hasDemoVersion")
    private boolean hasDemoVersion;
    @Column(name = "dlcCount")
    private int dlcCount;
    @Column(name = "metacriticScore")
    private int metacriticScore;
    @Column(name = "isControllerSupported")
    private boolean isControllerSupported;
    @Column(name = "recommendationCount")
    private int recommendationCount;
    @Column(name = "achievementCount")
    private int achievementCount;
    @Column(name = "initialPrice")
    private float initialPrice;
    @Column(name = "finalPrice")
    private float finalPrice;
    @Column(name = "ownerCount")
    private int ownerCount;

    public Game(){
        //for query purpose
    }

    private Game(String name, int releaseYear, int requiredAge, boolean hasDemoVersion, int dlcCount, int metacriticScore, boolean isControllerSupported, int recommendationCount, int achievementCount, float initialPrice, float finalPrice, int ownerCount) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.requiredAge = requiredAge;
        this.hasDemoVersion = hasDemoVersion;
        this.dlcCount = dlcCount;
        this.metacriticScore = metacriticScore;
        this.isControllerSupported = isControllerSupported;
        this.recommendationCount = recommendationCount;
        this.achievementCount = achievementCount;
        this.initialPrice = initialPrice;
        this.finalPrice = finalPrice;
        this.ownerCount = ownerCount;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRequiredAge() {
        return requiredAge;
    }

    public void setRequiredAge(int requiredAge) {
        this.requiredAge = requiredAge;
    }

    public boolean isHasDemoVersion() {
        return hasDemoVersion;
    }

    public void setHasDemoVersion(boolean hasDemoVersion) {
        this.hasDemoVersion = hasDemoVersion;
    }

    public int getDlcCount() {
        return dlcCount;
    }

    public void setDlcCount(int dlcCount) {
        this.dlcCount = dlcCount;
    }

    public int getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(int metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    public boolean isControllerSupported() {
        return isControllerSupported;
    }

    public void setControllerSupported(boolean controllerSupported) {
        isControllerSupported = controllerSupported;
    }

    public int getRecommendationCount() {
        return recommendationCount;
    }

    public void setRecommendationCount(int recommendationCount) {
        this.recommendationCount = recommendationCount;
    }

    public int getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(int achievementCount) {
        this.achievementCount = achievementCount;
    }

    public float getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(float initialPrice) {
        this.initialPrice = initialPrice;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(int ownerCount) {
        this.ownerCount = ownerCount;
    }

    public static class Builder {
        private static final Pattern RELEASE_YEAR_PATTERN = Pattern.compile(".*(\\d{4})");
        private boolean shouldBuild = true;

        private String name;
        private int releaseYear;
        private int requiredAge;
        private boolean hasDemoVersion;
        private int dlcCount;
        private int metacriticScore;
        private boolean isControllerSupported;
        private int recommendationCount;
        private int achievementCount;
        private float initialPrice;
        private float finalPrice;
        private int ownerCount;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setReleaseYear(String releaseYear) {
            Matcher matcher = RELEASE_YEAR_PATTERN.matcher(releaseYear);
            if (matcher.matches()) {
                try {
                    this.releaseYear = Integer.parseInt(matcher.group(1));
                } catch (Exception e) {
                    if (name != null) {
                        logger.warning("Problem with parsing releaseYear: " + releaseYear + " for: " + name);
                    } else {
                        logger.warning("Problem with parsing releaseYear: " + releaseYear);
                    }
                    shouldBuild = false;
                }
            } else {
                if (name != null) {
                    logger.warning("Missing release year for: " + name);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setRequiredAge(String requiredAge) {
            try {
                this.requiredAge = Integer.parseInt(requiredAge);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing requiredAge: " + requiredAge + " for: " + name);
                } else {
                    logger.warning("Problem with parsing requiredAge: " + requiredAge);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setHasDemoVersion(String demoCount) {
            try {
                int demoVersionCount = Integer.parseInt(demoCount);
                this.hasDemoVersion = demoVersionCount != 0;
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing hasDemoVersion: " + demoCount + " for: " + name);
                } else {
                    logger.warning("Problem with parsing hasDemoVersion: " + demoCount);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setDlcCount(String dlcCount) {
            try {
                this.dlcCount = Integer.parseInt(dlcCount);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing dlcCount: " + dlcCount + " for: " + name);
                } else {
                    logger.warning("Problem with parsing dlcCount: " + dlcCount);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setMetacriticScore(String metacriticScore) {
            try {
                this.metacriticScore = Integer.parseInt(metacriticScore);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing metacriticScore: " + metacriticScore + " for: " + name);
                } else {
                    logger.warning("Problem with parsing metacriticScore: " + metacriticScore);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setIsControllerSupported(String isControllerSupported) {
            this.isControllerSupported = "true".equals(isControllerSupported);
            return this;
        }

        public Builder setRecommendationCount(String recommendationCount) {
            try {
                this.recommendationCount = Integer.parseInt(recommendationCount);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing recommendationCount: " + recommendationCount + " for: " + name);
                } else {
                    logger.warning("Problem with parsing recommendationCount: " + recommendationCount);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setAchievementCount(String achievementCount) {
            try {
                this.achievementCount = Integer.parseInt(achievementCount);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing achievementCount: " + achievementCount + " for: " + name);
                } else {
                    logger.warning("Problem with parsing achievementCount: " + achievementCount);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setInitialPrice(String initialPrice) {
            try {
                this.initialPrice = Float.parseFloat(initialPrice);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing initialPrice: " + initialPrice + " for: " + name);
                } else {
                    logger.warning("Problem with parsing initialPrice: " + initialPrice);
                }
            }
            return this;
        }

        public Builder setFinalPrice(String finalPrice) {
            try {
                this.finalPrice = Float.parseFloat(finalPrice);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing finalPrice: " + finalPrice + " for: " + name);
                } else {
                    logger.warning("Problem with parsing finalPrice: " + finalPrice);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Builder setOwnerCount(String ownerCount) {
            try {
                this.ownerCount = Integer.parseInt(ownerCount);
            } catch (Exception e) {
                if (name != null) {
                    logger.warning("Problem with parsing ownerCount: " + ownerCount + " for: " + name);
                } else {
                    logger.warning("Problem with parsing ownerCount: " + ownerCount);
                }
                shouldBuild = false;
            }
            return this;
        }

        public Optional<Game> build() {
            if (shouldBuild) {
                return Optional.of(new Game(name, releaseYear, requiredAge, hasDemoVersion, dlcCount, metacriticScore, isControllerSupported, recommendationCount, achievementCount,
                        initialPrice, finalPrice, ownerCount));
            } else {
                return Optional.empty();
            }
        }
    }
}
