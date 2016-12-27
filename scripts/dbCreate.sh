#!/usr/bin/env bash
createdb advert
createdb advert_test
createuser advert
createuser advert_test
psql advert -c 'CREATE USER advert SUPERUSER;'
psql advert_test -c 'CREATE USER advert_test SUPERUSER;'