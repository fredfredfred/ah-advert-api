#!/usr/bin/env bash
mkdir -p /usr/local/var/postgres_9.6.1
mkdir -p /usr/local/var/log
pg_ctl -D /usr/local/var/postgres_9.6.1 start >> /usr/local/var/log/postgres_9.6.1.log 2>&1
