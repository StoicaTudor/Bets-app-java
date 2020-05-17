package bets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectUser implements Constants {

    public static void main(String[] args) {
        new DatabaseConnectUser(0);
    }

    public Connection con;
    public Statement st;
    public Statement st2;
    public ResultSet rs;

    public DatabaseConnectUser() {
    }

    public UserData createNewUser(String name, String password) {

        StringBuilder insertNewUserQuerrySB = new StringBuilder();
        insertNewUserQuerrySB.append("INSERT INTO `user` (`userID`, `userName`,`userPassword`,`userBalance`) "
                + "VALUES (NULL,'");
        insertNewUserQuerrySB.append(name);
        insertNewUserQuerrySB.append("','");
        insertNewUserQuerrySB.append(password);
        insertNewUserQuerrySB.append("','");
        insertNewUserQuerrySB.append(INITIAL_BETTING_SUM);
        insertNewUserQuerrySB.append("')");

        String insertNewUserQuerry = insertNewUserQuerrySB.toString();

        try {
            st.executeUpdate(insertNewUserQuerry, Statement.RETURN_GENERATED_KEYS);
            rs = st.getGeneratedKeys();

            if (rs.next()) {

                int insertID = rs.getInt(1);
               // String insertedUserName = rs.getString(2);

                BettingTicket[] onGoingTickets = new BettingTicket[MAX_NR_ADDITIONAL_TICKETS_SUBMITTED_ON_APP_SESSION];
                BettingTicket[] wonTickets = new BettingTicket[MAX_NR_ADDITIONAL_TICKETS_SUBMITTED_ON_APP_SESSION];
                BettingTicket[] lostTickets = new BettingTicket[MAX_NR_ADDITIONAL_TICKETS_SUBMITTED_ON_APP_SESSION];

                UserData userObj = new UserData(insertID, name, INITIAL_BETTING_SUM, 0, onGoingTickets, 0, wonTickets, 0, lostTickets);
                /// rs.close(); ///// may cause trouble
                return userObj;
            }
            return null;
        } catch (SQLException ex) {

            System.out.println("createNewUser" + " : " + ex);
            return null;
        }
    }

    public UserData validateSignIn(String name, String password) {

        StringBuilder searchUserSB = new StringBuilder();
        searchUserSB.append("SELECT * from `user` where `userName`='");
        searchUserSB.append(name);
        searchUserSB.append("' and `userPassword`='");
        searchUserSB.append(password);
        searchUserSB.append("'");

        String searchUser = searchUserSB.toString();

        try {
            ResultSet resultset;
            resultset = st.executeQuery(searchUser);

            while (resultset.next()) { /// it found result

                int userID = resultset.getInt(1);
                String userName = name;
                float userBalance = resultset.getFloat(4);

                /// we found result => we can browse bettingticket tabel
                /// compute the nr of tickets / each category
                int nrWonTickets = browseNrTicketsWithStatus(userID, WON_TICKET_STATUS);
                int nrOnGoingTickets = browseNrTicketsWithStatus(userID, ON_GOING_TICKET_STATUS);
                int nrLostTickets = browseNrTicketsWithStatus(userID, LOST_TICKET_STATUS);
                ///

                /// compute tickets / each category
                BettingTicket[] wonTicket = browseTicketsWithStatus(userID, WON_TICKET_STATUS, nrWonTickets);
                BettingTicket[] onGoingTicket = browseTicketsWithStatus(userID, ON_GOING_TICKET_STATUS, nrOnGoingTickets);
                BettingTicket[] lostTicket = browseTicketsWithStatus(userID, LOST_TICKET_STATUS, nrLostTickets);
                ///

                ///
                UserData userObj = new UserData(userID, userName, userBalance, nrOnGoingTickets,
                        onGoingTicket, nrWonTickets, wonTicket, nrLostTickets, lostTicket);

                resultset.close();
                return userObj;
            }
            resultset.close();
            return null;
        } catch (SQLException ex) {

            System.out.println("validateSignIn" + " : " + ex);
            return null;
        }
    }

    public DatabaseConnectUser(int a) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306", user = "root", password = "";
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();

            rs = conn.getMetaData().getCatalogs();
            boolean dbUserExists = false;

            while (rs.next()) {
                String databaseName = rs.getString(1);

                if (databaseName.equals("user")) {
                    dbUserExists = true;
                    break;
                }
            }

            if (!dbUserExists) {
                String createDB = "CREATE DATABASE IF NOT EXISTS user";
                stmt.executeUpdate(createDB);

                StringBuilder urlSB = new StringBuilder();
                urlSB.append(url);
                urlSB.append("/user");
                url = urlSB.toString();
                con = DriverManager.getConnection(url, user, password);
                st = con.createStatement();
                st2 = con.createStatement();
                createUsersTable();
                createTransactionsHistoryTable();
                createBettingTicketHistoryTable();
                createBetsHistoryTable();
                createInfoTable();

            } else {
                StringBuilder urlSB = new StringBuilder();
                urlSB.append(url);
                urlSB.append("/user");
                url = urlSB.toString();
                con = DriverManager.getConnection(url, user, password);
                st = con.createStatement();
                st2 = con.createStatement();
            }

        } catch (Exception ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + " DatabaseConnect " + " : " + ex);
        }
    }

    private void createUsersTable() {
        String queryUserTable
                = "CREATE TABLE IF NOT EXISTS `user`.`user` (`userID` INT NOT NULL AUTO_INCREMENT , `userName` VARCHAR(25) NOT NULL, `userPassword` VARCHAR(25) NOT NULL, `userBalance` FLOAT NOT NULL, PRIMARY KEY (`userID`)) ENGINE = InnoDB;";
        try {
            st.executeUpdate(queryUserTable);
        } catch (SQLException ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + "createUsersTable" + " : " + ex);
        }
    }

    private void createTransactionsHistoryTable() {
        String queryTransactionsHistoryTable
                = "CREATE TABLE IF NOT EXISTS `user`.`transactionsHistory` (`transactionID` INT NOT NULL AUTO_INCREMENT , `transactionSum` FLOAT NOT NULL, `transactionDirection` BOOLEAN NOT NULL, `userID` INT NOT NULL,`date` BIGINT NOT NULL, PRIMARY KEY (`transactionID`)) ENGINE = InnoDB;";

        String queryIndex = "CREATE INDEX fk_index ON transactionsHistory (userID)";

        String queryFK = "ALTER TABLE transactionsHistory ADD CONSTRAINT fk_userID_transactionsHistory FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE ON UPDATE CASCADE;";

        try {
            st.executeUpdate(queryTransactionsHistoryTable);
            st.executeUpdate(queryIndex);
            st.executeUpdate(queryFK);
        } catch (SQLException ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + "createTransactionsHistoryTable" + " : " + ex);
        }
    }

    private void createBettingTicketHistoryTable() {
        String queryBettingTicketHistoryTable
                = "CREATE TABLE IF NOT EXISTS `user`.`bettingTicket` (`bettingTicketID` INT NOT NULL AUTO_INCREMENT , `bettingTicketName` VARCHAR(15) NOT NULL, `bettingTicketOdd` FLOAT NOT NULL, `bettingTicketSum` FLOAT NOT NULL, `bettingTicketStatus` INT NOT NULL default 0, `userID` INT NOT NULL, PRIMARY KEY (`bettingTicketID`)) ENGINE = InnoDB;";

        String queryIndex = "CREATE INDEX fk_index ON bettingTicket (userID)";

        String queryFK = "ALTER TABLE bettingTicket ADD CONSTRAINT fk_userID_bettingTicket FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE ON UPDATE CASCADE;";

        try {
            st.executeUpdate(queryBettingTicketHistoryTable);
            st.executeUpdate(queryIndex);
            st.executeUpdate(queryFK);
        } catch (SQLException ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + " createBettingTicketHistoryTable " + " : " + ex);
        }
    }

    private void createBetsHistoryTable() {
        String queryBetsHistoryTable
                = "CREATE TABLE IF NOT EXISTS `user`.`betsHistory` (`betID` INT NOT NULL AUTO_INCREMENT , `match` VARCHAR(70) NOT NULL,`leagueRapidAPI_ID` INT NOT NULL, `startTime` BIGINT NOT NULL, `bet` VARCHAR(55) NOT NULL,`odd` VARCHAR(35) NOT NULL, `betOdd` FLOAT NOT NULL, `betStatus` INT NOT NULL default 0, `bettingTicketID` INT NOT NULL, `userID` INT NOT NULL,`teamARapidAPI_id` INT NOT NULL,`teamBRapidAPI_id` INT NOT NULL, `teamAname` VARCHAR(35) NOT NULL, `teamBname` VARCHAR(35) NOT NULL,PRIMARY KEY (`betID`)) ENGINE = InnoDB;";

        String queryIndex = "CREATE INDEX fk_index ON betsHistory (bettingTicketID)";
        String queryIndex2 = "CREATE INDEX fk_index2 ON betsHistory (userID)";

        String queryFK = "ALTER TABLE betsHistory ADD CONSTRAINT fk_bettingTicketID_betsHistory FOREIGN KEY (bettingTicketID) REFERENCES bettingTicket (bettingTicketID) ON DELETE CASCADE ON UPDATE CASCADE;";
        String queryFK2 = "ALTER TABLE betsHistory ADD CONSTRAINT fk_bettingTicketID_betsHistory2 FOREIGN KEY (userID) REFERENCES bettingTicket (userID) ON DELETE CASCADE ON UPDATE CASCADE;";

        try {
            st.executeUpdate(queryBetsHistoryTable);
            st.executeUpdate(queryIndex);
            st.executeUpdate(queryIndex2);
            st.executeUpdate(queryFK);
            st.executeUpdate(queryFK2);
        } catch (SQLException ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + "createBetsHistoryTable" + " : " + ex);
        }
    }

    private void createInfoTable() {
        String querryInfoTable
                = "CREATE TABLE IF NOT EXISTS `user`.`appInfo` "
                + "(`id` INT NOT NULL AUTO_INCREMENT , "
                + "`remainingCalls` INT NOT NULL, `date` TIMESTAMP NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB;";

        try {
            st.executeUpdate(querryInfoTable);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnectUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public BettingTicket insertIntoUserTicket(int userID, BettingTicket ticket) { /// insertTicket pt ca e insert in maimulte tabele

        StringBuilder searchUserSB = new StringBuilder();
        searchUserSB.append("SELECT * from `user` where `userID`='");
        searchUserSB.append(userID);
        searchUserSB.append("'");

        try {
            ResultSet resultset;
            resultset = st.executeQuery(searchUserSB.toString());
            float currentBalance = 0;

            while (resultset.next()) { /// it found result
                currentBalance = resultset.getFloat(4);
                ///  resultset.close();
            }

            currentBalance -= ticket.bettingTicketSum;

            StringBuilder querryUserBalance = new StringBuilder();
            querryUserBalance.append("UPDATE user SET userBalance = ");
            querryUserBalance.append(currentBalance);
            querryUserBalance.append(" WHERE userID = ");
            querryUserBalance.append(userID);

            StringBuilder querryAddTicket = new StringBuilder();
            querryAddTicket.append("INSERT INTO `bettingticket` "
                    + "(`bettingTicketID`, `bettingTicketName`,`bettingTicketOdd`,`bettingTicketSum`"
                    + ",`bettingTicketStatus`,`userID`) VALUES (NULL, '");
            querryAddTicket.append(ticket.bettingTicketName);
            querryAddTicket.append("', '");
            querryAddTicket.append(ticket.bettingTicketOdd);
            querryAddTicket.append("', '");
            querryAddTicket.append(ticket.bettingTicketSum);
            querryAddTicket.append("', '");
            querryAddTicket.append(0); /// pentru ca orice bilet introdus e onGoing
            querryAddTicket.append("', '");
            querryAddTicket.append(ticket.userID);
            querryAddTicket.append("')");

            try {

                st.executeUpdate(querryUserBalance.toString());
                st.executeUpdate(querryAddTicket.toString(), Statement.RETURN_GENERATED_KEYS);
                rs = st.getGeneratedKeys();
                int ticketID = -1;

                if (rs.next()) {
                    ticketID = rs.getInt(1);
                    ticket.ticketIDinDB = ticketID;
                } else {
                    System.out.println("Something went wrong in insertIntoUserTicket0. I will return -1. -> at ticket");
                    return null;
                }

                StringBuilder querryAddBets = new StringBuilder();
                querryAddBets.append("INSERT INTO `betshistory` (`betID`, `match`,`leagueRapidAPI_ID`,`startTime`, `bet`,`odd`,"
                        + " `betOdd`, `betStatus`, `bettingTicketID`, "
                        + "`userID`,`teamARapidAPI_id`,`teamBRapidAPI_id`,`teamAname`,`teamBname`) VALUES ");
                querryAddBets.append(computeBetsQuerry(ticket, ticketID));

                st.executeUpdate(querryAddBets.toString(), Statement.RETURN_GENERATED_KEYS);

                rs = st.getGeneratedKeys();

                int betID = -1;
                int index = 0;

                while (rs.next()) {
                    betID = rs.getInt(1);
                    ticket.bet[index++].betIDinDB = betID;
                    betID = -1;
                }

                return ticket;

            } catch (SQLException ex) {

                System.out.println("insertIntoUserTicket0" + " : " + ex);
                return null;
            }

        } catch (SQLException ex) {

            System.out.println("insertIntoUserTicket1" + " : " + ex);
            return null;
        }
    }

    public UserData getUserWithID(int id) {

        StringBuilder userQuerrySB = new StringBuilder();
        userQuerrySB.append("SELECT * FROM user WHERE userID=");
        userQuerrySB.append(id);
        String userQuerry = userQuerrySB.toString();

        try {
            rs = st.executeQuery(userQuerry);

            while (rs.next()) {

                String userName = rs.getString(2);
                float userBalance = rs.getFloat(4);

                /// we found result => we can browse bettingticket tabel
                /// compute the nr of tickets / each category
                int nrWonTickets = browseNrTicketsWithStatus(id, WON_TICKET_STATUS);
                int nrOnGoingTickets = browseNrTicketsWithStatus(id, ON_GOING_TICKET_STATUS);
                int nrLostTickets = browseNrTicketsWithStatus(id, LOST_TICKET_STATUS);
                ///

                /// compute tickets / each category
                BettingTicket[] wonTicket = browseTicketsWithStatus(id, WON_TICKET_STATUS, nrWonTickets);
                BettingTicket[] onGoingTicket = browseTicketsWithStatus(id, ON_GOING_TICKET_STATUS, nrOnGoingTickets);
                BettingTicket[] lostTicket = browseTicketsWithStatus(id, LOST_TICKET_STATUS, nrLostTickets);
                ///

                ///
                UserData userObj = new UserData(id, userName, userBalance, nrOnGoingTickets,
                        onGoingTicket, nrWonTickets, wonTicket, nrLostTickets, lostTicket);

                return userObj;
            }
            return null;
        } catch (SQLException ex) {
            System.out.println(ex + " : in getUserWithID");
            return null;
        }
    }

    private int browseNrTicketsWithStatus(int userID, int status) {

        StringBuilder countQuerrySB = new StringBuilder();
        countQuerrySB.append("SELECT COUNT(bettingTicketID) FROM bettingticket where bettingTicketStatus=");
        countQuerrySB.append(status);
        countQuerrySB.append(" and userID=");
        countQuerrySB.append(userID);
        String countQuerry = countQuerrySB.toString();

        try {

            rs = st.executeQuery(countQuerry);

            while (rs.next()) { /// it found result

                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println(ex + " in browseNrTicketsWithStatus");
            return -1;
        }
    }

    private BettingTicket[] browseTicketsWithStatus(int userID, int status, int length) {

        StringBuilder QuerrySB = new StringBuilder();
        QuerrySB.append("SELECT * FROM bettingticket where bettingTicketStatus=");
        QuerrySB.append(status);
        QuerrySB.append(" and userID=");
        QuerrySB.append(userID);
        String Querry = QuerrySB.toString();

        try {
            Statement mySt = st;
            ResultSet resSet = st.executeQuery(Querry);

            /// we must declare a few more spaces for the array, just in case the user wins, 
            /// loses or submits other bets; we return this array
            BettingTicket[] ticket = new BettingTicket[length + MAX_NR_ADDITIONAL_TICKETS_SUBMITTED_ON_APP_SESSION];
            ///

            int len = 0;

            while (resSet.next()) {

                int ticketID = resSet.getInt(1);
                ticket[len] = new BettingTicket();
                ticket[len].bettingTicketName = resSet.getString(2);
                ticket[len].bettingTicketOdd = resSet.getFloat(3);
                ticket[len].bettingTicketSum = resSet.getFloat(4);
                ticket[len].bettingTicketStatus = status;
                ticket[len].userID = resSet.getInt(6);
                ticket[len].ticketIDinDB = resSet.getInt(1);

                ticket[len].nrOfBetsOnTicket = browseNrBetsOnTicketWithID(userID, ticketID);
                ticket[len].bet = browseBetsOnTicketWithID(userID, ticketID, ticket[len].nrOfBetsOnTicket);

                len++;
            }
            resSet.close();
            return ticket;
        } catch (SQLException ex) {
            System.out.println(ex + " in browseTicketsWithStatus");
            return null;
        }
    }

    private int browseNrBetsOnTicketWithID(int userID, int ticketID) {

        StringBuilder countBetsSB = new StringBuilder();
        countBetsSB.append("SELECT COUNT(betID) FROM betshistory where bettingTicketID=");
        countBetsSB.append(ticketID);
        countBetsSB.append(" and userID=");
        countBetsSB.append(userID);
        String countBets = countBetsSB.toString();

        try {

            ResultSet resSet = st2.executeQuery(countBets);

            while (resSet.next()) { /// found result

                int nrBets = resSet.getInt(1);
                resSet.close();
                return nrBets;
            }
            resSet.close();
            return -1;

        } catch (SQLException ex) {
            System.out.println(ex + " : in browseNrBetsOnTicketWithID");
            return -1;
        }
    }

    private BetForBettingTicket[] browseBetsOnTicketWithID(int userID, int ticketID, int nrBets) {

        StringBuilder browseBetsSB = new StringBuilder();
        browseBetsSB.append("SELECT * FROM betshistory where bettingTicketID=");
        browseBetsSB.append(ticketID);
        browseBetsSB.append(" and userID=");
        browseBetsSB.append(userID);
        String browseBets = browseBetsSB.toString();

        try {

            ResultSet resSet = st2.executeQuery(browseBets);
            BetForBettingTicket[] bet = new BetForBettingTicket[nrBets];
            int index = 0;

            while (resSet.next()) {
                bet[index] = new BetForBettingTicket();
                bet[index].matchTag = resSet.getString(2);
                bet[index].leagueRapidAPI_id = resSet.getInt(3);
                bet[index].startTime = resSet.getLong(4);
                bet[index].betName = resSet.getString(5);
                bet[index].oddName = resSet.getString(6);
                bet[index].oddValue = resSet.getFloat(7);
                bet[index].betStatus = resSet.getInt(8);
                bet[index].teamARapidAPI_id = resSet.getInt(11);
                bet[index].teamBRapidAPI_id = resSet.getInt(12);
                bet[index].teamA = resSet.getString(13);
                bet[index].teamB = resSet.getString(14);
                bet[index].betIDinDB = resSet.getInt(1);

                index++;
            }
            resSet.close();
            if (index > 0) {
                return bet;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            System.out.println(ex + " : in browseBetsOnTicketwWithID");
            return null;
        }
    }

    private String computeBetsQuerry(BettingTicket ticket, int ticketID) {

        StringBuilder querrySB = new StringBuilder();

        for (int i = 0; i < ticket.nrOfBetsOnTicket; i++) {

            querrySB.append("(NULL,'");
            querrySB.append(ticket.bet[i].matchTag);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].leagueRapidAPI_id);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].startTime);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].betName);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].oddName);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].oddValue);
            querrySB.append("','");
            querrySB.append(0);
            querrySB.append("','");
            querrySB.append(ticketID);
            querrySB.append("','");
            querrySB.append(ticket.userID);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].teamARapidAPI_id);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].teamBRapidAPI_id);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].teamA);
            querrySB.append("','");
            querrySB.append(ticket.bet[i].teamB);

            if (i < ticket.nrOfBetsOnTicket - 1) {
                querrySB.append("'), ");
            } else if (i == ticket.nrOfBetsOnTicket - 1) {
                querrySB.append("')");
            }
        }

        return querrySB.toString();
    }

    private void createCompetitionsTable() {

        String queryTable = "CREATE TABLE IF NOT EXISTS `user`.`competition` (`id` INT NOT NULL AUTO_INCREMENT , `competitionName` VARCHAR(35) NOT NULL,`season` INT NOT NULL,`rapidAPIcompetitionID` INT NOT NULL,`country` VARCHAR(25) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String rapidAPIcompetitionID_UNIQUE_KEY = "ALTER TABLE `user`.`competition` ADD UNIQUE(`rapidAPIcompetitionID`);";
        try {
            st.executeUpdate(queryTable);
            st.executeUpdate(rapidAPIcompetitionID_UNIQUE_KEY);
        } catch (SQLException ex) {

            System.out.println(ex + " createCompetitionsTable ");
        }
    }

    private void createTeamsTable() {

        String queryTable = "CREATE TABLE IF NOT EXISTS `user`.`teams` (`id` INT NOT NULL AUTO_INCREMENT , `teamName` VARCHAR(35) NOT NULL, `rapidAPIcompetitionID` INT NOT NULL, `rapidAPI_id` INT NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String queryIndex = "CREATE INDEX fk_index ON teams (rapidAPIcompetitionID)";
        String queryFK = "ALTER TABLE teams ADD CONSTRAINT fk_rapidAPIcompetitionID FOREIGN KEY (rapidAPIcompetitionID) REFERENCES competition (rapidAPIcompetitionID) ON DELETE CASCADE ON UPDATE CASCADE;";

        try {
            st.executeUpdate(queryTable);
            st.executeUpdate(queryIndex);
            st.executeUpdate(queryFK);

        } catch (SQLException ex) {
            String className = this.getClass().getSimpleName();
            System.out.println(className + " createTeamsTable " + " : " + ex);
        }
    }

    public int getIDTeam(String team) {
        StringBuilder selectQuerrySB = new StringBuilder();
        selectQuerrySB.append("SELECT `rapidAPI_id` FROM teams where `teamName`=");
        selectQuerrySB.append('"');
        selectQuerrySB.append(team);
        selectQuerrySB.append('"');

        try {
            rs = st.executeQuery(selectQuerrySB.toString());

            while (rs.next()) {
                int teamID = rs.getInt(1);

                return teamID;
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println(ex + " : in getIDTeam");
            return -1;
        }
    }

}

////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//A ResultSet object is automatically closed when the Statement object that generated it is closed, 
//re-executed, or used to retrieve the next result from a sequence of multiple results.
/////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
