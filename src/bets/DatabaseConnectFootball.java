//package bets;
//
//import java.sql.*;
//
//public class DatabaseConnectFootball {
//
//    private Connection con;
//    private Statement st;
//    private ResultSet rs;
//
//    public DatabaseConnectFootball() {
//
//    }
//
//    public static void main(String[] args) {
//        new DatabaseConnectFootball(0);
//    }
//
//    public DatabaseConnectFootball(int a) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            String url = "jdbc:mysql://localhost:3306", user = "root", password = "";
//            Connection conn = DriverManager.getConnection(url, user, password);
//
//            Statement stmt = conn.createStatement();
//            String createDB = "CREATE DATABASE IF NOT EXISTS football";
//            stmt.executeUpdate(createDB);
//
//            StringBuilder urlSB = new StringBuilder();
//            urlSB.append(url);
//            urlSB.append("/football");
//            url = urlSB.toString();
//            con = DriverManager.getConnection(url, user, password);
//            st = con.createStatement();
//
//            createCompetitionsTable();
//            createTeamsTable();
//            createShapeTable();
//            createCardsTable();
//            createGoals_StatsTable();
//            createGamesTable();
//            createOddsTable();
//
//        } catch (Exception ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " DatabaseConnect " + " : " + ex);
//        }
//    }
//
//    private void createCompetitionsTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`competition` (`competitionID` INT NOT NULL AUTO_INCREMENT , `competitionName` VARCHAR(15) NOT NULL,`season` INT NOT NULL,`competitionID_RapidAPI` INT NOT NULL, PRIMARY KEY (`competitionID`)) ENGINE = InnoDB;";
//        try {
//            st.executeUpdate(queryTable);
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createCompetitionsTable " + " : " + ex);
//        }
//    }
//
//    private void createTeamsTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`teams` (`teamID` INT NOT NULL AUTO_INCREMENT , `teamName` VARCHAR(25) NOT NULL, `teamPlace` INT NOT NULL, `competitionID` INT NOT NULL, `teamForm` VARCHAR(25) NOT NULL, `teamFormHome` VARCHAR(25) NOT NULL, `teamFormAway` VARCHAR(25) NOT NULL,`rapidAPI_id` INT NOT NULL, PRIMARY KEY (`teamID`)) ENGINE = InnoDB;";
//        String queryIndex = "CREATE INDEX fk_index ON teams (competitionID)";
//        String queryFK = "ALTER TABLE teams ADD CONSTRAINT fk_competitionID FOREIGN KEY (competitionID) REFERENCES competition (competitionID) ON DELETE CASCADE ON UPDATE CASCADE;";
//
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createTeamsTable " + " : " + ex);
//        }
//    }
//
//    private void createShapeTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`shape` (`teamID` INT NOT NULL , `teamForm` VARCHAR(25) NOT NULL, `teamFormHome` VARCHAR(25) NOT NULL, `teamFormAway` VARCHAR(25) NOT NULL, `averageWinRate` INT NOT NULL, `averageDrawRate` INT NOT NULL, `averageWinRateHome` INT NOT NULL, `averageWinRateAway` INT NOT NULL, `averageDrawRateHome` INT NOT NULL, `averageDrawRateAway` INT NOT NULL, PRIMARY KEY (`teamID`)) ENGINE = InnoDB;";
//        String queryIndex = "CREATE INDEX fk_index ON shape (teamID)";
//        String queryFK = "ALTER TABLE shape ADD CONSTRAINT fk_teamID_SHAPE FOREIGN KEY (teamID) REFERENCES teams (teamID) ON DELETE CASCADE ON UPDATE CASCADE;";
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createShapeTable " + " : " + ex);
//        }
//    }
//
//    private void createGoals_StatsTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`goals` "
//                + "(`teamID` INT NOT NULL, "
//                + "`averageGoalsScored` FLOAT NOT NULL, `averageGoalsConceded` FLOAT NOT NULL, "
//                + "`averageGoalsScoredHome` FLOAT NOT NULL, `averageGoalsConcededHome` FLOAT NOT NULL, "
//                + "`averageGoalsScoredAway` FLOAT NOT NULL, `averageGoalsConcededAway` FLOAT NOT NULL, "
//                + "`averageGoalsScoredFirstHalf` FLOAT NOT NULL, `averageGoalsScoredSecondHalf` FLOAT NOT NULL,"
//                + " PRIMARY KEY (`teamID`)) ENGINE = InnoDB;";
//
//        String queryIndex = "CREATE INDEX fk_index ON goals (teamID)";
//        String queryFK = "ALTER TABLE goals ADD CONSTRAINT fk_teamID_GOALS FOREIGN KEY (teamID) REFERENCES teams (teamID) ON DELETE CASCADE ON UPDATE CASCADE;";
//
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createGoals_StatsTable " + " : " + ex);
//        }
//
//    }
//
//    private void createCardsTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`cards` "
//                + "(`teamID` INT NOT NULL,"
//                + " `averageYellowCards` FLOAT NOT NULL, `averageRedCards` FLOAT NOT NULL,"
//                + " `averageYellowCardsHome` FLOAT NOT NULL, `averageYellowCardsAway` FLOAT NOT NULL,"
//                + " `averageRedCardsHome` FLOAT NOT NULL, `averageRedCardsAway` FLOAT NOT NULL,"
//                + " PRIMARY KEY (`teamID`)) ENGINE = InnoDB;";
//
//        String queryIndex = "CREATE INDEX fk_index ON cards (teamID)";
//        String queryFK = "ALTER TABLE cards ADD CONSTRAINT fk_teamID_CARDS FOREIGN KEY (teamID) REFERENCES teams (teamID) ON DELETE CASCADE ON UPDATE CASCADE;";
//
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createCardsTable " + " : " + ex);
//        }
//    }
//
//    private void createGamesTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`games` (`gameID` INT NOT NULL AUTO_INCREMENT , `gameName` VARCHAR(25) NOT NULL, `nrSpectators` INT NOT NULL, `gameStatus` INT NOT NULL, `homeTeamGoals` INT NOT NULL, `awayTeamGoals` INT NOT NULL, `competitionID` INT NOT NULL, PRIMARY KEY (`gameID`)) ENGINE = InnoDB;";
//        String queryIndex = "CREATE INDEX fk_index ON games (competitionID)";
//        String queryFK = "ALTER TABLE games ADD CONSTRAINT fk_teamID_GAMES FOREIGN KEY (competitionID) REFERENCES competition (competitionID) ON DELETE CASCADE ON UPDATE CASCADE;";
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createGamesTable " + " : " + ex);
//        }
//    }
//
//    private void createOddsTable() {
//
//        String queryTable = "CREATE TABLE IF NOT EXISTS `football`.`odds` (`betID` INT NOT NULL AUTO_INCREMENT, `gameID` INT NOT NULL, `betName` VARCHAR(25) NOT NULL, `odd` FLOAT NOT NULL, PRIMARY KEY (`betID`)) ENGINE = InnoDB;";
//        String queryIndex = "CREATE INDEX fk_index ON odds (gameID)";
//        String queryFK = "ALTER TABLE odds ADD CONSTRAINT fk_teamID_ODDS FOREIGN KEY (gameID) REFERENCES games (gameID) ON DELETE CASCADE ON UPDATE CASCADE;";
//        try {
//            st.executeUpdate(queryTable);
//            st.executeUpdate(queryIndex);
//            st.executeUpdate(queryFK);
//
//        } catch (SQLException ex) {
//            String className = this.getClass().getSimpleName();
//            System.out.println(className + " createOddsTable " + " : " + ex);
//        }
//    }
//    
//    public void getIDTeam(String teamName){
//        
//        StringBuilder selectQuerrySB=new StringBuilder();
//        selectQuerrySB.append("SELECT ");
//    } 
//}
