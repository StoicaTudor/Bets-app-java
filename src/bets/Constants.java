package bets;

import java.awt.Dimension;
import java.awt.Toolkit;

class Entity {

    String httpLink;
    String leagueName;
    String rapidApiName;
    String country;
    int rapidAPI_LeagueID;

    public Entity() {
        this.httpLink = null;
        this.leagueName = null;
        this.rapidApiName = null;
        this.country = null;
        this.rapidAPI_LeagueID = 0;
    }
}

class Leagues implements Constants {

    Entity[] league = new Entity[MAX_NR_LEAGUES];
    int objectLength;

    public Leagues() {

        this.objectLength = 10;

        for (int i = 0; i < this.objectLength; i++) {
            this.league[i] = new Entity();
        }

        this.league[0].leagueName = "SUPER LEAGUE TURCIA";
        this.league[0].httpLink = "https://ro.betano.com/league/Soccer-FOOT/S-per-Lig-Turkey-17093";
        this.league[0].rapidApiName = "Super Lig";
        this.league[0].country = "Turkey";
        this.league[0].rapidAPI_LeagueID = 782;

        this.league[1].leagueName = "PREMIER LEAGUE";
        this.league[1].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Premier-League-England-1";
        this.league[1].rapidApiName = "Premier League";
        this.league[1].country = "England";
        this.league[1].rapidAPI_LeagueID = 524;

        this.league[2].leagueName = "BUNDESLIGA";
        this.league[2].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Bundesliga-Germany-216";
        this.league[2].rapidApiName = "Bundesliga 1";
        this.league[2].country = "Germany";
        this.league[2].rapidAPI_LeagueID = 754;

        this.league[3].leagueName = "LA LIGA";
        this.league[3].httpLink = "https://ro.betano.com/league/Soccer-FOOT/LaLiga-Spain-5";
        this.league[3].rapidApiName = "Primera Division";
        this.league[3].country = "Spain";
        this.league[3].rapidAPI_LeagueID = 775;

        this.league[4].leagueName = "SERIE A";
        this.league[4].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Serie-A-Italy-1635";
        this.league[4].rapidApiName = "Serie A";
        this.league[4].country = "Italy";
        this.league[4].rapidAPI_LeagueID = 891;

        this.league[5].leagueName = "LIGUE 1";
        this.league[5].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Ligue-1-France-215";
        this.league[5].rapidApiName = "Ligue 1";
        this.league[5].country = "France";
        this.league[5].rapidAPI_LeagueID = 525;

        this.league[6].leagueName = "PREMIER LEAGUE RUSSIA";
        this.league[6].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Premier-League-Russia-17098";
        this.league[6].rapidApiName = "Premier League";
        this.league[6].country = "Russia";
        this.league[6].rapidAPI_LeagueID = 511;

        this.league[7].leagueName = "LIGA 1";
        this.league[7].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Liga-1-Romania-17088";
        this.league[7].rapidApiName = "Liga I";
        this.league[7].country = "Romania";
        this.league[7].rapidAPI_LeagueID = 589;

        this.league[8].leagueName = "PREMIER LEAGUE BELARUS";
        this.league[8].httpLink = "https://ro.betano.com/league/Soccer-FOOT/Premier-League-Belarus-17221";
        this.league[8].rapidApiName = "Vyscha Liga";
        this.league[8].country = "Belarus";
        this.league[8].rapidAPI_LeagueID = 1383;

        this.league[9].leagueName = "SOUTH KOREA - K League";
        this.league[9].httpLink = "https://ro.betano.com/sport/fotbal/coreea-de-sud/k-league/17060/";
        this.league[9].rapidApiName = "K-League Classic";
        this.league[9].country = "South-Korea";
        this.league[9].rapidAPI_LeagueID = 1336;
    }
}

public interface Constants {

    final int MAX_NUMBER_BETTING_TYPES = 105; /// ex Rezultat Final, Total goluri, etc
    final int MAX_NUMBER_BETTING_ODDS = 105; /// ex Ambele Inscriu & 1, 1, Total Goluri peste 4.5, etc
    final int maxNrOfMatchesOnPage = 105;
    final int MAX_NR_LEAGUES = 105;

    final String processMethodErrorFile = "processMethodErrorFile.txt";

    final Leagues leaguesObjBetano = new Leagues();

    final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    final int SCREEN_HEIGHT = SCREEN_SIZE.height;
    final int SCREEN_WIDTH = SCREEN_SIZE.width;
    final Dimension gamesListPrefferedSize = new Dimension(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 4);
    // final Dimension gamesListPrefferedSize=new Dimension(100,100);
    int REFRESH_TIME_IN_ms = 600000; /// 10 min
    int MAX_NR_ODDS_PER_BET = 55;
    int MAX_NR_BETS_PER_TICKET = 105;
    int MAX_NR_ACTIVE_NON_SUBMITTED_TICKETS = 10;
    int MAX_NR_ADDITIONAL_TICKETS_SUBMITTED_ON_APP_SESSION = 105;
    float INITIAL_BETTING_SUM = 100;
    int MAX_NR_GAMES_IN_A_LEAGUE_ON_ALL_ON_GOING_TICKETS = 380;
    /// worst case scenario: premier league 380 games and tickets havent been evaluated since the beginning of the season
    /// and user have placed bets on all games in the season -> this is impossible though, but safety first
    /// and yeah, for memory purposes, I could lower this nr

    int WON_TICKET_STATUS = 1;
    int ON_GOING_TICKET_STATUS = 0;
    int LOST_TICKET_STATUS = -1;
  
    int MAX_NR_ONGOING_TICKETS=105;
    int MAX_NR_LOST_TICKETS=1005;
    int MAX_NR_WON_TICKETS=1005;
    int MAX_NR_TICKETS=1005+1005+105;
}
