#!/bin/bash

TEST_COMMAND="cd /teezinator && ./mvn test"

CRON_SCHEDULE="0 2 * * *"

echo "$CRON_SCHEDULE $TEST_COMMAND" | crontab -

