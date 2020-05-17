package bets;

public class Registration extends javax.swing.JFrame implements Constants {

    public Registration() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        submitButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        passwordField = new javax.swing.JPasswordField();
        nameTextField = new javax.swing.JFormattedTextField();
        signInCheckBox = new javax.swing.JCheckBox();
        registerCheckBox = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        inputAlertTextFields = new java.awt.TextField();
        checkBoxAlertTextField = new java.awt.TextField();
        submitUserAlert = new java.awt.TextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 255, 102));
        setExtendedState(MAXIMIZED_BOTH);
        setPreferredSize(SCREEN_SIZE
        );

        submitButton.setBackground(new java.awt.Color(0, 153, 255));
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        jTextPane3.setEditable(false);
        jTextPane3.setBackground(new java.awt.Color(0, 153, 255));
        jTextPane3.setText("Enter your password");
        jScrollPane4.setViewportView(jTextPane3);

        passwordField.setBackground(new java.awt.Color(0, 153, 255));

        nameTextField.setBackground(new java.awt.Color(0, 153, 255));

        signInCheckBox.setBackground(new java.awt.Color(0, 153, 255));
        signInCheckBox.setText("Sign In");
        signInCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInCheckBoxActionPerformed(evt);
            }
        });

        registerCheckBox.setBackground(new java.awt.Color(0, 153, 255));
        registerCheckBox.setText("Register\n");
        registerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerCheckBoxActionPerformed(evt);
            }
        });

        jTextPane2.setEditable(false);
        jTextPane2.setBackground(new java.awt.Color(0, 153, 255));
        jTextPane2.setText("Enter your name");
        jScrollPane3.setViewportView(jTextPane2);

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(0, 153, 255));
        jTextPane1.setText("    Register or Sign in the application");
        jTextPane1.setAlignmentX(2);
        jTextPane1.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(jTextPane1);

        inputAlertTextFields.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inputAlertTextFields.setEditable(false);
        inputAlertTextFields.setEnabled(false);
        inputAlertTextFields.setText("You must complete your name as well as your password");

        checkBoxAlertTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        checkBoxAlertTextField.setEditable(false);
        checkBoxAlertTextField.setEnabled(false);
        checkBoxAlertTextField.setText("You must choose whether you register or sign in");

        submitUserAlert.setEditable(false);
        submitUserAlert.setEnabled(false);
        submitUserAlert.setText("Something went wrong with your submission, try again");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputAlertTextFields, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkBoxAlertTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(registerCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                        .addComponent(signInCheckBox))
                                    .addComponent(nameTextField)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passwordField))
                                .addGap(32, 32, 32)
                                .addComponent(submitButton))
                            .addComponent(submitUserAlert, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(submitButton)))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerCheckBox)
                    .addComponent(signInCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAlertTextFields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxAlertTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(submitUserAlert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(525, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(368, 368, 368))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerCheckBoxActionPerformed

        if (registerCheckBox.isSelected() && signInCheckBox.isSelected()) {
            registerCheckBox.setSelected(false);
        }
    }//GEN-LAST:event_registerCheckBoxActionPerformed

    private void signInCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInCheckBoxActionPerformed

        if (registerCheckBox.isSelected() && signInCheckBox.isSelected()) {
            signInCheckBox.setSelected(false);
        }
    }//GEN-LAST:event_signInCheckBoxActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed

        boolean formOK = true;

        if (!registerCheckBox.isSelected() && !signInCheckBox.isSelected()) {

            checkBoxAlertTextField.setVisible(true);
            checkBoxAlertTextField.setEnabled(true);
            formOK = false;
        }

        String name = nameTextField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (name.equals("") || password.equals("")) {

            inputAlertTextFields.setVisible(true);
            inputAlertTextFields.setEnabled(true);
            formOK = false;
        }

        if (formOK == true) {

            DatabaseConnectUser userDBobj = new DatabaseConnectUser(0);

            if (registerCheckBox.isSelected()) {

                UserData user = userDBobj.createNewUser(name, password);
                try {
                    if (user.userID == 0) {
                        System.out.println("bbbbbbbbbbb");
                        submitUserAlert.setVisible(true);
                        submitUserAlert.setEnabled(true);
                    } else {

                        this.setVisible(false);
                        new Menu(user, userDBobj).setVisible(true);
                    }
                } catch (Exception ex) {
                    submitUserAlert.setVisible(true);
                    submitUserAlert.setEnabled(true);
                    System.out.println(ex + " : in submitButtonActionPerformed register");
                }
            } else if (signInCheckBox.isSelected()) {

                UserData user = userDBobj.validateSignIn(name, password);
                try {
                    if (user.userID == 0) {

                        submitUserAlert.setVisible(true);
                        submitUserAlert.setEnabled(true);
                    } else {

                        this.setVisible(false);
                        new Menu(user, userDBobj).setVisible(true);
                    }
                } catch (Exception ex) {
                    submitUserAlert.setVisible(true);
                    submitUserAlert.setEnabled(true);
                    System.out.println(ex + " : in submitButtonActionPerformed sign in");
                }
            }
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Registration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.TextField checkBoxAlertTextField;
    private java.awt.TextField inputAlertTextFields;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JFormattedTextField nameTextField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JCheckBox registerCheckBox;
    private javax.swing.JCheckBox signInCheckBox;
    private javax.swing.JButton submitButton;
    private java.awt.TextField submitUserAlert;
    // End of variables declaration//GEN-END:variables
}
