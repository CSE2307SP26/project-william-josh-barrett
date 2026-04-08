#!/bin/bash

cd src/main
javac MainMenu.java BankAccount.java BankManager.java BrokerMenu.java Security.java
cd ..
java main.MainMenu
rm main/MainMenu.class
rm main/BankAccount.class
rm main/BankManager.class
rm main/BrokerMenu.class
rm main/Security.class
rm main/MainMenu\$accountSelections.class
rm main/MainMenu\$adminSelections.class
rm main/MainMenu\$startSelections.class
cd ..