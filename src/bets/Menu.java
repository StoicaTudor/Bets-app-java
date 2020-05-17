package bets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.currentThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Menu extends javax.swing.JFrame implements Constants {

    private int ticketIndexAtMatching;

    ActionListener refreshGamesAndBets = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            refresh();
//            verifyGames(user); !!! daca toate meciurile de pe bilet s-au terminat, trebuie evaluat biletul
//            insertBackIntoDB(user);
            FindGamesResults verify = new FindGamesResults(user, dbUserObj);
            user = verify.user; /// update the user object (with the finished bets and tickets statuses) !!! this is essential
        }
    };

    private FindMatching findMatchingAObj = null;
    private int teamArapidAPI_id = -1;

    private FindMatching findMatchingBObj = null;
    private int teamBrapidAPI_id = -1;

    ActionListener newTicket = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {

            if (findMatchingAObj != null && findMatchingAObj.frameIsOpened == false) {
                teamArapidAPI_id = findMatchingAObj.teamRapidAPIid;
            }

            if (findMatchingBObj != null && findMatchingBObj.frameIsOpened == false) {
                teamBrapidAPI_id = findMatchingBObj.teamRapidAPIid;
            }

            if (teamArapidAPI_id != -1 && teamBrapidAPI_id != -1) {

                ticket[ticketIndexAtMatching].createNewBetEntity(
                        scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].gameTag,
                        scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].dateAndTime,
                        currentBet,
                        currentOdd, scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].startTime,
                        leaguesObjBetano.league[indexOfSelectedLeague].rapidAPI_LeagueID, teamArapidAPI_id, teamBrapidAPI_id,
                        scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamA,
                        scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamB);

                ticket[ticketIndexAtMatching].computeBettingTicketOdd();
                displayBetsListOnTicket(ticketIndexAtMatching);
                modifyTotalOdd(ticket[ticketIndexAtMatching].bettingTicketOdd);
                checkIfBothMatchingsWereMadeTimer.stop();

                teamArapidAPI_id = teamBrapidAPI_id = -1;
                findMatchingAObj = findMatchingBObj = null;
            }
        }
    };

    Timer checkIfBothMatchingsWereMadeTimer = new Timer(1000, newTicket);
    Timer refreshTimer = new Timer(REFRESH_TIME_IN_ms, refreshGamesAndBets);
    boolean firstTime = true;

    private void refresh() {
        JSonReader league;

        for (int i = 0; i < leaguesObjBetano.objectLength; i++) {

            //       AccessWebPageBetano leagueObj = new AccessWebPageBetano();
            try {
                ParseJSON jsonParser = new ParseJSON();
                league = new JSonReader(leaguesObjBetano.league[i].httpLink, 1);

                if (league.sizeOfListOfLinks > 0) {
                    JSonReader match;
                    for (int j = 0; j < league.sizeOfListOfLinks; j++) {
                        match = new JSonReader(league.listOfLinks[j], 0);
                        jsonParser.processGame(match.JSon);
                    }
                    scheduledMatchesObj[i] = jsonParser.matchesLinks;
                    scheduledMatchesObj[i].leagueName = leaguesObjBetano.league[i].leagueName;

                } else {
                    scheduledMatchesObj[i] = new League();
                    scheduledMatchesObj[i].size = 0;
                }

            } catch (Exception ex) {
                System.out.println(ex + " : in refresh method");
            }
        }
    }

    public Menu(UserData userObj, DatabaseConnectUser dbObj) {
        magic();
        dbUserObj = dbObj;
        user = userObj;
        refreshTimer.start();
        refresh();

        FindGamesResults verify = new FindGamesResults(user, dbUserObj);
        user = verify.user; /// update the user object (with the finished bets and tickets statuses) !!! this is essential
        
        initComponents();
        modifyUserBalance(userObj.userBalance);

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leaguesComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        gamesListJList = new javax.swing.JList<>();
        selectMatchButton = new javax.swing.JButton();
        betsComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        oddsList = new javax.swing.JList<>();
        bettingTicketsComboBox = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        addToBettingTicketButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        ticketsBetsList = new javax.swing.JList<>();
        deleteTicketBetButton = new javax.swing.JButton();
        createNewTicketButton = new javax.swing.JButton();
        submitTicketButton = new javax.swing.JButton();
        deleteTicketButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        totalOddTextPane = new javax.swing.JTextPane();
        userBalanceTextField = new javax.swing.JTextField();
        jMenuBar2 = new javax.swing.JMenuBar();
        utilities = new javax.swing.JMenu();
        ticketsHistory = new javax.swing.JMenuItem();
        finances = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");
        setExtendedState(MAXIMIZED_BOTH);
        setName("Menu Frame"); // NOI18N

        leaguesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());
        leaguesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaguesComboBoxActionPerformed(evt);
            }
        });

        gamesListJList.setBackground(new java.awt.Color(0, 204, 204));
        gamesListJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        gamesListJList.setMaximumSize(gamesListPrefferedSize);
        gamesListJList.setMinimumSize(gamesListPrefferedSize);
        gamesListJList.setPreferredSize(gamesListPrefferedSize);
        gamesListJList.setSelectionBackground(new java.awt.Color(0, 51, 204));
        jScrollPane1.setViewportView(gamesListJList);

        selectMatchButton.setText("Select match");
        selectMatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMatchButtonActionPerformed(evt);
            }
        });

        betsComboBox.setBackground(new java.awt.Color(0, 153, 153));
        betsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betsComboBoxActionPerformed(evt);
            }
        });

        oddsList.setBackground(new java.awt.Color(0, 204, 204));
        oddsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "no odds available" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        oddsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(oddsList);

        bettingTicketsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bettingTicketsComboBoxActionPerformed(evt);
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(0, 204, 204));
        jTextPane1.setText("Select one of your unsubmitted betting tickets");
        jTextPane1.setOpaque(false);
        jScrollPane3.setViewportView(jTextPane1);

        addToBettingTicketButton.setBackground(new java.awt.Color(0, 204, 204));
        addToBettingTicketButton.setText("Add");
        addToBettingTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToBettingTicketButtonActionPerformed(evt);
            }
        });

        ticketsBetsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(ticketsBetsList);

        deleteTicketBetButton.setBackground(new java.awt.Color(0, 204, 204));
        deleteTicketBetButton.setText("Delete bet");
        deleteTicketBetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTicketBetButtonActionPerformed(evt);
            }
        });

        createNewTicketButton.setBackground(new java.awt.Color(0, 204, 204));
        createNewTicketButton.setText("Create new ticket");
        createNewTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewTicketButtonActionPerformed(evt);
            }
        });

        submitTicketButton.setBackground(new java.awt.Color(0, 204, 204));
        submitTicketButton.setText("Submit ticket");
        submitTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitTicketButtonActionPerformed(evt);
            }
        });

        deleteTicketButton.setBackground(new java.awt.Color(0, 204, 204));
        deleteTicketButton.setText("Delete ticket");
        deleteTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTicketButtonActionPerformed(evt);
            }
        });

        totalOddTextPane.setEditable(false);
        totalOddTextPane.setBackground(new java.awt.Color(0, 204, 204));
        totalOddTextPane.setText(currentTotalOddText);
        jScrollPane5.setViewportView(totalOddTextPane);

        userBalanceTextField.setEditable(false);
        userBalanceTextField.setBackground(new java.awt.Color(0, 204, 204));
        userBalanceTextField.setText(currentUserBalance);

        utilities.setText("Utilities");
        utilities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                utilitiesActionPerformed(evt);
            }
        });

        ticketsHistory.setText("Tickets history");
        ticketsHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticketsHistoryActionPerformed(evt);
            }
        });
        utilities.add(ticketsHistory);

        finances.setText("Finances");
        finances.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                financesActionPerformed(evt);
            }
        });
        utilities.add(finances);

        jMenuBar2.add(utilities);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leaguesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(257, 257, 257)
                .addComponent(userBalanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(betsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectMatchButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(addToBettingTicketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(deleteTicketBetButton)
                                .addGap(59, 59, 59))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(bettingTicketsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(submitTicketButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(createNewTicketButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(deleteTicketButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(117, 117, 117))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leaguesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userBalanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(selectMatchButton)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(createNewTicketButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(deleteTicketButton)
                                .addGap(6, 6, 6)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(betsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bettingTicketsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(submitTicketButton))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addToBettingTicketButton)
                        .addComponent(deleteTicketBetButton))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(149, 149, 149))
        );

        for(int i=0;i<leaguesObjBetano.objectLength;i++){
            leaguesComboBox.addItem(leaguesObjBetano.league[i].leagueName);
        }

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void leaguesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaguesComboBoxActionPerformed

        gamesList = new String[maxNrOfMatchesOnPage];

        currentLeague = (String) leaguesComboBox.getSelectedItem();

        for (int i = 0; i < leaguesObjBetano.objectLength; i++) {

            if (currentLeague.equals(leaguesObjBetano.league[i].leagueName)) {

                indexOfSelectedLeague = i;

                if (scheduledMatchesObj[i].size > 0) {
                    try {
                        for (int j = 0; j < scheduledMatchesObj[i].size; j++) {

                            StringBuilder gameSb = new StringBuilder();
                            gameSb.append(scheduledMatchesObj[i].entityObj[j].gameTag);
                            gameSb.append("  ->  ");
                            gameSb.append(scheduledMatchesObj[i].entityObj[j].dateAndTime);
                            gamesList[j] = gameSb.toString();
                            gamesListSize++;
                        }
                    } catch (Exception ex) {
                        System.out.println(ex + " : in leagueComboBoxActionPerformed method");
                    }
                } else {
                    gamesList[0] = "There are no available games in this league at the moment";
                    gamesListSize = 1;
                }
                displayNewGamesList();
                break;
            }
        }
    }//GEN-LAST:event_leaguesComboBoxActionPerformed

    private void selectMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectMatchButtonActionPerformed

        currentMatchSelection = gamesListJList.getSelectedValue();

        if (currentMatchSelection != null && !currentMatchSelection.equals("There are no available games in this league at the moment")) {

            currentMatchSelection = currentMatchSelection.substring(0, currentMatchSelection.indexOf("  ->"));

            for (int i = 0; i < scheduledMatchesObj[indexOfSelectedLeague].size; i++) {

                if (currentMatchSelection.equals(scheduledMatchesObj[indexOfSelectedLeague].entityObj[i].gameTag)) {

                    betsComboBox.removeAllItems();
                    selectedMatchIndex = i;

                    for (int j = 0;
                            j < scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.bettingTypesLength; j++) {

                        String betName
                                = scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[j].betName;
                        betsComboBox.addItem(betName);
                    }
                    break;
                }
            }
        }
    }//GEN-LAST:event_selectMatchButtonActionPerformed

    private void betsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betsComboBoxActionPerformed

        currentBet = (String) betsComboBox.getSelectedItem();

        if (currentBet != null && !currentBet.equals("There is no bet available")) {
            for (int i = 0; i < scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.bettingTypesLength; i++) {

                if (currentBet.equals(scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[i].betName)) {

                    oddsStringList = new String[MAX_NR_ODDS_PER_BET];
                    oddsStringListSize = 0;

                    if (scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[i].nrOfodds > 0) {

                        for (int j = 0;
                                j < scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[i].nrOfodds;
                                j++) {

                            String oddName
                                    = scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[i].odds[j].oddName;

                            String oddValue
                                    = String.valueOf(scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].bet.betTypesObj[i].odds[j].oddValue);

                            StringBuilder listElementSB = new StringBuilder();
                            listElementSB.append(oddName);
                            listElementSB.append("  ->  ");
                            listElementSB.append(oddValue);

                            oddsStringList[oddsStringListSize++] = listElementSB.toString();
                        }
                    } else {
                        oddsStringList[oddsStringListSize++] = "no odds available for this bet sorry :(";
                    }

                    displayNewOddsList();
                    break;
                }
            }
        }

    }//GEN-LAST:event_betsComboBoxActionPerformed

    private void addToBettingTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToBettingTicketButtonActionPerformed

        currentOdd = (String) oddsList.getSelectedValue();

        if (currNrTickets == 0) {
            createNewTicket();
            bettingTicketsComboBox.addItem(ticket[currNrTickets - 1].bettingTicketName);
        }
        oddsList.requestFocusInWindow();

        if (currentTicket == null || currentMatchSelection == null || currentBet == null || currentOdd == null) {
            return;
        }

        for (int i = 0; i < currNrTickets; i++) {

            if (ticket[i].bettingTicketName.equals(currentTicket)) {

                ticketIndexAtMatching = i;
                if (matchDuplicateOnTicket(ticket[i], scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].gameTag)) {

                    JFrame alertFrame = new JFrame();
                    JOptionPane.showMessageDialog(alertFrame, "You cannot add multiple bets of the same match on the same ticket");
                    return;
                }

                String teamA = scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamA;
                String teamB = scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamB;

                teamArapidAPI_id = dbUserObj.getIDTeam(teamA);

                if (teamArapidAPI_id == -1) {/// team is not registered yet in the table

                    findMatchingAObj = new FindMatching(teamA, leaguesObjBetano.league[indexOfSelectedLeague].rapidAPI_LeagueID, dbUserObj);
                    checkIfBothMatchingsWereMadeTimer.start();
                    /// asta ne pune sa ii gasim corespondenta echipei in bd cu echipele din liga cu id-ul parametru
                    /// si insereaza in bd football -> teams echipa

                    ///teamArapidAPI_id = dbUserObj.getIDTeam(teamA);  
                }

                teamBrapidAPI_id = dbUserObj.getIDTeam(teamB);

                if (teamBrapidAPI_id == -1) {/// team is not registered yet in the table

                    findMatchingBObj = new FindMatching(teamB, leaguesObjBetano.league[indexOfSelectedLeague].rapidAPI_LeagueID, dbUserObj);
                    checkIfBothMatchingsWereMadeTimer.start();
                    /// asta ne pune sa ii gasim corespondenta echipei in bd cu echipele din liga cu id-ul parametru
                    /// si insereaza in bd football -> teams echipa

                    ///   teamBrapidAPI_id = dbUserObj.getIDTeam(teamB);
                }

                if (teamArapidAPI_id != -1 && teamArapidAPI_id != 0 && teamBrapidAPI_id != -1 && teamBrapidAPI_id != 0) {

                    ticket[i].createNewBetEntity(
                            scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].gameTag,
                            scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].dateAndTime,
                            currentBet,
                            currentOdd, scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].startTime,
                            leaguesObjBetano.league[indexOfSelectedLeague].rapidAPI_LeagueID,
                            teamArapidAPI_id, teamBrapidAPI_id,
                            scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamA,
                            scheduledMatchesObj[indexOfSelectedLeague].entityObj[selectedMatchIndex].teamB);

                    ticket[i].computeBettingTicketOdd();
                    displayBetsListOnTicket(i);
                    modifyTotalOdd(ticket[i].bettingTicketOdd);

                    teamArapidAPI_id = teamBrapidAPI_id = -1;
                }
                break;
            }
        }
    }//GEN-LAST:event_addToBettingTicketButtonActionPerformed

    private void deleteTicketBetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTicketBetButtonActionPerformed

        selectedMatchIndex = ticketsBetsList.getSelectedIndex();
        currentTicketIndex = bettingTicketsComboBox.getSelectedIndex();

        if (selectedMatchIndex != -1) {

            deleteBetOnTicket(selectedMatchIndex, currentTicketIndex);
        }
    }//GEN-LAST:event_deleteTicketBetButtonActionPerformed

    private void bettingTicketsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bettingTicketsComboBoxActionPerformed

        currentTicket = (String) bettingTicketsComboBox.getSelectedItem();

        for (int i = 0; i < currNrTickets; i++) {

            if (ticket[i].bettingTicketName.equals(currentTicket)) {
                currentTicketIndex = i;

                displayBetsListOnTicket(i);
                modifyTotalOdd(ticket[i].bettingTicketOdd);
                break;
            }
        }
    }//GEN-LAST:event_bettingTicketsComboBoxActionPerformed

    private void createNewTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewTicketButtonActionPerformed

        createNewTicket();
        bettingTicketsComboBox.addItem(ticket[currNrTickets - 1].bettingTicketName);
    }//GEN-LAST:event_createNewTicketButtonActionPerformed

    private void submitTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitTicketButtonActionPerformed
        try {
            if (currNrTickets > 0 && ticket[currentTicketIndex].nrOfBetsOnTicket > 0) { /// conditia a doua nu cred ca mai trebe

                JFrame ticketNameFrame = new JFrame();
                Object result = JOptionPane.showInputDialog(ticketNameFrame, "Enter the ticket's sum");
                String ticketSumString = (String) result;

                float ticketSum = Float.parseFloat(ticketSumString);
                ticket[currentTicketIndex].bettingTicketSum = ticketSum;

                if (user.userBalance >= ticketSum) {

                    user.onGoingTicket[user.nrOfOnGoingTickets++] = ticket[currentTicketIndex]; /// important
                    user.onGoingTicket[user.nrOfOnGoingTickets] = dbUserObj.insertIntoUserTicket(user.userID, ticket[currentTicketIndex]);
                    /// atribuirea asta ii ca sa primeasca fiecare ticket si fiecare bet id-ul coresp. in bd
                    /// insertIntoUserTicket completeaza user.onGoingTicket[user.nrOfOnGoingTickets]
                    /// cu ticketIDinDB si, pt fiecare bet, betIDinDB
                    
                    deleteTicket(currentTicketIndex);
                    resetTicketComboBoxAfterDelete();

                    user.userBalance -= ticketSum;
                    modifyUserBalance(user.userBalance);
                } else {
                    JFrame alertFrame = new JFrame();
                    JOptionPane.showMessageDialog(alertFrame, "Insufficient funds");
                }

            } else {
                JFrame alertFrame = new JFrame();
                JOptionPane.showMessageDialog(alertFrame,
                        "You must have at least 1 bet on you ticket and you must select a ticket. Also, please write the sum in the format number.number");
            }
        } catch (Exception ex) {
            JFrame alertFrame = new JFrame();
            JOptionPane.showMessageDialog(alertFrame,
                    "You must have at least 1 bet on you ticket and you must select a ticket. Also, please write the sum in the format number.number");
        }
    }//GEN-LAST:event_submitTicketButtonActionPerformed

    private void deleteTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTicketButtonActionPerformed
        deleteTicket(currentTicketIndex);
        resetTicketComboBoxAfterDelete();
    }//GEN-LAST:event_deleteTicketButtonActionPerformed

    private void utilitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_utilitiesActionPerformed

    }//GEN-LAST:event_utilitiesActionPerformed

    private void financesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_financesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_financesActionPerformed

    private void ticketsHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticketsHistoryActionPerformed
                new TicketsHistory(user);
    }//GEN-LAST:event_ticketsHistoryActionPerformed

    private void displayNewOddsList() {

        oddsList = new javax.swing.JList<>();
        oddsList.setModel(new javax.swing.AbstractListModel<String>() {

            public int getSize() {
                return oddsStringListSize;
            }

            public String getElementAt(int i) {

                return oddsStringList[i];
            }
        });
        jScrollPane2.setViewportView(oddsList);
    }

    private void displayNewGamesList() {

        gamesListJList = new javax.swing.JList<>();
        gamesListJList.setModel(new javax.swing.AbstractListModel<String>() {

            public int getSize() {
                return gamesListSize;
            }

            public String getElementAt(int i) {

                return gamesList[i];
            }
        });
        jScrollPane1.setViewportView(gamesListJList);
    }

    private void displayBetsListOnTicket(int index) {

        if (index == -1 || ticket[index].nrOfBetsOnTicket <= 0) {
            ticketsBetsList.removeAll();
            ticketsBetsList = new javax.swing.JList<>();
            jScrollPane4.setViewportView(ticketsBetsList);
            modifyTotalOdd(-1);
            return;
        }
        // if (ticket[index].nrOfBetsOnTicket > 0) {
        ticketsBetsList = new javax.swing.JList<>();
        ticketsBetsList.setModel(new javax.swing.AbstractListModel<String>() {

            public int getSize() {
                return ticket[index].nrOfBetsOnTicket;
            }

            public String getElementAt(int i) {

                String[] ticketsOnList = ticket[index].computeBetsTags();

                return ticketsOnList[i];
            }
        });
        jScrollPane4.setViewportView(ticketsBetsList);
        //  } else {

        //  }
    }

    private void createNewTicket() {

        if (currNrTickets < 10) {

            JFrame ticketNameFrame = new JFrame();
            Object result = JOptionPane.showInputDialog(ticketNameFrame, "Enter the ticket's name");
            String stringResult = (String) result;

            if (stringResult == null || stringResult.equals("\n") || stringResult.equals("")) {

                StringBuilder resultSB = new StringBuilder();
                resultSB.append("Ticket ");
                resultSB.append(currNrTickets);
                stringResult = resultSB.toString();
            }

            ticket[currNrTickets++] = new BettingTicket(stringResult, user.userID);
        } else {

            JFrame alertFrame = new JFrame();
            JOptionPane.showMessageDialog(alertFrame, "You reached the maximum amount of active bets");
        }
    }

    private void deleteTicket(int index) {

        if (!(currNrTickets > 0)) { /// safety
            return;
        }

        for (int i = index + 1; i < currNrTickets; i++) {

            ticket[i - 1] = ticket[i];
        }

        currNrTickets--;
    }

    private boolean matchDuplicateOnTicket(BettingTicket ticket, String newMatchTag) {

        for (int i = 0; i < ticket.nrOfBetsOnTicket; i++) {

            if (ticket.bet[i].matchTag.equals(newMatchTag)) {
                return true;
            }
        }
        return false;
    }

    private void modifyTotalOdd(float ticketTotalOdd) {

        if (ticketTotalOdd == -1) {
            totalOddTextPane.setText("Total odd: 0.0");
            return;
        }

        StringBuilder totalOddSB = new StringBuilder();
        totalOddSB.append("Total odd: ");
        totalOddSB.append(ticketTotalOdd);

        currentTotalOddText = totalOddSB.toString();
        totalOddTextPane.setText(currentTotalOddText);
    }

    private void modifyUserBalance(float userBalance) {
        StringBuilder userBalanceSB = new StringBuilder();
        userBalanceSB.append("Your balance: ");
        userBalanceSB.append(userBalance);
        currentUserBalance = userBalanceSB.toString();
        userBalanceTextField.setText(currentUserBalance);
    }

    private void deleteBetOnTicket(int selectedMatchIndex, int selectedTicketIndex) {

        if (selectedTicketIndex >= 0) {
            if (!(ticket[selectedTicketIndex].nrOfBetsOnTicket > 0)) { /// safety
                return;
            }

            for (int i = selectedMatchIndex + 1; i < ticket[selectedTicketIndex].nrOfBetsOnTicket; i++) {

                ticket[selectedTicketIndex].bet[i - 1] = ticket[selectedTicketIndex].bet[i];
            }

            ticket[selectedTicketIndex].nrOfBetsOnTicket--;

            ticket[selectedTicketIndex].computeBettingTicketOdd();
            displayBetsListOnTicket(selectedTicketIndex);
            modifyTotalOdd(ticket[selectedTicketIndex].bettingTicketOdd);
        }
    }

    private void resetTicketComboBoxAfterDelete() {

        if (currNrTickets <= 0) {

            currentTicketIndex = 0;
            displayBetsListOnTicket(-1);
        } else {
            displayBetsListOnTicket(0);
        }

        currentTicketIndex = 0;

        bettingTicketsComboBox.removeAllItems();
        for (int j = 0; j < currNrTickets; j++) {

            bettingTicketsComboBox.addItem(ticket[j].bettingTicketName);
        }
    }

    private void magic() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DatabaseConnectUser dbObj = new DatabaseConnectUser(0);
                new Menu(dbObj.getUserWithID(1), dbObj).setVisible(true);
            }
        });
    }

    private BettingTicket[] ticket = new BettingTicket[MAX_NR_ACTIVE_NON_SUBMITTED_TICKETS];
    private int currNrTickets = 0;
    private UserData user;
    private DatabaseConnectUser dbUserObj;

    //////////
    private int indexOfSelectedLeague = 0;
    private int selectedMatchIndex = 0;
    private int currentTicketIndex = 0;
    private String currentMatchSelection = null;
    private String currentTicket = null;
    private String currentLeague = null;
    private String currentBet = null;
    private String currentOdd = null;
    private String currentMatchOnTicket = null;
    private String currentTotalOddText = "Total odd: 0.0";
    private String currentUserBalance = "NA";
    //////////

    private int gamesListSize = 0;
    private String[] gamesList = new String[maxNrOfMatchesOnPage];

    private String[] oddsStringList;
    private int oddsStringListSize = 0;

    /// for each league, a list of games
    private League[] scheduledMatchesObj = new League[MAX_NR_LEAGUES];

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToBettingTicketButton;
    private javax.swing.JComboBox<String> betsComboBox;
    private javax.swing.JComboBox<String> bettingTicketsComboBox;
    private javax.swing.JButton createNewTicketButton;
    private javax.swing.JButton deleteTicketBetButton;
    private javax.swing.JButton deleteTicketButton;
    private javax.swing.JMenuItem finances;
    private javax.swing.JList<String> gamesListJList;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JComboBox<String> leaguesComboBox;
    private javax.swing.JList<String> oddsList;
    private javax.swing.JButton selectMatchButton;
    private javax.swing.JButton submitTicketButton;
    private javax.swing.JList<String> ticketsBetsList;
    private javax.swing.JMenuItem ticketsHistory;
    private javax.swing.JTextPane totalOddTextPane;
    private javax.swing.JTextField userBalanceTextField;
    private javax.swing.JMenu utilities;
    // End of variables declaration//GEN-END:variables

}

/// status should become -1
/// if status -1, do no longer display the bet in the table
/// if statud -1, do not pass it into the db
/// once a game has started, you can check its status in json, maybe the current minute
/// if not, you can calculate it with java.util.date.currentTime
/// it is important to check for that SPECIFIC GAME, not for all (action listener, timer -> 
/// remake those in order to be capable of refreshing a specific game, not all)
/// by minute 88, minute to minute and take the last score as the final score
/// !!!! this may not work -> make a plan b manual match input so that you can write the match details by yourself, thus avoiding app's bugs
