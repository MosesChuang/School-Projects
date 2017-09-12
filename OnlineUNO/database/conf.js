

const db_no_password = {
    host: 'localhost',
	port: 5432,
	db: 'UNO',
	user: '',
	password: '',
}

const db_default_password = {
    host: 'localhost',
	port: 5432,
	db: 'UNO',
	user: 'postgres',
	password: '123',
}

const db_heroku = {
    host: 'ec2-23-21-186-138.compute-1.amazonaws.com',
	port: 5432,
	db: 'de1go5p26ktd3a',
	user: 'nwxnrvovzpotoj',
	password: '8c69d2fbd3b6a1ab8a8305b7f15fa2c8581b04394cb00499d3fc974f2715b6b4',
}

module.exports=db_heroku;
