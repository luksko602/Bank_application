/**
 * Klass som hanterar all logik i banken. Ex, öppna konton
 * stänga konton etc.
 * 
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;

public class BankLogic {

   static ArrayList<Customer> customers;

   /**
    * Konstruktor
    */
   public BankLogic() {
      customers = new ArrayList<Customer>();
   }

   /**
    * Returnerar en arraylist med alla kunders info + konton
    * 
    * @return Arralist med all info tillhörande kund
    */
   public static ArrayList<String> getAllCustomers() {
      ArrayList<String> allCustomers = new ArrayList<String>();
      for (int i = 0; i < customers.size(); i++) {
         Customer customer = customers.get(i);
         String personalInfo = customer.getPersonalInfo();
         allCustomers.add(personalInfo);
      }
      return allCustomers;
   }

   /**
    * Skapar en kund, kontrollerar först att personnnumret inte redan är
    * registrerat
    * 
    * @param name    kundens förnamn
    * @param surname kundens efternamn
    * @param pNo     kundens personnummer
    * @return boolean, true om ny kund skapats
    */
   public boolean createCustomer(String name, String surname, String pNo) {
      if (lookForCustomer(pNo)) {
         return false;
      } else {
         Customer newCustomer = new Customer(name, surname, pNo);
         customers.add(newCustomer);
         return true;
      }
   }

   /**
    * Skapar en ArrayList med kundens personliga uppgifter + konton
    * 
    * @param pNo kundens personnummer
    * @return Arraylist med kundens information, null om kunden inte finns
    */
   public static ArrayList<String> getCustomer(String pNo) {
      ArrayList<String> customerInfo = new ArrayList<String>();
      if (!lookForCustomer(pNo)) {
         return null;
      }
      int index = customerArrayLocation(pNo);
      customerInfo.addAll(customers.get(index).getAccountInfo(0));
      customerInfo.add(0, customers.get(index).getPersonalInfo());
      return customerInfo;
   }

   /**
    * Kollar om ett personnummer redan finns registrerat
    * 
    * @param pNo sökt personnummer
    * @return boolean, true om personnumret redan finns registrerat
    */
   private static boolean lookForCustomer(String pNo) {
      for (int i = 0; i < customers.size(); i++) {
         String personalId = customers.get(i).getPersonalId();
         if (pNo == personalId) {
            return true;
         }
      }
      return false;
   }

   /**
    * Söker fram vart i Arraylisten en kund finns
    * 
    * @param pNo kundens personnummer
    * @return int med index i Arraylisten
    */
   private static int customerArrayLocation(String pNo) {
      String personalId = customers.get(0).getPersonalId();
      int index = 0;
      while (pNo != personalId) {
         index++;
         personalId = customers.get(index).getPersonalId();
      }
      return index;
   }

   /**
    * Byter namn på en kund, kontrollerar så kunden är registrerad först
    * 
    * @param name    nytt förnamn
    * @param surname nytt efternamn
    * @param pNo     kundens personnummer
    * @return boolean, true om namnet är bytt
    */
   public boolean changeCustomerName(String name, String surname, String pNo) {
      if (!lookForCustomer(pNo)) {
         return false;
      } else {
         int index = customerArrayLocation(pNo);
         customers.get(index).setSurName(surname);
         customers.get(index).setFirstName(name);
         return true;
      }
   }

   /**
    * Tar bort en kund ur banken
    * 
    * @param pNo kundens personnummer
    * @return Sträng med information om den borttagna kunden
    */
   public static ArrayList<String> deleteCustomer(String pNo) {
      ArrayList<String> delCustom = new ArrayList<String>();
      if (!lookForCustomer(pNo)) {
         return null;
      } else {
         int index = customerArrayLocation(pNo);
         delCustom.addAll(customers.get(index).getAccountInfo(1));
         delCustom.add(0, customers.get(index).getPersonalInfo());
         customers.remove(index);
         return delCustom;
      }
   }

   /**
    * Skapar ett nytt sparkonto till en befintlig kund
    * 
    * @param pNo kundens personnummer
    * @return int med det nya kontonumret, -1 om misslyckats
    */
   public int createSavingsAccount(String pNo) {
      if (lookForCustomer(pNo)) {
         return customers.get(customerArrayLocation(pNo)).createSavingsAccount();
      } else {
         return -1;
      }
   }

   /**
    * Hämtar info om ett visst konto
    * 
    * @param pNo       kundens personnummer
    * @param accountId kontonumret som eftersöks
    * @return Sträng med info om det sökta kontot, null om kontot inte finns
    */
   public static String getAccount(String pNo, int accountId) {
      int index = customerArrayLocation(pNo);
      Boolean accExist = customers.get(index).lookForAccount(accountId);
      if (accExist) {
         return customers.get(index).oneAccountInfo(accountId, 0);
      } else {
         return null;
      }
   }

   /**
    * Gör en insättning på önskat konto
    * 
    * @param pNo       kundens personnummer
    * @param accountId kundens kontonummer
    * @param amount    insättningssumma
    * @return boolean, true om insättningen lyckas
    */
   public boolean deposit(String pNo, int accountId, double amount) {
      if (lookForCustomer(pNo)) {
         int index = customerArrayLocation(pNo);
         if (customers.get(index).deposit(accountId, amount)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Uttag från önskat konto
    * 
    * @param pNo       kundens personnummer
    * @param accountId kundens kontonummer
    * @param amount    uttagssumma
    * @return boolean, true om uttaget lyckas
    */
   public boolean withdraw(String pNo, int accountId, double amount) {
      if (lookForCustomer(pNo)) {
         int index = customerArrayLocation(pNo);
         if (customers.get(index).withdraw(accountId, amount)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Stänger ett specifikt konto
    * 
    * @param pNo       kundens personnummer
    * @param accountId kontonummer som ska stängas
    * @return Sträng med info om det stängda kontot
    */
   public String closeAccount(String pNo, int accountId) {
      String accInfo = null;
      if (lookForCustomer(pNo)) {
         int index = customerArrayLocation(pNo);
         accInfo = customers.get(index).oneAccountInfo(accountId, 1);
         if (customers.get(index).deleteAccount(accountId)) {
            return accInfo;
         }
      }
      return null;
   }

   /**
    * Skapar ett nytt kreditkonto
    * 
    * @param pNo kundens personnummer
    * @return int med det nya kontonumret, -1 om misslyckande
    */
   public int createCreditAccount(String pNo) {
      if (lookForCustomer(pNo)) {
         return customers.get(customerArrayLocation(pNo)).createCreditAccount();
      } else {
         return -1;
      }
   }

   /**
    * Skapar en ArrayList med transaktioner på givet konto
    * 
    * @param pNo       kundens personnummer
    * @param accountId kundens kontonummer
    * @return ArrayList med alla transaktioner
    */
   public static ArrayList<String> getTransactions(String pNo, int accountId) {
      ArrayList<String> transactions = new ArrayList<String>();
      if (lookForCustomer(pNo)) {
         int index = customerArrayLocation(pNo);
         int accIndex = customers.get(index).accountIndex(accountId);
         if (!(accIndex == -1)) {
            transactions = customers.get(index).accounts.get(accIndex).getTransactions();
         } else
            return null;
      }
      return transactions;
   }

   /**
    * Hämtar personnummer på givet index
    * 
    * @param index kundens index i "customers"
    * @return den sökta kundens personnummer
    */
   public static String getPNo(int index) {
      return customers.get(index).getPersonalId();
   }

   /**
    * Hämtar kontonummer på givet index
    * 
    * @param pNo      kundens personnummer
    * @param accIndex kontots index i "accounts"
    * @return sökta kontonumret
    */
   public static int getAccountNo(String pNo, int accIndex) {
      if (lookForCustomer(pNo)) {
         int index = customerArrayLocation(pNo);
         return customers.get(index).getAccountNo(accIndex);
      } else
         return 0;
   }

   /**
    * Sparar alla kunder till en vald fil. Ej läsbar fil för användare
    * 
    * @param fileName Filen som kunderna ska skrivas till
    * @throws IOException
    */
   public void saveAllCustomer(File fileName) throws IOException {
      FileOutputStream file = new FileOutputStream(fileName);
      ObjectOutputStream saveOut = new ObjectOutputStream(file);
      saveOut.writeInt(customers.size());
      for (int i = 0; i < customers.size(); i++) {
         saveOut.writeObject(customers.get(i));
      }
      saveOut.close();
      file.close();
   }

   /**
    * Läser in kunder från en vald fil. Kräver samma format som "saveAllCustomer"
    * genererar
    * 
    * @param fileName Filen som kunder ska läsas ifrån
    * @throws StreamCorruptedException
    * @throws ClassNotFoundException
    * @throws IOException
    */
   public void readInCustomers(File fileName) throws StreamCorruptedException, ClassNotFoundException, IOException {
      FileInputStream file = new FileInputStream(fileName);
      ObjectInputStream readIn = new ObjectInputStream(file);
      int noToRead = readIn.readInt();
      for (int i = 0; i < noToRead; i++) {
         customers.add((Customer) readIn.readObject());
      }
      readIn.close();
      file.close();
   }

   /**
    * Sparar transaktioner kopplade till giver konto, läsbar fil för användare
    * 
    * @param pNo       kundens personnummer
    * @param accountId kontonumret som utdrag ska sparas till
    * @throws FileNotFoundException
    * @throws IOException
    */
   public void saveTransactions(String pNo, int accountId) throws IOException {
      try {
         ArrayList<String> transactions = getTransactions(pNo, accountId);
         PrintWriter saveOut = new PrintWriter(new FileWriter("luksok8_Files/" + pNo + "_" + accountId + ".txt"));
         saveOut.println("Dagens Datum: " + new Date());
         saveOut.println();
         for (int i = 0; i < transactions.size(); i++) {
            String[] split = transactions.get(i).split(" ");
            saveOut.printf("Datum: %s %s %nTransaktion: %skr %nSaldo: %skr%n%n", split[0], split[1], split[2],
                  split[3]);
         }
         saveOut.close();
      } catch (FileNotFoundException fNFE) {
         File dir = new File("luksok8_Files");
         dir.mkdir();
         saveTransactions(pNo, accountId);
      }
   }
}
