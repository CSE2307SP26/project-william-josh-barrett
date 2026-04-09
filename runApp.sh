#!/bin/bash

cd src/main
javac *.java
cd ..
java main.MainMenu
rm main/*.class
cd ..