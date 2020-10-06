/**
 * Klass som hanterar kreditkonton och är en supklass till "account"
 * 
 * @author Lukas Skog Andersen, luksok-8
 */

package luksok8;

@SuppressWarnings("serial")
public class CreditAccount extends Account {

   private double interest;
   final private double MAX_CREDIT = -5000;

   /**
    * Konstruktor
    */
   public CreditAccount() {
      super();
      interest = 0.50;
   }

   /**
    * Åtkomst till kontotyp
    * 
    * @return Sträng med kontotyp
    */
   public String getAccountType() {
      return "Kreditkonto";
   }

   /**
    * Åtkomst till totala räntan på kontot
    * 
    * @return double med totala räntan på kontot
    */
   public double getInterest() {
      return super.getAccountBalance() * interest / 100.00;
   }

   /**
    * Skapar en sträng med kontoinfo
    * 
    * @return Sträng med info om kontot
    */
   public String toString() {
      return super.toString() + "Kreditkonto" + " " + interest;
   }

   /**
    * Hanterar ett konto, kontrollerar att krediten inte överstigs.
    * 
    * @param amount uttagssumma
    * @return boolean, true om uttag lyckas
    */
   public boolean withdraw(double amount) {
      if (super.getAccountBalance() - amount >= MAX_CREDIT) {
         super.deposit(-amount);
         if (super.getAccountBalance() < 0) {
            interest = 7.00;
         } else if (super.getAccountBalance() > 0) {
            interest = 0.50;
         }
         return true;
      } else {
         return false;
      }
   }
}