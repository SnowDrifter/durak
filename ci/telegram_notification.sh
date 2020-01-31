#!/bin/sh

if [ $TRAVIS_TEST_RESULT -eq 0 ]; then
    build_status="✅"
else
    build_status="❌"
fi

BOT_URL="https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendMessage"

send_msg () {
    curl -s -X POST ${BOT_URL} -d chat_id=$TELEGRAM_CHAT_ID \
        -d text="$1" -d parse_mode=Markdown
}

send_msg "*${TRAVIS_REPO_SLUG}*

Status: ${build_status}
Branch: ${TRAVIS_BRANCH}
Message: ${TRAVIS_COMMIT_MESSAGE}

[Job Log](${TRAVIS_JOB_WEB_URL})"