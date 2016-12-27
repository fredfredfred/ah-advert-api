#!/usr/bin/env bash
pg_ctl -D /usr/local/var/postgres_9.6.1 stop >> /usr/local/var/log/postgres_9.6.1.log 2>&1
