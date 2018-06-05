var express = require('express');
var app = express();
var mysql = require('mysql');

     
var ShowData =require('./showdata'); 
var InsertData =require('./insertdata');
var connection = mysql.createConnection({   //配置你本地的数据库
    host     : 'localhost',
    user     : 'root',
    password : 'root',
    database : 'rank'
});

connection.connect();  //一直开启连接

var server = app.listen(3389, function () {
  var host = server.address().address;
  var port = server.address().port;
  console.log('Example app listening at http://%s:%s', host, port);
});
app.use('/show',ShowData);
app.use('/insert',InsertData);