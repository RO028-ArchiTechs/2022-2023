#!/bin/bash

find .  -not -name ".*" -print0 | xargs -0 -I {} rm -vrf {}

mv ~/w/org.zip .
unzip org.zip
rm org.zip

mv firstinspires/ftc/teamcode/* .
rm -rf firstinspires

mv firstinspires.ftc.teamcode/* .
rm -rf firstinspires.ftc.teamcode

