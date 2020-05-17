package bets;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.util.preventers.AbstractLeakPreventer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadAndParseRapidAPIFixtures {

    public static void main(String args[]) throws IOException {
        new ReadAndParseRapidAPIFixtures(0);
        /// new ReadAndParseRapidAPIFixtures();
    }
////////////////////////////////////// argument -> league id
    private String json;
    public GameEntity[] game = new GameEntity[Constants.MAX_NR_GAMES_IN_A_LEAGUE_ON_ALL_ON_GOING_TICKETS]; /// max nr of games played/returned by api in a season + 5
    private int gameIndex;
    public int nrGamesOnTicketsInThisLeague;

    ReadAndParseRapidAPIFixtures(int leagueID) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrlLeagueID(leagueID))
                    .get()
                    .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "605cec7763msh9b3f1deb00864a6p1d11b7jsn5fd062ffc1c0")
                    .build();

            Response response = client.newCall(request).execute();

            this.json = response.body().string();
            //  parseJson();
        } catch (IOException ex) {
            System.out.println(ex + ": in ReadAndParseRapidAPI");
        }
    }

    ReadAndParseRapidAPIFixtures(int teamA_ID, int teamB_ID) {

    }

    public Competition parseJson(Competition competitionObj) {

        this.json = this.json.substring(7, this.json.length() - 1); /// this makes api{} obsolete

        if (this.json != null && !this.json.equals("")) {
            Object jsonObject = null;
            try {
                jsonObject = new JSONParser().parse(this.json);
                JSONObject jsonObj0 = new JSONObject((Map) jsonObject);

                JSONArray array = (JSONArray) jsonObj0.get("fixtures");
//                System.out.println("array.size() --> " + array.size());
                for (int i = 0; i < array.size(); i++) {

                    Object entity0 = array.get(i);
                    JSONObject jsonObj1 = new JSONObject((Map) entity0);

                    long dateAndTime = (long) jsonObj1.get("event_timestamp") * 1000; // bcs for some reason we receive it /1000

                    Object entity1 = jsonObj1.get("homeTeam");
                    JSONObject jsonObj2 = new JSONObject((Map) entity1);
                    String teamA = (String) jsonObj2.get("team_name");
                    int teamA_ID = (int) (long) jsonObj2.get("team_id");

                    entity1 = jsonObj1.get("awayTeam");
                    jsonObj2 = new JSONObject((Map) entity1);
                    String teamB = (String) jsonObj2.get("team_name");
                    int teamB_ID = (int) (long) jsonObj2.get("team_id");

                   // System.out.println("teamA_ID -> " + teamA_ID + "   vs    teamB_ID -->" + teamB_ID + "   this.nrGamesOnTicketsInThisLeague  ->  " + nrGamesOnTicketsInThisLeague);

                    int[] currentMatchOnTicketIndexes
                            = currentMatchOnTicketIndexes(competitionObj, teamA_ID, teamB_ID, dateAndTime);

                    if (currentMatchOnTicketIndexes == null) {
                        /// we verify wether this game is on our list or not. 
                        /// bcs there may be hundreds of games and we must verify just a specific one
                        /// continue lets us jump to the start of the loop without doing further parsing
                        continue;
                    }
                    /// else
                    // competitionObj.gameObj[currentMatchOnAnyTicketIndex].indexInAPIfindings = i;  
                    // competitionObj.gameObj[currentMatchOnTicketIndexes].indexInAPIfindings = this.gameIndex;

                    for (int k = 0; k < currentMatchOnTicketIndexes.length; k++) {
                        competitionObj.gameObj[currentMatchOnTicketIndexes[k]].indexInAPIfindings = this.gameIndex;
                        //  System.out.println(currentMatchOnTicketIndexes[k]+"   "+this.gameIndex);
                    }

                    entity1 = jsonObj1.get("score");
                    jsonObj2 = new JSONObject((Map) entity1);
                    String scoreFH = (String) jsonObj2.get("halftime");
                    String scoreSH = (String) jsonObj2.get("fulltime");

                    if (scoreFH != null && scoreSH != null) {
                        int scoreTeamA_FH = (int) scoreFH.charAt(0) - 48;
                        int scoreTeamB_FH = (int) scoreFH.charAt(2) - 48;

                        int scoreTeamA_SH = (int) scoreSH.charAt(0) - 48 - scoreTeamA_FH;
                        int scoreTeamB_SH = (int) scoreSH.charAt(2) - 48 - scoreTeamB_FH;

                        this.game[this.gameIndex++] = new GameEntity(dateAndTime, teamA, teamB, scoreTeamA_FH, scoreTeamB_FH,
                                scoreTeamA_SH, scoreTeamB_SH);

                        if (this.nrGamesOnTicketsInThisLeague == 0) {

                            return competitionObj;
                            /// break; 
                            /// daca am verificat toate meciurile din liga x de pe toate biletele, cnt ajunge la 0 si iesim de
                            /// aici
                            /// optimizam, nu cautam inca 150 de meciuri, daca noi ne-am terminat deja treaba
                        }
                    } else {
                        ///////////// repetam peste 20 min ----> meciul nu e terminat/a fost intrerupt/amanat
                        //////////// repetarea e facuta de timerul de 10 min refresh din menu
                    }
                }

            } catch (ParseException ex) {
                Logger.getLogger(ReadAndParseRapidAPIFixtures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private String getUrlLeagueID(int leagueID) {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append("https://api-football-v1.p.rapidapi.com/v2/fixtures/league/");
        urlSB.append(leagueID);

        return urlSB.toString();
    }

    private int[] currentMatchOnTicketIndexes(Competition competitionObj, int teamA_ID, int teamB_ID,
            long dateAndTime) {

        int[] indexArray = new int[competitionObj.nrGames];
        int totalNrIndexes = 0;
        int[] returnArray;

        for (int i = 0; i < competitionObj.nrGames; i++) {

            if (competitionObj.gameObj[i].idA == teamA_ID && competitionObj.gameObj[i].idB == teamB_ID
                    && competitionObj.gameObj[i].startTime == dateAndTime) {
                indexArray[totalNrIndexes++] = i;
                this.nrGamesOnTicketsInThisLeague--;
            }
        }

        if (totalNrIndexes == 0) {
            return null;
        } else {

            returnArray = new int[totalNrIndexes];

            for (int i = 0; i < totalNrIndexes; i++) {
                returnArray[i] = indexArray[i];
            }

            return returnArray;
        }
    }
}
