/**
 * Abstrakt superklass till kontotyper som finns hos banken.
 * 
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.*;
import java.util.*;

@SuppressWarnings("serial")
public abstract class Account implements Serializable {

   private static int lastAssignedAccountNo = 1000;
   private double accountBalance;
   private int accountNo;

   private ArrayList<String> actions = new ArrayList<String>();

   /**
    * Konstruktor
    */
   public Account() {
      accountBalance = 0.00;
      lastAssignedAccountNo++;
      accountNo = lastAssignedAccountNo;
   }

   /**
    * Returnerar ett kontonummer
    * 
    * @return Heltal med kontonumret
    */
   public int getAccountNo() {
      return accountNo;
   }

   /**
    * Hämtar kontobalans
    * 
    * @return Double med saldo på kontot
    */
   public double getAccountBalance() {
      return accountBalance;
   }

   /**
    * Returnerar en sträng med Kontoinfo
    *
    * @return En sträng med info om kontot
    */
   public String toString() {
      return accountNo + " " + accountBalance + " ";
   }

   /**
    * Hanterar en insättning på kontot
    * 
    * @param amount insättningssumma
    */
   public void deposit(double amount) {
      accountBalance += amount;
      adTransaction(amount);
   }

   /**
    * Lägger till en transatkion med det aktuella datumet och tiden
    * 
    * @param amount transaktionssumma
    * 
    */
   public void adTransaction(double amount) {
      Date now = new Date();
      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      actions.add(ft.format(now) + " " + amount + " " + accountBalance);
   }

   /**
    * Åtkomst till alla transaktioner
    * 
    * @return Arraylist med alla transaktioner för kontot
    */
   public ArrayList<String> getTransactions() {
      return actions;
   }

   private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
      in.defaultReadObject();
      if (this.accountNo > lastAssignedAccountNo) {
         lastAssignedAccountNo = this.accountNo;
      }
   }

   /**
    * Hanterar ett uttag på kontot
    * 
    * @param amount uttagssumma
    * @return boolean, true om insättningen lyckats
    */
   public abstract boolean withdraw(double amount);

   /**
    * Hämtar kontotyp
    * 
    * @return Sträng med kontotyp
    */
   public abstract String getAccountType();

   /**
    * Hämtar totala räntan på kontot
    * 
    * @return double med totala räntan
    */
   public abstract double getInterest();
}