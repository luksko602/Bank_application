/**
 * En klass som hanterar Bankkonton och är en subklass till "Account".
 *
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

@SuppressWarnings("serial")
public class SavingsAccount extends Account {

   private double interest;
   boolean freeWithdrawal = true;

   /**
    * Konstruktor
    */
   public SavingsAccount() {
      super();
      interest = 1;
   }

   /**
    * Åtkomst till kontotyp
    * 
    * @return Sträng med kontotyp
    */
   public String getAccountType() {
      return "Sparkonto";
   }

   /**
    * Åtkomst till ränta på kontot
    * 
    * @return double med totala räntan
    */
   public double getInterest() {
      return super.getAccountBalance() * interest / 100.00;
   }

   /**
    * Skapar en sträng med kontoinfo
    * 
    * @return Sträng med kontoinfo
    */
   public String toString() {
      return super.toString() + "Sparkonto" + " " + interest;
   }

   /**
    * Hanterar ett uttag, kontrollerar så tillräckligt med pengar finns på kontot
    *
    * @param amount uttagssumma
    * @return boolean, true om uttaget lyckas
    */
   public boolean withdraw(double amount) {
      if (!freeWithdrawal) {
         amount = amount * 1.02;
      }
      if (amount <= super.getAccountBalance()) {
         super.deposit(-amount);
         freeWithdrawal = false;
         return true;
      } else {
         return false;
      }
   }
}