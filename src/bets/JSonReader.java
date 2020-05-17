package bets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class JSonReader implements Constants {

    String JSon;
    String[] listOfLinks;
    int sizeOfListOfLinks;

    JSonReader(String urlToMatch, int option) {

        if (option == 0) { /// get json for a specific match
            getJSon(urlToMatch, "window[\"initial_state\"]={\"data\":{\"event\":", '{', '}');
        }

        if (option == 1) { /// get json for list of games
            getJSon(urlToMatch, "<script type=\"application/ld+json\">            ", '[', ']');
            
            if (this.JSon != null && !this.JSon.equals("")) {
                ParseJSON jsonParser = new ParseJSON();
                this.listOfLinks = jsonParser.processLinks(this.JSon);
                this.sizeOfListOfLinks = this.listOfLinks.length;
            } else {

            }
        }
    }

    public static void main(String[] args) {
        new JSonReader("https://ro.betano.com/sport/fotbal/belarus/premier-league/17221/", 1);
    }

    public void getJSon(String urlToMatch, String stringToLookFor, char bracket1, char bracket2) {
        try {
            URL obj = new URL(urlToMatch);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(20000);
            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", "Chrome");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String JSon = getJSonFromString(response.toString(), stringToLookFor, bracket1, bracket2);

            this.JSon = JSon;

        } catch (Exception ex) {
            // System.out.println(ex + " in reading json");

            if (ex.toString().equals("java.net.SocketTimeoutException: connect timed out")) {
                
                /***************
                 * loadPreviousSession()
                 * erase games that finalised
                 **************/
                System.out.println(ex);
            }

            this.JSon = null;
        }
    }

    private String getJSonFromString(String response, String stringToLookFor, char bracket1, char bracket2) {

        int responseLength = response.length();
        int stringToLookForLength = stringToLookFor.length();
        boolean found = false;
        int stackOfBrackets = 0;
        int startIndex = 0, stopIndex = 0;

        for (int i = 0; i < responseLength; i++) {

            if (!found && response.substring(i, i + stringToLookForLength).equals(stringToLookFor)) {

                startIndex = i + stringToLookForLength;
                i = i + stringToLookForLength;
                found = true;
            }

            if (found) {

                if (response.charAt(i) == bracket1) {
                    stackOfBrackets++;
                    continue;
                }

                if (response.charAt(i) == bracket2) {
                    stackOfBrackets--;
                    continue;
                }

                if (stackOfBrackets == 0) {
                    stopIndex = i;
                    return response.substring(startIndex, stopIndex);
                }
            }
        }
        return null;
    }
}
