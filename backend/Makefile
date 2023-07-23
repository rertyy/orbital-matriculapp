include .env

MIGRATION_PATH = database/migration/

migration_up:
	migrate -path $(MIGRATION_PATH) -database ${PSQL_CONN} -verbose up

migration_down_one:
	migrate -path $(MIGRATION_PATH) -database ${PSQL_CONN} -verbose down 1

migration_fix:
	migrate -path $(MIGRATION_PATH) -database ${PSQL_CONN} -verbose force 1