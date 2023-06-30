#!/bin/bash

rm -r *

mv ~/w/org.zip .
unzip org.zip
rm org.zip

mv firstinspires/ftc/teamcode/* .
rm -rf firstinspires

mv firstinspires.ftc.teamcode/* .
rm -rf firstinspires.ftc.teamcode

