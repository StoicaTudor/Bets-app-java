package bets;
/////////// de optimizat: this.totalGoals, this.totalGoalsFirstHalf, this.firstNrInthis.oddName etc

public class ProcessOddStatus {

    private int scoreTeamA;
    private int scoreTeamB;
    private int totalGoals;
    public int isWinningBet;
    private String oddName;

    ProcessOddStatus(String teamA, String teamB, String betName, String oddName, int scoreTeamAFirstHalf,
            int scoreTeamBFirstHalf, int scoreTeamASecondHalf, int scoreTeamBSecondHalf) {
        
        this.scoreTeamA = scoreTeamAFirstHalf + scoreTeamASecondHalf;
        this.scoreTeamB = scoreTeamBFirstHalf + scoreTeamBSecondHalf;
        this.totalGoals = this.scoreTeamA + this.scoreTeamB;
        this.isWinningBet = -1;
        this.oddName = oddName;

        final String totalGoluriTeamA = forge(teamA);
        final String totalGoluriTeamB = forge(teamB);

        switch (betName) {
            case "Rezultat final":
                rezultatFinal();
                break;

            case "Total goluri Peste/Sub":
                totalGoluriPesteSub();
                break;

            case "Total goluri Peste/Sub (suplimentar)":
                totalGoluriPesteSubSuplimentar();
                break;

            case "Ambele echipe înscriu (Da/Nu)":
                ambeleEchipeInscriu();
                break;

            case "Șansă dublă":
                sansaDubla();
                break;

            case "Pauză/Final":
                pauzaFinal(scoreTeamAFirstHalf, scoreTeamBFirstHalf, scoreTeamASecondHalf, scoreTeamBSecondHalf);
                break;

            case "Total goluri (Exact)":
                totalGoluriExact();
                break;

            case "Total goluri":
                totalGoluri();
                break;

            case "Scor corect":
                scorCorect();
                break;

            case "Rezultat meci & Ambele echipe să înscrie":
                rezultatMeciSiAmbeleSaInscrie(teamA, teamB);
                break;

            case "Goluri prima reriză Peste/Sub":
                goluriPrimaReprizaPesteSub(scoreTeamAFirstHalf, scoreTeamBFirstHalf);
                break;

            case "Rezultat Prima repriză":
                rezultatPrimaRepriza(scoreTeamAFirstHalf, scoreTeamBFirstHalf);
                break;

            default:
                break;

        }

        if (strstr(betName, "Rezultat meci & Peste/Sub") > 0) {
            rezultatMeciSiPesteSub((int) betName.charAt(27)); /// index of the nr of goals
        } else if (strstr(betName, "Ambele echipe să înscrie Peste/Sub") > 0) {
            ambeleSaInscrieSiPesteSub((int) betName.charAt(36)); /// index of the nr of goals
        } else if (betName.equals(totalGoluriTeamA)) {
            totalGoluriTeamA();
        } else if (betName.equals(totalGoluriTeamB)) {
            totalGoluriTeamB();
        }
      ///  System.out.println(this.isWinningBet);
    }

    private void rezultatFinal() {

        switch (this.oddName) {
            case "1":
                if (this.scoreTeamA > this.scoreTeamB) {
                    this.isWinningBet = 1;
                }
                break;
            case "X":
                if (this.scoreTeamA == this.scoreTeamB) {
                    this.isWinningBet = 1;
                }
                break;
            case "2":
                if (this.scoreTeamA < this.scoreTeamB) {
                    this.isWinningBet = 1;
                }
                break;
            default:
                break;
        }
    }

    private void totalGoluriPesteSub() {

        //// getting the first nr in the this.oddName array since it is our landmark
        int oddNameLen = this.oddName.length();
        int goals = 0;
        for (int i = 0; i < oddNameLen; i++) {
            if ((int) this.oddName.charAt(i) >= 48 && (int) this.oddName.charAt(i) <= 57) {

                goals = (int) this.oddName.charAt(i)-48;
                i = oddNameLen; /// exiting loop
            }
        }
        ///////

        if (strstr(this.oddName, "Peste") > 0) {
            if (this.totalGoals > goals) {
                this.isWinningBet = 1;
            }
        } else if (strstr(this.oddName, "Sub") > 0) {
            if (this.totalGoals <= goals) {
                this.isWinningBet = 1;
            }
        }
    }

    public int strstr(String X, String Y) {
        // if X is null or if X's length is less than that of Y's
        if (X == null || Y.length() > X.length()) {
            return -1;
        }

        // if Y is null or is empty
        if (Y == null || Y.length() == 0) {
            return -1;/// return -1 -> smthg is not good
        }

        int xLen = X.length();
        int yLen = Y.length();

        for (int i = 0; i <= xLen - yLen; i++) {
            int j;
            for (j = 0; j < yLen; j++) {
                if (Y.charAt(j) != X.charAt(i + j)) {
                    break;
                }
            }

            if (j == yLen) {
                return i + 1; //////// !!!!! I do this bcs I realized that a return 0; could be ok and, bcs I do not want 
                /// to search the entire class for >0 (to transform them in >=0), I just return smthg >=1
            }
        }

        return -1;
    }

    private String forge(String team) {

        StringBuilder totalGoluri = new StringBuilder(team);
        totalGoluri.append(" Total goluri Peste/Sub");

        return totalGoluri.toString();
    }

    private void totalGoluriPesteSubSuplimentar() {
        totalGoluriPesteSub();
    }

    private void ambeleEchipeInscriu() {

        if (strstr(this.oddName, "Da") > 0) {
            if (this.scoreTeamA > 0 && this.scoreTeamB > 0) {
                this.isWinningBet = 1;
            }
        } else if (strstr(this.oddName, "Nu") > 0) {
            if (this.scoreTeamA == 0 || this.scoreTeamB == 0) {
                this.isWinningBet = 1;
            }
        }
    }

    private void sansaDubla() {

        if (strstr(this.oddName, "1X") > 0) {
            if (this.scoreTeamA >= this.scoreTeamB) {
                this.isWinningBet = 1;
            }
        } else if (strstr(this.oddName, "2X") > 0) {
            if (this.scoreTeamA <= this.scoreTeamB) {
                this.isWinningBet = 1;
            }
        } else if (strstr(this.oddName, "12") > 0) {
            if (this.scoreTeamA > this.scoreTeamB || this.scoreTeamA < this.scoreTeamB) {
                this.isWinningBet = 1;
            }
        }
    }

    private void pauzaFinal(int scoreTeamAFirstHalf, int scoreTeamBFirstHalf, int scoreTeamASecondHalf, int scoreTeamBSecondHalf) {

        char firstHalf = this.oddName.charAt(0);
        char secondHalf = this.oddName.charAt(2);
        boolean validFirstHalf = false, validSecondHalf = false;

        switch (firstHalf) {
            case '1':
                if (scoreTeamAFirstHalf > scoreTeamBFirstHalf) {
                    validFirstHalf = true;
                }
                break;
            case 'X':
                if (scoreTeamAFirstHalf == scoreTeamBFirstHalf) {
                    validFirstHalf = true;
                }
                break;
            case '2':
                if (scoreTeamAFirstHalf < scoreTeamBFirstHalf) {
                    validFirstHalf = true;
                }
                break;
            default:
                break;
        }

        switch (secondHalf) {
            case '1':
                if (scoreTeamASecondHalf > scoreTeamBSecondHalf) {
                    validSecondHalf = true;
                }
                break;
            case 'X':
                if (scoreTeamASecondHalf == scoreTeamBSecondHalf) {
                    validSecondHalf = true;
                }
                break;
            case '2':
                if (scoreTeamASecondHalf < scoreTeamBSecondHalf) {
                    validSecondHalf = true;
                }
                break;
            default:
                break;
        }

        if (validFirstHalf == true && validSecondHalf == true) {
            this.isWinningBet = 1;
        }
    }

    private void totalGoluriExact() {
        //// getting the first nr in the this.oddName array since it is our landmark
        /// and if there is number+ format
        int oddNameLen = this.oddName.length();
        int goals = 0;
        boolean isPlus = false;

        for (int i = 0; i < oddNameLen; i++) {
            if ((int) this.oddName.charAt(i) >= 48 && (int) this.oddName.charAt(i) <= 57) {

                goals = (int) this.oddName.charAt(i)-48;
                if (i + 1 < oddNameLen && this.oddName.charAt(i + 1) == '+') {
                    isPlus = true;
                }
                i = oddNameLen; /// exiting loop
            }
        }
        ///////

        if (isPlus == false && goals == this.totalGoals) {
            this.isWinningBet = 1;
            return;
        }

        if (isPlus == true && goals <= this.totalGoals) {
            this.isWinningBet = 1;
            return;
        }
    }

    private void totalGoluri() {

        if (strstr(this.oddName, "+") > 0) {

            int goals = (int) this.oddName.charAt(0)-48;

            if (this.totalGoals >= goals) {

                this.isWinningBet = 1;
            }
            return;
        }

        /// because it looks like this: number-number, ex 2-6, 3-5
        int leftGoalsBorder = (int) this.oddName.charAt(0)-48;
        int rightGoalsBorder = (int) this.oddName.charAt(2)-48;

        if (this.totalGoals >= leftGoalsBorder && this.totalGoals <= rightGoalsBorder) {
            this.isWinningBet = 1;
        }
    }

    private void scorCorect() {

        /// 2-0, 5-2
        int goalsA =(int) this.oddName.charAt(0)-48;
        int goalsB =(int) this.oddName.charAt(2)-48;

        if (goalsA == this.scoreTeamA && goalsB == this.scoreTeamB) {
            this.isWinningBet = 1;
        }
    }

    private void rezultatMeciSiAmbeleSaInscrie(String teamA, String teamB) {

        /// teamA & Da, Egal & Nu
        if (strstr(this.oddName, teamA) > 0 && strstr(this.oddName, "Da") > 0) {

            if (this.scoreTeamA > this.scoreTeamB && this.scoreTeamB > 0) {
                this.isWinningBet = 1;
                return;
            }
        }
        if (strstr(this.oddName, teamA) > 0 && strstr(this.oddName, "Nu") > 0) {
            if (this.scoreTeamA > this.scoreTeamB && this.scoreTeamB == 0) {
                this.isWinningBet = 1;
                return;
            }
        }
        if (strstr(this.oddName, "Egal") > 0 && strstr(this.oddName, "Da") > 0) {
            if (this.scoreTeamA == this.scoreTeamB && this.scoreTeamB > 0) {
                this.isWinningBet = 1;
                return;
            }
        }
        if (strstr(this.oddName, "Egal") > 0 && strstr(this.oddName, "Nu") > 0) {
            if (this.scoreTeamA == this.scoreTeamB && this.scoreTeamB == 0) {
                this.isWinningBet = 1;
                return;
            }
        }
        if (strstr(this.oddName, teamB) > 0 && strstr(this.oddName, "Da") > 0) {
            if (this.scoreTeamA < this.scoreTeamB && this.scoreTeamA > 0) {
                this.isWinningBet = 1;
                return;
            }
        }
        if (strstr(this.oddName, teamB) > 0 && strstr(this.oddName, "Nu") > 0) {
            if (this.scoreTeamA < this.scoreTeamB && this.scoreTeamA == 0) {
                this.isWinningBet = 1;
                return;
            }
        }

    }

    private void goluriPrimaReprizaPesteSub(int scoreTeamAFirstHalf, int scoreTeamBFirstHalf) {
        //// getting the first nr in the this.oddName array since it is our landmark
        int oddNameLen = this.oddName.length();
        int goals = 0;
        for (int i = 0; i < oddNameLen; i++) {
            if ((int) this.oddName.charAt(i) >= 48 && (int) this.oddName.charAt(i) <= 57) {

                goals = (int) this.oddName.charAt(i)-48;
                i = oddNameLen; /// exiting loop
            }
        }
        ///////

        if (strstr(this.oddName, "Peste") > 0) {
            if (scoreTeamAFirstHalf + scoreTeamBFirstHalf > goals) {
                this.isWinningBet = 1;
            }
        } else if (strstr(this.oddName, "Sub") > 0) {
            if (scoreTeamAFirstHalf + scoreTeamBFirstHalf <= goals) {
                this.isWinningBet = 1;
            }
        }
    }

    private void rezultatPrimaRepriza(int scoreTeamAFirstHalf, int scoreTeamBFirstHalf) {
        switch (this.oddName) {
            case "1":
                if (scoreTeamAFirstHalf > scoreTeamBFirstHalf) {
                    this.isWinningBet = 1;
                }
                break;
            case "X":
                if (scoreTeamAFirstHalf == scoreTeamBFirstHalf) {
                    this.isWinningBet = 1;
                }
                break;
            case "2":
                if (scoreTeamAFirstHalf < scoreTeamBFirstHalf) {
                    this.isWinningBet = 1;
                }
                break;
            default:
                break;
        }
    }

    private void rezultatMeciSiPesteSub(int nrGoals) {
        /// ex : 1 & peste 2.5;
        switch (this.oddName.charAt(0)) {
            case '1':
                if (this.scoreTeamA > this.scoreTeamB) {
                    if (strstr(this.oddName, "Peste") > 0 && this.totalGoals > nrGoals) {
                        this.isWinningBet = 1;
                    } else if (strstr(this.oddName, "Sub") > 0 && this.totalGoals <= nrGoals) {
                        this.isWinningBet = 1;
                    }
                }
                break;

            case 'X':
                if (this.scoreTeamA == this.scoreTeamB) {
                    if (strstr(this.oddName, "Peste") > 0 && this.totalGoals > nrGoals) {
                        this.isWinningBet = 1;
                    } else if (strstr(this.oddName, "Sub") > 0 && this.totalGoals <= nrGoals) {
                        this.isWinningBet = 1;
                    }
                }
                break;

            case '2':
                if (this.scoreTeamA < this.scoreTeamB) {
                    if (strstr(this.oddName, "Peste") > 0 && this.totalGoals > nrGoals) {
                        this.isWinningBet = 1;
                    } else if (strstr(this.oddName, "Sub") > 0 && this.totalGoals <= nrGoals) {
                        this.isWinningBet = 1;
                    }
                }
                break;

            default:
                break;
        }
    }

    private void ambeleSaInscrieSiPesteSub(int nrGoals) {
        switch (this.oddName.substring(0, 1)) {
            case "Da":
                if (this.scoreTeamA > 0 && this.scoreTeamB > 0) {
                    if (strstr(this.oddName, "Peste") > 0 && this.totalGoals > nrGoals) {
                        this.isWinningBet = 1;
                    } else if (strstr(this.oddName, "Sub") > 0 && this.totalGoals <= nrGoals) {
                        this.isWinningBet = 1;
                    }
                }
                break;

            case "Nu":
                if (this.scoreTeamA == 0 || this.scoreTeamB == 0) {
                    if (strstr(this.oddName, "Peste") > 0 && this.totalGoals > nrGoals) {
                        this.isWinningBet = 1;
                    } else if (strstr(this.oddName, "Sub") > 0 && this.totalGoals <= nrGoals) {
                        this.isWinningBet = 1;
                    }
                }
                break;

            default:
                break;
        }
    }

    private void totalGoluriTeamA() {

        //// getting the first nr in the this.oddName array since it is our landmark
        int oddNameLen = this.oddName.length();
        int goals = 0;
        for (int i = 0; i < oddNameLen; i++) {
            if ((int) this.oddName.charAt(i) >= 48 && (int) this.oddName.charAt(i) <= 57) {

                goals = (int) this.oddName.charAt(i)-48;
                i = oddNameLen; /// exiting loop
            }
        }
        ///////

        if (strstr(this.oddName, "Peste") > 0 && this.scoreTeamA > goals) {
            this.isWinningBet = 1;
            return;
        } else if (strstr(this.oddName, "Sub") > 0 && this.scoreTeamA <= goals) {
            this.isWinningBet = 1;
            return;
        }
    }

    private void totalGoluriTeamB() {
        //// getting the first nr in the this.oddName array since it is our landmark
        int oddNameLen = this.oddName.length();
        int goals = 0;
        for (int i = 0; i < oddNameLen; i++) {
            if ((int) this.oddName.charAt(i) >= 48 && (int) this.oddName.charAt(i) <= 57) {

                goals = (int) this.oddName.charAt(i)-48;
                i = oddNameLen; /// exiting loop
            }
        }
        ///////

        if (strstr(this.oddName, "Peste") > 0 && this.scoreTeamB > goals) {
            this.isWinningBet = 1;
            return;
        } else if (strstr(this.oddName, "Sub") > 0 && this.scoreTeamB <= goals) {
            this.isWinningBet = 1;
            return;
        }
    }
}
