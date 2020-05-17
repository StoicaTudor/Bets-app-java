package bets;

public class UserData implements Constants {

    UserData(int userID, String userName, float userBalance, int nrOfOnGoingTickets, BettingTicket[] onGoingTicket,
            int nrOfWonTickets, BettingTicket[] wonTicket, int nrOfLostTickets, BettingTicket[] lostTicket) {

        this.userID = userID;
        this.userBalance = userBalance;
        this.userName = userName;

        this.nrOfOnGoingTickets = nrOfOnGoingTickets;
        this.onGoingTicket = onGoingTicket;

        this.nrOfWonTickets = nrOfWonTickets;
        this.wonTicket = wonTicket;

        this.nrOfLostTickets = nrOfLostTickets;
        this.lostTicket = lostTicket;
    } ////// !!!!!!!!! exceptie daca depaseste nr

    int userID;
    float userBalance;
    String userName;

    int nrOfOnGoingTickets;
    int nrOfWonTickets;
    int nrOfLostTickets;

    BettingTicket[] onGoingTicket;
    BettingTicket[] wonTicket;
    BettingTicket[] lostTicket;
}
