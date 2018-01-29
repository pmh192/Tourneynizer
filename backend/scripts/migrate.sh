set -e

mvn -Dflyway.configFiles=scripts/test.conf flyway:migrate
mvn -Dflyway.configFiles=scripts/dev.conf flyway:migrate
mvn -Dflyway.configFiles=scripts/prod.conf flyway:migrate
