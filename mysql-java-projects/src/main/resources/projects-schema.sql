DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
	project_id INT AUTO_INCREMENT NOT NULL,
    project_name VARCHAR(128) NOT NULL,
    estimated_hours DECIMAL(7,2),
    actual_hours DECIMAL(7,2),
    difficulty INT,
    notes TEXT,
    PRIMARY KEY (project_id)
);

CREATE TABLE category (
	category_id INT AUTO_INCREMENT NOT NULL,
    category_name VARCHAR(128) NOT NULL,
    PRIMARY KEY (category_id)
);

CREATE TABLE project_category (
	project_id INT NOT NULL,
	category_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
    UNIQUE KEY (project_id, category_id)
);

CREATE TABLE step (
	step_id INT AUTO_INCREMENT NOT NULL,
	project_id INT NOT NULL,
	step_text text NOT NULL,
	step_order int NOT NULL,
    PRIMARY KEY (step_id),
    FOREIGN KEY (project_id)
		REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE material (
	material_id INT AUTO_INCREMENT NOT NULL,
    project_id INT NOT NULL,
    material_name VARCHAR(128) NOT NULL,
    num_required INT,
    cost DECIMAL(7,2),
    PRIMARY KEY (material_id),
	FOREIGN KEY (project_id)
		REFERENCES project (project_id) ON DELETE CASCADE
);

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES ('Hang a door', 3, 2, 2, 'Do not drop it on your toes');
INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES ('Install a lighbulb', 2, 1, 1, 'Use LED bulbs');
INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES ('Repair chair upholstry', 3, 2, 3, 'Make sure chair does not wobble');
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'Door Hinges', 10, 15.5);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'Screws', 20, 5);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, '40 Watt LED bulbs', 1, 3.5);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (3, 'Yards of Fabric', 1, 5); 
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Make sure door is level before hanging', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Screw hinges to door', 2);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Attach door hinges, with door, to door frame', 3);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'Remove lightbulb cover', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'Twist lighbulb into socket', 2);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'Return lightbulb cover', 3);
INSERT INTO step (project_id, step_text, step_order) VALUES (3, 'Measure damaged uphostry', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (3, 'Use fabric to patch damaged section', 2);
INSERT INTO category (category_id, category_name) VALUES (1, 'Doors and windows');
INSERT INTO category (category_id, category_name) VALUES (2, 'Repairs');
INSERT INTO category (category_id, category_name) VALUES (3, 'Furniture');
INSERT INTO category (category_id, category_name) VALUES (4, 'Lighting');
INSERT INTO category (category_id, category_name) VALUES (5, 'Other');
INSERT INTO project_category (project_id, category_id) VALUES (1, 1);
INSERT INTO project_category (project_id, category_id) VALUES (1, 2);
INSERT INTO project_category (project_id, category_id) VALUES (2, 4);
INSERT INTO project_category (project_id, category_id) VALUES (3, 2);
INSERT INTO project_category (project_id, category_id) VALUES (3, 3);


