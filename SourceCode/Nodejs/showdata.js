var express = require('express');
var router = express.Router();
var mysql = require('mysql');
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
  var  sql = 'select * from score_rank order by score desc;';
  connection.query(sql,function (err, result) {
        if(err){
          console.log('[SHOW ERROR] - ',err.message);
          return;
        }
       console.log('--------------------------SHOW----------------------------');
       console.log(result);
       console.log('------------------------------------------------------------\n\n');  
      // connection.end();
       res.send(result);
 });

});

module.exports = router;