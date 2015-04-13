
mongoose = require('mongoose');

module.exports = function() {

 var db = mongoose.connect('mongodb://localhost/friendly');


require('../models/User');
require('../models/Message');


 User = mongoose.model('User');
 Message = mongoose.model('Message');
 return db;

};
