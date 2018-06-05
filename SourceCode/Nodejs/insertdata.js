var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var url=require('url');
var connection = mysql.createConnection({  //ÅäÖÃÄã±¾µØµÄÊý¾Ý¿â
    host     : 'localhost',
    user     : 'root',
    password : 'root',
    database : 'rank'
  });

// ¸ÃÂ·ÓÉÊ¹ÓÃµÄÖÐ¼ä¼þ
router.use(function timeLog(req, res, next) {
  console.log('Time: ', Date.now());  //»ñÈ¡µ±Ç°µÄ²Ù×÷Ê±¼ä  ¿ÉÒÔÈ¡Ïû
  next();
});
router.get('/', function(req, res) {
	var params = url.parse(req.url, true).query;
	var score =params.score;
  var newmsg = [ score,score ];
  var sql ="insert into score_rank(score) select ? from dual where not exists(select * from score_rank where score = ?);";
	connection.query(sql, newmsg, function(err, result) {
    if (err) throw err;
    //  返回记录的id值
    console.log('-----------------insert---------------------',newmsg);
    res.send('Insert Successfully!');
});
  
});

module.exports = router;