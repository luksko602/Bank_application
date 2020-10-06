/**
 * Klass som innehåller ett GUI för luksok-8 privata bank.
 * 
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class BankGUI extends JFrame {

   private static final int FRAME_WIDTH = 500;
   private static final int FRAME_HEIGHT = 400;

   private BankLogic bankLogic;

   // Komponenter till menyn
   private JMenu mainMenu;
   private JMenu fileMenu;
   private JMenuItem exitItem;
   private JMenuItem readFileItem;
   private JMenuItem saveFileItem;
   private JMenuItem transactionItem;
   private JMenuItem backItem;
   private JMenuBar menuBar;
   private JFileChooser chooser;

   // Komponenter till startsidan
   private JPanel startPanel;
   private JPanel userPanel;
   private JPanel masterPanel;
   @SuppressWarnings("rawtypes")
   private JList personList;
   private JButton manageCustomer;
   private JButton newCustomer;
   private JButton deleteCustomer;
   private JTextField personInfo;
   private JTextField pNoField;
   private JTextField firstNameField;
   private JTextField surNameField;

   // Komponenter till "hantera person"-panelen
   private JPanel personPanel;
   private JPanel userPanelTop;
   private JPanel userPanelBot;
   private JPanel accountPanel;
   private JPanel accountPanelBot1;
   private JPanel accountPanelBot2;
   private JTextField updateFirstNameField;
   private JTextField updateSurNameField;
   private JTextField updatePNoField;
   @SuppressWarnings("rawtypes")
   private JList accountList;
   private JButton updateButton;
   private JButton transactionButton;
   private JButton showAccButton;
   private JButton withdrawlButton;
   private JButton depositButton;
   private JButton creditButton;
   private JButton savingsButton;
   private JButton deleteAccountButton;
   private JTextField accountInfo;
   private JTextField amountField;
   private boolean show;

   /**
    * Mainmetod som skapar en ny BankGUI-frame
    * 
    * @param args
    */
   public static void main(String[] args) {
      JFrame frame = new BankGUI();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setTitle("Luksok-8 Privata Sparbank");
      frame.setVisible(true);
   }

   /**
    * Konstruktor
    */
   public BankGUI() {
      bankLogic = new BankLogic();
      createMenu();
      createComponents();
   }

   /**
    * Skapar menyraden till fönstret
    */
   private void createMenu() {
      ActionListener menuListener = new MenuItemListener();

      mainMenu = new JMenu("Arkiv");
      fileMenu = new JMenu("Filhantering");
      readFileItem = new JMenuItem("Läs in kunder från fil");
      readFileItem.addActionListener(menuListener);
      saveFileItem = new JMenuItem("Spara kunder till fil");
      saveFileItem.addActionListener(menuListener);
      transactionItem = new JMenuItem("Spara kontoutdrag");
      transactionItem.addActionListener(menuListener);
      exitItem = new JMenuItem("Avsluta");
      exitItem.addActionListener(menuListener);
      backItem = new JMenuItem("Tillbaka");
      backItem.addActionListener(menuListener);

      fileMenu.add(readFileItem);
      fileMenu.add(saveFileItem);
      fileMenu.add(transactionItem);
      mainMenu.add(fileMenu);
      mainMenu.add(backItem);
      mainMenu.add(exitItem);

      menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      menuBar.add(mainMenu);
   }

   /**
    * Klass som innehåller lyssnare till alla menyval
    *
    */
   class MenuItemListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
         String menuText = event.getActionCommand();

         if (menuText.equals("Läs in kunder från fil")) {
            try {
               chooser = new JFileChooser();
               if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                  File selectedFile = chooser.getSelectedFile();
                  bankLogic.readInCustomers(selectedFile);
                  updatePersonList();
               }
            } catch (ClassNotFoundException cNFE) {
               JOptionPane.showMessageDialog(null, "Programfiler saknas");
            } catch (StreamCorruptedException sCE) {
               JOptionPane.showMessageDialog(null, "Filen du försökte öppna är korrupt");
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (menuText.equals("Spara kunder till fil")) {
            try {
               chooser = new JFileChooser();
               if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                  File selectedFile = chooser.getSelectedFile();
                  bankLogic.saveAllCustomer(selectedFile);
               }
            } catch (FileNotFoundException fNFE) {
               JOptionPane.showMessageDialog(null, "Det gick inte att spara till den fil ni valt");
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (menuText.equals("Spara kontoutdrag")) {
            int position = accountList.getSelectedIndex();
            if (position < 0) {
               JOptionPane.showMessageDialog(null, "Du måste välja ett konto i listan");
            } else {
               try {
                  bankLogic.saveTransactions(getPNo(), getAccNo());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
         if (menuText.equals("Avsluta")) {
            System.exit(0);
         }
         if (menuText.equals("Tillbaka")) {
            startPanel.setVisible(true);
            personPanel.setVisible(false);
            updatePersonList();
            clear();
         }
      }
   }

   /**
    * Skapar de två stora panelerna som bygger upp GUIT
    */
   private void createComponents() {
      startPanel = new JPanel(new BorderLayout());
      startPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

      personPanel = new JPanel(new BorderLayout());
      personPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

      JPanel framePanel = new JPanel();
      createStartPanel();
      framePanel.add(startPanel);
      createPersonPanel();
      framePanel.add(personPanel);

      add(framePanel);
      pack();
      updatePersonList();
   }

   /**
    * Skapar startsidan men alla dess komponenter.
    */
   @SuppressWarnings("rawtypes")
   private void createStartPanel() {
      ActionListener listener = new ClickStartListener();

      masterPanel = new JPanel(new GridLayout(1, 2, 5, 5));
      userPanel = new JPanel(new GridLayout(2, 1, 5, 25));
      userPanelTop = new JPanel(new GridLayout(4, 1, 5, 5));
      userPanelBot = new JPanel(new GridLayout(3, 1, 5, 25));

      firstNameField = new JTextField();
      firstNameField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Förnamn"));
      userPanelTop.add(firstNameField);

      surNameField = new JTextField();
      surNameField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Efternamn"));
      userPanelTop.add(surNameField);

      pNoField = new JTextField();
      pNoField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Personnummer"));
      userPanelTop.add(pNoField);

      newCustomer = new JButton("Skapa ny kund");
      newCustomer.addActionListener(listener);
      userPanelTop.add(newCustomer);

      manageCustomer = new JButton("Hantera vald kund");
      manageCustomer.addActionListener(listener);
      userPanelBot.add(manageCustomer);

      deleteCustomer = new JButton("Ta bort vald kund");
      deleteCustomer.addActionListener(listener);
      userPanelBot.add(deleteCustomer);

      personInfo = new JTextField();
      personInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Vald Kund"));
      personInfo.setEditable(false);
      userPanelBot.add(personInfo);

      userPanel.add(userPanelTop);
      userPanel.add(userPanelBot);
      masterPanel.add(userPanel);

      personList = new JList();
      personList.setBorder(BorderFactory.createTitledBorder("Kunder"));
      personList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent evt) {
            setPersonInfo();
         }
      });

      masterPanel.add(personList);
      startPanel.add(masterPanel);
      startPanel.setVisible(true);
   }

   /**
    * Skapar personpanelen med alla dess komponenter
    */
   @SuppressWarnings("rawtypes")
   private void createPersonPanel() {
      ActionListener listener = new ClickPersonListener();

      masterPanel = new JPanel(new GridLayout(1, 2, 5, 5));

      accountPanel = new JPanel(new GridLayout(2, 1, 5, 5));
      accountPanelBot1 = new JPanel(new GridLayout(4, 1, 5, 5));
      accountPanelBot2 = new JPanel(new GridLayout(1, 2, 5, 5));

      userPanel = new JPanel(new GridLayout(2, 1, 5, 5));
      userPanelTop = new JPanel(new GridLayout(4, 1, 5, 5));
      userPanelBot = new JPanel(new GridLayout(4, 1, 5, 5));

      updatePNoField = new JTextField();
      updatePNoField
            .setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Personnummer"));
      updatePNoField.setEditable(false);
      userPanelTop.add(updatePNoField);

      updateFirstNameField = new JTextField();
      updateFirstNameField
            .setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Förnamn"));
      userPanelTop.add(updateFirstNameField);

      updateSurNameField = new JTextField();
      updateSurNameField
            .setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Efternamn"));
      userPanelTop.add(updateSurNameField);

      updateButton = new JButton("Uppdatera kunduppgifter");
      updateButton.addActionListener(listener);
      userPanelTop.add(updateButton);

      accountInfo = new JTextField();
      accountInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Valt konto"));
      accountInfo.setEditable(false);
      userPanelBot.add(accountInfo);

      amountField = new JTextField();
      amountField
            .setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Uttag/Insättning"));
      userPanelBot.add(amountField);

      withdrawlButton = new JButton("Insättning");
      withdrawlButton.addActionListener(listener);
      userPanelBot.add(withdrawlButton);

      depositButton = new JButton("Uttag");
      depositButton.addActionListener(listener);
      userPanelBot.add(depositButton);

      userPanel.add(userPanelTop);
      userPanel.add(userPanelBot);

      accountList = new JList();
      accountList.setBorder(BorderFactory.createTitledBorder("Konton"));

      accountList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent evt) {
            setAccountInfo(show);
         }
      });

      accountPanel.add(accountList);

      savingsButton = new JButton("Skapa nytt sparkonto");
      savingsButton.addActionListener(listener);
      accountPanelBot1.add(savingsButton);

      creditButton = new JButton("Skapa nytt kreditkonto");
      creditButton.addActionListener(listener);
      accountPanelBot1.add(creditButton);

      deleteAccountButton = new JButton("Avsluta valt konto");
      deleteAccountButton.addActionListener(listener);
      accountPanelBot1.add(deleteAccountButton);

      transactionButton = new JButton("Transaktioner");
      transactionButton.addActionListener(listener);
      accountPanelBot2.add(transactionButton);

      showAccButton = new JButton("Alla konton");
      showAccButton.addActionListener(listener);
      accountPanelBot2.add(showAccButton);

      accountPanelBot1.add(accountPanelBot2);
      accountPanel.add(accountPanelBot1);
      masterPanel.add(userPanel);
      masterPanel.add(accountPanel);
      personPanel.add(masterPanel);
      personPanel.setVisible(false);

   }

   /**
    * Inre klass som innehåller lyssnare till alla knappar på startpanelen
    *
    */
   public class ClickStartListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
         String buttonText = event.getActionCommand();
         if (buttonText.equals("Skapa ny kund")) {
            if (firstNameField.getText().equals("")) {
               JOptionPane.showMessageDialog(null, "Du måste mata in ett förnamn");
            } else if (surNameField.getText().equals("")) {
               JOptionPane.showMessageDialog(null, "Du måste mata in ett efternamn");
            } else if (pNoField.getText().equals("")) {
               JOptionPane.showMessageDialog(null, "Du måste mata in ett personnummer");
            } else if (bankLogic.createCustomer(firstNameField.getText(), surNameField.getText(), pNoField.getText())) {
               updatePersonList();
               clear();
            } else {
               JOptionPane.showMessageDialog(null, "Kunden finns redan hos banken");
            }
         }
         if (buttonText.equals("Hantera vald kund")) {
            int position = personList.getSelectedIndex();
            if (position > -1) {
               updateAccountList();
               startPanel.setVisible(false);
               personPanel.setVisible(true);
               show = true;
               updatePNoField.setText(getPNo());
            } else {
               JOptionPane.showMessageDialog(null, "Du måste välja en kund");
            }
         }
         if (buttonText.equals("Ta bort vald kund")) {
            int position = personList.getSelectedIndex();
            if (position > -1) {
               ArrayList<String> tempInfo = BankLogic.deleteCustomer(BankLogic.getPNo(position));
               String info = tempInfo.get(0);
               JOptionPane.showMessageDialog(null, info + "\nAvslutade sitt avtal med banken");
               personInfo.setText("");
               updatePersonList();
            }
         }
      }
   }

   /**
    * Inre klass som innehåller lyssnare till alla knappar på personpanelen
    *
    */
   public class ClickPersonListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {

         String buttonText = event.getActionCommand();

         if (buttonText.equals("Alla konton")) {
            show = true;
            updateAccountList();
         }
         if (show) {
            if (buttonText.equals("Transaktioner")) {
               int accountPosition = accountList.getSelectedIndex();
               if (accountPosition >= 0) {
                  if (show) {
                     setTransactionInfo();
                     show = false;
                  }
               } else {
                  JOptionPane.showMessageDialog(null, "Du måste välja ett konto i listan");
               }
            }
            if (buttonText.equals("Insättning") || buttonText.equals("Uttag")) {
               int position = accountList.getSelectedIndex();
               if (position < 0) {
                  JOptionPane.showMessageDialog(null, "Du måste välja ett konto i listan");
               } else {
                  String temp = amountField.getText();
                  boolean numeric = true;
                  try {
                     @SuppressWarnings("unused")
                     Double num = Double.parseDouble(temp);
                  } catch (NumberFormatException e) {
                     numeric = false;
                  }
                  if (temp.equals("")) {
                     JOptionPane.showMessageDialog(null, "Du måste mata in en summa");
                  } else if (!numeric) {
                     JOptionPane.showMessageDialog(null, "Skriv summan med siffor. Ex: \"500\"");
                     amountField.setText("");
                  } else {
                     if (buttonText.equals("Insättning")) {
                        if (bankLogic.deposit(getPNo(), getAccNo(), Double.parseDouble(temp))) {
                           updateAccountList();
                           clear();
                        } else {
                           JOptionPane.showMessageDialog(null, "Du måste mata in en summa");
                        }
                     } else if (buttonText.equals("Uttag")) {
                        if (bankLogic.withdraw(getPNo(), getAccNo(), Double.parseDouble(temp))) {
                           updateAccountList();
                           clear();
                        } else
                           JOptionPane.showMessageDialog(null, "Otillräckliga likvida medel på valt konto");
                     }
                  }
               }
            }
            if (buttonText.equals("Skapa nytt sparkonto")) {
               bankLogic.createSavingsAccount(getPNo());
               updateAccountList();
            }
            if (buttonText.equals("Skapa nytt kreditkonto")) {
               bankLogic.createCreditAccount(getPNo());
               updateAccountList();
            }
            if (buttonText.equals("Avsluta valt konto")) {
               int position = accountList.getSelectedIndex();
               if (position < 0) {
                  JOptionPane.showMessageDialog(null, "Du måste välja ett konto i listan");
               } else {
                  String info;
                  info = bankLogic.closeAccount(getPNo(), getAccNo());
                  String[] splitInfo = new String[5];
                  splitInfo = info.split(" ");

                  info = "Konto: " + splitInfo[0] + " är nu avslutat med utgående saldo: " + splitInfo[1]
                        + " och ränta: " + splitInfo[4] + ".";
                  JOptionPane.showMessageDialog(null, info);
                  updateAccountList();
               }
            }
            if (buttonText.equals("Uppdatera kunduppgifter")) {
               if (updateFirstNameField.getText().equals("") || updateSurNameField.getText().equals("")) {
                  JOptionPane.showMessageDialog(null, "Mata in ett nytt förnamn och efternamn");
               } else {
                  bankLogic.changeCustomerName(updateFirstNameField.getText(), updateSurNameField.getText(), getPNo());
                  startPanel.setVisible(true);
                  personPanel.setVisible(false);
                  updatePersonList();
                  clear();
               }
            }
         } else {
            JOptionPane.showMessageDialog(null, "Gå tillbaka till kontomenyn med \"Alla konton\"");
         }
      }
   }

   /**
    * Uppdaterar "Vald kund" med vald kund i kundlistan
    */
   private void setPersonInfo() {
      int position = personList.getSelectedIndex();
      if (position > -1) {
         String info = (String) personList.getSelectedValue();
         personInfo.setText(info);
      }
   }

   /**
    * Uppdaterar "Valt konto" med valt konto i kontolistan
    * 
    * @param show boolean som avgör om "valt konto" skall visas
    */
   private void setAccountInfo(boolean show) {
      int position = personList.getSelectedIndex();
      int accountPosition = accountList.getSelectedIndex();
      if (show) {
         if (position > -1 && accountPosition > 0) {
            String info = (String) accountList.getSelectedValue();
            String[] splitInfo = new String[4];
            splitInfo = info.split(" ");
            info = "Konto:" + splitInfo[0] + "    Saldo: " + splitInfo[1];
            accountInfo.setText(info);
         } else {
            accountInfo.setText("");
         }
      } else {
         accountInfo.setText("");
      }
   }

   /**
    * Visar transaktioner för valt konto
    */
   @SuppressWarnings("unchecked")
   private void setTransactionInfo() {
      accountList.setBorder(BorderFactory.createTitledBorder("Transaktioner"));
      accountList.setListData(BankLogic.getTransactions(getPNo(), getAccNo()).toArray());
   }

   /**
    * Tömmer alla textfält
    */
   private void clear() {
      firstNameField.setText("");
      surNameField.setText("");
      pNoField.setText("");
      updateFirstNameField.setText("");
      updateSurNameField.setText("");
   }

   /**
    * Uppdaterar kundlistan
    */
   @SuppressWarnings("unchecked")
   public void updatePersonList() {
      personList.setListData(BankLogic.getAllCustomers().toArray());
   }

   /**
    * Uppdaterar kontolistan
    */
   @SuppressWarnings("unchecked")
   public void updateAccountList() {
      accountList.setBorder(BorderFactory.createTitledBorder("Konton"));
      ArrayList<String> temp = BankLogic.getCustomer(getPNo());
      temp.remove(0);
      accountList.setListData(temp.toArray());
   }

   /**
    * Hämtar personnumret på vald kund i kundlistan
    * 
    * @return kundens personnummer
    */
   public String getPNo() {
      int position = personList.getSelectedIndex();
      String tempPNo = "";
      if (position > -1) {
         tempPNo = BankLogic.getPNo(position);
      }
      return tempPNo;
   }

   /**
    * Hämtar kontonumret på valt konto i kontolistan
    * 
    * @return kontots kontonummer
    */
   public int getAccNo() {
      int position = accountList.getSelectedIndex();
      int tempAccNo = 0;
      if (position >= 0) {
         tempAccNo = BankLogic.getAccountNo(getPNo(), position);
      }
      return tempAccNo;
   }
}