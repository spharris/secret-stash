CREATE TABLE project (
       project_id VARCHAR(255) NOT NULL PRIMARY KEY,
       description TEXT
);

CREATE TABLE environment (
       project_id VARCHAR(255) NOT NULL,
       environment_id VARCHAR(255) NOT NULL,
       description TEXT,

       -- ACL-related items
       policy_arn VARCHAR(255) NOT NULL,
       roles TEXT DEFAULT '',
       groups TEXT DEFAULT ''
);

CREATE UNIQUE INDEX idx_env_key ON environment (project_id, environment_id);

CREATE TABLE secret (
       project_id VARCHAR(255) NOT NULL,
       environment_id VARCHAR(255) NOT NULL,
       secret_id VARCHAR(255) NOT NULL,
       description TEXT
);

CREATE UNIQUE INDEX idx_secret_key ON secret (project_id, environment_id, secret_id);
