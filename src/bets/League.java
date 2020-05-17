
package bets;

public class League implements Constants {

    GameEntity[] entityObj = new GameEntity[maxNrOfMatchesOnPage];
    int size;
    String leagueName;

    public League() {

    }

    public void createNewEntity(
            String gameTag, String url, String dateAndTime,
            String teamA, String teamB, BetClass bet, long startTime) {

        this.entityObj[this.size] = new GameEntity(gameTag, url, dateAndTime, teamA, teamB, bet, startTime);
        this.size++;
    }
}
