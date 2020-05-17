package bets;

public class TicketsHistory extends javax.swing.JFrame implements Constants {

    UserData user;

    public TicketsHistory(UserData user) {
        this.user = user;
        magic();
        initComponents();
        addRadioButtonsToButtonGroup(buttonGroup1, allTicketsRadio, onGoingTicketsRadio, lostTicketsRadio, wonTicketsRadio);
        setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        ticketsComboBox = new javax.swing.JComboBox<>();
        allTicketsRadio = new javax.swing.JRadioButton();
        onGoingTicketsRadio = new javax.swing.JRadioButton();
        wonTicketsRadio = new javax.swing.JRadioButton();
        lostTicketsRadio = new javax.swing.JRadioButton();
        betsComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tickets' History");
        setExtendedState(MAXIMIZED_BOTH);
        setName("TicketsHistoryFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1162, 495));

        ticketsComboBox.setBackground(new java.awt.Color(0, 204, 204));
        ticketsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticketsComboBoxActionPerformed(evt);
            }
        });

        allTicketsRadio.setBackground(new java.awt.Color(0, 204, 204));
        allTicketsRadio.setText("All tickets");
        allTicketsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allTicketsRadioActionPerformed(evt);
            }
        });

        onGoingTicketsRadio.setBackground(new java.awt.Color(0, 204, 204));
        onGoingTicketsRadio.setText("On going tickets");
        onGoingTicketsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onGoingTicketsRadioActionPerformed(evt);
            }
        });

        wonTicketsRadio.setBackground(new java.awt.Color(0, 204, 204));
        wonTicketsRadio.setText("Won tickets");
        wonTicketsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wonTicketsRadioActionPerformed(evt);
            }
        });

        lostTicketsRadio.setBackground(new java.awt.Color(0, 204, 204));
        lostTicketsRadio.setText("Lost tickets");
        lostTicketsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lostTicketsRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onGoingTicketsRadio)
                    .addComponent(allTicketsRadio)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lostTicketsRadio)
                        .addComponent(wonTicketsRadio)))
                .addGap(18, 18, 18)
                .addComponent(ticketsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(betsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ticketsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allTicketsRadio)
                    .addComponent(betsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(onGoingTicketsRadio)
                .addGap(18, 18, 18)
                .addComponent(wonTicketsRadio)
                .addGap(18, 18, 18)
                .addComponent(lostTicketsRadio)
                .addContainerGap(289, Short.MAX_VALUE))
        );

        for(int i=0;i<nrTicketsOnList;i++){
            ticketsComboBox.addItem(ticketsList[i]);
        }

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ticketsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticketsComboBoxActionPerformed

        int index = ticketsComboBox.getSelectedIndex();

        if (index >= 0) {
            if (isSelectedOnGoingTicketsRadio) {

                nrBetsOnList = this.user.onGoingTicket[index].nrOfBetsOnTicket;
                int cnt = 0;

                for (int i = 0; i < index; i++) {

                    StringBuilder betSB = new StringBuilder();
                    this.user.onGoingTicket[index].bet[i].computeFullBetName();
                    betSB.append(this.user.onGoingTicket[index].bet[i].fullBetName);
                    betsList[cnt++] = betSB.toString();
                    System.out.println(betSB.toString());
                }

                refreshBetsComboBox();

            } else if (isSelectedLostTicketsRadio) {

            } else if (isSelectedWonTicketsRadio) {

            } else if (isSelectedAllTicketsRadio) {

            }
        }
    }//GEN-LAST:event_ticketsComboBoxActionPerformed

    private void allTicketsRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allTicketsRadioActionPerformed

        if (isSelectedAllTicketsRadio == false) {

            nrTicketsOnList = this.user.nrOfLostTickets + this.user.nrOfOnGoingTickets + this.user.nrOfWonTickets;

            int cnt = 0;

            for (int i = 0; i < this.user.nrOfOnGoingTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.onGoingTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }

            for (int i = 0; i < this.user.nrOfLostTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.lostTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.lostTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.lostTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.lostTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }

            for (int i = 0; i < this.user.nrOfWonTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.wonTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.wonTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.wonTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.wonTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }
        }

        isSelectedAllTicketsRadio = true;
        isSelectedLostTicketsRadio = false;
        isSelectedOnGoingTicketsRadio = false;
        isSelectedWonTicketsRadio = false;

        refreshTicketsComboBox();
    }//GEN-LAST:event_allTicketsRadioActionPerformed

    private void onGoingTicketsRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onGoingTicketsRadioActionPerformed
        if (isSelectedOnGoingTicketsRadio == false) {

            nrTicketsOnList = this.user.nrOfOnGoingTickets;

            int cnt = 0;

            for (int i = 0; i < this.user.nrOfOnGoingTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.onGoingTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.onGoingTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }
        }

        isSelectedAllTicketsRadio = false;
        isSelectedLostTicketsRadio = false;
        isSelectedOnGoingTicketsRadio = true;
        isSelectedWonTicketsRadio = false;

        refreshTicketsComboBox();
    }//GEN-LAST:event_onGoingTicketsRadioActionPerformed

    private void wonTicketsRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wonTicketsRadioActionPerformed
        if (isSelectedWonTicketsRadio == false) {

            nrTicketsOnList = this.user.nrOfWonTickets;

            int cnt = 0;

            for (int i = 0; i < this.user.nrOfWonTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.wonTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.wonTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.wonTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.wonTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }
        }

        isSelectedAllTicketsRadio = false;
        isSelectedLostTicketsRadio = false;
        isSelectedOnGoingTicketsRadio = false;
        isSelectedWonTicketsRadio = true;

        refreshTicketsComboBox();
    }//GEN-LAST:event_wonTicketsRadioActionPerformed

    private void lostTicketsRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lostTicketsRadioActionPerformed
        if (isSelectedLostTicketsRadio == false) {

            nrTicketsOnList = this.user.nrOfLostTickets;

            int cnt = 0;

            for (int i = 0; i < this.user.nrOfLostTickets; i++) {

                StringBuilder ticketLabel = new StringBuilder();
                ticketLabel.append(this.user.lostTicket[i].bettingTicketName);
                ticketLabel.append(" -> SUM : ");
                ticketLabel.append(this.user.lostTicket[i].bettingTicketSum);
                ticketLabel.append(" -> TOTAL ODD : ");
                ticketLabel.append(this.user.lostTicket[i].bettingTicketOdd);
                ticketLabel.append(" -> TOTAL BETS : ");
                ticketLabel.append(this.user.lostTicket[i].nrOfBetsOnTicket);

                ticketsList[cnt++] = ticketLabel.toString();
            }
        }

        isSelectedAllTicketsRadio = false;
        isSelectedLostTicketsRadio = true;
        isSelectedOnGoingTicketsRadio = false;
        isSelectedWonTicketsRadio = false;

        refreshTicketsComboBox();
    }//GEN-LAST:event_lostTicketsRadioActionPerformed

    private void addRadioButtonsToButtonGroup(javax.swing.ButtonGroup buttonGroup, javax.swing.JRadioButton rb0,
            javax.swing.JRadioButton rb1, javax.swing.JRadioButton rb2, javax.swing.JRadioButton rb3) {

        buttonGroup.add(rb0);
        buttonGroup.add(rb1);
        buttonGroup.add(rb2);
        buttonGroup.add(rb3);
    }

    private void refreshTicketsComboBox() {

        betsComboBox.removeAllItems();
        ticketsComboBox.removeAllItems();

        for (int i = 0; i < nrTicketsOnList; i++) {
            ticketsComboBox.addItem(ticketsList[i]);
        }
    }

    private void refreshBetsComboBox() {

        betsComboBox.removeAllItems();

        for (int i = 0; i < nrBetsOnList; i++) {

            betsComboBox.addItem(betsList[i]);
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TicketsHistory(null).setVisible(true);
            }
        });
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
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketsHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    private boolean isSelectedAllTicketsRadio;
    private boolean isSelectedOnGoingTicketsRadio;
    private boolean isSelectedLostTicketsRadio;
    private boolean isSelectedWonTicketsRadio;

    private String[] ticketsList = new String[MAX_NR_TICKETS];
    int nrTicketsOnList;

    private String[] betsList = new String[MAX_NR_BETS_PER_TICKET];
    int nrBetsOnList;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton allTicketsRadio;
    private javax.swing.JComboBox<String> betsComboBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton lostTicketsRadio;
    private javax.swing.JRadioButton onGoingTicketsRadio;
    private javax.swing.JComboBox<String> ticketsComboBox;
    private javax.swing.JRadioButton wonTicketsRadio;
    // End of variables declaration//GEN-END:variables
}
