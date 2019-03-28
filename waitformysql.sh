HOST="localhost"
PORT="$1"
USER="rememberer"
PASSWORD="rememberer"
DATABASE="rememberer"

until echo '\q' | mysql --protocol TCP -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD" $DATABASE; do
    >&2 echo "MySQL is unavailable - sleeping"
    sleep 1
done

>&2 echo "MySQL and Data are up - executing command"
