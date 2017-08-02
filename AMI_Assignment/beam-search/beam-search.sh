#!/bin/bash
@echo off
clear
echo "-------------- STARTING SCRIPT --------------"
echo "-------------- COMPILING --------------"
make clean
make
echo "-------------- COMPILE COMPLETE --------------"
echo "-------------- RUNNING PROGRAM --------------"
java BeamSearch $1 $2 $3 $4 $5
echo "-------------- EXITED PROGRAM --------------"
echo "-------------- ENDING SCRIPT --------------"