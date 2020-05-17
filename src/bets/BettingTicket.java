package bets;

class BetForBettingTicket {

    String teamA;
    String teamB;
    int leagueRapidAPI_id;
    long startTime;
    String matchTag;
    String date;
    String fullBetName;
    float oddValue;
    String betName;
    String oddName;
    int betStatus; /// gameStatus
    int teamARapidAPI_id;
    int teamBRapidAPI_id;
    int betIDinDB;

    private void computeBetOdd(String oddNameAndValue) {

        this.oddName = oddNameAndValue.substring(0, oddNameAndValue.indexOf("  ->"));//////////eroare aici string out of range -1
        this.oddValue = Float.valueOf(oddNameAndValue.substring(oddNameAndValue.indexOf("  ->") + 6, oddNameAndValue.length()));
    }

    public void computeFullBetName() {

        StringBuilder fullBetNameSB = new StringBuilder();
        fullBetNameSB.append(matchTag);
        fullBetNameSB.append(" ->  ");
        fullBetNameSB.append(date);
        fullBetNameSB.append(" ->  ");
        fullBetNameSB.append(betName);
        fullBetNameSB.append(" ->  ");
        fullBetNameSB.append(oddName);
        fullBetNameSB.append(" ->  ");
        fullBetNameSB.append(oddValue);

        this.fullBetName = fullBetNameSB.toString();
    }

    BetForBettingTicket() {

    }

    BetForBettingTicket(String matchTag, String date, String betName,
            String oddNameAndValue, long startTime, int leagueRapidAPI_id,
            int teamARapidAPI_id, int teamBRapidAPI_id, String teamA, String teamB) {

        this.matchTag = matchTag;
        this.date = date;
        this.betName = betName;
        this.computeBetOdd(oddNameAndValue);
        this.betStatus = 1;
        this.computeFullBetName();
        this.leagueRapidAPI_id = leagueRapidAPI_id;
        this.startTime = startTime;
        this.teamARapidAPI_id = teamARapidAPI_id;
        this.teamBRapidAPI_id = teamBRapidAPI_id;
        this.teamA = teamA;
        this.teamB = teamB;
    }
}

public class BettingTicket implements Constants {

    String bettingTicketName; /// cu tot cu data cred
    int nrOfBetsOnTicket;
    float bettingTicketOdd;
    float bettingTicketSum;
    int bettingTicketStatus;
    int userID;
    
    int ticketIDinDB;

    BetForBettingTicket[] bet = new BetForBettingTicket[MAX_NR_BETS_PER_TICKET];

    BettingTicket() {

    }

    BettingTicket(String bettingTicketName, int userID) {

        this.bettingTicketName = bettingTicketName;
        this.userID = userID;
        this.bettingTicketStatus = 1;
    }

    public void computeBettingTicketOdd() {

        float odd = 1;

        for (int i = 0; i < this.nrOfBetsOnTicket; i++) {

            odd *= this.bet[i].oddValue;
        }

        this.bettingTicketOdd = odd;
    }

    public void createNewBetEntity(String matchTag, String date, String betName, String oddNameAndValue,
            long startTime, int leagueRapidAPI_id, int teamARapidAPI_id, int teamBRapidAPI_id, String teamAname,
            String teamBname) {

        if (this.nrOfBetsOnTicket < MAX_NR_BETS_PER_TICKET) {

            this.bet[this.nrOfBetsOnTicket] = new BetForBettingTicket(matchTag, date, betName, oddNameAndValue,
                    startTime, leagueRapidAPI_id, teamARapidAPI_id, teamBRapidAPI_id, teamAname, teamBname);
            this.nrOfBetsOnTicket++;
        } else {
            /// throw error message
        }
    }

    public String[] computeBetsTags() {

        String[] betTag = new String[MAX_NR_BETS_PER_TICKET];

        for (int i = 0; i < this.nrOfBetsOnTicket; i++) {

            betTag[i] = this.bet[i].fullBetName;
        }

        return betTag;
    }

}
