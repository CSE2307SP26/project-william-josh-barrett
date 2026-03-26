# project26

## Team Members:

* William Vargas
* Barrett Lin
* Duy Huynh (Josh)
* 

## User stories

1. A bank customer should be able to deposit into an existing account. (Shook)
2. A bank customer should be able to withdraw from an account. 
3. A bank customer should be able to check their account balance. 
4. A bank customer should be able to view their transaction history for an account. 
5. A bank customer should be able to create an additional account with the bank. 
6. A bank customer should be able to close an existing account.
7. A bank customer should be able to transfer money from one account to another. 
8. A bank adminstrator should be able to collect fees from existing accounts when necessary.
9. A bank adminstrator should be able to add an interest payment to an existing account when necessary.

    
## What user stories were completeted in this iteration?
  User Stories 2, 3, 5, 6, 7, 8, and 9 were completed in this iteration. 
  
## What user stories do you intend to complete next iteration?
  We intend to complete User Story #4 next iteration, that a bank customer should be able to view their
  transaction history for an account. We intend to do this by creating an ArrayList holding strings and 
  appending key transactions after they are called. 
## Is there anything that you implemented but doesn't currently work?
  Currently, all implemented user stories seem to work as intended. There are some areas that can be cleaned up in the 
  UI/UX design (standardizing decimal places, increasing clarity with messages, preventing anyone from accessing admin,        etc.) but functionality-wise it works. 
## What commands are needed to compile and run your code from the command line?
  We have a runApp.sh that includes the lines needed to run code from the command line. 

  #!/bin/bash
  
  cd src/main
  javac MainMenu.java BankAccount.java
  cd ..
  java main.MainMenu
  rm main/BankAccount.class
  rm main/MainMenu.class
  rm main/MainMenu\$accountSelections.class
  rm main/MainMenu\$startSelections.class
  cd ..
