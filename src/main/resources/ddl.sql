-- DDL (Database Definition Language)
-- Table: FOLDER as hierarchical structure. Column 'parentid' accossiate selfs as 1 to n others.
CREATE TABLE folder (
	id VARCHAR(255) PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	parentid VARCHAR(255),
	CONSTRAINT fkparent FOREIGN KEY (parentid) REFERENCES folder (id) ON DELETE CASCADE
);

-- Table: TASK
CREATE TABLE task (
	id VARCHAR(255) PRIMARY KEY,
	description VARCHAR(500) NOT NULL,
	completed BOOLEAN NOT NULL DEFAULT FALSE,
	folderid VARCHAR(255) NOT NULL,
	CONSTRAINT fktaskfolder FOREIGN KEY (folderid) REFERENCES folder (id) ON DELETE CASCADE
);
-- Optional: Indexe for faster access
CREATE INDEX idxfolderparent ON folder (parentid);
CREATE INDEX idxtaskfolder ON task (folderid);


-- ----------------------------------------------------------------------
-- DML (Data Manipulation Language)
-- Test data records for FOLDER and TASK tables
-- ----------------------------------------------------------------------

-- INSERT FOLDERS (3-level hierarchy)

-- 1. Root Folder (no parentid)
INSERT INTO folder (id, name, parentid) VALUES ('f100', 'Gesamtprojekt Organisation', NULL);

-- 2. Subfolders of f100
INSERT INTO folder (id, name, parentid) VALUES ('f101', 'Frontend Development', 'f100');
INSERT INTO folder (id, name, parentid) VALUES ('f102', 'Backend Tasks', 'f100');

-- 3. Subfolder of f101 (Deeper nesting)
INSERT INTO folder (id, name, parentid) VALUES ('f103', 'Styling und UX', 'f101');
INSERT INTO folder (id, name, parentid) VALUES ('f104', 'Personal und Verwaltung', 'f100');


-- INSERT TASKS

-- Tasks in folder 'Frontend Development' (f101)
INSERT INTO task (id, description, completed, folderid) VALUES ('t201', 'PrimeFaces Tree Komponente binden', TRUE, 'f101');
INSERT INTO task (id, description, completed, folderid) VALUES ('t202', 'Tree Rendering mit f:event optimieren', FALSE, 'f101');

-- Tasks in folder 'Backend Tasks' (f102)
INSERT INTO task (id, description, completed, folderid) VALUES ('t203', 'Datenbank DDL Migration erstellen', TRUE, 'f102');
INSERT INTO task (id, description, completed, folderid) VALUES ('t204', 'Entity Persistence Logik implementieren', FALSE, 'f102');

-- Tasks in folder 'Styling und UX' (f103)
INSERT INTO task (id, description, completed, folderid) VALUES ('t205', 'CSS-Anpassungen für Mobile-Ansicht', FALSE, 'f103');

-- Tasks in folder 'Personal und Verwaltung' (f104)
INSERT INTO task (id, description, completed, folderid) VALUES ('t206', 'Reisekostenabrechnung einreichen', TRUE, 'f104');