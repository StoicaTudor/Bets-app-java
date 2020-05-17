package bets;

import java.awt.event.WindowEvent;
import static java.lang.Thread.currentThread;
import java.sql.SQLException;

public class FindMatching extends javax.swing.JFrame implements Runnable{

    DatabaseConnectUser dbUserObj;
    String teamNameBetano;
    int rapidAPI_LeagueID;
    int teamRapidAPIid;
    
    boolean frameIsOpened=false;

    public FindMatching(String teamName, int rapidAPI_LeagueID, DatabaseConnectUser dbUserObj){

        this.frameIsOpened=true;
        this.dbUserObj = dbUserObj;
        this.teamNameBetano = teamName;
        this.rapidAPI_LeagueID = rapidAPI_LeagueID;

        textFieldContent = compute_textFieldContent(teamName);
        RapidAPIteams teamsObj = new RapidAPIteams(dbUserObj, rapidAPI_LeagueID);
        rapidAPItemsNames = teamsObj.getRapidAPIteamsNames();
        
        initComponents();
        
        this.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rapidAPIteamsComboBox = new javax.swing.JComboBox<>();
        teamNameTextField = new javax.swing.JTextField();
        submitTeamButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        getContentPane().add(rapidAPIteamsComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 94, 383, -1));
        for(int i=0 ; i<rapidAPItemsNames.length ; i++){
            rapidAPIteamsComboBox.addItem(rapidAPItemsNames[i]);
        }

        teamNameTextField.setEditable(false);
        teamNameTextField.setText(textFieldContent);
        getContentPane().add(teamNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 63, 383, -1));

        submitTeamButton.setForeground(new java.awt.Color(0, 204, 204));
        submitTeamButton.setText("Submit team");
        submitTeamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitTeamButtonActionPerformed(evt);
            }
        });
        getContentPane().add(submitTeamButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 130, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitTeamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitTeamButtonActionPerformed
        /// insert into db team with id leagueID then close window

        StringBuilder selectTeam = new StringBuilder();
        selectTeam.append("SELECT `rapidAPI_id` FROM teams WHERE `teamName`=");
        selectTeam.append('"');
        selectTeam.append(rapidAPIteamsComboBox.getSelectedItem().toString());
        selectTeam.append('"');

        try {
            this.dbUserObj.rs = this.dbUserObj.st.executeQuery(selectTeam.toString());

            if (this.dbUserObj.rs.next()) {

                this.teamRapidAPIid=this.dbUserObj.rs.getInt(1);
                /// we fetched rapidAPI_id
                /// now we will introduce betano team in teams table

                StringBuilder insertTeam = new StringBuilder();
                insertTeam.append("INSERT INTO `teams` (`id`,`teamName`,`rapidAPIcompetitionID`,`rapidAPI_id`) VALUES ");
                insertTeam.append("(NULL,'");
                insertTeam.append(this.teamNameBetano);
                insertTeam.append("','");
                insertTeam.append(this.rapidAPI_LeagueID);
                insertTeam.append("','");
                insertTeam.append(this.teamRapidAPIid);
                insertTeam.append("')");

                this.dbUserObj.st.executeUpdate(insertTeam.toString());
            } else {
                System.out.println("SOMETHING WENT WRONG IN submitTeamButtonActionPerformed");
            }

        } catch (SQLException ex) {
            System.out.println(ex + " : in submitTeamButtonActionPerformed");
        }

        this.dispose();
        this.frameIsOpened=false;
    }//GEN-LAST:event_submitTeamButtonActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FindMatching.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FindMatching.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FindMatching.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FindMatching.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FindMatching(null, -1, null).setVisible(true);
            }
        });
    }

    private String textFieldContent;
    private String[] rapidAPItemsNames;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> rapidAPIteamsComboBox;
    private javax.swing.JButton submitTeamButton;
    private javax.swing.JTextField teamNameTextField;
    // End of variables declaration//GEN-END:variables

    private String compute_textFieldContent(String teamName) {
        StringBuilder textFieldContentSB = new StringBuilder();
        textFieldContentSB.append("Please select the corresponding name for -> ");
        textFieldContentSB.append(teamName);
        return textFieldContentSB.toString();
    }

   
    public void run() {
        
    }
}
