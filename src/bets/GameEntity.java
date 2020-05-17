package bets;

public class GameEntity {

    int scoreA;
    int scoreB;
    int scoreAfirstHalf;
    int scoreBfirstHalf;
    int scoreAsecondHalf;
    int scoreBsecondHalf;
    long startTime;

    String link;
    String dateAndTime;
    String rawJSon;
    String niceJSon;
    String teamA, teamB;
    String gameTag;
    BetClass bet = new BetClass();

    public GameEntity(
            String gameTag, String url,
            String dateAndTime,
            String teamA, String teamB, BetClass bet, long startTime) {

        this.gameTag = gameTag;
        this.dateAndTime = dateAndTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.bet = bet;
        this.link = url;
        this.startTime = startTime;
    }

    public GameEntity(long dateAndTime,String teamA, String teamB, int scoreAfirstHalf, int scoreBfirstHalf,
            int scoreAsecondHalf, int scoreBsecondHalf) {

        this.startTime=dateAndTime;
        this.teamA=teamA;
        this.teamB=teamB;
        this.scoreAfirstHalf=scoreAfirstHalf;
        this.scoreBfirstHalf=scoreBfirstHalf;
        this.scoreAsecondHalf=scoreAsecondHalf;
        this.scoreBsecondHalf=scoreBsecondHalf;
    }

    public void finalScore(int scoreAfirstHalf, int scoreBfirstHalf,
            int scoreAsecondHalf, int scoreBsecondHalf) {

        this.scoreAfirstHalf = scoreAfirstHalf;
        this.scoreAsecondHalf = scoreAsecondHalf;

        this.scoreBfirstHalf = scoreBfirstHalf;
        this.scoreBsecondHalf = scoreBsecondHalf;

        this.scoreA = scoreAfirstHalf + scoreAsecondHalf;
        this.scoreB = scoreBfirstHalf + scoreBsecondHalf;
    }
}
