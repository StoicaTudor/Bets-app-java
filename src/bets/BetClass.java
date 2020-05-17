package bets;

class Bet implements Constants {

    class Odd {

        String oddName;
        float oddValue;

        protected Odd(String oddName, float oddValue) {
            this.oddName = oddName;
            this.oddValue = oddValue;
        }
    }

    String betName;
    int nrOfodds;
    Odd[] odds = new Odd[MAX_NUMBER_BETTING_ODDS];

    protected Bet(String betName) {
        this.betName = betName;
    }

    public void createNewBetOddsEntity(String oddName, float oddValue) {

        this.odds[this.nrOfodds] = new Odd(oddName, oddValue);
        this.nrOfodds++;
    }

    protected void outputOdds() {
        for (int i = 0; i < this.nrOfodds; i++) {

            System.out.println(this.betName + " " + this.odds[i].oddName + " " + this.odds[i].oddValue);
        }
    }
}

public class BetClass implements Constants {

    Bet[] betTypesObj = new Bet[MAX_NUMBER_BETTING_TYPES];
    int bettingTypesLength;

    public BetClass() {

    }

    public void createNewBetEntity(String betName) {

        this.betTypesObj[this.bettingTypesLength] = new Bet(betName);
        this.bettingTypesLength++;
    }

    public void outputObject(int objIndex) {
        this.betTypesObj[objIndex].outputOdds();
    }
}
