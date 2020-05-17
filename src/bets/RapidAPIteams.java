package bets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class StringAndInt {

    int teamID;
    String teamName;

    StringAndInt(int teamID, String teamName) {
        this.teamID = teamID;
        this.teamName = teamName;
    }
}

public class RapidAPIteams {

    private boolean searchInDB = false;
    private DatabaseConnectUser dbUserObj;
    private int leagueID;
    private String[] teamsExtractedFromJSON;

    RapidAPIteams(DatabaseConnectUser dbUserObj, int leagueID) {

        this.dbUserObj = dbUserObj;
        this.leagueID = leagueID;

        try {
            dbUserObj.rs = dbUserObj.st.executeQuery(computeFindLeagueWithIDQuerry(leagueID));
            if (dbUserObj.rs.next()) { /// found result (it can only be one)

                /// we search teams in db (we do not need to call api and parse it)
                /// if leagueID exists in competition table => there exist all teams with leagueID in teams table
                searchInDB = true;
            } else { /// we need to rapid api json parse them and insert them into db
                searchInDB = false; /// it is false by default anyway
            }
        } catch (SQLException ex) {
            System.out.println(ex + " : in RapidAPIteams constructor");
        }
    }

    public String[] getRapidAPIteamsNames() {

        if (searchInDB == true) {
            return getTeamsFromDB(this.dbUserObj, this.leagueID);
        } else { /// we must querry a json, place all teams in db and return db items

            getLeagueNameCountrySeasonAndInsertInDB(this.dbUserObj, this.leagueID);
            readParseAndPlaceIntoDBJSON(this.dbUserObj, this.leagueID);

            return teamsExtractedFromJSON;
        }
    }

    private String computeFindLeagueWithIDQuerry(int leagueID) {

        StringBuilder selectQuerrySB = new StringBuilder();
        selectQuerrySB.append("SELECT * FROM competition WHERE rapidAPIcompetitionID=");
        selectQuerrySB.append(leagueID);

        return selectQuerrySB.toString();
    }

    private String[] getTeamsFromDB(DatabaseConnectUser dbUserObj, int leagueID) {

        int nrTeams = 0;
        String[] auxTeamsArray = new String[35]; /// define 35 as max nr of teams in a league (although too much)

        try {
            dbUserObj.rs = dbUserObj.st.executeQuery(computeFindTeamsWithIDQuerry(leagueID));

            while (dbUserObj.rs.next()) {
                auxTeamsArray[nrTeams] = dbUserObj.rs.getString(1);
                nrTeams++;
            }

            String[] teamsArray = new String[nrTeams];

            for (int i = 0; i < nrTeams; i++) { /// now we pass auxArray into the original array with the exact nr of teams
                teamsArray[i] = auxTeamsArray[i];
            }

            return teamsArray;

        } catch (SQLException ex) {
            System.out.println(ex + " : in getTeamsFromDB");
            return null;
        }
    }

    private String computeFindTeamsWithIDQuerry(int leagueID) {
        StringBuilder selectTeamsNamesQuerrySB = new StringBuilder();
        selectTeamsNamesQuerrySB.append("SELECT teamName FROM teams WHERE rapidAPIcompetitionID=");
        selectTeamsNamesQuerrySB.append(leagueID);
        return selectTeamsNamesQuerrySB.toString();
    }

    private void readParseAndPlaceIntoDBJSON(DatabaseConnectUser dbUserObj, int leagueID) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getURLWithLeagueID(leagueID))
                .get()
                .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "605cec7763msh9b3f1deb00864a6p1d11b7jsn5fd062ffc1c0")
                .build();

        try {
            Response response = client.newCall(request).execute();
            parseJSONandPutInDB(response.body().string(), dbUserObj, leagueID);
        } catch (IOException ex) {
            Logger.getLogger(RapidAPIteams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getURLWithLeagueID(int leagueID) {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append("https://api-football-v1.p.rapidapi.com/v2/teams/league/");
        urlSB.append(leagueID);
        return urlSB.toString();
    }

    private void parseJSONandPutInDB(String json, DatabaseConnectUser dbUserObj, int leagueID) {

        json = json.substring(7, json.length() - 1); /// this makes api{} obsolete
//        introduce in competition liga cu id-ul ligii!!!
//        apoi un string la care tot adaug numele echipelor primite din json
        if (json != null && !json.equals("")) {
            Object jsonObject = null;

            try {
                jsonObject = new JSONParser().parse(json);
                JSONObject jsonObj0 = new JSONObject((Map) jsonObject);
                JSONArray array = (JSONArray) jsonObj0.get("teams");

                StringAndInt[] rapidAPIteamsArray = new StringAndInt[array.size()];
                teamsExtractedFromJSON = new String[array.size()];

                for (int i = 0; i < array.size(); i++) {

                    Object entity0 = array.get(i);
                    JSONObject jsonObj1 = new JSONObject((Map) entity0);

                    int teamID = (int) (long) jsonObj1.get("team_id");
                    String name = (String) jsonObj1.get("name");

                    teamsExtractedFromJSON[i] = name;
                    rapidAPIteamsArray[i] = new StringAndInt(teamID, name);
                }

                putInDBteamsArrayWithLeagueID(dbUserObj, rapidAPIteamsArray, leagueID);

            } catch (ParseException ex) {
                System.out.println(ex + " : in parseJSONandPutInDB");
            }
        }
    }

    private void putInDBteamsArrayWithLeagueID(DatabaseConnectUser dbUserObj, StringAndInt[] rapidAPIteamsArray, int leagueID) {

        String insertRapidAPIteamsQuerry = computeInsertRapidAPIteamsQuerry(rapidAPIteamsArray, leagueID);
        System.out.println(insertRapidAPIteamsQuerry);
        try {
            dbUserObj.st.executeUpdate(insertRapidAPIteamsQuerry);
        } catch (SQLException ex) {
            System.out.println(ex + " : in putInDBteamsArrayWithLeagueID");
        }
    }

    private String computeInsertRapidAPIteamsQuerry(StringAndInt[] rapidAPIteamsArray, int leagueID) {

        StringBuilder insertRapidAPIteamsQuerrySB = new StringBuilder();
        insertRapidAPIteamsQuerrySB.append("INSERT INTO `teams` "
                + "(`id`,`teamName`,`rapidAPIcompetitionID`,`rapidAPI_id`) VALUES ");

        for (int i = 0; i < rapidAPIteamsArray.length; i++) {

            insertRapidAPIteamsQuerrySB.append("(NULL,'");

            insertRapidAPIteamsQuerrySB.append(rapidAPIteamsArray[i].teamName);
            insertRapidAPIteamsQuerrySB.append("','");

            insertRapidAPIteamsQuerrySB.append(leagueID);
            insertRapidAPIteamsQuerrySB.append("','");

            insertRapidAPIteamsQuerrySB.append(rapidAPIteamsArray[i].teamID);

            if (i < rapidAPIteamsArray.length - 1) {
                insertRapidAPIteamsQuerrySB.append("'), ");
            } else {
                insertRapidAPIteamsQuerrySB.append("')");
            }
        }

        return insertRapidAPIteamsQuerrySB.toString();
    }

    private void getLeagueNameCountrySeasonAndInsertInDB(DatabaseConnectUser dbUserObj, int leagueID) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(computeGetCompetitionWithIDurl(leagueID))
                .get()
                .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "605cec7763msh9b3f1deb00864a6p1d11b7jsn5fd062ffc1c0")
                .build();

        try {
            Response response = client.newCall(request).execute();
            parseCompetitionLeagueAndInsertIntoDB(response.body().string(), dbUserObj, leagueID);
        } catch (IOException ex) {
            Logger.getLogger(RapidAPIteams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String computeGetCompetitionWithIDurl(int leagueID) {
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("https://api-football-v1.p.rapidapi.com/v2/leagues/league/");
        urlSB.append(leagueID);
        return urlSB.toString();
    }

    private void parseCompetitionLeagueAndInsertIntoDB(String json, DatabaseConnectUser dbUserObj, int leagueID) {

        json = json.substring(7, json.length() - 1); /// this makes api{} obsolete

        if (json != null && !json.equals("")) {
            Object jsonObject = null;

            try {
                jsonObject = new JSONParser().parse(json);
                JSONObject jsonObj0 = new JSONObject((Map) jsonObject);
                JSONArray array = (JSONArray) jsonObj0.get("leagues");

                StringAndInt[] rapidAPIteamsArray = new StringAndInt[array.size()];

                for (int i = 0; i < array.size(); i++) {

                    Object entity0 = array.get(i);
                    JSONObject jsonObj1 = new JSONObject((Map) entity0);

                    String country = (String) jsonObj1.get("country");
                    String leagueName = (String) jsonObj1.get("name");
                    int season = (int) (long) jsonObj1.get("season");
                    insertLeagueInDB(dbUserObj, leagueID, leagueName, country, season);
                }

            } catch (ParseException ex) {
                System.out.println(ex + " : in parseCompetitionLeagueAndInsertIntoDB");
            }
        }
    }

    private void insertLeagueInDB(DatabaseConnectUser dbUserObj, int leagueID, String leagueName, String country, int season) {

        StringBuilder insertLeagueQuerySB = new StringBuilder();
        insertLeagueQuerySB.append("INSERT INTO `competition` "
                + "(`id`,`competitionName`,`season`,`rapidAPIcompetitionID`,`country`) VALUES (NULL,'");
        insertLeagueQuerySB.append(leagueName);
        insertLeagueQuerySB.append("','");
        insertLeagueQuerySB.append(season);
        insertLeagueQuerySB.append("','");
        insertLeagueQuerySB.append(leagueID);
        insertLeagueQuerySB.append("','");
        insertLeagueQuerySB.append(country);
        insertLeagueQuerySB.append("')");

        try {
            dbUserObj.st.executeUpdate(insertLeagueQuerySB.toString());
        } catch (SQLException ex) {
            System.out.println(ex + " : in insertLeagueInDB");
        }
    }
}
