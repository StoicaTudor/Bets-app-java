package bets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

class Competition {

    class UtilGame {

        int idA;
        int idB;
        long startTime;

        int ticketIndex;
        int typeOfTicket; /// this can either be 0 (onGoingTicket) or -1 (lostTicket)

        int oddIndex;
        int indexInAPIfindings;

        UtilGame(int idA, int idB, long startTime, int ticketIndex, int oddIndex, int typeOfTicket) {
            this.idA = idA;
            this.idB = idB;
            this.startTime = startTime;
            this.ticketIndex = ticketIndex;
            this.oddIndex = oddIndex;
            this.indexInAPIfindings = -1;
            this.typeOfTicket = typeOfTicket;
        }
    }

    int leagueID;
    int nrGames;
    UtilGame[] gameObj = new UtilGame[Constants.MAX_NR_GAMES_IN_A_LEAGUE_ON_ALL_ON_GOING_TICKETS];

    public Competition(int leagueID) {
        this.leagueID = leagueID;
    }

    public void createNewGame(int idA, int idB, long startTime, int ticketIndex, int oddIndex, int typeOfTicket) {

        this.gameObj[this.nrGames] = new UtilGame(idA, idB, startTime, ticketIndex, oddIndex, typeOfTicket);
        this.nrGames++;
    }
}

public class FindGamesResults implements Constants {

    public UserData user;
    private DatabaseConnectUser dbUserObj;
    private Competition[] leagueObj = new Competition[leaguesObjBetano.objectLength]; /// for every league
    private int leagueObjLength = 0;

    public FindGamesResults(UserData user, DatabaseConnectUser dbUserObj) {
        /**
         * **
         * it works somehow like a hash map: we try to encapsulate all games
         * from a specific competition (competition id) in a
         * IDsAndStartTime[index]; and because looping all IDsAndStartTime[] to
         * search for that specific leagueID is time consuming, using hash maps
         * makes it faster; suppose that 2005 is the max leagueID -> this might
         * change; we do all these stuff, because we have to call a specific api
         * for all results in a league; this has 2 problems: time & money ->
         * because I use rapid api which has a limit of 100 api calls/day and by
         * not doing these optimization (creating Competition[] leagueObj for
         * every league in which the user has bets in), we may call the same api
         * several times; and it is not efficient
         */
        int[] hashMapLeagueObj = new int[2005];
        hashMapLeagueObj = initIntArrayToMinus1(hashMapLeagueObj);
        this.user = user;
        this.dbUserObj = dbUserObj;

        for (int i = 0; i < user.nrOfOnGoingTickets; i++) {

            for (int j = 0; j < user.onGoingTicket[i].nrOfBetsOnTicket; j++) {

                if (weShouldVerifyBet(user.onGoingTicket[i].bet[j])) {

                    if (hashMapLeagueObj[user.onGoingTicket[i].bet[j].leagueRapidAPI_id] == -1) {

                        hashMapLeagueObj[user.onGoingTicket[i].bet[j].leagueRapidAPI_id] = leagueObjLength;

                        int idA = user.onGoingTicket[i].bet[j].teamARapidAPI_id;
                        int idB = user.onGoingTicket[i].bet[j].teamBRapidAPI_id;
                        long startTime = user.onGoingTicket[i].bet[j].startTime;
                        int leagueID = user.onGoingTicket[i].bet[j].leagueRapidAPI_id;

                        this.leagueObj[this.leagueObjLength] = new Competition(leagueID);
                        this.leagueObj[this.leagueObjLength].createNewGame(idA, idB, startTime, i, j, 0);
                        this.leagueObjLength++;
                    } else {

                        int index = hashMapLeagueObj[user.onGoingTicket[i].bet[j].leagueRapidAPI_id];

                        int idA = user.onGoingTicket[i].bet[j].teamARapidAPI_id;
                        int idB = user.onGoingTicket[i].bet[j].teamBRapidAPI_id;
                        long startTime = user.onGoingTicket[i].bet[j].startTime;
                        int leagueID = user.onGoingTicket[i].bet[j].leagueRapidAPI_id;

                        this.leagueObj[index].createNewGame(idA, idB, startTime, i, j, 0);
                    }
                }
            }
        }

        /// we do this because there might be lost tickets that do not have all their bets evaluated
        ///!!!! as an optimization -> do not take all bets from all lostTickets, because is time consuming
        /// I should better make a list of lostTickets which have ongoing bets on them
        for (int i = 0; i < user.nrOfLostTickets; i++) {

            for (int j = 0; j < user.lostTicket[i].nrOfBetsOnTicket; j++) {

                if (weShouldVerifyBet(user.lostTicket[i].bet[j])) {

                    if (hashMapLeagueObj[user.lostTicket[i].bet[j].leagueRapidAPI_id] == -1) {

                        hashMapLeagueObj[user.lostTicket[i].bet[j].leagueRapidAPI_id] = leagueObjLength;

                        int idA = user.lostTicket[i].bet[j].teamARapidAPI_id;
                        int idB = user.lostTicket[i].bet[j].teamBRapidAPI_id;
                        long startTime = user.lostTicket[i].bet[j].startTime;
                        int leagueID = user.lostTicket[i].bet[j].leagueRapidAPI_id;

                        this.leagueObj[this.leagueObjLength] = new Competition(leagueID);
                        this.leagueObj[this.leagueObjLength].createNewGame(idA, idB, startTime, i, j, -1);
                        this.leagueObjLength++;
                    } else {

                        int index = hashMapLeagueObj[user.lostTicket[i].bet[j].leagueRapidAPI_id];

                        int idA = user.lostTicket[i].bet[j].teamARapidAPI_id;
                        int idB = user.lostTicket[i].bet[j].teamBRapidAPI_id;
                        long startTime = user.lostTicket[i].bet[j].startTime;
                        int leagueID = user.lostTicket[i].bet[j].leagueRapidAPI_id;

                        this.leagueObj[index].createNewGame(idA, idB, startTime, i, j, -1);
                    }
                }
            }
        }
        callAPIforEachMatchInEachLeague();
        modifyTicketsStatusesOnGoingTickets();
    }

    private boolean weShouldVerifyBet(BetForBettingTicket bet) {

        Date currentDate = new Date();
        long currentTimeInMS = currentDate.getTime();

        // Date matchDate = new Date(bet.startTime);
//        System.out.println(bet.teamA + " vs " + bet.teamB + " -----> " + bet.startTime + " = " + matchDate.toString() + " status "
//                + bet.betStatus + " ---> currentTime = " + currentTimeInMS + " , " + (currentTimeInMS - bet.startTime >= 6900000));
        /*
        * if bet is on going (match hasn't finished)
        * and if there have passed 115 minutes since the game has started
         */
        if (bet.betStatus == 0 && currentTimeInMS - bet.startTime >= 6900000) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        // new FindGamesResults();
    }

    private void callAPIforEachMatchInEachLeague() {

        for (int i = 0; i < this.leagueObjLength; i++) {

            ReadAndParseRapidAPIFixtures apiObj = new ReadAndParseRapidAPIFixtures(this.leagueObj[i].leagueID);
            apiObj.nrGamesOnTicketsInThisLeague = this.leagueObj[i].nrGames; /// for optimization
            this.leagueObj[i] = apiObj.parseJson(this.leagueObj[i]);
            /// it returns indexInAPIfindings for each game
            //  System.out.println(this.leagueObj[i].nrGames);
            ///////////////////???????????int indexGameInAPIfindings = getIndexGameInAPIfindings(this.leagueObj);
            for (int j = 0; j < this.leagueObj[i].nrGames; j++) { /// pentru fiecare meci din liga, analizam daca e castigat betul

                int indexGameInAPIfindings = this.leagueObj[i].gameObj[j].indexInAPIfindings;
                int ticketIndex = this.leagueObj[i].gameObj[j].ticketIndex;
                int typeOfTicket = this.leagueObj[i].gameObj[j].typeOfTicket;
                int oddIndex = this.leagueObj[i].gameObj[j].oddIndex;

//                System.out.println("user.onGoingTicket[ticketIndex].bet[oddIndex].teamA  --> "
//                        + user.onGoingTicket[ticketIndex].bet[oddIndex].teamA);
//
//                System.out.println("user.onGoingTicket[ticketIndex].bet[oddIndex].teamB  --> "
//                        + user.onGoingTicket[ticketIndex].bet[oddIndex].teamB);
//
//                System.out.println("user.onGoingTicket[ticketIndex].bet[oddIndex].betName  --> "
//                        + user.onGoingTicket[ticketIndex].bet[oddIndex].betName);
//
//                System.out.println("user.onGoingTicket[ticketIndex].bet[oddIndex].oddName  --> "
//                        + user.onGoingTicket[ticketIndex].bet[oddIndex].oddName);
//
//                System.out.println("indexGameInAPIfindings --> " + indexGameInAPIfindings);
//
//                System.out.println("apiObj.game[indexGameInAPIfindings].scoreAfirstHalf  --> "
//                        + apiObj.game[indexGameInAPIfindings].scoreAfirstHalf);
//
//                System.out.println("apiObj.game[indexGameInAPIfindings].scoreBfirstHalf  --> "
//                        + apiObj.game[indexGameInAPIfindings].scoreBfirstHalf);
//
//                System.out.println("apiObj.game[indexGameInAPIfindings].scoreAsecondHalf  --> "
//                        + apiObj.game[indexGameInAPIfindings].scoreAsecondHalf);
//
//                System.out.println(" apiObj.game[indexGameInAPIfindings].scoreBsecondHalf  --> "
//                        + apiObj.game[indexGameInAPIfindings].scoreBsecondHalf);
//                System.out.println("\n\n");
                ProcessOddStatus processObj = null;

                if (typeOfTicket == 0) {

                    processObj
                            = new ProcessOddStatus(user.onGoingTicket[ticketIndex].bet[oddIndex].teamA,
                                    user.onGoingTicket[ticketIndex].bet[oddIndex].teamB, user.onGoingTicket[ticketIndex].bet[oddIndex].betName,
                                    user.onGoingTicket[ticketIndex].bet[oddIndex].oddName, apiObj.game[indexGameInAPIfindings].scoreAfirstHalf,
                                    apiObj.game[indexGameInAPIfindings].scoreBfirstHalf, apiObj.game[indexGameInAPIfindings].scoreAsecondHalf,
                                    apiObj.game[indexGameInAPIfindings].scoreBsecondHalf);

                    user.onGoingTicket[ticketIndex].bet[oddIndex].betStatus = processObj.isWinningBet; /// either 1 or -1
                } else if (typeOfTicket == 0) {
                    processObj
                            = new ProcessOddStatus(user.lostTicket[ticketIndex].bet[oddIndex].teamA,
                                    user.lostTicket[ticketIndex].bet[oddIndex].teamB, user.lostTicket[ticketIndex].bet[oddIndex].betName,
                                    user.lostTicket[ticketIndex].bet[oddIndex].oddName, apiObj.game[indexGameInAPIfindings].scoreAfirstHalf,
                                    apiObj.game[indexGameInAPIfindings].scoreBfirstHalf, apiObj.game[indexGameInAPIfindings].scoreAsecondHalf,
                                    apiObj.game[indexGameInAPIfindings].scoreBsecondHalf);

                    user.lostTicket[ticketIndex].bet[oddIndex].betStatus = processObj.isWinningBet; /// either 1 or -1
                    updateInDBOddsInLostTicket(user.lostTicket[ticketIndex].bet[oddIndex]);
                } else {
                    System.out.println("O INTRAT PE RAMURA DE DEFAULT LA callAPIforEachMatchInEachLeague -> FindGamesResults");
                }
            }

//                System.out.println(processObj.isWinningBet);
//                System.out.println(user.onGoingTicket[ticketIndex].bet[oddIndex].teamA);
//                System.out.println(user.onGoingTicket[ticketIndex].bet[oddIndex].teamB);
//                System.out.println(user.onGoingTicket[ticketIndex].bet[oddIndex].betName);
//                System.out.println(user.onGoingTicket[ticketIndex].bet[oddIndex].oddName + "\n\n\n");
        }
    }

    private int[] initIntArrayToMinus1(int[] hashMapLeagueObj) {
        for (int i = 0; i < hashMapLeagueObj.length; i++) {
            hashMapLeagueObj[i] = -1;
        }
        return hashMapLeagueObj;
    }

    private void modifyTicketsStatusesOnGoingTickets() {
        for (int i = 0; i < this.user.nrOfOnGoingTickets; i++) {

            int ticketIsWon = 1;
            boolean ticketHasAllGamesDone = true;

            for (int j = 0; j < this.user.onGoingTicket[i].nrOfBetsOnTicket; j++) {

                if (this.user.onGoingTicket[i].bet[j].betStatus == 0) { /// we found a game that has not ended yet
                    ticketHasAllGamesDone = false;
                    break;
                }

                if (this.user.onGoingTicket[i].bet[j].betStatus == -1) { /// we found a bet that is lost
                    ticketIsWon = -1; /// => the entire ticket is lost
                    break;
                    ///!!!! de implementat analiza biletelor pierdute care inca mai au meciuri neterminate
                }
            }

            if (ticketHasAllGamesDone == true) {

                this.user.onGoingTicket[i].bettingTicketStatus = ticketIsWon; /// either 1 or -1
                /* 
                now we must modify this.user.onGoingTicket[i], this.user.wonTicket[i], and this.user.lostTicket[i]
                because the onGoingTicket is either won or lost;
                 */

                BettingTicket tempTicket = this.user.onGoingTicket[i]; /// save it for assignements

                /// shift to left onGoingTicket array
                for (int k = i; k < this.user.nrOfOnGoingTickets; k++) {

                    this.user.onGoingTicket[k] = this.user.onGoingTicket[k + 1];
                }

                this.user.nrOfOnGoingTickets--;
                i--; /// !!!! very important, because we do not want to ckip the next ticket
                modifyInDBTicketAndItsOdds(tempTicket); /// make ticketStatus 1 or -1 and betsStatuses 1 or -1 (since ticketHasAllGamesDone == true)

                if (ticketIsWon == 1) { /// add to wonTickets this ticket

                    this.user.wonTicket[this.user.nrOfWonTickets++] = tempTicket;
                    user.userBalance += tempTicket.bettingTicketSum * tempTicket.bettingTicketOdd;
                    updateBalance(user.userID, user.userBalance);

                } else if (ticketIsWon == -1) { /// add to lostTickets this ticket

                    this.user.lostTicket[this.user.nrOfLostTickets++] = tempTicket;
                }
            }
        }
    }

    private void modifyInDBTicketAndItsOdds(BettingTicket tempTicket) {

        StringBuilder updateTicketSB = new StringBuilder();
        updateTicketSB.append("UPDATE bettingticket SET bettingTicketStatus=");
        updateTicketSB.append(tempTicket.bettingTicketStatus);
        updateTicketSB.append(" WHERE bettingTicketID=");
        updateTicketSB.append(tempTicket.ticketIDinDB);

        try {
            this.dbUserObj.st.executeUpdate(updateTicketSB.toString());

            StringBuilder updateBetSB;

            for (int i = 0; i < tempTicket.nrOfBetsOnTicket; i++) {

                updateBetSB = new StringBuilder();
                updateBetSB.append("UPDATE betshistory SET betStatus=");
                updateBetSB.append(tempTicket.bet[i].betStatus);
                updateBetSB.append(" WHERE betID=");
                updateBetSB.append(tempTicket.bet[i].betIDinDB);

                try {
                    this.dbUserObj.st.executeUpdate(updateBetSB.toString());
                } catch (SQLException ex) {
                    System.out.println(ex + " : in modifyInDB bet");
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex + " : in modifyInDB ticket");
        }
    }

    private void updateBalance(int userID, float newUserBalance) {

        StringBuilder updateBalanceQuerrySB = new StringBuilder();
        updateBalanceQuerrySB.append("UPDATE user SET userBalance=");
        updateBalanceQuerrySB.append(newUserBalance);
        updateBalanceQuerrySB.append(" WHERE userID=");
        updateBalanceQuerrySB.append(userID);

        try {
            this.dbUserObj.st.executeUpdate(updateBalanceQuerrySB.toString());
        } catch (SQLException ex) {
            System.out.println(ex + " : in updateBalance");
        }
    }

    private void updateInDBOddsInLostTicket(BetForBettingTicket lostTicket) {

        StringBuilder updateOddQuerrySB = new StringBuilder();
        updateOddQuerrySB.append("UPDATE betshistory SET betStatus=");
        updateOddQuerrySB.append(lostTicket.betStatus);
        updateOddQuerrySB.append(" WHERE betID=");
        updateOddQuerrySB.append(lostTicket.betIDinDB);

        try {
            this.dbUserObj.st.executeUpdate(updateOddQuerrySB.toString());
        } catch (SQLException ex) {
            System.out.println(ex + " : in updateBalance");
        }
    }
}
