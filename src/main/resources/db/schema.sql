CREATE TABLE project (
       project_id VARCHAR(255) NOT NULL PRIMARY KEY,
       description TEXT
);

CREATE TABLE environment (
       project_id VARCHAR(255) NOT NULL REFERENCES project(project_id) ON DELETE CASCADE,
       environment_id VARCHAR(255) NOT NULL,
       description TEXT,

       -- ACL-related items
       policy_arn VARCHAR(255) NOT NULL,
       roles TEXT DEFAULT '',
       groups TEXT DEFAULT '',

       PRIMARY KEY(project_id, environment_id)
);

CREATE TABLE secret (
       project_id VARCHAR(255) NOT NULL,
       environment_id VARCHAR(255) NOT NULL,
       secret_id VARCHAR(255) NOT NULL,
       description TEXT,

       PRIMARY KEY(project_id, environment_id, secret_id),
       FOREIGN KEY(project_id, environment_id) REFERENCES environment(project_id, environment_id) ON DELETE CASCADE
);
