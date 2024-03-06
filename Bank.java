import java.io.*;
import java.util.Random;

class CustomerDetails{
 private String customer_name,customer_address,customer_email;
 private int customer_aadhar,phone_number,customer_dob;
 
 public CustomerDetails(String name,String address,String email,int aadhar,int phone,int dob){
  this.customer_name=name;
  this.customer_address=address;
  this.customer_email=email;
  this.customer_aadhar=aadhar;
  this.customer_dob=dob;
  this.phone_number=phone;
 }
}

class AccountDetails{
 private static int account_list[]=new int[10];
 private int account_number;
 private static int current_account=0;
 private String account_type;
 private double balance;
 public CustomerDetails customer;
 
 public AccountDetails(CustomerDetails customer,String type,double balance){
  this.customer = customer;
  this.account_type=type;
  this.balance=balance;
  generateAccountNumber();
 }

 private void generateAccountNumber(){
  Random rand = new Random();
  account_number=rand.nextInt(3000);
  
  for(int i=0;i<current_account;i++){
   if(account_number==account_list[i]){
    generateAccountNumber();
   }
  }
  account_list[current_account]=account_number;
  current_account++;
  System.out.println("Account Created Successfully!");
 }

 public int retreiveAccountNumber(int account_number){
  for(int i=0;i<current_account;i++){
   if(account_number==account_list[i]){
    current_account--;
    return i;
   }
  }
  else{
    return -1;
   }
 }
}

class CreateAccount extends AccountDetails{
 private static final int MAX_ACCOUNTS = 10;
 private static int count_account=0;
 
 CustomerDetails[] customer_details=new CustomerDetails[MAX_ACCOUNTS];
 AccountDetails[] account_details=new AccountDetails[MAX_ACCOUNTS]; 
 
 public void getEntry(String name,String address,String email,int aadhar,int phone,int dob,String type,double balance){
  if(count_account!=MAX_ACCOUNTS){
   if(type=='S'){
    
   }
   customer_details[count_account]=new CustomerDetails(name,address,email,aadhar,phone,dob);
   account_details[count_account]=new AccountDetails(customer_details[count_account],type,balance);
   count_account++;
  }
  else{
   System.out.println("Fields are full now!");
  }
 }

 public void deleteAccount(int account_number){
  int index=super.retreiveAccountNumber(account_number);
  if(index==-1){
   System.out.println("Account doesn't exist!");
   return;
  }
  else{
   for(int i=index;i<count_account-1;i++){
    customer_details[i]=customer_details[i+1];
    account_details[i]=account_details[i+1];
   }
   count_account--;
   System.out.println("Account Deleted Successfully!");
 }
}

class AccessAccount extends CreateAccount{
 public void withdrawal(){
  System.out.print(account_details[0].account_type);
 }
}

class Bank{
 public static void main(String[] args)throws IOException{
  BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
 }
}