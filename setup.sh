#!/bin/bash
set -e
echo "Productivity app REST Server builder & runner"
cd deploy
sudo docker-compose up --build
