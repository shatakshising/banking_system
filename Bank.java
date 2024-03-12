import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Random;

abstract class ValueManipulation{
 protected abstract void writeIntoFile();
}

class CustomerDetails extends ValueManipulation{
 private static final int MAX_ACCOUNTS=10;
 private static int count_account=0;
 protected String customer_name,customer_address,customer_email;
 protected int customer_phonenumber, customer_aadhar,customer_dob;
 
 public CustomerDetails(String name,String address,String email,int phonenumber,int aadhar,int dob){
  this.customer_name=name;
  this.customer_address=address;
  this.customer_email=email;
  this.customer_phonenumber=phonenumber;
  this.customer_aadhar=aadhar;
  this.customer_dob=dob;
  writeIntoFile();
 }
 
 protected void writeIntoFile(){
  try{
   if(count_account<MAX_ACCOUNTS){
    BufferedWriter writer=new BufferedWriter(new FileWriter("Bank.txt",true));
    writer.write(customer_name+","+customer_address+","+customer_email+","+customer_phonenumber+","+customer_aadhar+","+customer_dob);
    writer.close();
    System.out.println("Customer Details updated Successfully!");
    count_account++;
   }
  }
  catch(IOException e){
   System.out.println("Unable to create account!");
  }
 }
}

class AccountDetails extends ValueManipulation{
 private static final int MAX_ACCOUNTS=10;
 private static int count_account=0;
 private char account_type;
 private double account_balance;
 private CustomerDetails customer;
 protected String account_number;
 
 public AccountDetails(CustomerDetails customer,char type,double balance){
  this.customer=customer;
  this.account_type=type;
  this.account_balance=balance;
  writeIntoFile();
 }

 protected void writeIntoFile(){
  try{
   if(count_account<MAX_ACCOUNTS){
    generateAccountNumber();
    BufferedWriter writer=new BufferedWriter(new FileWriter("Bank.txt",true));
    writer.write(","+account_type+","+account_balance+","+account_number+"\n");
    writer.close();
    System.out.println("Account created Successfully!");
    count_account++;
   }
   else{
    System.out.println("Fields are full now!");
   }
  }
  catch(IOException e){
   System.out.println("Unable to create account!");
  }
 }

 private void generateAccountNumber() {
  Random rand=new Random();
  account_number=String.valueOf(rand.nextInt(5000));
  try (BufferedReader reader=new BufferedReader(new FileReader("Bank.txt"))){
   String line;
   while ((line=reader.readLine())!=null){
    String[] parts=line.split(",");
    if(parts.length > 8 && parts[8].equals(account_number)){
     generateAccountNumber();   
    }
   }
  }catch(IOException e){
    System.out.println("Unable to create account!");
  }
  System.out.println("Your account number is: " + account_number);
 }
}

abstract class ReadAndWrite{
  private String[] items=new String[10];
  protected int count=0;

  protected String[] readFromFile() throws IOException {
    try (BufferedReader reader=new BufferedReader(new FileReader("Bank.txt"))) {
       String line;
       while ((line=reader.readLine())!=null) {
         items[count]=line;
         count++;
       }
     }
     return items;
  }

  protected void writeToFile(String[] readString) throws IOException {
    try (BufferedWriter writer=new BufferedWriter(new FileWriter("Bank.txt"))) {
      for (int i=0; i<count; i++) {
        writer.write(readString[i] + "\n");
      }
    }
  }
}

class DeleteAccount extends ReadAndWrite {
    public void deleteAccount(String account_number) {
        try {
            String[] readString = readFromFile();
            boolean accountFound = false;
            for (int i = 0; i < count; i++) {
                String[] parts = readString[i].split(",");
                if (parts[8].equals(account_number)) {
                    accountFound = true;
                    for (int j = i; j < count - 1; j++) {
                        readString[j] = readString[j + 1];
                    }
                    count--;
                    System.out.println("Account Deleted Successfully!");
                    writeToFile(readString);
                    break;
                }
            }
            if (!accountFound) {
                System.out.println("Account doesn't exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Deposit extends ReadAndWrite{  
  public void depositAmount(String account_number,double amount)throws IOException{
  try{
   String[] readString = readFromFile();
   boolean accountFound = false;
   for(int i=0;i<count;i++) {
    String[] parts=readString[i].split(",");
    if(parts[8].equals(account_number)){
     accountFound = true;
     double balance=Double.parseDouble(parts[7]);
     balance+=amount;
     parts[7]=Double.toString(balance);
     readString[i] = String.join(",", parts);
     writeToFile(readString);
     System.out.println("Amount Deposited.\nBalance is:"+parts[7]);
    }
   }
   if(!accountFound){
     System.out.println("Account doesn't exist!");
   }
  } 
  catch(IOException e){
   e.printStackTrace();
  }
 }
}

class Withdrawal extends ReadAndWrite{
 private static final int MIN_SAVING=1000;
 private static final int MIN_CURRENT=10000;
 
 public void checkType(String account_number,double amount)throws IOException{
  String[] readString=readFromFile();
  boolean accountFound=false;
  for(int i=0;i<count;i++){
   String[] parts=readString[i].split(",");
   if(parts[8].equals(account_number)){
    accountFound=true;
    if(parts[6].equals("S")){
     parts[7]=Double.toString(withdrawSaving(amount,parts[7]));
     readString[i]=String.join(",",parts);
     writeToFile(readString);
     System.out.println("Amount has been withdrawal.\nBalance is:"+parts[7]);
     break;
    }
    else{
     parts[7]=Double.toString(withdrawCurrent(amount,parts[7]));
     readString[i]=String.join(",",parts);
     writeToFile(readString);
     System.out.println("Amount has been withdrawal.\nBalance is:"+parts[7]);
     break;
    }
   }
  }
  if(!accountFound){
   System.out.println("Account Doesn't Exist!");
  }
 }

 private double withdrawSaving(double amount,String org){
  double balance=Double.parseDouble(org);
  if(amount<=balance){
   balance-=amount;
   if(balance<MIN_SAVING){
    System.out.println("You account have balance less than 1000.Penalty of Rup 10 will be deducted from your account.");
    balance-=10;
   }
   return balance;
  }
  return 0;
 }

 private double withdrawCurrent(double amount,String org){
   double balance=Double.parseDouble(org);
   if(amount<=balance){
    balance-=amount;
    if(balance<MIN_SAVING){
     System.out.println("You account have balance less than 1000.Penalty of Rup 100 will be deducted from your account.");
     balance-=100;
   }
   return balance;
  }
  return 0;
 }
}

class checkBalance extends ReadAndWrite{
  double interest=0.3;
  public void checkType(String account_number)throws IOException{
    String[] readString=readFromFile();
    boolean accountFound=false;
    for(int i=0;i<count;i++){
     String[] parts=readString[i].split(",");
     if(parts[8].equals(account_number)){
      accountFound=true;
      if(parts[6].equals("S")){
       double balance=Double.parseDouble(parts[7]);
       balance+=(balance*interest);
       System.out.println("Account Holder's Name:"+parts[0]);
       System.out.println("Account Holder's Address:"+parts[1]);
       System.out.println("Account Holder's Email:"+parts[2]);
       System.out.println("Account Holder's Phone Number:"+parts[3]);
       System.out.println("Account Holder's Aadhar:"+parts[4]);
       System.out.println("Account Holder's Date of birth:"+parts[5]);
       System.out.println();
       System.out.println("Account Balance(with interest)"+balance);
       break;
     }
     else{
       double balance=Double.parseDouble(parts[7]);
       System.out.println("Account Holder's Name:"+parts[0]);
       System.out.println("Account Holder's Address:"+parts[1]);
       System.out.println("Account Holder's Email:"+parts[2]);
       System.out.println("Account Holder's Phone Number:"+parts[3]);
       System.out.println("Account Holder's Aadhar:"+parts[4]);
       System.out.println("Account Holder's Date of birth:"+parts[5]);
       System.out.println();
       System.out.println("Account Balance"+parts[7]);
       break;
    }
   }
  }
  if(!accountFound){
   System.out.println("Account Doesn't Exist!");
  }
 }
}

class Bank_Final2{
 public static void main(String[] args) throws IOException{
   BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
   String choice="yes";
   do{
     System.out.println("---------BANKING SYSTEM---------");
     System.out.println("1:Opening an Account");
     System.out.println("2:Deleting an Account");
     System.out.println("3:Accessing Account");
     int ch;
     System.out.print("Enter your choice:");
     ch=Integer.parseInt(br.readLine());
      
     
     switch(ch){
      case 1:
        String name,address,email;
        int phonenumber,aadhar,dob;
        char type;
        double balance;
        System.out.println("Enter Customer's name:");
        name=br.readLine();
        System.out.println("Enter Customer's address:");
        address=br.readLine();
        System.out.println("Enter Customer's email:");
        email=br.readLine();
        System.out.println("Enter Customer's phone number:");
        phonenumber=Integer.parseInt(br.readLine());   
        System.out.println("Enter Customer's aadhar:");
        aadhar=Integer.parseInt(br.readLine());         
        System.out.println("Enter Customer's Date of Birth:");
        dob=Integer.parseInt(br.readLine());  
        System.out.println("Enter account type:"); 
        type=br.readLine().charAt(0);
        System.out.println("Enter Amount:");
        balance=Double.parseDouble(br.readLine());

        CustomerDetails customer=new CustomerDetails(name,address,email,phonenumber,aadhar,dob);
        AccountDetails account=new AccountDetails(customer,type,balance);
        break;
      case 2:
        DeleteAccount delete=new DeleteAccount();
        String account_number;
        System.out.println("Enter the account number:");
        account_number=br.readLine();
        delete.deleteAccount(account_number);
        break;
      case 3:
        int ch2;
        System.out.println("4:Deposit an Amount");
        System.out.println("5:Withdraw an Amount");
        System.out.println("6:Check Balance");
        System.out.println("Enter your choice:");
        ch2=Integer.parseInt(br.readLine());
        switch(ch2){
          case 4:
            String accountNumber;
            double amount;
            System.out.println("Enter Account Number:");
            accountNumber=br.readLine();
            System.out.println("Enter the amount:");
            amount=Double.parseDouble(br.readLine());
            Deposit deposit=new Deposit();
            deposit.depositAmount(accountNumber,amount);
            break;
          case 5:
            String withaccount;
            double withamount;
            System.out.println("Enter Account Number:");
            withaccount=br.readLine();
            System.out.println("Enter the amount"); 
            withamount=Double.parseDouble(br.readLine());
            Withdrawal withdrawal=new Withdrawal();
            withdrawal.checkType(withaccount,withamount);
            break;
          case 6:
            String balaccount;
            System.out.println("Enter Account Number:");
            balaccount=br.readLine();
            checkBalance checkbalance=new checkBalance();
            checkbalance.checkType(balaccount);
            break;
          default: System.out.println("Invalid Choice!");
        }
        break;
      default: System.out.println("Invalid Choice!"); break;
     }
     System.out.println("Want to continue?yes/no");
     choice=br.readLine();
   }while(choice.equals("yes"));
 }
}
