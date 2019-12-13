CREATE TABLE IF NOT EXISTS groups(
	   id SERIAL PRIMARY KEY,
	   name VARCHAR (30) UNIQUE NOT NULL
   );

CREATE TABLE IF NOT EXISTS users(
	   id SERIAL PRIMARY KEY,
	   name varchar (30) NOT NULL,
	   surname varchar (30) NOT NULL,
	   email varchar (30) NOT NULL,
	   date_birth DATE,
	   date_creation TIMESTAMP NOT NULL,
	   last_update_date TIMESTAMP NOT NULL,
	   is_active BOOLEAN NOT NULL
   );

CREATE TABLE IF NOT EXISTS user_group(
	   id SERIAL PRIMARY KEY,
	   user_id integer,
	   group_id integer,
	   FOREIGN KEY (user_id) REFERENCES users(id),
	   FOREIGN KEY (group_id) REFERENCES groups(id)
   );


   CREATE TABLE IF NOT EXISTS user_user(
	   id SERIAL PRIMARY KEY,
	   left_user_id integer,
	   right_user_id integer,
	   FOREIGN KEY (left_user_id) REFERENCES users(id),
	   FOREIGN KEY (right_user_id) REFERENCES users(id)
   );