#!/usr/bin/env bash

SERVER_NAME="flow3-server"

DROPLET_URL="139.59.215.163"

DROPLET_USER="root";

echo "################################"
echo "Building The Project"
echo "################################"


echo "################################"
echo "Deploying The Project"
echo "################################"

scp -r ./deploy/* $DROPLET_USER@$DROPLET_URL:/var/deploy/$SERVER_NAME