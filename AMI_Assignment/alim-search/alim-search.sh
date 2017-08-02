#!/bin/bash
@echo off
clear
echo "-------------- STARTING SCRIPT --------------"
echo "-------------- COMPILING --------------"
javac *.java
echo "-------------- COMPILE COMPLETE --------------"
echo "-------------- RUNNING PROGRAM --------------"
java AlimSearch $1 $2 $3 $4
echo "-------------- EXITED PROGRAM --------------"
echo "-------------- ENDING SCRIPT --------------"