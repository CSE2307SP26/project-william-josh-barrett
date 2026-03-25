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