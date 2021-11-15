
CREATE TABLE IF NOT EXISTS session
(
  id INT NOT NULL AUTO_INCREMENT,
  startYear INT NOT NULL,
  isComplete BOOLEAN,
  isRegistrationOpen BOOLEAN,
  PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS user
(
  username VARCHAR(25) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  isAdmin BOOLEAN,
  PRIMARY KEY (username)
);


CREATE TABLE IF NOT EXISTS stream
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS application_form
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  dob Date NOT NULL,
  email VARCHAR(100) NOT NULL,
  fatherName VARCHAR(50) NOT NULL,
  phoneNo VARCHAR(20) NOT NULL,
  address VARCHAR(100) NOT NULL,
  date Date NOT NULL,
  interestedStreams VARCHAR(100) NOT NULL,
  sessionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (sessionId) REFERENCES session(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS student
(
  rollNo VARCHAR(25) UNIQUE NOT NULL,
  name VARCHAR(50) NOT NULL,
  dob Date NOT NULL,
  email VARCHAR(100) NOT NULL,
  fatherName VARCHAR(50) NOT NULL,
  phoneNo VARCHAR(20) NOT NULL,
  address VARCHAR(100) NOT NULL,
  sessionId INT NOT NULL,
  username VARCHAR(25) NOT NULL,
  streamId INT NOT NULL,
  applicationId INT NOT NULL,
  PRIMARY KEY (rollNo),
  FOREIGN KEY (sessionId) REFERENCES session(id) ON DELETE CASCADE,
  FOREIGN KEY (username) REFERENCES user(username),
  FOREIGN KEY (streamId) REFERENCES stream(id) ON DELETE CASCADE,
  FOREIGN KEY (applicationId) REFERENCES application_form(id)
);


CREATE TABLE IF NOT EXISTS classroom
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  fee INT NOT NULL,
  sessionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (sessionId) REFERENCES session(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS fee_transaction
(
  id INT NOT NULL AUTO_INCREMENT,
  amount INT NOT NULL,
  date DATE NOT NULL,
  remarks VARCHAR(100) NOT NULL,  
  transactionId VARCHAR(25) NOT NULL,
  studentRollNo VARCHAR(25) NOT NULL,
  classroomId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (studentRollNo) REFERENCES student(rollNo) ON DELETE CASCADE,
  FOREIGN KEY (classroomId) REFERENCES classroom(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS course_structure
(
  id INT NOT NULL AUTO_INCREMENT,
  streamId INT NOT NULL,
  classroomId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (streamId) REFERENCES stream(id) ON DELETE CASCADE,
  FOREIGN KEY (classroomId) REFERENCES classroom(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS classroom_registration
(
  id INT NOT NULL AUTO_INCREMENT,
  studentRollNo VARCHAR(25) NOT NULL,
  classroomId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (studentRollNo) REFERENCES student(rollNo) ON DELETE CASCADE,
  FOREIGN KEY (classroomId) REFERENCES classroom(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS teacher
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  phoneNo VARCHAR(20) NOT NULL,
  address VARCHAR(100) NOT NULL,
  dob DATE NOT NULL,
  username VARCHAR(25) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (username) REFERENCES user(username)
);

CREATE TABLE IF NOT EXISTS subject
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  code VARCHAR(25) NOT NULL,
  teacherId INT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (teacherId) REFERENCES teacher(id)
);


CREATE TABLE IF NOT EXISTS subject_course_structure_relation
(
  id INT NOT NULL AUTO_INCREMENT,
  subjectId INT NOT NULL,
  courseStructureId INT NOT NULL,
  optional BOOLEAN NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (subjectId) REFERENCES subject(id) ON DELETE CASCADE,
  FOREIGN KEY (courseStructureId) REFERENCES course_structure(id)  ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS subject_registration_relation
(
  id INT NOT NULL AUTO_INCREMENT,
  registrationId INT NOT NULL,
  subjectId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (registrationId) REFERENCES classroom_registration(id) ON DELETE CASCADE,
  FOREIGN KEY (subjectId) REFERENCES subject(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS result
(
  id INT NOT NULL AUTO_INCREMENT,
  grade ENUM('A', 'A-', 'B', 'B-', 'C', 'C-', 'F'),
  subjectRegistrationId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (subjectRegistrationId) REFERENCES subject_registration_relation(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS payout
(
  id INT NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  amount INT NOT NULL,
  remarks VARCHAR(100) NOT NULL,
  transactionId VARCHAR(25) NOT NULL,
  teacherId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (teacherId) REFERENCES teacher(id) ON DELETE CASCADE
);
