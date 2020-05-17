package bets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JsonObjectArray { /// elimina asta si foloseste mai putina memorie, nu ai nevoie de toti

    JSONObject[] jsonObjArray = new JSONObject[205];
    int size;

    JsonObjectArray(Object jsonFile) {
        this.jsonObjArray[0] = new JSONObject((Map) jsonFile);
        this.size = 1;
    }

    JsonObjectArray() {

    }

    public void createNewObj(Object object) {
        this.jsonObjArray[this.size] = new JSONObject((Map) object);
        this.size++;
    }
}

public class ParseJSON implements Constants {

    JsonObjectArray jsonObj = new JsonObjectArray();
    League matchesLinks = new League();

    public static void main(String[] args) {
        //new ParseJSON("in.json", null);
    }

    ParseJSON(String fileName, String stringJSon) {

        processGame(stringJSon);
    }

    ParseJSON(String stringJSon) {
        processGame(stringJSon);
    }
    
    ParseJSON(){
        
    }

    /**
     * *********************
     * option 0 for a specific match option 1 for a list of games
     * *********************
     */
    public void processGame(String stringJSon) {
        if (stringJSon != null && !stringJSon.equals("")) {
            try {
                Object jsonObject = null;

                jsonObject = new JSONParser().parse(stringJSon);

                JSONObject jsonObj0 = new JSONObject((Map) jsonObject);            // jsonObj.createNewObj(jsonObject);

                boolean liveNow = (boolean) jsonObj0.get("liveNow"); // (boolean) jsonObj.jsonObjArray[jsonObj.size - 1].get("liveNow");

                if (!liveNow) { /// won't take into consideration live games; for now
                    String gameTag = (String) jsonObj0.get("name");//(String) jsonObj.jsonObjArray[jsonObj.size - 1].get("name");
                    String url = getFullUrl((String) jsonObj0.get("url")); // getFullUrl((String) jsonObj.jsonObjArray[jsonObj.size - 1].get("url"));
                    long dateAndTimeInMS = (long) jsonObj0.get("startTime");// (long) jsonObj.jsonObjArray[jsonObj.size - 1].get("startTime");
                    String dateAndTime = getDate(dateAndTimeInMS);
                    String teamA = "";
                    String teamB = "";

                    JSONArray array = (JSONArray) jsonObj0.get("participants");

                    for (int i = 0; i < 2; i++) {

                        Object entity = array.get(i);
                        JSONObject jsonObj1 = new JSONObject((Map) entity);
                        //jsonObj.createNewObj(entity);

                        if (i == 0) {
                            teamA = (String) jsonObj1.get("name"); //(String) jsonObj.jsonObjArray[jsonObj.size - 1].get("name");
                        }

                        if (i == 1) {
                            teamB = (String) jsonObj1.get("name"); //(String) jsonObj.jsonObjArray[jsonObj.size - 1].get("name");
                        }
                    }

                    // matchesLinks.createNewEntity(gameTag, Long.toString(dateAndTime), teamA, teamB);
                    JSONArray array2 = (JSONArray) jsonObj0.get("markets");//(JSONArray) jsonObj.jsonObjArray[jsonObj.size - 3].get("markets");
                    BetClass bet = new BetClass();

                    for (int i = 0; i < array2.size(); i++) {

                        Object entity = array2.get(i);
                        JSONObject jsonObj1 = new JSONObject((Map) entity);  //jsonObj.createNewObj(entity);

                        String betName = ((String) jsonObj1.get("name")).replace('ț', 't').replace('ț', 'T').replace('?', 's').replace('�', 'i');//((String) jsonObj.jsonObjArray[jsonObj.size - 1].get("name")).replace('ț', 't').replace('ț', 'T').replace('?', 's').replace('�', 'i');

                        if (isDesiredBet(betName, teamA, teamB)) {

                            bet.createNewBetEntity(betName);

                            JSONArray array3
                                    = (JSONArray) jsonObj1.get("selections");//jsonObj.jsonObjArray[jsonObj.size - 1].get("selections");

                            String oddName = "";
                            float oddValue = 1;

                            for (int j = 0; j < array3.size(); j++) {

                                Object entity2 = array3.get(j);
                                JSONObject jsonObj2 = new JSONObject((Map) entity2);//jsonObj.createNewObj(entity2);
                                oddName = (String) jsonObj2.get("name");// (String) jsonObj.jsonObjArray[jsonObj.size - 1].get("name");
                                try {
                                    double oddValueDouble = (double) jsonObj2.get("price");
                                    oddValue = (float) oddValueDouble;
                                } catch (Exception ex) {
                                    long oddValueLong = (long) jsonObj2.get("price");
                                    oddValue = (float) oddValueLong;
                                }
                                bet.betTypesObj[bet.bettingTypesLength - 1].createNewBetOddsEntity(oddName, oddValue);
                            }
                            continue;
                        }
                    }
                    matchesLinks.createNewEntity(gameTag, url, dateAndTime, teamA, teamB, bet, dateAndTimeInMS);
                }
            } catch (ParseException ex) {
                System.out.println(ex + " : processGame");
            }

        } else {

        }
    }

    public String[] processLinks(String stringJSon) {
        
        if (stringJSon != null && !stringJSon.equals("")) {
            try {
                Object jsonObject = null;

                StringBuilder jsonSB = new StringBuilder();
                jsonSB.append("{\"participants\":");
                jsonSB.append(stringJSon);
                jsonSB.append('}');

                jsonObject = new JSONParser().parse(jsonSB.toString());

                JSONObject jsonObj0 = new JSONObject((Map) jsonObject);//jsonObj.createNewObj(jsonObject);
                JSONArray array = (JSONArray) jsonObj0.get("participants"); // (JSONArray) jsonObj.jsonObjArray[jsonObj.size - 1].get("participants");
                String[] gamesURL = new String[array.size()];

                for (int i = 0; i < array.size(); i++) {

                    Object entity = array.get(i);
                    JSONObject jsonObj1 = new JSONObject((Map) entity);// jsonObj.createNewObj(entity);
                    gamesURL[i] = ((String) jsonObj1.get("url")); //((String) jsonObj.jsonObjArray[jsonObj.size - 1].get("url"));
                }

                return gamesURL;

            } catch (ParseException ex) {
                System.out.println(ex + " : processGame");
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean isDesiredBet(String betName, String teamA, String teamB) {

        StringBuilder goalsTeamASB = new StringBuilder();
        goalsTeamASB.append(teamA.replace('ț', 't').replace('ț', 'T').replace('?', 's').replace('�', 'i'));
        goalsTeamASB.append(" Total goluri Peste/Sub");
        String goalsTeamA = goalsTeamASB.toString();

        StringBuilder goalsTeamBSB = new StringBuilder();
        goalsTeamBSB.append(teamB.replace('ț', 't').replace('ț', 'T').replace('?', 's').replace('�', 'i'));
        goalsTeamBSB.append(" Total goluri Peste/Sub");
        String goalsTeamB = goalsTeamBSB.toString();

        return (betName.equals("Rezultat final")
                || betName.equals("Total goluri Peste/Sub")
                || betName.equals("Total goluri Peste/Sub (suplimentar)")
                || betName.equals("Ambele echipe inscriu (Da/Nu)")
                //  || betName.equals("Urmitorul gol (Gol 1)")
                || betName.equals("Sansi dubli")
                || betName.equals("Pauzi/Final")
                || betName.equals("Total goluri (Exact)")
                || betName.equals("Total goluri")
                || betName.equals("Scor corect")
                || betName.equals("Rezultat meci & Peste/Sub (1.5)")
                || betName.equals("Rezultat meci & Peste/Sub (2.5)")
                || betName.equals("Rezultat meci & Peste/Sub (3.5)")
                || betName.equals("Rezultat meci & Peste/Sub (4.5)")
                || betName.equals("Rezultat meci & Peste/Sub (5.5)")
                || betName.equals("Rezultat meci & Peste/Sub (6.5)")
                || betName.equals("Rezultat meci & Peste/Sub (7.5)")
                || betName.equals("Rezultat meci & Ambele echipe să inscrie")
                || betName.equals("Ambele echipe si inscrie Peste/Sub (2.5)")
                || betName.equals("Ambele echipe si inscrie Peste/Sub (3.5)")
                || betName.equals("Ambele echipe si inscrie Peste/Sub (4.5)")
                || betName.equals("Ambele echipe si inscrie Peste/Sub (5.5)")
                || betName.equals("Goluri prima reprizi Peste/Sub")
                || betName.equals("Rezultat Prima reprizi")
                || betName.equals(goalsTeamA)
                || betName.equals(goalsTeamB)
                || isDesiredBetWithDiacritics(betName, teamA, teamB));
    }

    private boolean isDesiredBetWithDiacritics(String betName, String teamA, String teamB) {

        StringBuilder goalsTeamASB = new StringBuilder();
        goalsTeamASB.append(teamA);
        goalsTeamASB.append(" Total goluri Peste/Sub");
        String goalsTeamA = goalsTeamASB.toString();

        StringBuilder goalsTeamBSB = new StringBuilder();
        goalsTeamBSB.append(teamB);
        goalsTeamBSB.append(" Total goluri Peste/Sub");
        String goalsTeamB = goalsTeamBSB.toString();

        return (betName.equals("Rezultat final")
                || betName.equals("Total goluri Peste/Sub")
                || betName.equals("Total goluri Peste/Sub (suplimentar)")
                || betName.equals("Ambele echipe înscriu (Da/Nu)")
                //    || betName.equals("Următorul gol (Gol 1)")
                || betName.equals("Șansă dublă")
                || betName.equals("Pauză/Final")
                || betName.equals("Total goluri (Exact)")
                || betName.equals("Total goluri")
                || betName.equals("Scor corect")
                || betName.equals("Rezultat meci & Peste/Sub (1.5)")
                || betName.equals("Rezultat meci & Peste/Sub (2.5)")
                || betName.equals("Rezultat meci & Peste/Sub (3.5)")
                || betName.equals("Rezultat meci & Peste/Sub (4.5)")
                || betName.equals("Rezultat meci & Peste/Sub (5.5)")
                || betName.equals("Rezultat meci & Peste/Sub (6.5)")
                || betName.equals("Rezultat meci & Peste/Sub (7.5)")
                || betName.equals("Rezultat meci & Ambele echipe să înscrie")
                || betName.equals("Ambele echipe să înscrie Peste/Sub (2.5)")
                || betName.equals("Ambele echipe să înscrie Peste/Sub (3.5)")
                || betName.equals("Ambele echipe să înscrie Peste/Sub (4.5)")
                || betName.equals("Ambele echipe să înscrie Peste/Sub (5.5)")
                || betName.equals("Goluri prima repriză Peste/Sub")
                || betName.equals("Rezultat Prima repriză")
                || betName.equals(goalsTeamA)
                || betName.equals(goalsTeamB));
    }

    private String getDate(long matchDateInMs) {

        Date currentDate = new Date(matchDateInMs);
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String matchDateString = df.format(currentDate);
        return matchDateString;
    }

    private String getFullUrl(String personalizedUrl) {
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("https://ro.betano.com");
        urlSB.append(personalizedUrl);
        return urlSB.toString();
    }
}
