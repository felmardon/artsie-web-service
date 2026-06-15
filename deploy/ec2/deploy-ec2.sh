#!/usr/bin/env bash
# deploy-ec2.sh — Deploys the Artsie Web Service to an EC2 instance.
#
# Usage:
#   ./deploy/ec2/deploy-ec2.sh <ec2-host> [ssh-key]
#
# Prerequisites:
#   - The JAR has been built: mvn clean package -DskipTests
#   - SSH access to the EC2 instance
#   - Java 17+ installed on the instance

set -euo pipefail

EC2_HOST="${1:?Usage: deploy-ec2.sh <ec2-host> [ssh-key]}"
SSH_KEY="${2:-}"
JAR_PATH="artsie-app/target/artsie-app-0.0.1-SNAPSHOT.jar"
REMOTE_DIR="/opt/artsie"

if [ ! -f "$JAR_PATH" ]; then
    echo "ERROR: JAR not found at $JAR_PATH"
    echo "Run 'mvn clean package -DskipTests' first."
    exit 1
fi

# Build SSH options
SSH_OPTS="-o StrictHostKeyChecking=no"
if [ -n "$SSH_KEY" ]; then
    SSH_OPTS="$SSH_OPTS -i $SSH_KEY"
fi

echo "==> Deploying to $EC2_HOST"

# 1. Set up the application directory and service user (first-time only)
echo "==> Setting up remote environment..."
ssh $SSH_OPTS "$EC2_HOST" << 'SETUP'
    sudo mkdir -p /opt/artsie/logs
    # Create service user if it doesn't exist
    if ! id -u artsie &>/dev/null; then
        sudo useradd --system --shell /usr/sbin/nologin --home-dir /opt/artsie artsie
        echo "Created 'artsie' service user."
    fi
    sudo chown -R artsie:artsie /opt/artsie
SETUP

# 2. Copy the JAR
echo "==> Uploading JAR..."
scp $SSH_OPTS "$JAR_PATH" "$EC2_HOST:/tmp/artsie-app.jar"
ssh $SSH_OPTS "$EC2_HOST" "sudo mv /tmp/artsie-app.jar $REMOTE_DIR/app.jar && sudo chown artsie:artsie $REMOTE_DIR/app.jar"

# 3. Copy the systemd service file
echo "==> Installing systemd service..."
scp $SSH_OPTS deploy/ec2/artsie.service "$EC2_HOST:/tmp/artsie.service"
ssh $SSH_OPTS "$EC2_HOST" << 'SERVICE'
    sudo mv /tmp/artsie.service /etc/systemd/system/artsie.service
    sudo systemctl daemon-reload
    sudo systemctl enable artsie
SERVICE

# 4. Copy .env template if no .env exists yet
ssh $SSH_OPTS "$EC2_HOST" << 'ENVCHECK'
    if [ ! -f /opt/artsie/.env ]; then
        echo "WARNING: /opt/artsie/.env does not exist."
        echo "Copy .env.template to the instance and fill in your values:"
        echo "  scp deploy/ec2/.env.template <host>:/opt/artsie/.env"
        echo "  ssh <host> 'sudo chmod 600 /opt/artsie/.env && sudo chown artsie:artsie /opt/artsie/.env'"
    fi
ENVCHECK

# 5. Restart the service
echo "==> Restarting service..."
ssh $SSH_OPTS "$EC2_HOST" "sudo systemctl restart artsie"

# 6. Verify
echo "==> Checking status..."
ssh $SSH_OPTS "$EC2_HOST" "sudo systemctl status artsie --no-pager || true"

echo ""
echo "==> Deployment complete. Logs: ssh $EC2_HOST 'journalctl -u artsie -f'"
