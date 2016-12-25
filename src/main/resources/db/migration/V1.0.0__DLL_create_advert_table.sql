CREATE TABLE "Advert" (
  "id"                BIGSERIAL NOT NULL PRIMARY KEY,
  "title"             VARCHAR   NOT NULL,
  "fuel"              VARCHAR   NOT NULL,
  "price"             INTEGER   NOT NULL,
  "new"               BOOLEAN   NOT NULL,
  "mileage"           INTEGER,
  "firstRegistration" DATE
);
