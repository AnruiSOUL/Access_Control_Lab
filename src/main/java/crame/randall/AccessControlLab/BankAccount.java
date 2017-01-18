package crame.randall.AccessControlLab;

/**
 * Created by randallcrame on 1/17/17.
 */
public class BankAccount {
    private AccountTypes accountType;
    final long accountNumber = AccountNumberGenerator.createAccountNumber();

    double accountBalance =  100.00;

    String accountHoldersName = "Enter Name";
    double accountInterestRate = 0.00;
    AccountStatus accountStatus = AccountStatus.OPEN;
    OverDraftStatus overDraftStatus = OverDraftStatus.OFF;

    private BankAccount() {}

    public BankAccount(AccountTypes accountTypes,String accountHoldersName) {
        setAccountType(accountTypes);
        setAccountHoldersName(accountHoldersName);
    }

    void setAccountType(AccountTypes input){
        this.accountType = input;
    }

    public AccountTypes getAccountType() {
        return this.accountType;
    }

    public long getAccountNumber() {
      return this.accountNumber;
    }

    void setAccountBalance(double input){
       if (isAccountOpen(getAccountStatus()))
        this.accountBalance = input;
    }

    public double getAccountBalance(){
        return this.accountBalance;
    }

    public void setAccountHoldersName(String input) {
        if (isAccountOpen(getAccountStatus()))
            this.accountHoldersName = input;
    }

    public String getAccountHoldersName() {
        return this.accountHoldersName;
    }

    void setAccountInterestRate(double input){
        this.accountInterestRate = input;
    }
    public double getAccountInterestRate() {
        return this.accountInterestRate;
    }
    void setAccountStatus(AccountStatus input) {
        if (isAccountOpen(getAccountStatus()) || isAccountFrozen(getAccountStatus()))
        this.accountStatus = input;
    }
    void setAccountToClose(){
        if (getAccountBalance() == 0.00)
            accountStatus = AccountStatus.CLOSED;

    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setOverDraftStatus(OverDraftStatus input){
        this.overDraftStatus = input;
    }

    public OverDraftStatus getOverDraftStatus(){
        return overDraftStatus;
    }
    private boolean isAccountOpen(AccountStatus input) {
        return input.equals(AccountStatus.OPEN);
    }


    private boolean isAccountFrozen(AccountStatus input) {
        return input.equals(AccountStatus.FROZEN);
    }
    private boolean isOverDraftEnabled(OverDraftStatus input){
        return input.equals((OverDraftStatus.ON));
    }

    private boolean isNSF(double amount){
        return (amount <= getAccountBalance() || isOverDraftEnabled(getOverDraftStatus()));
    }

    private String unableToCompleteRequest(){
        return "Unable to Complete Request...";
    }
    public String balanceInquiry(){
        return (isAccountFrozen(getAccountStatus())) ? unableToCompleteRequest(): String.valueOf(getAccountBalance());
    }

    private String debitAccount(double input){
        this.accountBalance-= input;
        return "Debit Complete";
    }

    public String requestDebitAccount(double input){
        return (isAccountOpen(getAccountStatus()) && isNSF(input)) ? debitAccount(input) : unableToCompleteRequest();
    }

    private String creditAccount(double input){
        this.accountBalance+= input;
        return "Credit Complete";
    }

    public String requestCreditAccount(double input) {
        return (isAccountOpen(getAccountStatus()))? creditAccount(input) : unableToCompleteRequest();
    }
    private boolean accountOwnershipCheck(BankAccount input){
        return (getAccountHoldersName().equals(input.getAccountHoldersName()));
    }

    private boolean areFundsTransferable(BankAccount account){
        return (accountOwnershipCheck(account)
                && isAccountOpen(this.getAccountStatus())
                && isAccountOpen(account.getAccountStatus()));
    }

    private String debitTransfer(BankAccount account, double amount){
        this.setAccountBalance(this.accountBalance-= amount);
        account.setAccountBalance(account.accountBalance+=amount);
        return "Transfer Complete";
    }
    private String creditTransfer(BankAccount account, double amount){
        account.setAccountBalance(account.accountBalance-=amount);
        this.setAccountBalance(this.accountBalance+=amount);
        return "Transfer Complete";
    }

    public String requestTransferToAccount(BankAccount account, double amount) {
        return areFundsTransferable(account) && this.isNSF(amount)
                    ? debitTransfer(account, amount): unableToCompleteRequest();
    }

    public String requestTransferFromAccount(BankAccount account, double amount) {
        return areFundsTransferable(account) && account.isNSF(amount)
                    ? creditTransfer(account, amount): unableToCompleteRequest();
    }

}


