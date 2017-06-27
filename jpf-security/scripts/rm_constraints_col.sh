#!/bin/bash

# Kasper

# Removes constraints column from the csv file provided as input

IN=$1
OUT=$1"_clean"
cut --complement -f 11 -d, $IN > $OUT
