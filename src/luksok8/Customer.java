/**
 * Klass som hanterar kunder. Varje kund har sina konton i en arraylist.
 * 
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Customer implements Serializable {

   private String surName;
   private String firstName;
   private String personalIdentityNo;

   ArrayList<Account> accounts = new ArrayList<Account>();

   /**
    * Konstruktor
    * @param firstName kundens förnamn
    * @param surName kundens efternamn
    * @param personalIdentityNo kundens personnummer
    */
   public Customer(String firstName, String surName, String personalIdentityNo) {
      this.surName = surName;
      this.firstName = firstName;
      this.personalIdentityNo = personalIdentityNo;
   }

   /**
    * Ändrar kundens efternamn
    * 
    * @param newSurName nytt efternamn
    */
   public void setSurName(String newSurName) {
      surName = newSurName;
   }

   /**
    * Ändrar kundens förnamn
    * 
    * @param newFirstName nytt förnamn
    */
   public void setFirstName(String newFirstName) {
      firstName = newFirstName;
   }

   /**
    * Hämtar personnummer
    * 
    * @return Sträng med kundens personnummer
    */
   public String getPersonalId() {
      return personalIdentityNo;
   }

   /**
    * Hämtar en sträng med personlig info
    * 
    * @return Sträng med personlig information om kunden
    */
   public String getPersonalInfo() {
      return firstName + " " + surName + " " + personalIdentityNo;
   }

   /**
    * Hämtar en ArrayList med info om alla konton
    * 
    * @param interestYesNo int, 1 för ränta, 0 om inte
    * @return ArrayList med kundens alla konton
    */
   public ArrayList<String> getAccountInfo(int interestYesNo) {
      ArrayList<String> accountInfo = new ArrayList<String>();
      for (int i = 0; i < accounts.size(); i++) {
         Account konto = accounts.get(i);
         if (interestYesNo == 1) {
            double interest = Math.round(accounts.get(i).getInterest());
            accountInfo.add(konto.toString() + " " + Double.toString(interest));
         } else {
            accountInfo.add(konto.toString());
         }
      }
      return accountInfo;
   }

   /**
    * Tar bort ett konto
    * 
    * @param accountId kontonumret som ska tas bort
    * @return boolean, true om kontot tas bort
    */
   public boolean deleteAccount(int accountId) {
      int accIndex = accountIndex(accountId);
      if (!(accIndex == -1)) {
         accounts.remove(accIndex);
         return true;
      }
      return false;
   }

   /**
    * Skapar ett sparkonto
    * 
    * @return int med det nya kontonumret
    */
   public int createSavingsAccount() {
      SavingsAccount newAccount = new SavingsAccount();
      accounts.add(newAccount);
      return accounts.get(accounts.size() - 1).getAccountNo();
   }

   /**
    * Skapar ett kreditkonto
    * 
    * @return int med det nya kontonumret
    */
   public int createCreditAccount() {
      CreditAccount newAccount = new CreditAccount();
      accounts.add(newAccount);
      return accounts.get(accounts.size() - 1).getAccountNo();
   }

   /**
    * Hämtar info om ett specifikt konto
    * 
    * @param accountId     Sökt kontonummer
    * @param interestYesNo int = 1 för ränta, 0 annars
    * @return Sträng med info om kontot
    */
   public String oneAccountInfo(int accountId, int interestYesNo) {
      int index = accountIndex(accountId);
      if (!(index == -1)) {
         if (interestYesNo == 1) {
            double interest = accounts.get(index).getInterest();
            return accounts.get(index).toString() + " " + interest;
         } else {
            return accounts.get(index).toString();
         }
      } else {
         return "";
      }
   }

   /**
    * Kollar om ett visst kontonummer finns hos kund
    * 
    * @param accountId Sökt kontonummer
    * @return Boolean, true om kontot finns hos kund
    */
   public Boolean lookForAccount(int accountId) {
      int accIndex = accountIndex(accountId);
      if (!(accIndex == -1)) {
         return true;
      }
      return false;
   }

   /**
    * Kollar vilket index ett visst konto har hos en kund
    * 
    * @param accountId Sökt kontonummer
    * @return int med det index som kontot har, -1 om det inte tillhör kund
    */
   public int accountIndex(int accountId) {
      int accNo = 0;
      for (int i = 0; i < accounts.size(); i++) {
         accNo = accounts.get(i).getAccountNo();
         if (accNo == accountId) {
            return i;
         }
      }
      return -1;
   }

   /**
    * Hanterar en insättning
    * 
    * @param accountId Önskat kontonummer
    * @param amount    Insättningssumma
    * @return boolean, true om insättning lyckas
    */
   public boolean deposit(int accountId, double amount) {
      int accIndex = accountIndex(accountId);
      if (!(accIndex == -1)) {
         accounts.get(accIndex).deposit(amount);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Hanterar ett uttag
    * 
    * @param accountId Önskat kontonummer
    * @param amount    Uttagssumma
    * @return boolean, true om uttag lyckas
    */
   public boolean withdraw(int accountId, double amount) {
      int accIndex = accountIndex(accountId);
      if (!(accIndex == -1)) {
         if (accounts.get(accIndex).withdraw(amount)) {
            return true;
         }
      }
      return false;
   }
   
   /**
    * Metod som hämtar kontonummer till konto på givet index.
    * @param index det index som sökta kontot har
    * @return det sökta kontonumret
    */
   public int getAccountNo(int index) {
      return accounts.get(index).getAccountNo();
   }
   
   public void saveCustomer() {
      for(int i = 0; i < accounts.size(); i++ ) {
         
      }
   }
}